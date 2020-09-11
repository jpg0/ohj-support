/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
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

package org.openhab.automation.module.script.extension.sitemap;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.openhab.core.model.sitemap.SitemapProvider;
import org.openhab.core.model.sitemap.sitemap.*;

/**
 *
 * @author Jonathan Gilbert - Initial contribution
 */
public class WidgetFactory {

    public static WidgetFactory create() {
        return new WidgetFactory();
    }

    public SitemapProvider newFixedSitemapProvider(List<Sitemap> sitemaps) {
        return new SimpleSitemapProvider(sitemaps);
    }

    public Sitemap newSitemap(String name, String label, String icon, List<Widget> children) {
        Sitemap rv = SitemapFactory.eINSTANCE.createSitemap();
        rv.setName(name);
        rv.setLabel(label);
        rv.setIcon(icon);
        rv.getChildren().addAll(emptyIfNull(children));
        return rv;
    }

    public Text newText(String item, String label, String icon, List<ColorArray> labelColor,
            List<ColorArray> valueColor, List<VisibilityRule> visibility, List<Widget> children) {
        Text rv = SitemapFactory.eINSTANCE.createText();
        fillLinkableWidget(rv, item, label, icon, labelColor, valueColor, visibility, children);
        return rv;
    }

    private void fillLinkableWidget(LinkableWidget widget, String item, String label, String icon,
            List<ColorArray> labelColor, List<ColorArray> valueColor, List<VisibilityRule> visibility,
            List<Widget> children) {
        fillWidget(widget, item, label, icon, labelColor, valueColor, visibility);
        widget.getChildren().addAll(emptyIfNull(children));
    }

    private void fillWidget(Widget widget, String item, String label, String icon, List<ColorArray> labelColor,
            List<ColorArray> valueColor, List<VisibilityRule> visibility) {
        widget.setItem(item);
        widget.setLabel(label);
        widget.setIcon(icon);
        widget.getLabelColor().addAll(emptyIfNull(labelColor));
        widget.getValueColor().addAll(emptyIfNull(valueColor));
        widget.getVisibility().addAll(emptyIfNull(visibility));
    }

    public Frame newFrame(String item, String label, String icon, List<ColorArray> labelColor,
            List<ColorArray> valueColor, List<VisibilityRule> visibility, List<Widget> children) {
        Frame rv = SitemapFactory.eINSTANCE.createFrame();
        fillLinkableWidget(rv, item, label, icon, labelColor, valueColor, visibility, children);
        return rv;
    }

    public Switch newSwitch(String item, String label, String icon, List<ColorArray> labelColor,
            List<ColorArray> valueColor, List<VisibilityRule> visibility, List<Mapping> mappings) {
        Switch rv = SitemapFactory.eINSTANCE.createSwitch();
        fillWidget(rv, item, label, icon, labelColor, valueColor, visibility);
        rv.getMappings().addAll(emptyIfNull(mappings));
        return rv;
    }

    public Selection newSelection(String item, String label, String icon, List<ColorArray> labelColor,
            List<ColorArray> valueColor, List<VisibilityRule> visibility, List<Mapping> mappings) {
        Selection rv = SitemapFactory.eINSTANCE.createSelection();
        fillWidget(rv, item, label, icon, labelColor, valueColor, visibility);
        rv.getMappings().addAll(emptyIfNull(mappings));
        return rv;
    }

    public Webview newWebview(String item, String label, String icon, List<ColorArray> labelColor,
            List<ColorArray> valueColor, List<VisibilityRule> visibility, int height, String url) {
        Webview rv = SitemapFactory.eINSTANCE.createWebview();
        fillWidget(rv, item, label, icon, labelColor, valueColor, visibility);
        rv.setHeight(height);
        rv.setUrl(url);
        return rv;
    }

    public Setpoint newSetpoint(String item, String label, String icon, List<ColorArray> labelColor,
            List<ColorArray> valueColor, List<VisibilityRule> visibility, BigDecimal minValue, BigDecimal maxValue,
            BigDecimal step) {
        Setpoint rv = SitemapFactory.eINSTANCE.createSetpoint();
        fillWidget(rv, item, label, icon, labelColor, valueColor, visibility);
        rv.setMinValue(minValue);
        rv.setMaxValue(maxValue);
        rv.setStep(step);
        return rv;
    }

    public Default newDefault(String item, String label, String icon, List<ColorArray> labelColor,
            List<ColorArray> valueColor, List<VisibilityRule> visibility, int height) {
        Default rv = SitemapFactory.eINSTANCE.createDefault();
        fillWidget(rv, item, label, icon, labelColor, valueColor, visibility);
        rv.setHeight(height);
        return rv;
    }

    public Group newGroup(String item, String label, String icon, List<ColorArray> labelColor,
            List<ColorArray> valueColor, List<VisibilityRule> visibility, List<Widget> children) {
        Group rv = SitemapFactory.eINSTANCE.createGroup();
        fillLinkableWidget(rv, item, label, icon, labelColor, valueColor, visibility, children);
        return rv;
    }

    public Slider newSlider(String item, String label, String icon, List<ColorArray> labelColor,
            List<ColorArray> valueColor, List<VisibilityRule> visibility, int frequency, boolean switchEnabled,
            BigDecimal minValue, BigDecimal maxValue, BigDecimal step) {
        Slider rv = SitemapFactory.eINSTANCE.createSlider();
        fillWidget(rv, item, label, icon, labelColor, valueColor, visibility);
        rv.setFrequency(frequency);
        rv.setSwitchEnabled(switchEnabled);
        rv.setMinValue(minValue);
        rv.setMaxValue(maxValue);
        rv.setStep(step);
        return rv;
    }

    public ColorArray newColorArray(String item, String condition, String sign, String state, String arg) {
        ColorArray rv = SitemapFactory.eINSTANCE.createColorArray();
        rv.setItem(item);
        rv.setCondition(condition);
        rv.setSign(sign);
        rv.setState(state);
        rv.setArg(arg);
        return rv;
    }

    public VisibilityRule newVisibilityRule(String item, String condition, String sign, String state) {
        VisibilityRule rv = SitemapFactory.eINSTANCE.createVisibilityRule();
        rv.setItem(item);
        rv.setCondition(condition);
        rv.setSign(sign);
        rv.setState(state);
        return rv;
    }

    public Mapping newMapping(String cmd, String label) {
        Mapping rv = SitemapFactory.eINSTANCE.createMapping();
        rv.setCmd(cmd);
        rv.setLabel(label);
        return rv;
    }

    /**
     * Helper method to return an empty list if provided one is null.
     *
     * @param list the list
     * @return the provided list or an empty one if it was null
     */
    private static <T> List<T> emptyIfNull(List<T> list) {
        if (list == null) {
            return Collections.emptyList();
        }
        return list;
    }
}
