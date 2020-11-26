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
package org.openhab.automation.module.script.extension.module;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import org.openhab.core.automation.Action;
import org.openhab.core.automation.Module;
import org.openhab.core.automation.handler.BaseModuleHandlerFactory;
import org.openhab.core.automation.handler.ModuleHandler;
import org.openhab.core.automation.handler.ModuleHandlerFactory;
import org.openhab.core.automation.module.script.rulesupport.shared.ScriptedHandler;
import org.openhab.core.automation.module.script.rulesupport.shared.simple.SimpleActionHandler;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom module handler factory to support custom rule manager
 *
 * @author Jonathan Gilbert - Initial contribution
 */
@Component(immediate = true, service = { GraalJSPrivateModuleHandlerFactory.class, ModuleHandlerFactory.class })
public class GraalJSPrivateModuleHandlerFactory extends BaseModuleHandlerFactory {
    private static final String PRIV_ID = "privId";
    private static final Collection<String> TYPES = Arrays.asList("graaljs.ScriptedAction", "graaljs.ScriptedCondition",
            "graaljs.ScriptedTrigger");

    private final Logger logger = LoggerFactory.getLogger(GraalJSPrivateModuleHandlerFactory.class);
    private final HashMap<String, ScriptedHandler> privateTypes = new HashMap<>();

    private int nextId = 0;

    @Override
    public Collection<String> getTypes() {
        return TYPES;
    }

    @Override
    protected ModuleHandler internalCreate(Module module, String ruleUID) {
        ModuleHandler moduleHandler = null;

        ScriptedHandler scriptedHandler = null;
        try {
            scriptedHandler = privateTypes.get(module.getConfiguration().get(PRIV_ID));
        } catch (Exception e) {
            logger.warn("ScriptedHandler {} for ruleUID {} not found", module.getConfiguration().get(PRIV_ID), ruleUID);
        }

        if (scriptedHandler != null) {
            moduleHandler = new GraalJSActionHandlerDelegate((Action) module, (SimpleActionHandler) scriptedHandler);
        }

        return moduleHandler;
    }

    public String addHandler(String privId, ScriptedHandler scriptedHandler) {
        privateTypes.put(privId, scriptedHandler);
        return privId;
    }

    public String addHandler(ScriptedHandler scriptedHandler) {
        String privId = "i" + (nextId++);
        privateTypes.put(privId, scriptedHandler);
        return privId;
    }

    public void removeHandler(String privId) {
        privateTypes.remove(privId);
    }
}
