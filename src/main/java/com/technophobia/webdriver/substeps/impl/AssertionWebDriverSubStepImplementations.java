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


import com.technophobia.substeps.model.Scope;
import com.technophobia.substeps.model.SubSteps.Step;
import com.technophobia.substeps.model.SubSteps.StepImplementations;
import com.technophobia.substeps.runner.ExecutionContext;
import com.technophobia.substeps.step.StepImplementationUtils;
import com.technophobia.webdriver.substeps.runner.DefaultExecutionSetupTearDown;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Function;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

/**
 * set of step implementations primarily concerned with various assertions
 */
@StepImplementations(requiredInitialisationClasses = DefaultExecutionSetupTearDown.class)
public class AssertionWebDriverSubStepImplementations extends AbstractWebDriverSubStepImplementations {

    private static final Logger logger = LoggerFactory.getLogger(AssertionWebDriverSubStepImplementations.class);

    private final FinderWebDriverSubStepImplementations finder = new FinderWebDriverSubStepImplementations();


    /**
     * Check that the current element has the expected text value
     *
     * @param expected the expected text
     * @org.substeps.step.example AssertCurrentElement text="Hello World!"
     * @org.substeps.step.section Assertions
     */
    @Step("AssertCurrentElement text=\"([^\"]*)\"")
    public void assertTextInCurrentElement(final String expected) {
        logger.debug("Asserting the current element has the text " + expected);
        Assert.assertThat(webDriverContext().getCurrentElement().getText(), is(expected));
    }


    /**
     * Check that the current input field has the expected text value
     *
     * @param expected the expected value
     * @org.substeps.step.example AssertCurrentInput value="Hello World!"
     * @org.substeps.step.section Assertions
     */
    @Step("AssertCurrentInput value=\"([^\"]*)\"")
    public void assertValueInCurrentInput(final String expected) {
        logger.debug("Asserting the current input has the value" + expected);
        Assert.assertThat(webDriverContext().getCurrentElement()
                .getAttribute("value"), is(expected));
    }

    /**
     * Check that the current input field contains the expected text value
     *
     * @param expected part of the expected value
     * @org.substeps.step.example AssertCurrentInput value contains "Hello"
     * @org.substeps.step.section Assertions
     */
    @Step("AssertCurrentInput value contains \"([^\"]*)\"")
    public void assertCurrentInputContainsText(final String expected) {
        logger.debug("Asserting the current input contains the value" + expected);
        Assert.assertThat(webDriverContext().getCurrentElement()
                .getAttribute("value"), containsString(expected));
    }


    /**
     * Check that the current element contains the specified text
     *
     * @param expected the expected text
     * @org.substeps.step.example AssertCurrentElement text contains "Hello world"
     * @org.substeps.step.section Assertions
     */
    @Step("AssertCurrentElement text contains \"([^\"]*)\"")
    public void assertTextInCurrentElementContains(final String expected) {
        logger.debug("Asserting current element contains the text " + expected);
        Assert.assertThat(webDriverContext().getCurrentElement().getText(), containsString(expected));
    }


    /**
     * Check that the current element has the specified attribute and value
     *
     * @param attribute the attribute name
     * @param expected  the expected value of the attribute
     * @org.substeps.step.example AssertCurrentElement attribute="class" value="icon32x32"
     * @org.substeps.step.section Assertions
     */
    @Step("AssertCurrentElement attribute=\"([^\"]*)\" value=\"([^\"]*)\"")
    public void assertAttributeInCurrentElement(final String attribute, final String expected) {
        logger.debug("Asserting current element has the attribute " + attribute + "with value " + expected);

        final String attributeValue = webDriverContext().getCurrentElement().getAttribute(attribute);
        Assert.assertNotNull("Expecting to find attribute " + attribute + " on current element", attributeValue);
        Assert.assertThat(attributeValue, is(expected));
    }


    /**
     * Check that the page title is ....
     *
     * @param expectedTitle the expected title
     * @org.substeps.step.example AssertPageTitle is "My Home Page"
     * @org.substeps.step.section Assertions
     */
    @Step("AssertPageTitle is \"([^\"]*)\"")
    public void assertPageTitle(final String expectedTitle) {
        logger.debug("Asserting the page title is " + expectedTitle);
        Assert.assertEquals("unexpected page title", expectedTitle, webDriver().getTitle());
    }


    /**
     * Simple text search on page source
     *
     * @param expected the text you expect to find in the page source - this can
     *                 include quotes.
     * @org.substeps.step.example AssertPageSourceContains "foobar"
     * @org.substeps.step.section Assertions
     */
    @Step("AssertPageSourceContains \"(.*)\"$")
    public void pageSourceContains(final String expected) {
        logger.debug("Checking page source for expeted content [" + expected + "]");

        final String pageSource = webDriver().getPageSource();

        Assert.assertThat(pageSource, containsString(expected));
    }


    /**
     * Check that the current element, a checkbox is checked or not
     *
     * @param checkedString whether the radio button is checked or not
     * @org.substeps.step.example AssertCheckBox checked=true/false
     * @org.substeps.step.section Assertions
     */
    @Step("AssertCheckBox checked=\"?([^\"]*)\"?")
    public void assertCheckBoxIsChecked(final String checkedString) {

        // check that the current element is not null and is a radio btn
        final WebElement currentElem = webDriverContext().getCurrentElement();

        assertElementIs(currentElem, "input", "checkbox");

        // check the state
        final boolean checked = Boolean.parseBoolean(checkedString.trim());
        if (checked) {
            Assert.assertTrue("expecting checkbox to be checked", currentElem.isSelected());
        } else {
            Assert.assertFalse("expecting checkbox not to be checked", currentElem.isSelected());
        }
    }


    /**
     * Check that the current element, a radio button, is checked or not
     *
     * @param checkedString whether the radio button is checked or not
     * @org.substeps.step.example AssertRadioButton checked=true/false
     * @org.substeps.step.section Assertions
     */
    @Step("AssertRadioButton checked=\"?([^\"]*)\"?")
    public void assertRadioButtonIsChecked(final String checkedString) {

        // check that the current element is not null and is a radio btn
        final WebElement currentElem = webDriverContext().getCurrentElement();

        assertElementIs(currentElem, "input", "radio");

        // check the state
        final boolean checked = Boolean.parseBoolean(checkedString.trim());
        if (checked) {
            Assert.assertTrue("expecting radio button to be checked", currentElem.isSelected());
        } else {
            Assert.assertFalse("expecting radio button not to be checked", currentElem.isSelected());
        }
    }


    /**
     * Check that the current element has the specified attributes
     *
     * @param attributeString comma separated list of attributes and quoted values
     * @org.substeps.step.example AssertCurrentElement has
     * attributes=[type="submit",value="Search"]
     * @org.substeps.step.section Assertions
     */

    @Step("AssertCurrentElement has attributes=\\[(.*)\\]")
    public void assertCurrentElementHasAttributes(final String attributeString) {

        final WebElement currentElem = webDriverContext().getCurrentElement();

        final Map<String, String> expectedAttributes = StepImplementationUtils.convertToMap(attributeString);

        Assert.assertTrue("element doesn't have expected attributes: " + attributeString,
                elementHasExpectedAttributes(currentElem, expectedAttributes));

    }


    /**
     * Utility method to check that an element is of a particular tag and type
     *
     * @param elem the element
     * @param tag  the expected tag
     * @param type the expected type attribute
     */
    public static void assertElementIs(final WebElement elem, final String tag, final String type) {

        Assert.assertNotNull("expecting an element", elem);
        Assert.assertTrue("unexpected tag", elem.getTagName() != null
                && elem.getTagName().compareToIgnoreCase(tag) == 0);

        if (type != null) {
            Assert.assertTrue("unexpected type", elem.getAttribute("type") != null
                    && elem.getAttribute("type").compareToIgnoreCase(type) == 0);
        }
    }


    /**
     * Utility method to check that an element is of a particular tag
     *
     * @param elem the element to check
     * @param tag  the expected tag
     */
    public static void assertElementIs(final WebElement elem, final String tag) {
        assertElementIs(elem, tag, null);
    }


    /**
     * Grab the text of an element (identified by id) and save it for the
     * duration of this scenario
     *
     * @param elementId    The ID of the HTML element
     * @param nameToSaveAs The variable name to save the text as for later retrieval
     * @org.substeps.step.section Assertions
     * @org.substeps.step.example RememberForScenario textFrom "projectName" as "savedProjectName"
     */
    @Step("RememberForScenario textFrom \"([^\"]*)\" as \"([^\"]*)\"")
    public void rememberForScenario(final String elementId, final String nameToSaveAs) {

        final WebElement element = this.finder.findById(elementId);
        final String text = element.getText();
        ExecutionContext.put(Scope.SCENARIO, nameToSaveAs, text);
    }


    /**
     * Compare the text of an element (identified by ID) to a value previously
     * remembered
     *
     * @param rememberedValueName The variable name to save the text as for later retrieval
     * @param elementId           The ID of the HTML element
     * @org.substeps.step.example AssertDifferent rememberedValue "savedProjectName"
     * compareToElement "projectName"
     * @org.substeps.step.section Assertions
     */
    @Step("AssertDifferent rememberedValue \"([^\"]*)\" compareToElement \"([^\"]*)\"")
    public void assertDifferent(final String rememberedValueName, final String elementId) {

        final WebElement element = this.finder.findById(elementId);
        final String text = element.getText();

        Object retrievedValue = null;
        for (final Scope scope : Scope.values()) {
            final Object valueFromScope = ExecutionContext.get(scope, rememberedValueName);
            if (valueFromScope != null) {
                retrievedValue = valueFromScope;
            }
        }

        Assert.assertNotNull("remembered value is null", retrievedValue);
        Assert.assertFalse("The remembered value was different to the text of the element compared against",
                retrievedValue.equals(text));
    }


    /**
     * Compare the text of an element (identified by ID) to a value previously
     * remembered - assert they're the same
     *
     * @param elementId           The ID of the HTML element
     * @param rememberedValueName The variable name to save the text as for later retrieval
     * @org.substeps.step.example AssertSame rememberedValue "savedProjectName" compareToElement
     * "projectName"
     * @org.substeps.step.section Assertions
     */
    @Step("AssertSame rememberedValue \"([^\"]*)\" compareToElement \"([^\"]*)\"")
    public void assertSame(final String rememberedValueName, final String elementId) {

        final WebElement element = this.finder.findById(elementId);
        final String text = element.getText();

        Object retrievedValue = null;
        for (final Scope scope : Scope.values()) {
            final Object valueFromScope = ExecutionContext.get(scope, rememberedValueName);
            if (valueFromScope != null) {
                retrievedValue = valueFromScope;
            }
        }
        Assert.assertNotNull("remembered value is null", retrievedValue);

        Assert.assertEquals("The remembered value was different to the text of the element compared against",
                retrievedValue.toString(), text);
    }


    /**
     * Assert that the specified text is not found within the page source
     *
     * @param text the text that shouldn't be present
     * @org.substeps.step.example AssertNotPresent text="undesirable text"
     * @org.substeps.step.section Assertions
     */
    @Step("AssertNotPresent text=\"([^\"]*)\"")
    public void assertNotPresent(final String text) {

        final String pageSource = webDriver().getPageSource();
        Assert.assertThat(pageSource, not(containsString(text)));
    }


    /**
     * Wait for an element to contain some (any) text
     *
     * @param by WebDriver By object that identifies the element
     * @return the web element
     * @org.substeps.step.example
     */
    public WebElement waitForElementToContainSomeText(final By by) {

        final WebDriverWait wait = new WebDriverWait(webDriver(), 10);
        final Function<WebDriver, WebElement> condition2 = new Function<WebDriver, WebElement>() {
            public WebElement apply(final WebDriver driver) {
                final WebElement rtn = driver.findElement(by);

                final String potentialVal = rtn.getText();

                if (potentialVal != null) {
                    return rtn;
                }

                return null;
            }
        };

        return wait.until(condition2);
    }

}
