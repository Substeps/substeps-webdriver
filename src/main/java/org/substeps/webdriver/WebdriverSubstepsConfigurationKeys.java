package org.substeps.webdriver;

/**
 * Created by ian on 05/06/17.
 */
public interface WebdriverSubstepsConfigurationKeys {

    static final String ROOT = "org.substeps";
    static final String WEBDRIVER_EXECUTION_KEY = ROOT + ".executionConfig.webdriver";

    static final String DRIVER_TYPE_KEY = WEBDRIVER_EXECUTION_KEY + ".driver.type";

    static final String WEBDRIVER_MANAGER_PROPERTIES_KEY = ROOT + ".webdriver.webdriver.manager.properties";
    static final String ROOT_WEBDRIVER_KEY = ROOT + ".webdriver";
    static final String BASE_URL_KEY = ROOT_WEBDRIVER_KEY + ".base.url";
    static final String DEFAULT_WEBDRIVER_TIMEOUT_KEY = ROOT_WEBDRIVER_KEY + ".default.timeout.secs";


    static final String REMOTE_WEBDRIVER_BASE_CAPABILITY_KEY = WEBDRIVER_EXECUTION_KEY + ".remote.driver.base.capability";
    static final String REMOTE_WEBDRIVER_PLATFORM_KEY = WEBDRIVER_EXECUTION_KEY + ".remote.driver.platform";
    static final String REMOTE_WEBDRIVER_BROWSER_VERSION_KEY = WEBDRIVER_EXECUTION_KEY + ".remote.driver.version";
    static final String REMOTE_WEBDRIVER_URL = WEBDRIVER_EXECUTION_KEY + ".remote.driver.url";
}
