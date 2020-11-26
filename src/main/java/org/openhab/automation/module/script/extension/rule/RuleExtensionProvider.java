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

import static org.osgi.service.component.annotations.ReferenceCardinality.MANDATORY;

import org.openhab.automation.module.script.extension.ScriptDisposalAwareScriptExtensionProvider;
import org.openhab.automation.module.script.extension.module.GraalJSPrivateModuleHandlerFactory;
import org.openhab.core.automation.module.script.ScriptExtensionProvider;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Provider to supply the custom GraalJS rules
 *
 * @author Jonathan Gilbert - initial contribution
 */
@Component(immediate = true, service = ScriptExtensionProvider.class)
public class RuleExtensionProvider extends ScriptDisposalAwareScriptExtensionProvider {

    @Reference(cardinality = MANDATORY)
    public void setGraalJSPrivateModuleHandlerFactory(
            GraalJSPrivateModuleHandlerFactory graalJSPrivateModuleHandlerFactory) {
        this.graalJSPrivateModuleHandlerFactory = graalJSPrivateModuleHandlerFactory;
    }

    private GraalJSPrivateModuleHandlerFactory graalJSPrivateModuleHandlerFactory;

    @Override
    protected String getPresetName() {
        return "rules";
    }

    @Override
    protected void initializeTypes(BundleContext context) {
        addType("factory", k -> new RuleFactory(graalJSPrivateModuleHandlerFactory));
    }
}
