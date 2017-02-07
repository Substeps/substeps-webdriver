/*
 *	Copyright Technophobia Ltd 2012
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

import static org.hamcrest.CoreMatchers.is;

import java.io.File;
import java.util.List;

import com.technophobia.substeps.model.Configuration;
import com.technophobia.substeps.model.SubSteps;
import com.technophobia.webdriver.substeps.runner.WebdriverSubstepsPropertiesConfiguration;
import com.technophobia.webdriver.util.WebDriverSubstepsBy;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Supplier;
import com.technophobia.substeps.model.SubSteps.Step;
import com.technophobia.substeps.model.SubSteps.StepImplementations;
import com.technophobia.substeps.model.SubSteps.StepParameter;
import com.technophobia.substeps.model.parameter.BooleanConverter;
import com.technophobia.webdriver.substeps.runner.DefaultExecutionSetupTearDown;
import com.technophobia.webdriver.util.WebDriverContext;

@StepImplementations(requiredInitialisationClasses = DefaultExecutionSetupTearDown.class)
public class FormWebDriverSubStepImplementations extends
        AbstractWebDriverSubStepImplementations {

    private static final Logger logger = LoggerFactory
            .getLogger(FormWebDriverSubStepImplementations.class);

    private final FinderWebDriverSubStepImplementations locator;

    private final ActionWebDriverSubStepImplementations actions;


    public FormWebDriverSubStepImplementations() {
        super();
        this.locator = new FinderWebDriverSubStepImplementations();
        this.actions = new ActionWebDriverSubStepImplementations();
    }





    /**
     * Submit the form of the current element. NB using click is preferable as
     * javascript may be executed on click, which this method would bypass
     * 
     * @example Submit
     * @section Clicks
     */
    @Step("Submit")
    public void submit() {
        logger.debug("About to submit the form");
        webDriverContext().getCurrentElement().submit();
    }


    // TODO quote this, or just send the remainder of the line
    /**
     * Enters text to the current element, without clearing any current content
     * first
     * 
     * @example SendKeys hello
     * @section Forms
     * @param value
     *            the value
     */
    @Step("SendKeys \"([^\"]*)\"")
    public void sendKeys(final String value) {
        logger.debug("About to send keys " + value + " to the current element");
        webDriverContext().getCurrentElement().sendKeys(value);
    }

    /**
     * Enters the given key into the current element, without clearing any current content
     * 
     * Note this is to be used for 'special' keys defined by org.openqa.selenium.Keys
     * 
     * @example SendKey Key.RETURN
     * @section Forms
     * @param key a value from {@link Keys}
     */
    @Step("SendKey Key\\.(.+)")
    public void sendKey(final String key) {
        logger.debug("About to send key " + key + " to the current element");
    	webDriverContext().getCurrentElement().sendKeys(Keys.valueOf(key));
    }

    /**
     * Find an element by id, clear any text from the element, and enter text
     * 
     * @example ClearAndSendKeys "fred" to id username
     * @section Forms
     * @param id
     *            the id
     * @param value
     *            the value
     */
    @Step("ClearAndSendKeys \"([^\"]*)\" to id ([^\"]*)")
    public void sendKeysById(final String value, final String id) {
        logger.debug("About to send keys" + value + " to item with id " + id);
        this.locator.findById(id);
        clearAndSendKeys(value);
    }

    /**
     * Clear any text from the element, and enter text (to the current element)
     * 
     * @example ClearAndSendKeys "hello"
     * @section Forms
     * @param value
     *            the value
     */
    @Step("ClearAndSendKeys \"([^\"]*)\"")
    public void clearAndSendKeys(final String value) {
        logger.debug("About to clear the current element and send the keys "
                + value);

        WebDriverWait wait = new WebDriverWait(webDriver(), WebdriverSubstepsPropertiesConfiguration.INSTANCE.defaultTimeout());
        wait.until(ExpectedConditions.visibilityOfElementLocated(WebDriverSubstepsBy.ByCurrentWebElement(webDriverContext().getCurrentElement())));

        webDriverContext().getCurrentElement().clear();
        webDriverContext().getCurrentElement().sendKeys(value);
    }

    /**
     * Select a value in the option list that has the id
     * 
     * @example ChooseOption "fred" in id usersList
     * @section Forms
     * @param value
     *            the value
     * @param id
     *            the id
     */
    @Step("ChooseOption \"([^\"]*)\" in id ([^\"]*)")
    public void selectValueInId(final String value, final String id) {
        logger.debug("About to choose option " + value
                + " in select box with id " + id);

        final WebElement selectElement = this.locator.findById(id);

        chooseOptionByTextInSelect(value, selectElement);

    }


    /**
     * choose an option by visible text within a select
     * @param value the value to be chosen
     * @param selectElement the select element
     */
    public void chooseOptionByTextInSelect(final String value,
            final WebElement selectElement) {
        final Select select = new Select(selectElement);
        select.selectByVisibleText(value);
        Assert.assertTrue("expected value is not selected", select
                .getFirstSelectedOption().isSelected());
        Assert.assertThat("expected value is not selected", value, is(select
                .getFirstSelectedOption().getText()));
    }


    /**
     * Select a value in the option list in the current element, a Find
     * operation is required immediatebly before
     * 
     * @example ChooseOption "fred" in current element
     * @section Forms
     * @param value
     *            the value
     */
    @Step("ChooseOption \"([^\"]*)\" in current element")
    public void selectValueInCurrentElement(final String value) {

        this.actions.click();
        boolean found = false;

        final List<WebElement> options = webDriverContext().getCurrentElement()
                .findElements(By.tagName("option"));
        for (final WebElement e : options) {
            if (e.getText().equalsIgnoreCase(value)) {
                e.click();
                found = true;
                break;
            }
        }

        if (!found) {
            throw new IllegalStateException("failed to locate option in select");
        }
    }


    @Step("AssertSelect id=\"([^\"]*)\" text=\"([^\"]*)\" is currently selected")
    public void assertOptionIsSelected(final String id, final String value) {
        logger.debug("Asserting select box with id " + id + " has option "
                + value + " selected");
        final WebElement select = this.locator.findById(id);

        final List<WebElement> options = getOptions(select);
        for (final WebElement option : options) {

            if (option.getText().equals(value)) {
                Assert.assertTrue("option text: " + value + "is not selected",
                        option.isSelected());
                break;
            }
        }
    }


    @Step("AssertSelect id=\"([^\"]*)\" text=\"([^\"]*)\" is not currently selected")
    public void assertOptionIsNotSelected(final String id, final String value) {
        logger.debug("Asserting select box with id " + id + " has option "
                + value + " not selected");
        final WebElement select = this.locator.findById(id);

        final List<WebElement> options = getOptions(select);
        for (final WebElement option : options) {

            if (option.getText().equals(value)) {
                Assert.assertFalse(option.isSelected());
                break;
            }
        }
    }


    /**
     * Use: FindRadioButton inside tag=&quot;label&quot; with label=&quot;&lt;radio_button_text&gt;"
     * + SetRadioButton checked=&lt;true&gt; in preference as this will locate the
     * radio button by visible text rather than the underlying value.
     * 
     * Locates a radio button with a specific value and checks the radio button.
     * 
     * @example SetRadioButton name=opt_in, value=OFF, checked=true
     * @section Forms
     * @param name
     *            the name
     * @param value
     *            the value
     * @param checked
     *            the checked
     * @deprecated use "SetRadioButton checked=.." instead
     */
    @Deprecated
    @Step("SetRadioButton name =([^\"]*), value =([^\"]*), checked =([^\"]*)")
    public void setRadioButtonValue(final String name, final String value,
            final String checked) {

        throw new IllegalStateException(
                "change code to use SetRadioButton checked= true/false instead");
    }


    /**
     * Sets the value of the current element, assumed to be a radio button to...
     * 
     * @example SetRadioButton checked=true
     * @section Forms
     * @param checked
     *            the checked
     */
    @Step("SetRadioButton checked=([^\"]*)")
    public void setRadioButtonChecked(final String checked) {

        // assumes current element is not null and a radio button
        final WebElement currentElem =  webDriverContext()
                .getCurrentElement();

        AssertionWebDriverSubStepImplementations.assertElementIs(currentElem,
                "input", "radio");

        final boolean val = Boolean.parseBoolean(checked.trim());
        setCheckboxValue(currentElem, val);
    }


    /**
     * Sets the value of the current element, assumed to be a checkbox to...
     * 
     * @example SetCheckedBox checked=true
     * @section Forms
     * @param checked
     *            the checked
     */
    @Step("SetCheckedBox checked=([^\"]*)")
    public void setSetCheckedBoxChecked(final String checked) {

        // assumes current element is not null and a radio button
        final WebElement currentElem =  webDriverContext()
                .getCurrentElement();

        AssertionWebDriverSubStepImplementations.assertElementIs(currentElem,
                "input", "checkbox");

        final boolean val = Boolean.parseBoolean(checked.trim());
        setCheckboxValue(currentElem, val);
    }


    /**
     * Sets the value of a radio button
     * 
     * @example SetRadioButton name="opt_in", text="radio button text"
     * @section Forms
     * @param name
     *            the name
     * @param text
     *            text value
     * @deprecated use SetRadioButton checked=true / false instead with an
     *             apporpriate finder method
     */
    @Deprecated
    @Step("SetRadioButton name=\"([^\"]*)\", text=\"([^\"]*)\"")
    public void setRadioButton(final String name, final String text) {

        throw new IllegalStateException(
                "change code to use SetRadioButton checked=true / false instead");
    }


    /**
     * Asserts a value of a radio button
     * 
     * @example AssertRadioButton name="radio_btn_name", text="text",
     *          checked="true"
     * @section Forms
     * @param name
     *            the name
     * @param text
     *            text value
     * @param checked
     *            true or false to indicate wether the checkbox is checked or
     *            not
     * @deprecated use AssertRadioButton checked=true
     */
    @Deprecated
    @Step("AssertRadioButton name=\"([^\"]*)\", text=\"([^\"]*)\", checked=\"([^\"]*)\"")
    public void assertRadioButton(
            final String name,
            final String text,
            @StepParameter(converter = BooleanConverter.class) final Boolean checked) {

        throw new IllegalStateException(
                "change code to use AssertRadioButton checked= true/false instead");
    }

    /**
     * Sets a check box value; deprecated use
     * 
     * @example SetCheckBox name="accept", checked=true
     * @section Forms
     * @param name
     *            the name
     * @param checked
     *            the checked
     * @deprecated use SetCheckedBox checked= true/false instead
     */
    @Deprecated
    @Step("SetCheckBox name=\"([^\"]*)\", checked=([^\"]*)")
    public void setSetCheckBox(final String name, final String checked) {
        throw new IllegalStateException(
                "change code to use SetCheckedBox checked= true/false instead");
        }


    /**
     * Sets the checkbox value.
     * 
     * @example
     * @param checkboxField
     *            the checkbox field
     * @param value
     *            the value
     */
    protected void setCheckboxValue(final WebElement checkboxField,
            final boolean value) {
        logger.debug("About to set checkbox " + checkboxField + "to "
                + (value ? "checked" : "not checked"));
        if (checkboxField.isSelected() && !value) {
            checkboxField.click();
        } else if (!checkboxField.isSelected() && value) {
            checkboxField.click();
        }
    }


    private List<WebElement> getOptions(final WebElement select) {
        return select.findElements(By.tagName("option"));
    }

    @SubSteps.Step("Select \"([^\"]*)\" option in class \"([^\"]*)\"")
    public void selectOptionInClass(String optionText, String cssClassName) {

        By by = new By.ByClassName(cssClassName);
        WebElement selectElement = waitFor(by, "expecting an element with class ", cssClassName);

        final Select select = new Select(selectElement);
        select.selectByVisibleText(optionText);
    }


    @SubSteps.Step("Select \"([^\"]*)\" option in Id \"([^\"]*)\"")
    public WebElement selectOptionInId(String optionText, String id) throws InterruptedException {

        String xpathString = String.format("//*[@id='%s']//option[ text() ='%s']/..", id, optionText);

        logger.debug("looking for option with xpath: " + xpathString);

        By by = new By.ByXPath(xpathString);

        WebElement selectElement = waitFor(by, "expecting an element with Id ", id);

        final Select select = new Select(selectElement);
        select.selectByVisibleText(optionText);

        int attempt = 0;

        // Make sure text is displayed after the item is selected from the dropdown list
        while (attempt < 3) {
            try {
                WebElement option = select.getFirstSelectedOption();
                String test = option.getText();

                if (test.equals(optionText)) {
                    break;
                }
            }
            catch (NotFoundException e) {
                logger.debug("element not found " + e);
            }

            // Try to select again, sometimes it doesn't do it properly
            logger.debug("Trying to click again");
            select.selectByVisibleText(optionText);

            Thread.sleep(500);
            attempt++;
        }

        return selectElement;
    }


    /**
     * used to pass the path of a file for file uploads
     * @param filePropertyName
     */
    @SubSteps.Step("SendKeys pathOf property \"([^\"]*)\" to current element")
    public void sendKeysToCurrentElement(String filePropertyName) {

        String fileName = Configuration.INSTANCE.getString(filePropertyName);

        logger.debug("csv filename: " + fileName);

        File csvFile = new File(fileName);

        logger.debug("About to send keys " + csvFile.getAbsolutePath() + " to current element");

        WebElement target = this.webDriverContext().getCurrentElement();



        Assert.assertNotNull("target element is null", target);
        target.sendKeys(new CharSequence[]{csvFile.getAbsolutePath()});

    }

    @SubSteps.Step("SendKeys pathOf property \"([^\"]*)\" to id \"([^\"]*)\"")
    public void sendKeysToId(String filePropertyName, String id) {

        String fileName = Configuration.INSTANCE.getString(filePropertyName);

        logger.debug("csv filename: " + fileName);

        File csvFile = new File(fileName);

        logger.debug("About to send keys " + csvFile.getAbsolutePath() + " to id " + id);

        WebElement fileInput = this.webDriver().findElement(By.id(id));

        Assert.assertNotNull("fileInput is null", fileInput);
        fileInput.sendKeys(new CharSequence[]{csvFile.getAbsolutePath()});

    }

}
