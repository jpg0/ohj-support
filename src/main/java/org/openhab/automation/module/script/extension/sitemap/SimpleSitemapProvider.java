package org.openhab.automation.module.script.extension.sitemap;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.Nullable;
import org.openhab.core.model.core.ModelRepositoryChangeListener;
import org.openhab.core.model.sitemap.SitemapProvider;
import org.openhab.core.model.sitemap.sitemap.Sitemap;

public class SimpleSitemapProvider implements SitemapProvider {
    private List<Sitemap> sitemaps;

    public SimpleSitemapProvider(List<Sitemap> sitemaps) {
        this.sitemaps = sitemaps;
    }

    @Override
    public @Nullable Sitemap getSitemap(String name) {
        return sitemaps.stream().filter(s -> s.getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public Set<String> getSitemapNames() {
        return sitemaps.stream().map(Sitemap::getName).collect(Collectors.toSet());
    }

    @Override
    public void addModelChangeListener(ModelRepositoryChangeListener modelRepositoryChangeListener) {
        // ignore
    }

    @Override
    public void removeModelChangeListener(ModelRepositoryChangeListener modelRepositoryChangeListener) {
        // ignore
    }
}
