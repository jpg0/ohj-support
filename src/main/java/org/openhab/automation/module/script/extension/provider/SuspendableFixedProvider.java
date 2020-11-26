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

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.jdt.annotation.NonNull;

/**
 * {@link FixedProvider} that can be temporarily suspended (provided items revoked)
 *
 * @param <E> The type of element provided
 *
 * @author Jonathan Gilbert - Initial contribution
 */
public class SuspendableFixedProvider<@NonNull E> extends FixedProvider<E> {
    private final AtomicBoolean suspended = new AtomicBoolean();

    public SuspendableFixedProvider(Collection<E> all) {
        super(all);
    }

    @Override
    public Collection<E> getAll() {
        return suspended.get() ? Collections.emptyList() : super.getAll();
    }

    public boolean suspend() {
        boolean updated = suspended.compareAndSet(false, true);

        if (updated) {
            super.getAll().forEach(this::notifyListenersAboutRemovedElement);
        }

        return updated;
    }

    public boolean resume() {
        boolean updated = suspended.compareAndSet(true, false);

        if (updated) {
            super.getAll().forEach(this::notifyListenersAboutAddedElement);
        }

        return updated;
    }
}
