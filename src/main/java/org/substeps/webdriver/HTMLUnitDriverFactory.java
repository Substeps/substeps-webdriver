package org.substeps.webdriver;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.technophobia.substeps.model.Configuration;
import com.typesafe.config.Config;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * Created by ian on 12/12/16.
 */
public class HTMLUnitDriverFactory extends BaseDriverFactory implements DriverFactory{

    private static final Logger log = LoggerFactory.getLogger(HTMLUnitDriverFactory.class);

    public static DriverFactoryKey KEY = new DriverFactoryKey("HTMLUNIT", false, HTMLUnitDriverFactory.class);

    @Override
    public DriverFactoryKey getKey() {
        return KEY;
    }

    @Override
    public WebDriver create(Config cfg) {

        log.debug("creating htmlunit driver");

        boolean jsDisabled = cfg.getBoolean("htmlunit.disable.javascript");


        final DesiredCapabilities htmlUnitCapabilities;

        if (jsDisabled){
            htmlUnitCapabilities = DesiredCapabilities.htmlUnit();
        }
        else {
            htmlUnitCapabilities = DesiredCapabilities.htmlUnitWithJs();
        }

        // need to use a different ctor if specifying a browsser version to mimic...BrowserVersion.FIREFOX_45

        final HtmlUnitDriver htmlUnitDriver = new HtmlUnitDriver(htmlUnitCapabilities);

        WebDriverFactoryUtils.setNetworkCapabilities(htmlUnitCapabilities, cfg);

        setDriverLocale(htmlUnitDriver);

        return htmlUnitDriver;
    }

    /**
     * By default the HtmlUnit driver is set to en-us. This can cause problems
     * with formatters.
     */
    private void setDriverLocale(final WebDriver driver) {

        try {
            final Field field = driver.getClass().getDeclaredField("webClient");
            if (field != null) {
                final boolean original = field.isAccessible();
                field.setAccessible(true);

                final WebClient webClient = (WebClient) field.get(driver);
                if (webClient != null) {
                    webClient.addRequestHeader("Accept-Language", "en-gb");
                }
                field.setAccessible(original);
            } else {
                Assert.fail("Failed to get webclient field to set accept language");
            }
        } catch (final IllegalAccessException ex) {

            log.warn(ex.getMessage());

        } catch (final SecurityException e) {

            log.warn(e.getMessage());
        } catch (final NoSuchFieldException e) {

            log.warn(e.getMessage());
        }
    }
}
