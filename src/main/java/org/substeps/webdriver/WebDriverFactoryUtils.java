package org.substeps.webdriver;

import com.typesafe.config.Config;
import org.apache.commons.lang3.StringUtils;
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
public class WebDriverFactoryUtils {

    private static final Logger LOG = LoggerFactory.getLogger(WebDriverFactoryUtils.class);

    public static void setLoggingPreferences(final DesiredCapabilities chromeCapabilities, Config cfg) {
        // TODO switch on based on properties
        final LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.BROWSER, Level.ALL);
        chromeCapabilities.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
    }

    public static void setNetworkCapabilities(final DesiredCapabilities capabilities, Config cfg) {
        final String proxyHost = cfg.getString("network.proxy.host");

        if (StringUtils.isNotEmpty(proxyHost)) {
            final int proxyPort = cfg.getInt("network.proxy.port");
            final String proxyHostAndPort = proxyHost + ":" + proxyPort;
            final org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
            proxy.setHttpProxy(proxyHostAndPort).setFtpProxy(proxyHostAndPort).setSslProxy(proxyHostAndPort);
            capabilities.setCapability(CapabilityType.PROXY, proxy);
            LOG.info("Proxy set to {}", proxyHostAndPort);
        }
    }

}
