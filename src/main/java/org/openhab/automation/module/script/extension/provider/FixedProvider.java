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

package org.openhab.automation.module.script.extension.provider;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;
import org.openhab.core.common.registry.AbstractProvider;

/**
 * Concrete implementation {@link AbstractProvider} which simply wraps a fixed collection of elements
 *
 * @param <E> The type of element provided
 *
 * @author Jonathan Gilbert - Initial contribution
 */
public class FixedProvider<@NonNull E> extends AbstractProvider<E> {
    private Collection<E> all;

    public FixedProvider(Collection<E> all) {
        this.all = new ArrayList<>(all);
    }

    @Override
    public Collection<E> getAll() {
        return all;
    }
}
