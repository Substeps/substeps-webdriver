/*
 *  Copyright Technophobia Ltd 2012
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
package com.technophobia.webdriver.util;

import com.technophobia.substeps.model.Configuration;
import com.technophobia.webdriver.substeps.runner.Condition;
import com.technophobia.webdriver.substeps.runner.WebdriverSubstepsPropertiesConfiguration;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

/**
 * Collection of static utility methods to locate elements
 */
public class ElementLocators {

    private static final Logger LOG = LoggerFactory.getLogger(ElementLocators.class);

    private static final int attempts = 10;
    private static final long polltime = 1000;

    /**
     * private to avoid construction, use static methods
     */
    private ElementLocators(){}


    /**
     * Wait for an element to be located using the specified By, with the default timeout specified from config
     * @param by the By to use to locate the element
     * @param webDriver the webdriver used to locate the element
     * @return the WebElement or null.  NB this method won't assert on results
     */
    public static WebElement waitForElement(final By by, WebDriver webDriver) {
        return waitForElement(by, WebdriverSubstepsPropertiesConfiguration.INSTANCE.defaultTimeout(), webDriver);
    }


    /**
     * Waits for a WebElement that can be found using the specified By to exist, or for a timeout to occur.
     *
     * @param by             the by
     * @param timeOutSeconds the time out seconds
     * @param webDriver      the web driver
     * @return the discovered WebElement, or null if it does not exist within the timeout period.
     */
    public static WebElement waitForElement(final By by, final long timeOutSeconds, WebDriver webDriver) {
        final WebDriverWait wait = new WebDriverWait(webDriver, timeOutSeconds);
        final Function<WebDriver, WebElement> condition2 = new Function<WebDriver, WebElement>() {
            public WebElement apply(final WebDriver driver) {
                return driver.findElement(by);
            }
        };

        // Implementations should wait until the condition evaluates to a value
        // that is neither null nor false.
        return waitUntil(wait, condition2, webDriver);
    }


    private static WebElement waitUntil(final WebDriverWait wait, final Function<WebDriver, WebElement> condition,
                                        WebDriver webDriver) {
        WebElement elem = null;
        try {
            elem = wait.until(condition);
        } catch (final TimeoutException e) {

            if (Configuration.INSTANCE.getConfig().getBoolean("org.substeps.webdriver.log.pagesource.onerror")) {
                LOG.debug("timed out page src:\n" + webDriver.getPageSource());
            }
        }
        return elem;
    }

    /**
     * Convenience method to wait for the given condition.  Will iterate a number of times, test the condition, if false, sleep for 1 sec, repeat.  Currently not configurable, but perhaps not widely used.
     * @param condition the condition to test
     * @return true if the condition was satisfied
     */

    public static boolean waitForCondition(final Condition condition) {

        int count = 0;

        while (!condition.conditionMet()) {

            count++;

            try {
                Thread.sleep(polltime);
            } catch (final InterruptedException ignore) {
                // Do Nothing
            }

            if (count == attempts) {
                return false;
            }
        }

        return true;
    }
}
