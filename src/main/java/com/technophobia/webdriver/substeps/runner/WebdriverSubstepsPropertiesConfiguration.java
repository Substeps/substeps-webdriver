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

import com.technophobia.substeps.model.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.substeps.webdriver.WebdriverSubstepsConfigurationKeys;
import org.substeps.webdriver.config.WebdriverSubstepsConfig;

/**
 * Enum formerly used to initialise and access webdriver substeps config.  Since v1.1.0 config is loaded using Typesafe config, but this interface is preserved
 * for backwards compatibility
 */
public enum WebdriverSubstepsPropertiesConfiguration implements WebdriverSubstepsConfigurationKeys {

    INSTANCE; // uninstantiable

    private final Logger LOG = LoggerFactory.getLogger(WebdriverSubstepsPropertiesConfiguration.class);

    WebdriverSubstepsPropertiesConfiguration() {

        if (Configuration.INSTANCE.getConfig().hasPath("webdriver.shutdown") ||
                Configuration.INSTANCE.getConfig().hasPath("visual.webdriver.close.on.fail") ||
                Configuration.INSTANCE.getConfig().hasPath("webdriver.reuse")) {

            LOG.warn("** Webdriver shutdown properties have changed!\nwebdriver.shutdown, visual.webdriver.close.on.fail and webdriver.reuse have all been replaced by:\n" +
                    "org.substeps.webdriver {\n\treuse-strategy = \"shutdown_and_create_new\"\n}\nOther values are resuse_unless_error_keep_visuals_in_error, " +
                    " resuse_unless_error, leave_and_create_new.\nSee org.substeps.webdriver.runner.WebdriverReuseStategy for further details");
        }
    }

    /**
     * @return the base url which can be used to prefix urls in NavigateTo methods, in this way the same tests can be used across different environments
     */
    public String baseURL() {

        return WebdriverSubstepsConfig.determineBaseURL(Configuration.INSTANCE.getString(BASE_URL_KEY));
    }

    /**
     * @return the default timeout that the framework will wait for conditions or elements
     */
    public long defaultTimeout() {
        return Configuration.INSTANCE.getInt(DEFAULT_WEBDRIVER_TIMEOUT_KEY);
    }

}
