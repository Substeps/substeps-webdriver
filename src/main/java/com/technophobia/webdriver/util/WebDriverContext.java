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

import com.technophobia.substeps.model.Scope;
import com.technophobia.substeps.runner.ExecutionContext;
import com.technophobia.webdriver.substeps.runner.Condition;
import com.technophobia.webdriver.substeps.runner.DriverType;
import com.technophobia.webdriver.substeps.runner.WebdriverSubstepsPropertiesConfiguration;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.substeps.webdriver.DriverFactoryKey;

/**
 * A container used to hold the webdriver instance and the current element used
 * by step implementations.  Instances of this class will be bound to a thread local variable
 *
 * @author imoore
 */
public class WebDriverContext {

    private static final Logger logger = LoggerFactory.getLogger(WebDriverContext.class);

    public static final String EXECUTION_CONTEXT_KEY = "_webdriver_context_key";

    private final DriverFactoryKey driverFactoryKey;
    private final WebDriver webDriver;

    private WebElement currentElement = null;
    private boolean failed = false;

    /**
     * default constructor that accepts the DriverFactoryKey and WebDriver as parameters
     *
     * @param driverFactoryKey the DriverFactoryKey associated with this context
     * @param webDriver        the driver associated with this context
     */
    public WebDriverContext(final DriverFactoryKey driverFactoryKey, final WebDriver webDriver) {
        this.driverFactoryKey = driverFactoryKey;
        this.webDriver = webDriver;
    }

    /**
     * @return the current WebElement, asserting that it's not null
     */
    public WebElement getCurrentElement() {
        Assert.assertNotNull("expecting current element not to be null", this.currentElement);
        return this.currentElement;
    }

    /**
     * sets the current element for subsequent use
     *
     * @param currentElement the currently located element
     */
    public void setCurrentElement(final WebElement currentElement) {
        this.currentElement = currentElement;
    }

    /**
     * utility method to return the webdriver
     *
     * @return the webdriver
     */
    public WebDriver getWebDriver() {
        return this.webDriver;
    }

    /**
     * @return the DriverType associated with this context
     */
    public DriverType getDriverType() {
        return this.driverFactoryKey;
    }

    /**
     * @return the DriverFactoryKey
     * @deprecated not used in webdriver-substeps code. This method will be removed in future releases.  Use getDriverType() instead.
     */
    @Deprecated
    public DriverFactoryKey getDriverFactoryKey() {
        return this.driverFactoryKey;
    }

    /**
     * @deprecated use the methods on DriverFactory instead
     */
    @Deprecated
    public void shutdownWebDriver() {
        logger.debug("Shutting WebDriver down");
        if (this.webDriver != null) {
            WebDriverBrowserLogs.printBrowserLogs(webDriver);
            this.webDriver.manage().deleteAllCookies();
            this.webDriver.quit();
        }
    }

    /**
     * @deprecated use the methods on DriverFactory instead
     */
    @Deprecated
    public void resetWebDriver() {
        logger.debug("Resetting WebDriver");
        if (this.webDriver != null) {
            WebDriverBrowserLogs.printBrowserLogs(webDriver);
            this.webDriver.manage().deleteAllCookies();
        }
    }

    /**
     * accessor to determine if this context has failed
     * @return true if a node using this WebDriverContext has failed
     */
    public boolean hasFailed() {
        return this.failed;
    }

    /**
     * mutator to set the failed state of this WebDriverContext
     */
    public void setFailed() {
        this.failed = true;
    }

    /**
     * Convenience method around ElementLocators, using the timeout from config and the webdriver associated with this context.
     * @param by the By used to locate the element
     * @return the Element located or null
     */
    public WebElement waitForElement(final By by) {
        return ElementLocators.waitForElement(by, WebdriverSubstepsPropertiesConfiguration.INSTANCE.defaultTimeout(),
                this.webDriver);
    }

    /**
     * Convenience method around ElementLocators, using the specified timeout and the webdriver associated with this context.
     * @param by the By used to locate the element
     * @param timeOutSeconds the timeout in seconds to wait
     * @return the Element located or null
     */
    public WebElement waitForElement(final By by, final long timeOutSeconds) {
        return ElementLocators.waitForElement(by, timeOutSeconds, this.webDriver);
    }

    /**
     * Convenience method to wait for the given condition.  Will iterate a few times, test the condition, if false, sleep, repeat
     * @param condition the condition to test
     * @return true if the condition was satisfied
     */
    public boolean waitForCondition(final Condition condition) {
        return ElementLocators.waitForCondition(condition);
    }

    /**
     * Convenience method to stash an element in a scenario scoped executionContext
     * @param key the identifier under which the element should be stashed
     * @param element the element to stash
     */
    public void stashElement(final String key, final WebElement element) {

        Object existing = ExecutionContext.get(Scope.SCENARIO, key);

        if (existing != null){
            // do we care ?
            logger.debug("replacing existing object in stash using key: " + key);
        }

        ExecutionContext.put(Scope.SCENARIO, key, element);

    }

    /**
     * Convenience method to retrieve an element from Scenario scoped ExecutionContext.  Null values are asserted on.
     * @param key the key under which elements are stashed
     * @return the stashed WebElement.
     */
    public WebElement getElementFromStash(final String key) {

        WebElement elem = (WebElement)ExecutionContext.get(Scope.SCENARIO, key);

        Assert.assertNotNull("Attempt to retrieve a null element from the stash with key: " + key, elem);

        return elem;
    }
}
