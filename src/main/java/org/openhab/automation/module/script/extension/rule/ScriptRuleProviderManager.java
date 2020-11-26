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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.openhab.automation.module.script.extension.provider.SuspendableFixedProvider;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * Manager to allow bulk suspension/resumption of {@link SuspendableFixedProvider}s
 *
 * @author Jonathan Gilbert - Initial contribution
 */
@Component(service = ScriptRuleProviderManager.class)
public class ScriptRuleProviderManager {

    private List<ScriptRuleProvider> scriptRuleProviders = new CopyOnWriteArrayList<>();

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC, unbind = "removeScriptRuleProvider")
    public void addScriptRuleProvider(ScriptRuleProvider scriptRuleProvider) {
        scriptRuleProviders.add(scriptRuleProvider);
    }

    public void removeScriptRuleProvider(ScriptRuleProvider scriptRuleProvider) {
        scriptRuleProviders.remove(scriptRuleProvider);
    }

    public void suspendAll() {
        scriptRuleProviders.forEach(SuspendableFixedProvider::suspend);
    }

    public void resumeAll() {
        scriptRuleProviders.forEach(SuspendableFixedProvider::resume);
    }
}
