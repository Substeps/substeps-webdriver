/*
 *    Copyright Technophobia Ltd 2012
 *
 *   This file is part of Substeps.
 *
 *    Substeps is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    Substeps is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with Substeps.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.technophobia.webdriver.substeps.runner;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.technophobia.substeps.model.Configuration;
import org.substeps.webdriver.runner.WebdriverReuseStategy;

public enum WebdriverSubstepsPropertiesConfiguration implements WebdriverSubstepsConfiguration {

    INSTANCE ; // uninstantiable

    private final Logger LOG = LoggerFactory.getLogger(WebdriverSubstepsPropertiesConfiguration.class);

    private final String baseUrl;
    private final String driverLocale;
    private final String htmlUnitProxyHost;
    private final boolean htmlunitDisableJs;
    private final Integer htmlUnitProxyPort;
    private final String networkProxyHost;
    private final int networkProxyPort;


    private long defaultWebDriverTimeoutSecs;

    private WebdriverReuseStategy reuseStrategy;

    private WebdriverSubstepsPropertiesConfiguration() {

        baseUrl = determineBaseURL(Configuration.INSTANCE.getString("base.url"));

        driverLocale = Configuration.INSTANCE.getString("webdriver.locale");

        defaultWebDriverTimeoutSecs = Configuration.INSTANCE.getInt("default.webdriver.timeout.secs");

        htmlunitDisableJs = Configuration.INSTANCE.getBoolean("htmlunit.disable.javascript");

        htmlUnitProxyHost = Configuration.INSTANCE.getString("htmlunit.proxy.host");
        htmlUnitProxyPort = Configuration.INSTANCE.getInt("htmlunit.proxy.port");
        networkProxyHost = Configuration.INSTANCE.getString("network.proxy.host");
        networkProxyPort = Configuration.INSTANCE.getInt("network.proxy.port");


        reuseStrategy = WebdriverReuseStategy.fromConfig(Configuration.INSTANCE.getConfig());

        if (Configuration.INSTANCE.getConfig().hasPath("webdriver.shutdown") ||
                Configuration.INSTANCE.getConfig().hasPath("visual.webdriver.close.on.fail") ||
                Configuration.INSTANCE.getConfig().hasPath("webdriver.reuse") ){

            LOG.warn("** Webdriver shutdown properties have changed!\nwebdriver.shutdown, visual.webdriver.close.on.fail and webdriver.reuse have all been replaced by:\n" +
                    "org.substeps.webdriver {\n\treuse-strategy = \"shutdown_and_create_new\"\n}\nOther values are resuse_unless_error_keep_visuals_in_error, " +
                    " resuse_unless_error, leave_and_create_new.\nSee org.substeps.webdriver.runner.WebdriverReuseStategy for further details");

        }

        LOG.info("Using properties:\n" + Configuration.INSTANCE.getConfigurationInfo());
    }


    public String baseURL() {
        return baseUrl;
    }


    public String driverLocale() {
        return driverLocale;
    }

    public boolean isJavascriptDisabledWithHTMLUnit() {
        return htmlunitDisableJs;
    }

    public long defaultTimeout() {
        return defaultWebDriverTimeoutSecs;
    }


    public String getHtmlUnitProxyHost() {
        return htmlUnitProxyHost;
    }


    public Integer getHtmlUnitProxyPort() {
        return htmlUnitProxyPort;
    }

    public String getNetworkProxyHost() {
        return networkProxyHost;
    }

    public int getNetworkProxyPort() {
        return networkProxyPort;
    }

    private String determineBaseURL(final String baseUrlProperty) {

        final String resolvedBaseUrl;
        final String property = removeTrailingSlash(baseUrlProperty);

        if (!property.startsWith("http") && !property.startsWith("file://")) {

            resolvedBaseUrl = removeTrailingSlash(new File(property).toURI()
                    .toString());
        } else {
            resolvedBaseUrl = property;
        }

        return resolvedBaseUrl;
    }


    private String removeTrailingSlash(final String string) {
        if (string.endsWith("/")) {
            return string.substring(0, string.length() - 1);
        }
        return string;
    }


    public WebdriverReuseStategy getReuseStrategy() {
        return reuseStrategy;
    }



}
