package org.openhab.automation.module.script.extension.sitemap;

import org.openhab.automation.module.script.extension.AbstractScriptExtensionProvider;
import org.openhab.core.automation.module.script.ScriptExtensionProvider;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Component;

@Component(immediate = true, service = ScriptExtensionProvider.class)
public class SitemapScriptExtensionProvider extends AbstractScriptExtensionProvider {

    @Override
    protected String getPresetName() {
        return "sitemap";
    }

    @Override
    protected void initializeTypes(final BundleContext context) {
        WidgetFactory widgetFactory = new WidgetFactory();

        addType("factory", k -> widgetFactory);
    }
}
