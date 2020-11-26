/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.openhab.automation.module.script.extension.rule;

import java.util.*;

import org.openhab.automation.module.script.extension.ScriptDisposalAware;
import org.openhab.automation.module.script.extension.module.GraalJSPrivateModuleHandlerFactory;
import org.openhab.automation.module.script.extension.provider.LifecycleAwareDelegate;
import org.openhab.core.automation.Action;
import org.openhab.core.automation.Condition;
import org.openhab.core.automation.Rule;
import org.openhab.core.automation.Trigger;
import org.openhab.core.automation.module.script.rulesupport.shared.simple.SimpleActionHandler;
import org.openhab.core.automation.module.script.rulesupport.shared.simple.SimpleRuleActionHandler;
import org.openhab.core.automation.module.script.rulesupport.shared.simple.SimpleRuleActionHandlerDelegate;
import org.openhab.core.automation.util.ActionBuilder;
import org.openhab.core.automation.util.ModuleBuilder;
import org.openhab.core.automation.util.RuleBuilder;
import org.openhab.core.config.core.Configuration;

/**
 * Rule factory that allows unmanaged Rules
 *
 * @author Jonathan Gilbert - initial contribution
 */
public class RuleFactory implements ScriptDisposalAware {
    private final Set<String> privateHandlers = new HashSet<>();
    private final GraalJSPrivateModuleHandlerFactory graalJSPrivateModuleHandlerFactory;

    private final Set<LifecycleAwareDelegate<ScriptRuleProvider>> providers = new HashSet<>();

    RuleFactory(GraalJSPrivateModuleHandlerFactory graalJSPrivateModuleHandlerFactory) {
        this.graalJSPrivateModuleHandlerFactory = graalJSPrivateModuleHandlerFactory;
    }

    public ScriptRuleProvider newRuleProvider(Rule[] rules) {
        return new ScriptRuleProvider(new ArrayList<>(Arrays.asList(rules)));
    }

    public ScriptRuleProvider registeringRuleProvider(Rule[] rules) {
        ScriptRuleProvider provider = newRuleProvider(rules);
        LifecycleAwareDelegate<ScriptRuleProvider> delegate = new LifecycleAwareDelegate<>(provider,
                ScriptRuleProvider.class);
        providers.add(delegate);
        delegate.register();
        return provider;
    }

    public Rule processRule(Rule element) {
        return processRuleInternal(element);
    }

    /// internals

    private void removePrivateHandler(String privId) {
        if (privateHandlers.remove(privId)) {
            graalJSPrivateModuleHandlerFactory.removeHandler(privId);
        }
    }

    public String addPrivateActionHandler(SimpleActionHandler actionHandler) {
        String uid = graalJSPrivateModuleHandlerFactory.addHandler(actionHandler);
        privateHandlers.add(uid);
        return uid;
    }

    private Rule processRuleInternal(Rule element) {
        RuleBuilder builder = RuleBuilder.create(element.getUID());

        String name = element.getName();
        if (name == null || name.isEmpty()) {
            name = element.getClass().getSimpleName();
            if (name.contains("$")) {
                name = name.substring(0, name.indexOf('$'));
            }
        }

        builder.withName(name).withDescription(element.getDescription()).withTags(element.getTags());

        // used for numbering the modules of the rule
        int moduleIndex = 1;

        try {
            List<Condition> conditions = new ArrayList<>();
            for (Condition cond : element.getConditions()) {
                Condition toAdd = cond;
                if (cond.getId().isEmpty()) {
                    toAdd = ModuleBuilder.createCondition().withId(Integer.toString(moduleIndex++))
                            .withTypeUID(cond.getTypeUID()).withConfiguration(cond.getConfiguration())
                            .withInputs(cond.getInputs()).build();
                }

                conditions.add(toAdd);
            }

            builder.withConditions(conditions);
        } catch (Exception ex) {
            // conditions are optional
        }

        try {
            List<Trigger> triggers = new ArrayList<>();
            for (Trigger trigger : element.getTriggers()) {
                Trigger toAdd = trigger;
                if (trigger.getId().isEmpty()) {
                    toAdd = ModuleBuilder.createTrigger().withId(Integer.toString(moduleIndex++))
                            .withTypeUID(trigger.getTypeUID()).withConfiguration(trigger.getConfiguration()).build();
                }

                triggers.add(toAdd);
            }

            builder.withTriggers(triggers);
        } catch (Exception ex) {
            // triggers are optional
        }

        List<Action> actions = new ArrayList<>();
        actions.addAll(element.getActions());

        if (element instanceof SimpleRuleActionHandler) {
            String privId = addPrivateActionHandler(
                    new SimpleRuleActionHandlerDelegate((SimpleRuleActionHandler) element));

            Action scriptedAction = ActionBuilder.create().withId(Integer.toString(moduleIndex++))
                    .withTypeUID("graaljs.ScriptedAction").withConfiguration(new Configuration()).build();
            scriptedAction.getConfiguration().put("privId", privId);
            actions.add(scriptedAction);
        }

        builder.withActions(actions);

        return builder.build();
    }

    @Override
    public void unload(String scriptIdentifier) {
        for (String privId : privateHandlers) {
            removePrivateHandler(privId);
        }

        providers.forEach(LifecycleAwareDelegate::unregister);
        providers.clear();
    }
}
