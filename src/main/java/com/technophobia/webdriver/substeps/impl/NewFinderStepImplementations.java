package com.technophobia.webdriver.substeps.impl;

import com.technophobia.substeps.model.SubSteps;
import com.technophobia.webdriver.substeps.runner.DefaultExecutionSetupTearDown;
import com.technophobia.webdriver.util.WebDriverSubstepsBy;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Additional finder step implementations
 */

@SubSteps.StepImplementations(requiredInitialisationClasses = DefaultExecutionSetupTearDown.class)
public class NewFinderStepImplementations extends AbstractWebDriverSubStepImplementations {

    private static final Logger logger = LoggerFactory.getLogger(NewFinderStepImplementations.class);


    /**
     * Attempts to find an element with a specific id and matching the text against a regex
     *
     * @param id    the id to locate
     * @param regex the regex to test the element's text
     * @return the found web element
     * @example FindById "span-id-with-regex" with text matching regex \w* xyzabc.*
     * @section Finders
     */
    @SubSteps.Step("FindById \"([^\"]*)\" with text matching regex (.*)$")
    public WebElement findByIdWithRegex(String id, String regex) {

        logger.debug("Looking for id with text matching regex " + regex);
        this.webDriverContext().setCurrentElement(null);
        WebElement elem = this.webDriverContext().waitForElement(WebDriverSubstepsBy.ByIdWithTextMatchingRegex(id, regex));
        Assert.assertNotNull("expecting an element with id " + id + " and text that matches regex: " + regex, elem);
        this.webDriverContext().setCurrentElement(elem);

        return elem;

    }

    /**
     * Finds an element using an xpath expression which is merged using placeholders and additional token parameters. NB. the xpath expression is surrounded by $x(".... "),
     * (the same format that can be used directly in Chrome Dev tools) and therefore can't contain double quotes.
     *
     * @param xpath  an expression surrounded by $x(" &lt; xpath &gt; ")
     * @param tokens the tokens if any to be substituted into the xpath string using String format specifiers.  NB. If calling this directly in code, the tokens need to be quoted
     * @return the web element
     * @example FindBy xpath with token replacement $x("//li[a/i[contains(@class, '%s')]]") "FAILED"
     * @section Finders
     */
    @SubSteps.Step("FindBy xpath with token replacement \\$x\\(\"([^\"]*)\"\\)(.*)$")
    public WebElement findByXpathWithTokenReplacement(String xpath, String tokens) {

        logger.debug("findByXpathWithTokenReplacement format String: " + xpath + "   tokens: " + tokens);

        String finalXpath = null;
        if (!tokens.isEmpty()) {

            Pattern matcherPattern = Pattern.compile("\"([^\"]*)\"");

            Matcher matcher = matcherPattern.matcher(tokens);

            ArrayList<String> tokenValues = new ArrayList<>();
            while (matcher.find()) {

                tokenValues.add(matcher.group(1));
            }

            finalXpath = String.format(xpath, tokenValues.toArray());
        } else {
            finalXpath = xpath;
        }

        logger.debug("Looking for item with xpath " + finalXpath);
        this.webDriverContext().setCurrentElement(null);
        WebElement elem = this.webDriverContext().waitForElement(By.xpath(finalXpath));
        Assert.assertNotNull("expecting an element with xpath " + finalXpath, elem);
        this.webDriverContext().setCurrentElement(elem);

        return elem;

    }


    /**
     * Waits for the element with the specified id to become invisible (either visibility or display CSS attributes)
     *
     * @param id the id of the element
     * @example WaitFor id "invisible-div" to be invisible
     * @section Finders
     */
    @SubSteps.Step("WaitFor id \"([^\"]*)\" to hide")
    public void waitForElementToHide(String id) {

        waitUntil(ExpectedConditions.invisibilityOfElementLocated(By.id(id)));


    }


    /**
     * Waits for the element with the specified id to become visible (either visibility or display CSS attributes)
     *
     * @param id the id of the element
     * @example WaitFor id "visible-div" to be visible
     * @section Finders
     */
    @SubSteps.Step("WaitFor id \"([^\"]*)\" to be visible")
    public void waitForElementToBeVisible(String id) {

        waitUntil(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
    }


    /**
     * A step implementation to use when sketching out features and substep defs
     *
     * @example NotImplemented
     * @section Miscellaneous
     */
    @SubSteps.Step("NotImplemented")
    public void notImplementedNotFailing() {
        logger.warn("silent test success...");
    }


    /**
     * Finds an element with the specified tag, with the specified text
     *
     * @param tag  the tag to find
     * @param text the text to match on
     * @example FindByTag "div" with text "some text"
     * @section Finders
     */

    @SubSteps.Step("FindByTag \"([^\"]*)\" with text \"([^\"]*)\"")
    public void findByTagAndText(final String tag, String text) {

        final By by = WebDriverSubstepsBy.ByTagAndWithText(tag, text);

        waitFor(by, "expecting an element with tag", tag, "and text", text);
    }


    /**
     * Finds an element with the specified tag, containing the specified text
     *
     * @param tag  the tag to find
     * @param text the text to partially match on
     * @example FindByTag "span" containing text "xyzabc"
     * @section Finders
     */
    @SubSteps.Step("FindByTag \"([^\"]*)\" containing text \"([^\"]*)\"")
    public void findByTagContainingText(final String tag, String text) {

        final By by = WebDriverSubstepsBy.ByTagContainingText(tag, text);

        waitFor(by, "expecting an element with tag", tag, "and text", text);
    }

}
