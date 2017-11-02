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
package com.technophobia.webdriver.substeps.impl;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.technophobia.substeps.model.Scope;
import com.technophobia.substeps.runner.ExecutionContextSupplier;
import com.technophobia.substeps.runner.MutableSupplier;
import com.technophobia.substeps.runner.ProvidesScreenshot;
import com.technophobia.webdriver.substeps.runner.WebdriverSubstepsPropertiesConfiguration;
import com.technophobia.webdriver.util.WebDriverContext;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base class for webdriver based step implementations, provides number of utility methods for waiting for elements,
 * accessing the webdriver context and the webdriver
 */
public abstract class AbstractWebDriverSubStepImplementations implements ProvidesScreenshot {


    private final MutableSupplier<WebDriverContext> webDriverContextSupplier = new ExecutionContextSupplier<>(
        Scope.SUITE, WebDriverContext.EXECUTION_CONTEXT_KEY);

    /**
     * Wait for an element located using the By, asserting that the element is not null
     *
     * @param by       the By to locate the element by
     * @param messages to be used in the assertion message
     * @return the found WebElement
     */
    protected WebElement waitFor(By by, String... messages) {
        long timeout = WebdriverSubstepsPropertiesConfiguration.INSTANCE.defaultTimeout();
        return waitFor(by, timeout, messages);
    }


    /**
     * Wait for an element located using the By with the specified timeout, asserting that the element is not null
     *
     * @param by       the By to locate the element by
     * @param timeout  the timeout in seconds
     * @param messages to be used in the assertion message
     * @return the found WebElement
     */
    protected WebElement waitFor(By by, long timeout, String... messages) {
        this.webDriverContext().setCurrentElement(null);

        WebElement elem = this.webDriverContext().waitForElement(by, timeout);

        StringBuilder buf = new StringBuilder();

        for (String s : messages) {
            buf.append(s).append(" ");
        }

        Assert.assertNotNull(buf.toString(), elem);
        this.webDriverContext().setCurrentElement(elem);

        return elem;
    }


    /**
     * Utility generic method to wait until the ExpectedCondition
     *
     * @param ec  the expected condition
     * @param <T> the generic type of the expected condition
     * @return the T returned by the ExpectedCondition.apply method
     */
    protected <T> T waitUntil(ExpectedCondition<T> ec) {

        WebDriverWait wait = new WebDriverWait(webDriver(), WebdriverSubstepsPropertiesConfiguration.INSTANCE.defaultTimeout());

        return wait.until(new java.util.function.Function<WebDriver, T>() {

            @Override
            public T apply(WebDriver webDriver) {
                return ec.apply(webDriver);
            }
        });
    }


    /**
     * Utility method to check that the specified WebElement has the expected attributes
     *
     * @param e                  the element who's attributes we want to check
     * @param expectedAttributes a map of the expected attributes
     * @return true or false
     */
    protected boolean elementHasExpectedAttributes(final WebElement e, final Map<String, String> expectedAttributes) {
        final Map<String, String> actualValues = new HashMap<>();

        for (final String key : expectedAttributes.keySet()) {
            final String elementVal = e.getAttribute(key);

            // if no attribute will this throw an exception or just return
            // null ??
            actualValues.put(key, elementVal);
        }

        final MapDifference<String, String> difference = Maps.difference(expectedAttributes, actualValues);
        return difference.areEqual();
    }


    /**
     * Utility method to return the webdriver from the ExecutionContext
     *
     * @return the webdriver bound to the current scope and thread
     */
    protected WebDriver webDriver() {
        return this.webDriverContextSupplier.get().getWebDriver();
    }


    /**
     * Utility method to return the webdriver context from the ExecutionContext in scope and bound to the local thread
     *
     * @return the WebDriverContext
     */
    protected WebDriverContext webDriverContext() {
        return this.webDriverContextSupplier.get();
    }


    /**
     * Utility method to get a screenshot from the webdriver, if it supports it
     *
     * @return the screenshot bytes
     */
    public byte[] getScreenshotBytes() {

        final WebDriver webDriver = webDriver();

        return TakesScreenshot.class.isAssignableFrom(webDriver.getClass()) ? getScreenshotBytes((TakesScreenshot) webDriver)
                : null;
    }


    private byte[] getScreenshotBytes(final TakesScreenshot screenshotTaker) {

        return screenshotTaker.getScreenshotAs(OutputType.BYTES);
    }
}
