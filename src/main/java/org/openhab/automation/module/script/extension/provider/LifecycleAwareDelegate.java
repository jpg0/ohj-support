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

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;

/**
 * Delegate wrapping a class and allowing it to be registered and unregistered with OSGi
 *
 * @param <T> the type to register under
 *
 * @author Jonathan Gilbert - initial contribution
 */
@NonNullByDefault
public class LifecycleAwareDelegate<T> {
    private final BundleContext bundleContext;

    private final T delegate;
    private final Class<T> delegateClass;

    @NonNullByDefault({})
    private ServiceRegistration<T> registration;

    public LifecycleAwareDelegate(T delegate, Class<T> delegateClass) {
        this(delegate, delegateClass, FrameworkUtil.getBundle(LifecycleAwareDelegate.class).getBundleContext());
    }

    public LifecycleAwareDelegate(T delegate, Class<T> delegateClass, BundleContext bundleContext) {
        this.delegate = delegate;
        this.delegateClass = delegateClass;
        this.bundleContext = bundleContext;
    }

    public void register() {
        registration = bundleContext.registerService(delegateClass, delegate, null);
    }

    public void unregister() {
        ServiceRegistration<T> registration = this.registration;
        this.registration = null;
        registration.unregister();
    }
}
