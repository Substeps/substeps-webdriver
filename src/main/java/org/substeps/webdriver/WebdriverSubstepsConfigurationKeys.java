package org.substeps.webdriver;

/**
 * Created by ian on 05/06/17.
 */
public interface WebdriverSubstepsConfigurationKeys {

    String ROOT = "org.substeps";
    String WEBDRIVER_EXECUTION_KEY = ROOT + ".executionConfig.webdriver";

    String DRIVER_TYPE_KEY = WEBDRIVER_EXECUTION_KEY + ".driver.type";

    String WEBDRIVER_MANAGER_PROPERTIES_KEY = ROOT + ".webdriver.webdriver.manager.properties";
    String ROOT_WEBDRIVER_KEY = ROOT + ".webdriver";
    String BASE_URL_KEY = ROOT_WEBDRIVER_KEY + ".base.url";
    String DEFAULT_WEBDRIVER_TIMEOUT_KEY = ROOT_WEBDRIVER_KEY + ".default.timeout.secs";

    String WEBDRIVER_REUSE_STRATEGY_KEY = ROOT_WEBDRIVER_KEY + ".reuse-strategy";


    String REMOTE_WEBDRIVER_BASE_CAPABILITY_KEY = WEBDRIVER_EXECUTION_KEY + ".remote.driver.base.capability";
    String REMOTE_WEBDRIVER_PLATFORM_KEY = WEBDRIVER_EXECUTION_KEY + ".remote.driver.platform";
    String REMOTE_WEBDRIVER_BROWSER_VERSION_KEY = WEBDRIVER_EXECUTION_KEY + ".remote.driver.version";
    String REMOTE_WEBDRIVER_URL = WEBDRIVER_EXECUTION_KEY + ".remote.driver.url";

    String NETWORK_PROXY_HOST_KEY = ROOT_WEBDRIVER_KEY + ".network.proxy.host";
    String NETWORK_PROXY_PORT_KEY = ROOT_WEBDRIVER_KEY + ".network.proxy.port";

}
