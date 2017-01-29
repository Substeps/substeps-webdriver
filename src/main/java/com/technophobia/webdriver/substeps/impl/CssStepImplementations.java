package com.technophobia.webdriver.substeps.impl;

import com.technophobia.substeps.model.SubSteps;
import com.technophobia.webdriver.substeps.runner.DefaultExecutionSetupTearDown;
import com.technophobia.webdriver.util.WebDriverSubstepsBy;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by ian on 24/01/17.
 */
@SubSteps.StepImplementations(requiredInitialisationClasses = DefaultExecutionSetupTearDown.class)
public class CssStepImplementations  extends AbstractWebDriverSubStepImplementations {

    private static final Logger logger = LoggerFactory.getLogger(CssStepImplementations .class);


    /**
     *
     * @example FindByTag "tag" with cssClassRegex ".* marker-\d .*"
     *
     * @param tag the name of the HTML tag
     * @param cssRegex a regex applied to the css class attribute to restrict the selection of the tag
     */
    @SubSteps.Step("FindByTag \"([^\"]*)\" with cssClassRegex \"([^\"]*)\"")
    public WebElement findByTagAndCssWildcard(String tag, String cssRegex){

        By by = WebDriverSubstepsBy.ByTagWithCssClassWildcard(tag, cssRegex);

        return waitFor(by, "Failed to find tag " + tag + " with css class wildcard " + cssRegex);
    }




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


    @SubSteps.Step("FindByTag \"([^\"]*)\" with cssClassRegex \"([^\"]*)\" and text \"([^\"]*)\"")
    public WebElement findByTagAndCssWildcard(String tag, String cssRegex, String text){

        By by = WebDriverSubstepsBy.ByTagWithCssClassWildcardContainingText(tag, cssRegex, text);

        return waitFor(by, "Failed to find tag ", tag, " with css class wildcard ", cssRegex, "with text", text);

    }

    @SubSteps.Step("ClickByCss \"([^\"]*)\" if present")
    public void clickByCssClassNoFail(final String cssClassName) {

        By by = new By.ByClassName(cssClassName);

        // NB. no wait here - // TODO with find by there is a wait here, which we don't want
        List<WebElement> elems = this.webDriver().findElements(by);

        if (!elems.isEmpty()) {

            WebElement elem = elems.get(0);

            if (elem != null && elem.isDisplayed()) {
                elem.click();
            }
        }
    }


    @SubSteps.Step("FindByCss \"([^\"]*)\"")
    public void findWithCssClass(String cssClassName) {

        By by = new By.ByClassName(cssClassName);
        waitFor(by, "expecting an element with class ", cssClassName);

    }


    @SubSteps.Step("FindByCssClass \"([^\"]*)\" with text \"([^\"]*)\"")
    public void findByCssClassWithText(final String cssClass, final String text) {
        By by = WebDriverSubstepsBy.ByCssWithText(cssClass, text);
        waitFor(by, "expecting an element with css class", cssClass, "and text: " + text);
    }

    @SubSteps.Step("FindByCssClass \"([^\"]*)\" containing text \"([^\"]*)\"")
    public void findByCssClassContainingText(final String cssClass, final String text) {
        By by = WebDriverSubstepsBy.ByCssContainingText(cssClass, text);
        waitFor(by, "expecting an element with css class", cssClass, "and text: " + text);
    }

    @SubSteps.Step("FindByCssClass \"([^\"]*)\" using timeout \"(\\d+)\" with text \"([^\"]*)\"")
    public void findByCssClassWithTimeoutAndText(final String cssClass, long timeout, final String text) {
        By by = WebDriverSubstepsBy.ByCssWithText(cssClass, text);
        waitFor(by, timeout, "expecting an element with css class", cssClass, "and text: " + text);
    }

    @SubSteps.Step("FindByCssClass \"([^\"]*)\" using timeout \"(\\d+)\" containing \"([^\"]*)\"")
    public void findByCssClassContainingTimeoutAndText(final String cssClass, long timeout, final String text) {
        By by = WebDriverSubstepsBy.ByCssContainingText(cssClass, text);
        waitFor(by, timeout, "expecting an element with css class", cssClass, "and text: " + text);
    }

    @SubSteps.Step("FindNthByTagAndText with parent css \"([^\"]*)\", tag name \"([^\"]*)\", tag number \"(\\d+)\" containing text \"([^\"]*)\"")
    public void findNthByTagContainingText(String cssClass1, String tagName, int tagNumber, String text) throws InterruptedException {

        WebElement firstCss = null;
        int timeout = 0;
        while (timeout < 10) {
            try {
                firstCss = this.webDriver().findElement(By.cssSelector(cssClass1));
                timeout = 10;
            }
            catch (NoSuchElementException e) {
                logger.info("The element " + cssClass1 + " has not yet been found");
                Thread.sleep(1000);
                timeout++;
            }
        }

        assert firstCss != null;
        // Get the list of elements matching the tag
        List<WebElement> elems = firstCss.findElements(By.tagName(tagName));

        // Get the specific element and check its text
        WebElement e = elems.get(tagNumber);
        String elememtText = e.getText();

        Assert.assertTrue("Found text doesn't match: expected " + text + " but found " + elememtText, elememtText.contains(text));
    }



    @SubSteps.Step("AssertCssSelector \"([^\"]*)\" is not present")
    public void assertCssSelectorIsNotPresent(String cssSelector) {
        if (webDriver().findElements(By.id(cssSelector)).size() > 0) {
            Assert.fail("The element " + cssSelector + " is present when it should not be");
        } else {
            logger.info("The element " + cssSelector + " is correctly not present");
        }
    }

    @SubSteps.Step("AssertCssSelector \"([^\"]*)\" is present")
    public void assertCssSelectorIsPresent(String cssSelector) {
        if (webDriver().findElements(By.cssSelector(cssSelector)).size() != 0) {
            logger.info("The element " + cssSelector + " is present");
        } else {
            Assert.fail("The element " + cssSelector + " is not present when it should be");
        }
    }

    @SubSteps.Step("AssertCssSelector \"([^\"]*)\" has text which contains \"([^\"]*)\"")
    public void assertCssSelectorHasCorrectText(String cssSelector, String expectedText) {
        By by = new By.ByCssSelector(cssSelector);
        waitFor(by, "expecting an element with class ", cssSelector);
        String actualText = webDriver().findElement(By.cssSelector(cssSelector)).getText();
        Assert.assertEquals("Incorrect error message found", expectedText, actualText);
        logger.debug(cssSelector + " " + actualText);
    }

    @SubSteps.Step("FindByCssSelector \"([^\"]*)\"")
    public void findByCssSelector(String cssSelector) {
        By by = new By.ByCssSelector(cssSelector);
        waitFor(by, "expecting an element with class ", cssSelector);
    }


    @SubSteps.Step("Assert number of instances of \"([^\"]*)\" is \"([^\"]*)\" or less")
    public void assertNumberOfElementsIsNotGreaterThanExpectedSize(String cssSelector, int expectedSize) {
        int numberOfElements = webDriver().findElements(By.cssSelector(cssSelector)).size();
        if (expectedSize >= numberOfElements) {
            logger.info(numberOfElements + " instances of " + cssSelector + " were found");
        } else {
            Assert.fail("The number of instances of " + cssSelector + " found was " + numberOfElements + " but expect " + expectedSize + " or fewer");
        }

    }

    @SubSteps.Step("Assert number of instances of \"([^\"]*)\" is greater than \"([^\"]*)\"")
    public void assertNumberOfElementsIsGreaterThanExpectedSize(String cssSelector, int expectedSize) {
        int numberOfElements = webDriver().findElements(By.cssSelector(cssSelector)).size();
        if (expectedSize < numberOfElements) {
            logger.info(numberOfElements + " instances of " + cssSelector + " were found");
        } else {
            Assert.fail("The number of instances of " + cssSelector + " found was " + numberOfElements + " but was expected to be greater than " + expectedSize);
        }

    }

    /**
     * Uses two chained CSS Selector to find an element and set it is as the CurrentElement
     * Example in substeps:
     * Find Chained element by css: first element ".qa-compliance-footer-links", second element ".qa-compliance-legal-age"
     *
     * @param cssSelector1  The first CSS selector
     * @param cssSelector2  The second CSS selector
     *
     * @return WebElement   The element that is found
     *
     */
    @SubSteps.Step("Find Chained element by css: first element \"([^\"]*)\", second element \"([^\"]*)\"")
    public WebElement findByChainedCss(String cssSelector1, String cssSelector2) throws InterruptedException {

        this.webDriverContext().setCurrentElement(null);

        int timeout = 0;
        WebElement firstCss = null;
        while (timeout < 10) {
            try {
                firstCss = this.webDriver().findElement(By.cssSelector(cssSelector1));
                timeout = 10;
            }
            catch (NoSuchElementException e) {
                logger.info("The element " + cssSelector1 + " has not yet been found");
                Thread.sleep(1000);
                timeout++;
            }
        }

        timeout = 0;
        WebElement secondCss = null;
        while (timeout < 10) {
            try {
                assert firstCss != null;
                secondCss = firstCss.findElement(By.cssSelector(cssSelector2));
                timeout = 10;
            }
            catch (NoSuchElementException e) {
                // do nothing
                logger.info("The element " + cssSelector2 + " has not yet been found");
                Thread.sleep(1000);
                timeout++;
            }

        }

        Assert.assertNotNull("expecting an element back from the css selector: " + secondCss, secondCss);
        this.webDriverContext().setCurrentElement(secondCss);
        return secondCss;
    }


    /**
     * Searches for the provided CSS Selector to become visible for up to 3 seconds to allow react DOM changes to complete.
     *
     * @param cssSelector   The cssSelector used in the search
     */
    @SubSteps.Step("WaitFor CSS Selector \"([^\"]*)\" to be visibile")
    public void waitForCssSelector(String cssSelector) {
        waitUntil(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSelector)));
    }

    @SubSteps.Step("WaitFor CSS Selector \"([^\"]*)\" to be invisibile")
    public void waitForCssSelectorToHide(String cssSelector) {

        waitUntil(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(cssSelector)));
    }

}
