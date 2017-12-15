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

import com.google.common.io.Files;
import com.technophobia.substeps.model.Configuration;
import com.technophobia.substeps.model.SubSteps;
import com.technophobia.substeps.model.SubSteps.Step;
import com.technophobia.substeps.model.SubSteps.StepImplementations;
import com.technophobia.webdriver.substeps.runner.Condition;
import com.technophobia.webdriver.substeps.runner.DefaultExecutionSetupTearDown;
import com.technophobia.webdriver.substeps.runner.WebdriverSubstepsPropertiesConfiguration;
import com.technophobia.webdriver.util.WebDriverSubstepsBy;
import org.junit.Assert;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

/**
 * A set of step implementations primarily concerned with navigation, clicks, some assertions etc
 */
@StepImplementations(requiredInitialisationClasses = DefaultExecutionSetupTearDown.class)
public class ActionWebDriverSubStepImplementations extends AbstractWebDriverSubStepImplementations {

    private static final Logger logger = LoggerFactory.getLogger(ActionWebDriverSubStepImplementations.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmm");

    private final FinderWebDriverSubStepImplementations locator = new FinderWebDriverSubStepImplementations();

    /**
     * Navigate to a url, if the url begins with http or file, the url will be
     * used as is, if a relative url is specified then it will be prepended with
     * the base url property
     *
     * @param url the url
     * @org.substeps.step.example NavigateTo /myApp (will navigate to http://localhost/myApp if
     * base.url is set to http://localhost)
     * @org.substeps.step.section Location
     */
    @Step("NavigateTo ([^\"]*)")
    public void navigateTo(final String url) {
        logger.debug("About to navigate to " + url);

        if (url.startsWith("file") || url.startsWith("http")) {
            webDriver().get(url);
        } else {
            webDriver().get(normaliseURL(url));
        }
    }

    /**
     * Navigate to a url specified by a property in the config files
     *
     * @param urlProperty the property to lookup
     * @org.substeps.step.example NavigateTo url property "login.url"
     * @org.substeps.step.section Location
     */
    @SubSteps.Step("NavigateTo url property \"([^\"]*)\"")
    public void navigateToProperty(final String urlProperty) {

        final String url = Configuration.INSTANCE.getString(urlProperty);


        if (url.startsWith("file") || url.startsWith("http")) {

            logger.debug("About to navigate to base url : " + url);

            webDriver().get(url);
        } else {

            String normalised = normaliseURL(url);
            logger.debug("About to navigate to url : " + normalised);

            webDriver().get(normalised);
        }
    }


    /**
     * Find an element by id, then click it.
     *
     * @param id the id
     * @org.substeps.step.example ClickById login
     * @org.substeps.step.section Clicks
     */
    @Step("ClickById ([^\"]*)")
    public void clickById(final String id) {
        logger.debug("About to click item with id " + id);
        this.locator.findById(id);
        click();
    }


    /**
     * Click (the current element)
     *
     * @org.substeps.step.example Click
     * @org.substeps.step.section Clicks
     */
    @Step("Click")
    public void click() {
        logger.debug("About to click on current element");
        clickWhenClickable();
    }

    /**
     * Clicks (the current element) when the element can be clicked (visibility, enabled etc)
     *
     * @org.substeps.step.example ClickWhenClickable
     * @org.substeps.step.section Clicks
     */
    @SubSteps.Step("ClickWhenClickable")
    public void clickWhenClickable() {

        WebElement currentElement = webDriverContext().getCurrentElement();

        waitUntil(ExpectedConditions.elementToBeClickable(currentElement));

        currentElement.click();

    }


    /**
     * Finds a button that contains the specified text, waits until the element is clickable, then clicks it.
     *
     * @param text the text to partially match against the button text
     * @org.substeps.step.example ClickButton containing "button text"
     * @org.substeps.step.section Clicks
     */
    @SubSteps.Step("ClickButton containing \"([^\"]*)\"")
    public void clickButtonContainingText(String text) {

        final By by = WebDriverSubstepsBy.ByTagContainingText("button", text);

        waitFor(by, "expecting an element with tag", "button", "and text", text);

        logger.debug("about to wait until element clickable");
        waitUntil(ExpectedConditions.elementToBeClickable(by));

        logger.debug("element should be clickable");

        this.webDriverContext().getCurrentElement().click();
    }


    /**
     * Click the link "(....)" as it appears on the page
     *
     * @param linkText the link text
     * @org.substeps.step.example ClickLink "Contracts"
     * @org.substeps.step.section Clicks
     */
    @Step("ClickLink \"([^\"]*)\"")
    public void clickLink(final String linkText) {
        logger.debug("About to click link with text " + linkText);
        webDriverContext().setCurrentElement(null);
        final WebElement elem = webDriver().findElement(By.linkText(linkText));
        Assert.assertNotNull("expecting to find a link: " + linkText, elem);
        webDriverContext().setCurrentElement(elem);
        elem.click();
    }


    /**
     * Click a button that has the text, NB surrounding quotes are optional
     *
     * @param buttonText the button text
     * @org.substeps.step.example ClickButton "submit button"
     * @org.substeps.step.section Clicks
     */
    @SubSteps.Step("ClickButton \"?([^\"]*)\"?")
    public void clickButton(String buttonText) {

        final By by = WebDriverSubstepsBy.ByTagAndWithText("button", buttonText);
        waitFor(by, "expecting an element with tag", "button", "and text", buttonText);
        logger.debug("about to wait until element clickable");
        waitUntil(ExpectedConditions.elementToBeClickable(by));
        logger.debug("element should be clickable");
        this.webDriverContext().getCurrentElement().click();
    }

    /**
     * Finds an input element with the specified value, then clicks it.
     *
     * @param buttonText the button text
     * @org.substeps.step.section Form
     * @org.substeps.step.example ClickSubmitButton "Submit"
     */
    @Step("ClickSubmitButton \"([^\"]*)\"")
    public void clickInput(final String buttonText) {
        logger.debug("About to click submit button with text " + buttonText);
        webDriverContext().setCurrentElement(null);

        By by = WebDriverSubstepsBy.ByTagAndAttributes("input", "value=\"" + buttonText + "\"");

        WebElement webElement = waitFor(by, "expecting an input element with value=" + buttonText);

        webElement.click();
    }


    /**
     * Wait for the specified number of milliseconds
     *
     * @param value the value
     * @org.substeps.step.example WaitFor 10
     * @org.substeps.step.section Location
     */
    @Step("WaitFor ([^\"]*)")
    public void waitFor(final String value) {
        logger.debug("About to wait for " + value + "ms");
        final long ms = Long.parseLong(value);
        try {
            Thread.sleep(ms);
        } catch (final InterruptedException e) {
            logger.debug("interupt ex");
            // do we care?
        }
    }


    /**
     * Wait for the page title to change to the specified value
     *
     * @param expectedTitle the expected title
     * @org.substeps.step.example WaitForPageTitle "My Home Page"
     * @org.substeps.step.section Location
     */
    @Step("WaitForPageTitle \"([^\"]*)\"")
    public void waitForPageTitle(final String expectedTitle) {
        logger.debug("Waiting for " + expectedTitle + " page");

        final boolean conditionMet = webDriverContext().waitForCondition(new Condition() {
            public boolean conditionMet() {
                final String pageTitle = webDriver().getTitle();
                logger.debug(String.format("wait for page. Expected='%s', actual='%s'", expectedTitle, pageTitle));
                return pageTitle.equals(expectedTitle);
            }

        });

        if (!conditionMet) {
            logger.debug(expectedTitle + " page not found");
        }

        Assert.assertTrue(conditionMet);
    }


    private String normaliseURL(final String relativeURL) {
        return normalise(WebdriverSubstepsPropertiesConfiguration.INSTANCE.baseURL() + relativeURL);
    }


    private String normalise(final String urlToNormalise) {
        try {
            return new URI(urlToNormalise).toString();
        } catch (final URISyntaxException ex) {
            throw new IllegalStateException("The url " + urlToNormalise + " is invalid.", ex);
        }
    }


    /**
     * Performs a double click on the current element (set with a previous Find
     * method).
     *
     * @org.substeps.step.example PerformDoubleClick
     * @org.substeps.step.section Clicks
     */
    @Step("PerformDoubleClick")
    public void doDoubleClick() {

        final Actions actions = new Actions(webDriver());

        actions.doubleClick(webDriverContext().getCurrentElement());

        actions.perform();
    }


    /**
     * Performs a context click (typically right click, unless this has been
     * changed by the user) on the current element.
     *
     * @org.substeps.step.example PerformContextClick
     * @org.substeps.step.section Clicks
     */
    @Step("PerformContextClick")
    public void performContextClick() {

        final Actions actions = new Actions(webDriver());

        actions.contextClick(webDriverContext().getCurrentElement());

        actions.perform();
    }

    /**
     * Dismisses an Alert with specific text
     *
     * @param message the expected alert text
     * @org.substeps.step.section Clicks
     * @org.substeps.step.example DismissAlert with message "Popup"
     */
    @Step("DismissAlert with message \"([^\"]*)\"")
    public void dismissAlertWithMessage(final String message) {

        // Get a handle to the open alert, prompt or confirmation

        final Alert alert = webDriverContext().getWebDriver().switchTo().alert();
        // this will throw a org.openqa.selenium.NoAlertPresentException if no
        // alert is present

        logger.debug("alert says: " + alert.getText());

        Assert.assertThat(alert.getText(), is(message));
        // And acknowledge the alert (equivalent to clicking "OK")
        alert.accept();
    }

    /**
     * Asserts that the current element is visible, it will wait until this is true
     *
     * @org.substeps.step.example AssertCurrentElement is visible
     * @org.substeps.step.section Assertions
     */
    @Step("AssertCurrentElement is visible")
    public void assertCurrentElementIsVisible() {
        WebElement currentElement = webDriverContext().getCurrentElement();

        waitUntil(ExpectedConditions.visibilityOf(currentElement));
    }

    /**
     * Waits until the current element is invisible, either visibility: hidden or display:none
     *
     * @org.substeps.step.example AssertCurrentElement is invisible
     * @org.substeps.step.section Assertions
     */
    @Step("AssertCurrentElement is invisible")
    public void assertCurrentElementIsInVisible() {
        WebElement currentElement = webDriverContext().getCurrentElement();

        List<WebElement> elems = new ArrayList<>();
        elems.add(currentElement);
        waitUntil(ExpectedConditions.invisibilityOfAllElements(elems));


    }


    /**
     * Invoke the webdriver Javascript executor to run a line of javascript
     *
     * @param js the javascript expression
     * @org.substeps.step.section Actions
     * @org.substeps.step.example ExecuteJavascript document.getElementById("id-for-js-manipulation").innerHTML = "js fiddled"
     */
    @SubSteps.Step("ExecuteJavascript (.*)$")
    public void executeJavaScript(String js) {
        ((JavascriptExecutor) webDriver()).executeScript(js);

    }


    /**
     * Takes a screenshot and writes to a file with the specified prefix, appending a timestamp of the format (yyMMddHHmm)
     *
     * @param filePrefix the filename prefix
     * @org.substeps.step.example TakeScreenshot with prefix "self-test"
     * @org.substeps.step.section Actions
     */
    @SubSteps.Step("TakeScreenshot with prefix \"([^\"]*)\"")
    public void takeScreenshot(String filePrefix) {

        logger.debug("taking screenshot..");

        LocalDateTime timePoint = LocalDateTime.now();

        timePoint.getMinute();

        String formattedDate = timePoint.format(formatter);

        File out = new File(filePrefix + "_" + formattedDate + ".png");

        try {
            Files.write(getScreenshotBytes(), out);
        } catch (IOException e) {
            logger.error("Error taking screenshot", e);
        }
    }

}
