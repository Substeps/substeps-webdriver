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
 * Created by ian on 18/12/16.
 */
public class WebDriverFactoryUtils implements WebdriverSubstepsConfigurationKeys{

    private static final Logger logger = LoggerFactory.getLogger(WebDriverFactoryUtils.class);



    public static void setLoggingPreferences(final DesiredCapabilities chromeCapabilities, Config cfg) {
        // TODO switch on based on properties
        final LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.BROWSER, Level.ALL);
        chromeCapabilities.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
    }

    private static final String NETWORK_PROXY_HOST_KEY = ROOT_WEBDRIVER_KEY + ".network.proxy.host";
    private static final String NETWORK_PROXY_PORT_KEY = ROOT_WEBDRIVER_KEY + ".network.proxy.port";


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

    public static void setScreensize(WebDriver webdriver, Config cfg) {

        try {

            Config windowConfig = cfg.getConfig("org.substeps.webdriver.window");

            if (windowConfig.getBoolean("maximise")) {
                webdriver.manage().window().maximize();
            } else {
                if (windowConfig.hasPath("height") && windowConfig.hasPath("width")) {
                    int h = windowConfig.getInt("height");
                    int w = windowConfig.getInt("width");
                    logger.debug("setting screen size to: w;h " + w + ":" + h);
                    try {
                        webdriver.manage().window().setSize(new Dimension(w, h));
                    } catch (WebDriverException e) {
                        // best efforts to do this
                        logger.error("Failed to set screen size to w;h " + w + ":" + h, e);
                    }
                }
            }
        }
        catch (WebDriverException e) {
            // best efforts..
            logger.error("failed to set screen size", e);
        }
    }
}
