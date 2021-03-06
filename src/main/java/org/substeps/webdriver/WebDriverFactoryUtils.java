package org.substeps.webdriver;

import com.typesafe.config.Config;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.logging.Level;

/**
 * Collection of static helper methods concerned with the setup of Webdrivers, network settings, logginng etc.
 * Created by ian on 18/12/16.
 */
public abstract class WebDriverFactoryUtils implements WebdriverSubstepsConfigurationKeys {

    // use the static methods
    private WebDriverFactoryUtils(){}

    private static final Logger logger = LoggerFactory.getLogger(WebDriverFactoryUtils.class);


    /**
     * enable the logging preferences in the supplied webdriver desiredCapabilities
     *
     * @param desiredCapabilities the desired capabilities
     * @param cfg                 the config in scope
     */
    public static void setLoggingPreferences(final DesiredCapabilities desiredCapabilities, Config cfg) {
        // TODO switch on based on properties
        final LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.BROWSER, Level.ALL);
        desiredCapabilities.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
    }



    /**
     * sets up the network capabilities of the driver, eg running via a proxy.
     *
     * @param capabilities the capabilites of the required driver
     * @param cfg          the config in scope
     */
    public static void setNetworkCapabilities(final DesiredCapabilities capabilities, Config cfg) {
        final String proxyHost = cfg.getString(NETWORK_PROXY_HOST_KEY);

        if (StringUtils.isNotEmpty(proxyHost)) {
            final int proxyPort = cfg.getInt(NETWORK_PROXY_PORT_KEY);
            final String proxyHostAndPort = proxyHost + ":" + proxyPort;
            final org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
            proxy.setHttpProxy(proxyHostAndPort).setFtpProxy(proxyHostAndPort).setSslProxy(proxyHostAndPort);
            capabilities.setCapability(CapabilityType.PROXY, proxy);
            logger.info("Proxy set to {}", proxyHostAndPort);
        }
    }

    /**
     * sets the screen size of a webdriver based on config settings
     *
     * @param webdriver the webdriver
     * @param cfg       the config in scope containing the relevant keys to determine the size of the browser window or full screen.
     */
    public static void setScreensize(WebDriver webdriver, Config cfg) {

        try {

            Config windowConfig = cfg.getConfig("org.substeps.webdriver.window");

            if (windowConfig.getBoolean("maximise")) {
                webdriver.manage().window().maximize();
            } else if (windowConfig.hasPath("height") && windowConfig.hasPath("width")) {
                int h = windowConfig.getInt("height");
                int w = windowConfig.getInt("width");
                logger.debug("setting screen size to: w;h " + w + ":" + h);

                webdriver.manage().window().setSize(new Dimension(w, h));
            }
        } catch (WebDriverException e) {
            // best efforts..
            logger.error("failed to set screen size", e);
        }
    }
}
