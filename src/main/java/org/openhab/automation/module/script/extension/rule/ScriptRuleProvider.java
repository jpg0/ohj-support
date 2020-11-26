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

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.automation.module.script.extension.provider.SuspendableFixedProvider;
import org.openhab.core.automation.Rule;
import org.openhab.core.automation.RuleProvider;

/**
 * Provider for Script Rules
 *
 * @author Jonathan Gilbert - Initial contribution
 */
@NonNullByDefault
public class ScriptRuleProvider extends SuspendableFixedProvider<Rule> implements RuleProvider {
    public ScriptRuleProvider(Collection<Rule> all) {
        super(all);
    }
}
