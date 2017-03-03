package org.substeps.webdriver;

import com.technophobia.substeps.model.exception.SubstepsConfigurationException;
import com.typesafe.config.Config;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ian on 12/12/16.
 */
public class RemoteDriverFactory extends BaseDriverFactory implements DriverFactory{

    private static final Logger log = LoggerFactory.getLogger(RemoteDriverFactory.class);

    public static DriverFactoryKey KEY = new DriverFactoryKey("REMOTE", false, RemoteDriverFactory.class);

    @Override
    public DriverFactoryKey getKey() {
        return KEY;
    }

    @Override
    protected WebDriver createInternal(Config cfg) {

        log.debug("creating remote saucelabs driver");

        DesiredCapabilities caps = new DesiredCapabilities(cfg.getString("remote.driver.base.capability"), "", Platform.ANY);

        caps.setCapability("platform", cfg.getString("remote.driver.platform")); // "Linux"
        caps.setCapability("version", cfg.getString("remote.driver.version")); // "48.0"

        URL url;

        try {

            url = new URL(cfg.getString("remote.driver.url"));
        } catch (MalformedURLException e) {
            throw new SubstepsConfigurationException(e);
        }

        return new RemoteWebDriver(url, caps);
    }
}
