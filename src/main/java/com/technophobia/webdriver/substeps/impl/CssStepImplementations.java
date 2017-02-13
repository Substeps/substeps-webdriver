package com.technophobia.webdriver.substeps.impl;

import com.technophobia.substeps.model.SubSteps;
import com.technophobia.webdriver.substeps.runner.DefaultExecutionSetupTearDown;
import com.technophobia.webdriver.util.WebDriverSubstepsBy;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

/**
 * Created by ian on 24/01/17.
 */
@SubSteps.StepImplementations(requiredInitialisationClasses = DefaultExecutionSetupTearDown.class)
public class CssStepImplementations  extends AbstractWebDriverSubStepImplementations {

    private static final Logger logger = LoggerFactory.getLogger(CssStepImplementations .class);


    /**
     * Finds an element of the specified type and with css classes that match the specified expression
     *
     * @example FindByTag "tag" with cssClassRegex ".* marker-\d .*"
     * @section CSS
     *
     * @param tag the name of the HTML tag
     * @param cssRegex a regex applied to the css class attribute to restrict the selection of the tag
     */
    @SubSteps.Step("FindByTag \"([^\"]*)\" with cssClassRegex \"([^\"]*)\"")
    public WebElement findByTagAndCssWildcard(String tag, String cssRegex){

        By by = WebDriverSubstepsBy.ByTagWithCssClassWildcard(tag, cssRegex);

        return waitFor(by, "Failed to find tag " + tag + " with css class wildcard " + cssRegex);
    }


    /**
     * Finds an element with a css class that matches a regex, relative to the current, previously found element.
     * @example FindChildByTag "div" with cssClassRegex "nested-div-class-\d{4}"
     * @section CSS
     *
     * @param tag the tag of the child element to find
     * @param cssRegex a regex to be matched against the class attribute of the element
     * @return the child element
     */
    @SubSteps.Step("FindChildByTag \"([^\"]*)\" with cssClassRegex \"([^\"]*)\"")
    public WebElement findChildByTagAndCssWildcard(String tag, String cssRegex){

        By by = WebDriverSubstepsBy.ByTagWithCssClassWildcard(tag, cssRegex);

        WebElement parent = webDriverContext().getCurrentElement();

        webDriverContext().setCurrentElement(null);

        // TODO - this won't wait, candidate for chaining the BYs together
        WebElement elem = parent.findElement(by);

        Assert.assertNotNull("Failed to find child with tag: " + tag + " with cssClassRegEx: " + cssRegex, elem);

        webDriverContext().setCurrentElement(elem);

        return elem;
    }

    /**
     * Finds an element of the specified type and with css classes that match the specified expression and containing the specified text
     * @example FindByTag "h4" with cssClassRegex ".* markerClass-\d .*" and containing text "Another heading"
     * @section CSS
     *
     * @param tag the tag name
     * @param cssRegex a regex to test against the class attribute
     * @param text the text to match partially against the text of the element
     * @return the found element
     */
    @SubSteps.Step("FindByTag \"([^\"]*)\" with cssClassRegex \"([^\"]*)\" and containing text \"([^\"]*)\"")
    public WebElement findByTagAndCssWildcard(String tag, String cssRegex, String text){

        By by = WebDriverSubstepsBy.ByTagWithCssClassWildcardContainingText(tag, cssRegex, text);

        return waitFor(by, "Failed to find tag ", tag, " with css class wildcard ", cssRegex, "with text", text);

    }

    /**
     * Click on an element located by css classname if present, NB. this step implementation does not wait for the element to be present
     *
     * @example ClickByCssClass "not-present-button" if present
     * @section CSS
     *
     * @param cssClassName the CSS class of the element to click
     */
    @SubSteps.Step("ClickByCssClass \"([^\"]*)\" if present")
    public void clickByCssClassNoFail(final String cssClassName) {

        By by = new By.ByClassName(cssClassName);

        // NB. no wait here - // with find by there is a wait here, which we don't want
        List<WebElement> elems = this.webDriver().findElements(by);

        if (!elems.isEmpty()) {

            WebElement elem = elems.get(0);

            if (elem != null && elem.isDisplayed()) {
                elem.click();
            }
        }
    }

    /**
     * Find an element that has the specified CSS class (the element can have other classes that apply, but only one can be specified)
     *
     * @example FindByCssClass "markerClass-1"
     * @section CSS
     *
     * @param cssClassName the CSS Classname
     */
    @SubSteps.Step("FindByCssClass \"([^\"]*)\"")
    public void findWithCssClass(String cssClassName) {

        By by = new By.ByClassName(cssClassName);
        waitFor(by, "expecting an element with class ", cssClassName);

    }

    /**
     * Find an element that has the specified CSS class and text(the element can have other classes that apply, but only one can be specified)
     *
     * @example FindByCssClass "somethingElse" with text "Another heading"
     * @section CSS
     *
     * @param cssClass the CSS Classname
     * @param text the expected text
     */
    @SubSteps.Step("FindByCssClass \"([^\"]*)\" with text \"([^\"]*)\"")
    public void findByCssClassWithText(final String cssClass, final String text) {
        By by = WebDriverSubstepsBy.ByCssWithText(cssClass, text);
        waitFor(by, "expecting an element with css class", cssClass, "and text: " + text);
    }

    /**
     * Find an element that has the specified CSS class and contains the text (the element can have other classes that apply, but only one can be specified)
     *
     * @example FindByCssClass "markerClass-2" containing text "Another"
     * @section CSS
     *
     * @param cssClass the CSS Class
     * @param text the text to partially match against
     */
    @SubSteps.Step("FindByCssClass \"([^\"]*)\" containing text \"([^\"]*)\"")
    public void findByCssClassContainingText(final String cssClass, final String text) {
        By by = WebDriverSubstepsBy.ByCssContainingText(cssClass, text);
        waitFor(by, "expecting an element with css class", cssClass, "and text: " + text);
    }

    /**
     * Locates an element with the specified CSS class and text within the specified timeout
     *
     * @example FindByCssClass "two-divs" using timeout "500" with text "2"
     * @section CSS
     *
     * @param cssClass the CSS class of the element to find
     * @param timeoutSecs the timeout in seconds to wait
     * @param text the expected text
     */
    @SubSteps.Step("FindByCssClass \"([^\"]*)\" using timeout \"(\\d+)\" with text \"([^\"]*)\"")
    public void findByCssClassWithTimeoutAndText(final String cssClass, long timeoutSecs, final String text) {
        By by = WebDriverSubstepsBy.ByCssWithText(cssClass, text);
        waitFor(by, timeoutSecs, "expecting an element with css class", cssClass, "and text: " + text);
    }


    /**
     * Locates an element with the specified CSS class and containing the specified text within the specified timeout
     *
     * @example FindByCssClass "two-divs" using timeout "500" containing "1"
     * @section CSS
     *
     * @param cssClass the CSS class of the element to find
     * @param timeoutSecs the timeout in seconds to wait
     * @param text the text to be contained within the element
     */
    @SubSteps.Step("FindByCssClass \"([^\"]*)\" using timeout \"(\\d+)\" containing \"([^\"]*)\"")
    public void findByCssClassContainingTimeoutAndText(final String cssClass, long timeoutSecs, final String text) {
        By by = WebDriverSubstepsBy.ByCssContainingText(cssClass, text);
        waitFor(by, timeoutSecs, "expecting an element with css class", cssClass, "and text: " + text);
    }

    /**
     * Finds a parent element using a CSS Selector, then finds the n th (1 based) instance of particular tag with the expected text
     *
     * @example FindNthByTagAndText from parent by CSSSelector ".parent-div-class", tag name "span", tag number "2" containing text "two"
     * @section CSS
     *
     * @param parentCSSSelector CSS Selector to select the parent element
     * @param tagName tag of the children to find
     * @param tagNumber the nth child to find (1 based)
     * @param text the expected text
     */
    @SubSteps.Step("FindNthByTagAndText from parent by CSSSelector \"([^\"]*)\", tag name \"([^\"]*)\", tag number \"(\\d+)\" containing text \"([^\"]*)\"")
    public void findNthByTagContainingText(String parentCSSSelector, String tagName, int tagNumber, String text){

        WebElement parentElement = waitFor(By.cssSelector(parentCSSSelector), "Unable to find element with " + parentCSSSelector);

        Assert.assertNotNull(parentElement);

        // Get the list of elements matching the tag
        List<WebElement> elems = parentElement.findElements(By.tagName(tagName));

        // Get the specific element and check its text
        WebElement e = elems.get(tagNumber - 1);
        String elementText = e.getText();

        Assert.assertTrue("Found text doesn't match: expected " + text + " but found " + elementText, elementText.contains(text));
    }


    /**
     * Ensures that the CSS Selector returns no results.
     *
     * @example AssertCssSelector ".not-present-button" is not present
     * @section CSS
     *
     * @param cssSelector the CSS selector
     */
    @SubSteps.Step("AssertCssSelector \"([^\"]*)\" is not present")
    public void assertCssSelectorIsNotPresent(String cssSelector) {

        assertNumberOfElementsIsLessThanOrEqualToExpectedSize(cssSelector, 0);
    }

    /**
     * Ensure that the CSS Selector returns some results
     * @example AssertCssSelector ".two-divs" is present
     * @section CSS
     *
     * @param cssSelector
     */
    @SubSteps.Step("AssertCssSelector \"([^\"]*)\" is present")
    public void assertCssSelectorIsPresent(String cssSelector) {

        assertNumberOfElementsIsGreaterThanExpectedSize(cssSelector, 0);
    }

    /**
     * Finds an element by CSS selector with the specified text
     * @example FindByCssSelector ".present-button" with text "clicked"
     * @section CSS
     *
     * @param cssSelector the CSS Selector
     * @param expectedText the expected text
     */
    @SubSteps.Step("FindByCssSelector \"([^\"]*)\" with text \"([^\"]*)\"")
    public void assertCssSelectorHasCorrectText(String cssSelector, String expectedText) {
        By by = new By.ByCssSelector(cssSelector);
        WebElement webElement = waitFor(by, "expecting an element with class ", cssSelector);

        String actualText = webElement.getText();
        Assert.assertEquals("Incorrect error message found", expectedText, actualText);
        logger.debug(cssSelector + " " + actualText);
    }

    /**
     * Finds an element using a css selector; #id, .class for example, more css selectors <a href="http://www.w3schools.com/cssref/css_selectors.asp">here</a>
     * @example FindByCssSelector "#parent_div_id"
     * @section CSS
     *
     * @param cssSelector the css selector
     */
    @SubSteps.Step("FindByCssSelector \"([^\"]*)\"")
    public WebElement findByCssSelector(String cssSelector) {
        By by = new By.ByCssSelector(cssSelector);
        return waitFor(by, "expecting an element with class ", cssSelector);
    }

    /**
     * Checks that the number of elements located by the CSS Selector is less than the specified size
     *
     * @example AssertCssSelector "two-divs" count is "2" or less
     * @section CSS
     *
     * @param cssSelector the CSS Selector
     * @param expectedSize the expected maximum size
     */
    @SubSteps.Step("AssertCssSelector \"([^\"]*)\" count is \"([^\"]*)\" or less")
    public void assertNumberOfElementsIsLessThanOrEqualToExpectedSize(String cssSelector, int expectedSize) {
        int numberOfElements = webDriver().findElements(By.cssSelector(cssSelector)).size();

        Assert.assertThat("number of instances of " + cssSelector + " found", numberOfElements, lessThanOrEqualTo(expectedSize));
    }

    /**
     * Checks that the number of elements located by the CSS Selector is greater than the specified size
     *
     * @example AssertCssSelector "two-divs" count is greater than "1"
     * @section CSS
     *
     * @param cssSelector the CSS Selector
     * @param expectedSize the expected minimum size
     */
    @SubSteps.Step("AssertCssSelector \"([^\"]*)\" count is greater than \"([^\"]*)\"")
    public void assertNumberOfElementsIsGreaterThanExpectedSize(String cssSelector, int expectedSize) {
        int numberOfElements = webDriver().findElements(By.cssSelector(cssSelector)).size();

        Assert.assertThat("number of instances of " + cssSelector + " found", numberOfElements, greaterThan(expectedSize));

    }

    /**
     * Uses two chained CSS Selectors to find an element and set it is as the current element
     *
     * @example Find ByChained CSS selectors ".parent", ".nested"
     * @section CSS
     *
     * @param cssSelector1  The first CSS selector
     * @param cssSelector2  The second CSS selector
     *
     * @return WebElement   The element that is found
     *
     */
    @SubSteps.Step("Find ByChained CSS selectors \"([^\"]*)\", \"([^\"]*)\"")
    public WebElement findByChainedCss(String cssSelector1, String cssSelector2) {

        this.webDriverContext().setCurrentElement(null);

        By chain = new ByChained(By.cssSelector(cssSelector1), By.cssSelector(cssSelector2));

        WebElement element = this.webDriver().findElement(chain);

        Assert.assertNotNull("expecting an element back from the css selectors: " + cssSelector1 + " & " +  cssSelector1);
        this.webDriverContext().setCurrentElement(element);
        return element;

    }


    /**
     * Waits for an element located using the supplied CSS selector to be visible
     * @example WaitFor CSS Selector "#visible-div" to be visibile
     * @section CSS
     *
     * @param cssSelector   The cssSelector used in the search
     */
    @SubSteps.Step("WaitFor CSS Selector \"([^\"]*)\" to be visibile")
    public void waitForCssSelector(String cssSelector) {

        waitUntil(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSelector)));

    }

    /**
     * Waits for an element located using the supplied CSS selector to be invisible
     * @example WaitFor CSS Selector "#visible-div" to be invisibile
     * @section CSS
     *
     *
     * @param cssSelector the CSS selector
     */
    @SubSteps.Step("WaitFor CSS Selector \"([^\"]*)\" to be invisibile")
    public void waitForCssSelectorToHide(String cssSelector) {

        waitUntil(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(cssSelector)));
    }

}
