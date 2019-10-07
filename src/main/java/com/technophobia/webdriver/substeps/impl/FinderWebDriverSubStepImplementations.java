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


import com.technophobia.substeps.model.SubSteps.Step;
import com.technophobia.substeps.model.SubSteps.StepImplementations;
import com.technophobia.substeps.model.SubSteps.StepParameter;
import com.technophobia.substeps.model.parameter.IntegerConverter;
import com.technophobia.webdriver.substeps.runner.DefaultExecutionSetupTearDown;
import com.technophobia.webdriver.util.WebDriverSubstepsBy;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * set of step implementations concerned with finding elements by various means, ids, attributes, children of, xpaths, tag name, text etc
 */
@StepImplementations(requiredInitialisationClasses = DefaultExecutionSetupTearDown.class)
public class FinderWebDriverSubStepImplementations extends AbstractWebDriverSubStepImplementations {

    private static final Logger logger = LoggerFactory.getLogger(FinderWebDriverSubStepImplementations.class);


    /**
     * Find an element by it's ID
     *
     * @param id the id
     * @return the web element
     * @org.substeps.step.example FindById username
     * @org.substeps.step.section Location
     */
    @Step("FindById ([^\"]*)")
    public WebElement findById(final String id) {

        logger.debug("Looking for item with id " + id);
        webDriverContext().setCurrentElement(null);

        final WebElement elem = webDriverContext().waitForElement(By.id(id));
        Assert.assertNotNull("expecting an element with id " + id, elem);

        webDriverContext().setCurrentElement(elem);
        return elem;
    }


    /**
     * Find an element by it's ID with the specified timeout
     *
     * @param id      the id
     * @param timeout the timeout
     * @return the web element
     * @org.substeps.step.example FindByIdTimeout username timeout = 15 secs
     * @org.substeps.step.section Location
     */
    @Step("FindByIdTimeout ([^\"]*) timeout = ([^\"]*) secs")
    public WebElement findById(final String id, final String timeout) {
        logger.debug("Looking for item with id " + id + "within " + timeout + " seconds");
        final long t = Long.parseLong(timeout);

        webDriverContext().setCurrentElement(null);
        final WebElement elem = webDriverContext().waitForElement(By.id(id), t);
        Assert.assertNotNull("expecting an element with id " + id, elem);
        webDriverContext().setCurrentElement(elem);
        return elem;
    }


    // xpath query //*[contains(@id, 'f41_txt')]
    // $x("//*[@id='sogei_mapping_left_container']")

    // $x("//*[@id='sogei_mapping_left_container']//div[contains(text(),'BANDY')]")  selects the item

    // Handy little ! xpath to get to the chevron to do the whatsit
    // $x("//*[@id='sogei_mapping_left_container']//div[contains(text(),'BASKET')]/ancestor::div[contains(@class, 'mapperItem draggable')]//div[contains(@class, 'chevron')]")

    /**
     * Finds an element using an xpath expression, the expression may contain quotes, unlike an earlier implementation.
     * Xpath expressions should search rather than full paths to elements
     *
     * @param xpath the xpath expression
     * @return the web element found
     * @org.substeps.step.example FindByXpath //li[a/i[contains(@class, "NOT_RUN")]]
     * @org.substeps.step.section Finders
     */

    @Step("FindByXpath (.*)$")
    public WebElement findByXpath(final String xpath) {
        logger.debug("Looking for item with xpath " + xpath);
        webDriverContext().setCurrentElement(null);
        final WebElement elem = webDriverContext().waitForElement(By.xpath(xpath));
        Assert.assertNotNull("expecting an element with xpath " + xpath, elem);
        webDriverContext().setCurrentElement(elem);
        return elem;
    }


    /**
     * Find an element using the name attribute of the element
     *
     * @param name the name
     * @return the web element
     * @org.substeps.step.example FindByName "named field"
     * @org.substeps.step.section Location
     */
    @Step("FindByName \"?([^\"]*)\"?")
    public WebElement findByName(final String name) {
        logger.debug("Looking for item with name " + name);
        webDriverContext().setCurrentElement(null);
        final WebElement elem = webDriverContext().waitForElement(By.name(name));
        Assert.assertNotNull("expecting an element with name " + name, elem);
        webDriverContext().setCurrentElement(elem);
        return elem;
    }


    /**
     * Finds an element on the page with the specified tag and containing the specified text
     *
     * @param tag  the tag
     * @param text the text
     * @org.substeps.step.example FindFirstTagElementContainingText tag="ul"
     * text="list item itext"
     * @org.substeps.step.section Location
     */
    @Step("FindFirstTagElementContainingText tag=\"([^\"]*)\" text=\"([^\"]*)\"")
    public void findFirstTagElementContainingText(final String tag, final String text) {
        logger.debug("Finding tag element " + tag + "and asserting has text " + text);

        webDriverContext().setCurrentElement(null);

        final By by = WebDriverSubstepsBy.ByTagContainingText(tag, text);

        final WebElement elem = MatchingElementResultHandler.AtLeastOneElement.processResults(webDriverContext(), by,
                "expecting at least one child element to contain text: " + text);

        webDriverContext().setCurrentElement(elem);
    }


    /**
     * Finds an element that is a child of the current element using the name
     * attribute, another Find method should be used first
     *
     * @param name the name
     * @return the web element
     * @org.substeps.step.example FindChild ByName name="child name"
     * @org.substeps.step.section Location
     */
    @Step("FindChild ByName name=\"?([^\"]*)\"?")
    public WebElement findChildByName(final String name) {
        logger.debug("Looking for child with name " + name);
        Assert.assertNotNull("expecting a current element", webDriverContext().getCurrentElement());
        final WebElement elem = webDriverContext().getCurrentElement().findElement(By.name(name));

        Assert.assertNotNull("expecting an element with name " + name, elem);
        webDriverContext().setCurrentElement(elem);
        return elem;
    }


    /**
     * Finds an element that is a child of the current element using the tag
     * name and specified attributes, another Find method should be used first
     *
     * @param tag             the tag
     * @param attributeString the attribute string
     * @return the web element
     * @org.substeps.step.example FindChild ByTagAndAttributes tag="input"
     * attributes=[type="submit",value="Search"]
     * @org.substeps.step.section Location
     */
    @Step("FindChild ByTagAndAttributes tag=\"?([^\"]*)\"? attributes=\\[(.*)\\]")
    public WebElement findChildByTagAndAttributes(final String tag, final String attributeString) {
        logger.debug("Looking for child with tag " + tag + " and attributes " + attributeString);

        return findChildByTagAndAttributes(tag, attributeString, MatchingElementResultHandler.ExactlyOneElement);
    }


    private WebElement findChildByTagAndAttributes(final String tag, final String attributeString,
                                                   final MatchingElementResultHandler resultHandler) {

        final WebElement currentElement = webDriverContext().getCurrentElement();

        final By byTagAndAttributes = WebDriverSubstepsBy.ByTagAndAttributes(tag, attributeString);

        final By byCurrentElement = WebDriverSubstepsBy.ByCurrentWebElement(currentElement);

        final By chained = new ByChained(byCurrentElement, byTagAndAttributes);

        final String msg = "failed to locate a child element with tag: " + tag + " and attributes: " + attributeString;

        final WebElement elem = resultHandler.processResults(webDriverContext(), chained, msg);

        webDriverContext().setCurrentElement(elem);

        return elem;
    }


    /**
     * Finds an element that is a child of the current element using the tag
     * name, specified attributes and text, another Find method should be used first
     *
     * @param tag             the tag
     * @param attributeString the attribute string
     * @param text            the text to look for
     * @return the web element
     * @org.substeps.step.example FindChild ByTagAndAttributes tag="input"
     * attributes=[type="submit",value="Search"] with text="bob"
     * @org.substeps.step.section Location
     */
    @Step("FindChild ByTagAndAttributes tag=\"?([^\"]*)\"? attributes=\\[(.*)\\] with text=\"([^\"]*)\"")
    public WebElement findChildByTagAndAttributesWithText(final String tag, final String attributeString, final String text) {
        logger.debug("Looking for child with tag " + tag + " and attributes " + attributeString);

        return findChildByTagAndAttributes(tag, attributeString, text, MatchingElementResultHandler.ExactlyOneElement);
    }


    private WebElement findChildByTagAndAttributes(final String tag, final String attributeString, final String text,
                                                   final MatchingElementResultHandler resultHandler) {

        Assert.assertNotNull("expecting a current element", webDriverContext().getCurrentElement());

        final WebElement currentElement = webDriverContext().getCurrentElement();

        final By byTagAndAttributesWithText = WebDriverSubstepsBy.ByTagAndAttributesWithText(tag, attributeString, text);

        final By byCurrentElement = WebDriverSubstepsBy.ByCurrentWebElement(currentElement);

        final By chained = new ByChained(byCurrentElement, byTagAndAttributesWithText);

        final String msg = "failed to locate a child element with tag: " + tag + ", attributes: " + attributeString + " and text: " + text;

        final WebElement elem = resultHandler.processResults(webDriverContext(), chained, msg);

        webDriverContext().setCurrentElement(elem);

        return elem;
    }

    /**
     * Finds the first child element of the 'current' element using the tag name
     * and specified attributes, another Find method should be used first
     *
     * @param tag             the tag
     * @param attributeString the attribute string
     * @return the web element
     * @org.substeps.step.example FindFirstChild ByTagAndAttributes tag="input"
     * attributes=[type="submit",value="Search"]
     * @org.substeps.step.section Location
     */
    @Step("FindFirstChild ByTagAndAttributes tag=\"?([^\"]*)\"? attributes=\\[(.*)\\]")
    public WebElement findFirstChildByTagAndAttributes(final String tag, final String attributeString) {

        logger.debug("Looking for first child with tag " + tag + " and attributes " + attributeString);

        return findChildByTagAndAttributes(tag, attributeString, MatchingElementResultHandler.AtLeastOneElement);

    }


    /**
     * Finds a checkbox that is a child of the specified tag, that contains the
     * specified text; eg.
     * 
     * &lt;label&gt;<br/>
     * &lt;input type="checkbox" name="checkbox_name" value="yeah"/&gt;a checkbox &lt;span&gt;label&lt;/span&gt;<br/>
     * &lt;/label&gt;<br/>
     *
     * @param tag   the tag
     * @param label the checkbox label
     * @return the web element
     * @org.substeps.step.example FindCheckbox inside tag="label" with label="a checkbox label"
     * @org.substeps.step.section Location
     */
    @Step("FindCheckbox inside tag=\"?([^\"]*)\"? with label=\"([^\"]*)\"")
    public WebElement findCheckBox(final String tag, final String label) {

        return findInputInsideTag(label, tag, "checkbox");

    }


    // todo variant that also has attributes for the tag

    /**
     * Finds a radiobutton that is a child of the specified tag, that contains
     * the specified text; eg.
     *
     * &lt;label&gt;<br/>
     * &lt;input type="radio" name="radio_name" value="yeah"/&gt;a radio &lt;span&gt;label&lt;/span&gt;<br/>
     * &lt;/label&gt;<br/>
     *
     * @param tag   the tag
     * @param label the radio button label
     * @return the web element
     * @org.substeps.step.example FindRadioButton inside tag="label" with label="a radio label"
     * @org.substeps.step.section Location
     */
    @Step("FindRadioButton inside tag=\"?([^\"]*)\"? with label=\"([^\"]*)\"")
    public WebElement findRadioButton(final String tag, final String label) {

        return findInputInsideTag(label, tag, "radio");
    }


    /**
     * Find and input of the specified type within the specified tag that has the specified text
     *
     * @param label     the label of the input
     * @param tag       the tag
     * @param inputType the input type
     * @return the web element
     */
    public WebElement findInputInsideTag(final String label, final String tag, final String inputType) {
        WebElement elem = null;
        webDriverContext().setCurrentElement(null);

        final List<WebElement> matchingElems =
        webDriver().findElements(        By.xpath("//" + tag + "[normalize-space(.)='" + label + "']//input[@type='" + inputType + "']"));

        elem = MatchingElementResultHandler.checkForOneMatchingElement("expecting an input of type " + inputType + " inside tag [" + tag
                + "] with label [" + label + "]", matchingElems);

        webDriverContext().setCurrentElement(elem);
        return elem;
    }


    /**
     * checks that the list of elements is not null or empty
     *
     * @param msg      the assertion message
     * @param tagElems the element list to be checked for empty or null
     */
    public void checkElements(final String msg, final List<WebElement> tagElems) {
        Assert.assertNotNull(msg, tagElems);
        Assert.assertTrue(msg, !tagElems.isEmpty());
    }


    /**
     * Find an element by tag name and a set of attributes and corresponding
     * values
     *
     * @param tag             the tag
     * @param attributeString the attribute string
     * @return the web element
     * @org.substeps.step.example FindByTagAndAttributes tag="input"
     * attributes=[type="submit",value="Search"]
     * @org.substeps.step.section Location
     */
    @Step("FindByTagAndAttributes tag=\"?([^\"]*)\"? attributes=\\[(.*)\\]")
    public WebElement findByTagAndAttributes(final String tag, final String attributeString) {
        logger.debug("Looking for item with tag " + tag + " and attributes " + attributeString);

        return findByTagAndAttributes(tag, attributeString, MatchingElementResultHandler.ExactlyOneElement);
    }


    private WebElement findByTagAndAttributes(final String tag, final String attributeString,
                                              final MatchingElementResultHandler handler) {

        webDriverContext().setCurrentElement(null);

        WebElement rtn = null;

        final By by = WebDriverSubstepsBy.ByTagAndAttributes(tag, attributeString);

        final String msg = "failed to locate an element with tag: " + tag + " and attributes: " + attributeString;

        rtn = handler.processResults(webDriverContext(), by, msg);

        webDriverContext().setCurrentElement(rtn);

        return rtn;
    }


    /**
     * Finds the first element by tag name and a set of attributes and
     * corresponding values
     *
     * @param tag             the tag
     * @param attributeString the attribute string
     * @return the web element
     * @org.substeps.step.example FindFirstByTagAndAttributes tag="input"
     * attributes=[type="submit",value="Search"]
     * @org.substeps.step.section Location
     */
    @Step("FindFirstByTagAndAttributes tag=\"?([^\"]*)\"? attributes=\\[(.*)\\]")
    public WebElement findFirstByTagAndAttributes(final String tag, final String attributeString) {
        logger.debug("Looking for first item with tag " + tag + " and attributes " + attributeString);

        return findByTagAndAttributes(tag, attributeString, MatchingElementResultHandler.AtLeastOneElement);
    }


    /**
     * Finds the n th element by tag name and a set of attributes and
     * corresponding values
     *
     * @param nth             the nth matching element we wish to find
     * @param tag             the tag
     * @param attributeString the attribute string
     * @return the web element
     * @org.substeps.step.example FindNthByTagAndAttributes n=2 tag="input"
     * attributes=[type="submit",value="Search"]
     * @org.substeps.step.section Location
     */
    @Step("FindNthByTagAndAttributes n=\"?([^\"]*)\"? tag=\"?([^\"]*)\"? attributes=\\[(.*)\\]")
    public WebElement findNthByTagAndAttributes(@StepParameter(converter = IntegerConverter.class) final Integer nth,
                                                final String tag, final String attributeString) {
        logger.debug("Looking for nth item with tag " + tag + " and attributes " + attributeString);

        webDriverContext().setCurrentElement(null);

        WebElement rtn = null;

        final By by = WebDriverSubstepsBy.NthByTagAndAttributes(tag, attributeString, nth);

        final String msg = "failed to locate the " + nth + "th element with tag: " + tag
                + " and attributes: " + attributeString;

        final MatchingElementResultHandler.NthElement handler = new MatchingElementResultHandler.NthElement(nth);

        rtn = handler.processResults(webDriverContext(), by, msg);

        webDriverContext().setCurrentElement(rtn);

        return rtn;
    }


    /**
     * Finds an element by tag name and a set of attributes and corresponding
     * values, that has a child tag element of the specified type and having the
     * specified text
     *
     * @param tag             the parent tag
     * @param attributeString the parent attribute string
     * @param childTag        the child tag
     * @param childText       the child's text
     * @return the web element
     * @org.substeps.step.example FindParentByTagAndAttributes tag="table"
     * attributes=[class="mytable"] ThatHasChild tag="caption"
     * text="wahoo"
     * @org.substeps.step.section Location
     */
    @Step("FindParentByTagAndAttributes tag=\"?([^\"]*)\"? attributes=\\[(.*)\\] ThatHasChild tag=\"?([^\"]*)\"? text=\"([^\"]*)\"")
    public WebElement findParentByTagAndAttributesThatHasChildWithTagAndText(final String tag,
                                                                             final String attributeString, final String childTag, final String childText) {

        webDriverContext().setCurrentElement(null);

        WebElement rtn = null;

        final By by = WebDriverSubstepsBy.ByTagAndAttributes(tag, attributeString);

        final By childBy = WebDriverSubstepsBy.ByTagAndWithText(childTag, childText);

        final String assertionMessage = "Failed to locate a parent element with tag: " + tag + " and attributes: "
                + attributeString + " with a child element of tag: " + childTag + " with text: " + childText;

        final String findParentAssertionMessage = "failed to locate an element with tag: " + tag + " and attributes: "
                + attributeString;
        final String multipleChildrenMessage = "More than one child element found for parent with tag: " + childTag
                + " and text: " + childText;

        rtn = findParentByWithChildBy(by, childBy, assertionMessage, findParentAssertionMessage,
                multipleChildrenMessage);
        return rtn;
    }


    /**
     * @param by
     * @param childBy
     * @param assertionMessage
     * @param findParentAssertionMessage
     * @param multipleChildrenMessage
     * @return
     */
    private WebElement findParentByWithChildBy(final By by, final By childBy, final String assertionMessage,
                                               final String findParentAssertionMessage, final String multipleChildrenMessage) {

        boolean repeat = true;
        WebElement rtn = null;
        while (repeat) {

            repeat = false;
            try {
                rtn = findParentWithChildByInternal(by, childBy, assertionMessage, findParentAssertionMessage,
                        multipleChildrenMessage);
            } catch (StaleElementReferenceException e) {

                logger.debug("stale element exception caught, retrying..");
                repeat = true;
            }
        }
        Assert.assertNotNull(assertionMessage, rtn);
        webDriverContext().setCurrentElement(rtn);
        return rtn;
    }


    private WebElement findParentWithChildByInternal(final By by, final By childBy, final String assertionMessage,
                                                     final String findParentAssertionMessage, final String multipleChildrenMessage) {
        WebElement rtn;
        List<WebElement> matchingElements = null;

        final List<WebElement> candidateParentElements = webDriver().findElements(by);

        Assert.assertNotNull(findParentAssertionMessage, candidateParentElements);
        Assert.assertFalse(findParentAssertionMessage, candidateParentElements.isEmpty());

        for (final WebElement parent : candidateParentElements) {

            final List<WebElement> children = parent.findElements(childBy);

            // do we care if there are more than one matching child ? lets go
            // with no..
            if (children != null && !children.isEmpty()) {

                if (matchingElements == null) {
                    matchingElements = new ArrayList<>();
                }
                matchingElements.add(parent);
                if (children.size() > 1) {
                    logger.info(multipleChildrenMessage);
                }
            }
        }
        rtn = MatchingElementResultHandler.checkForOneMatchingElement(assertionMessage, matchingElements);
        return rtn;
    }


    /**
     * Finds an element by tag name and a set of attributes and corresponding
     * values, that has a child tag element of the specified type that has the
     * specified attributes..
     *
     * @param tag                  the parent tag
     * @param attributeString      the parent attribute string
     * @param childTag             the child tag
     * @param childAttributeString the child's attribute string
     * @return the web element
     * @org.substeps.step.example FindParentByTagAndAttributes tag="table"
     * attributes=[class="mytable"] ThatHasChild tag="caption"
     * attributes=[class="childClass"]
     * @org.substeps.step.section Location
     */
    @Step("FindParentByTagAndAttributes tag=\"?([^\"]*)\"? attributes=\\[(.*)\\] ThatHasChild tag=\"?([^\"]*)\"? attributes=\\[(.*)\\]")
    public WebElement findParentByTagAndAttributesThatHasChildWithTagAndAttributes(final String tag,
                                                                                   final String attributeString, final String childTag, final String childAttributeString) {

        webDriverContext().setCurrentElement(null);

        WebElement rtn = null;

        final By by = WebDriverSubstepsBy.ByTagAndAttributes(tag, attributeString);

        final By childBy = WebDriverSubstepsBy.ByTagAndAttributes(childTag, childAttributeString);

        final String assertionMessage = "Failed to locate a parent element with tag: " + tag + " and attributes: "
                + attributeString + " with a child element of tag: " + childTag + " and attributes: "
                + childAttributeString;

        final String findParentAssertionMessage = "failed to locate an element with tag: " + tag + " and attributes: "
                + attributeString;
        final String multipleChildrenMessage = "More than one child element found for parent with tag: " + childTag
                + " and and attributes: " + childAttributeString;

        rtn = findParentByWithChildBy(by, childBy, assertionMessage, findParentAssertionMessage,
                multipleChildrenMessage);
        return rtn;
    }


    /**
     * Gets the element with text.
     *
     * @param type the type
     * @param text the text
     * @return the element with text
     */
    public WebElement findElementWithText(final String type, final String text) {
        WebElement elem = null;
        final List<WebElement> elems = webDriver().findElements(By.tagName(type));
        if (elems != null) {
            for (final WebElement e : elems) {

                if (text.equalsIgnoreCase(e.getText())) {
                    elem = e;
                    break;
                }
            }
        }
        return elem;
    }


    /**
     * Find the element with id that has the text ....
     *
     * @param id       the id
     * @param expected the expected
     * @org.substeps.step.example FindById msg_id and text = "Hello World"
     * @org.substeps.step.section Location
     */
    @Step("FindById ([^\"]*) and text = \"([^\"]*)\"")
    public void findByIdAndText(final String id, final String expected) {
        logger.debug("Finding element with id " + id + " and text " + expected);

        try {
            webDriverContext().setCurrentElement(null);

            final By byIdAndText = WebDriverSubstepsBy.ByIdAndText(id, expected);

            final WebElement elem = webDriverContext().waitForElement(byIdAndText);

            Assert.assertNotNull("expecting to find an element with id: " + id, elem);
            webDriverContext().setCurrentElement(elem);
        } catch (final TimeoutException e) {
            logger.debug("timed out waiting for id: " + id + " with text: " + expected + " page src:\n"
                    + webDriver().getPageSource());
            throw e;
        }
    }


    /**
     * From the current element, apply the xpath selecting the first child that
     * has the text ...
     *
     * @param xpath the xpath expression, NB this is quoted and can't contain double quotes
     * @param text  the text
     * @org.substeps.step.example FindFirstChildElementContainingText xpath="li//a" text =
     * "Log Out"
     * @org.substeps.step.section Location
     */
    @Step("FindFirstChildElementContainingText xpath=\"([^\"]*)\" text=\"([^\"]*)\"")
    public void findChildElementContainingText(final String xpath, final String text) {
        logger.debug("Find child element with xpath " + xpath + " has the text " + text);

        final By byCurrentElement = WebDriverSubstepsBy.ByCurrentWebElement(webDriverContext().getCurrentElement());

        final By chained = new ByChained(byCurrentElement, WebDriverSubstepsBy.ByXpathContainingText(xpath, text));

        webDriverContext().setCurrentElement(null);

        final WebElement elem = MatchingElementResultHandler.AtLeastOneElement.processResults(webDriverContext(),
                chained, "expecting at least one child element to contain text: " + text);

        webDriverContext().setCurrentElement(elem);

    }


    /**
     * Finds the first html tag that starts with the specified text
     *
     * @param tag  the tag
     * @param text the text
     * @org.substeps.step.example FindTagElementStartingWithText tag="ul" text="list item itext"
     * @org.substeps.step.section Location
     */
    @Step("FindFirstTagElementStartingWithText tag=\"([^\"]*)\" text=\"([^\"]*)\"")
    public void findFirstTagElementStartingWithText(final String tag, final String text) {

        logger.debug("findTagElementStartsWithText tag: " + tag + " has text " + text);

        webDriverContext().setCurrentElement(null);

        final By by = WebDriverSubstepsBy.ByTagStartingWithText(tag, text);

        final WebElement elem = MatchingElementResultHandler.AtLeastOneElement.processResults(webDriverContext(), by,
                "expecting at least one child element to contain text: " + text);

        webDriverContext().setCurrentElement(elem);

    }

    /**
     * Find an element by tag name and a set of attributes and expected text
     *
     * @param tag             the tag
     * @param attributeString the attribute string
     * @param text            the expected text
     * @return the web element
     * @org.substeps.step.example FindByTagAndAttributesWithText tag="input" attributes=[type="submit",value="Search"] with text="abc"
     * @org.substeps.step.section Location
     */
    @Step("FindByTagAndAttributesWithText tag=\"?([^\"]*)\"? attributes=\\[(.*)\\] with text=\"([^\"]*)\"")
    public WebElement findByTagAndAttributesWithText(final String tag, final String attributeString, final String text) {
        return findByTagAndAttributesWithText(tag, attributeString, text,
                MatchingElementResultHandler.ExactlyOneElement);
    }

    private WebElement findByTagAndAttributesWithText(final String tag, final String attributeString,
                                                      final String text, final MatchingElementResultHandler handler) {

        webDriverContext().setCurrentElement(null);

        WebElement rtn = null;

        final By by = WebDriverSubstepsBy.ByTagAndAttributesWithText(tag, attributeString, text);

        final String msg = "failed to locate an element with tag: " + tag + ", attributes: " + attributeString
                + ", and text: " + text;

        rtn = handler.processResults(webDriverContext(), by, msg);

        webDriverContext().setCurrentElement(rtn);

        return rtn;
    }


    /**
     * Finds and waits for if necessary, an element by Id containing the specified text
     *
     * @param elementId HTML ID of element
     * @param text      the expected text
     * @org.substeps.step.example FindById id containing text="abc"
     * @org.substeps.step.section Finders
     */
    @Step("FindById ([^\"]*) containing text=\"([^\"]*)\"")
    public void assertEventuallyContains(final String elementId, final String text) {

        waitFor(WebDriverSubstepsBy.ByIdContainingText(elementId, text), String.format("Expected to find a element with 'id=%s' containing text '%s' but didn't.", elementId, text));
    }

}
