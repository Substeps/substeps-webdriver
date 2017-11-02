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

import com.technophobia.substeps.step.StepImplementationUtils;
import org.hamcrest.Matchers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Map;

/**
 * A wrapper class around some static convenience methods that provide a little sugar around the use of webdriver substeps custom Bys.  Pattern borrowed from the selenium code base
 *
 * @author imoore
 */
public abstract class WebDriverSubstepsBy {

    // use static methods
    private WebDriverSubstepsBy(){}
    /**
     *
     * By to locate an element using an id and with expected text (case insensitive)
     * @param id the element id
     * @param text the text value
     * @return the By instance
     *
     * @see ByIdAndText
     */
    public static ByIdAndText ByIdAndText(final String id, final String text) {
        return new ByIdAndText(id, text);
    }

    /**
     *
     * By to locate an element using an id and with expected text (case sensitive)
     * @param id the element id
     * @param text the text value
     * @return the By instance
     *
     * @see ByIdAndText
     */
    public static ByIdAndText ByIdAndCaseSensitiveText(final String id, final String text) {
        return new ByIdAndText(id, text, true);
    }

    /**
     * By to locate an element using tag name and a map of expected attributes.
     * @param tagName the tag name
     * @param requiredAttributes a map of attributes
     * @return the By instance
     *
     * @see ByTagAndAttributes
     */
    public static ByTagAndAttributes ByTagAndAttributes(final String tagName,
                                                        final Map<String, String> requiredAttributes) {
        return new ByTagAndAttributes(tagName, requiredAttributes);
    }

    /**
     * By to locate an element using tag name and string representation of expected attributes
     * @param tagName the tag name
     * @param attributeString the string representation of expected attributes (key1=value1,key2=value2)
     * @return the By instance
     *
     * @see ByTagAndAttributes
     */
    public static ByTagAndAttributes ByTagAndAttributes(final String tagName, final String attributeString) {

        final Map<String, String> expectedAttributes = StepImplementationUtils.convertToMap(attributeString);

        return new ByTagAndAttributes(tagName, expectedAttributes);
    }

    /**
     * By to locate an element using tag name and string representation of expected attributes, ensuring that at least n are found
     * @param tagName the tag name
     * @param attributeString the string representation of expected attributes (key1=value1,key2=value2)
     * @param nth the minimum number of matching elements expected
     * @return the By instance
     *
     * @see ByTagAndAttributes
     */
    public static ByTagAndAttributes NthByTagAndAttributes(final String tagName, final String attributeString,
                                                           final int nth) {

        final Map<String, String> expectedAttributes = StepImplementationUtils.convertToMap(attributeString);

        return new ByTagAndAttributes(tagName, expectedAttributes, nth);
    }

    /**
     * A By for use with the current web element, to be chained with other Bys, for example for finding children of this element
     * @param elem the element to use
     * @return the By instance
     *
     * @see ByCurrentWebElement
     */
    public static ByCurrentWebElement ByCurrentWebElement(final WebElement elem) {
        return new ByCurrentWebElement(elem);
    }

    /**
     * By to locate an element using tag name and a with expected text
     * @param tag the tag name
     * @param text the text value
     * @return the By instance
     *
     * @see ByTagAndWithText
     */
    public static ByTagAndWithText ByTagAndWithText(final String tag, final String text) {
        return new ByTagAndWithText(tag, text);
    }

    /**
     * By to match by tag name and where the element contains the specified text
     * @param tag the tag name
     * @param text the text that should be contained within the element's text value
     * @return the By instance
     *
     * @see ByTagAndContainingText
     */
    public static ByTagAndWithText ByTagContainingText(final String tag, final String text) {
        return new ByTagAndContainingText(tag, text);
    }

    /**
     * By to locate elements by tag name whose text value starts with the specified string
     * @param tag the tag name
     * @param text the requisite prefix
     * @return the By instance
     *
     * @see ByTagAndStartingWithText
     */
    public static ByTagAndWithText ByTagStartingWithText(final String tag, final String text) {
        return new ByTagAndStartingWithText(tag, text);
    }

    /**
     * A By to find an element by id and containing the specified string
     * @param id the id of the element
     * @param text string that should be contained in the element's text
     * @return the By instance
     *
     * @see ByIdContainingText
     */
    public static ByIdContainingText ByIdContainingText(final String id, final String text) {
        return new ByIdContainingText(id, text);
    }

    /**
     * A By to locate elements using an xpath that also contain the specified text, using a singular xpath expression might be better
     *
     * @param xpath the xpath expression
     * @param text the text to be contained within the element's text
     * @return the By instance
     *
     * @see BySomethingContainingText
     */
    public static BySomethingContainingText ByXpathContainingText(final String xpath, final String text) {
        return new BySomethingContainingText(By.xpath(xpath), text);
    }

    /**
     * A By to locate elements by tag name and apply a regex against the elements css classes
     * @param tagName the tagname
     * @param cssClassRegEx the regex to be applied against the elements css classes, matching elements are included
     * @return the By instance
     *
     * @see ByTagWithCssClassWildcard
     */
    public static ByTagWithCssClassWildcard ByTagWithCssClassWildcard(final String tagName,
                                                                      final String cssClassRegEx) {
        return new ByTagWithCssClassWildcard(tagName, cssClassRegEx, null);
    }

    /**
     *
     * A By to locate elements by tag name and apply a regex against the elements css classes and that contains the specified string
     * @param tagName the tagname
     * @param cssClassRegEx the regex to be applied against the elements css classes, matching elements are included
     * @param text the string that must be present in an elements text value
     * @return the By instance
     *
     * @see ByTagWithCssClassWildcardAndTextMatching
     */
    public static ByTagWithCssClassWildcardAndTextMatching ByTagWithCssClassWildcardContainingText(final String tagName,
                                                                                                   final String cssClassRegEx, String text) {
        return new ByTagWithCssClassWildcardAndTextMatching(tagName, cssClassRegEx, null, Matchers.containsString(text));
    }

    /**
     * A By to locate elements by tag name and apply a regex to include and exclude against the elements css classes
     * @param tagName the tagname
     * @param cssClassRegEx the regex to be applied against the elements css classes, matching elements are included
     * @param cssClassExcludesRegex the regex to apply to css classes to exclude elements by
     * @return the By instance
     *
     * @see ByTagWithCssClassWildcard
     */
    public static ByTagWithCssClassWildcard ByTagWithCssClassWildcard(final String tagName,
                                                                      final String cssClassRegEx,
                                                                      final String cssClassExcludesRegex) {
        return new ByTagWithCssClassWildcard(tagName, cssClassRegEx, cssClassExcludesRegex);
    }

    /**
     * By to locate elements that have the specified CSS class name and expected text
     *
     * @param cssClassName the required css class
     * @param expectedText the expected text of the element
     * @return the By instance
     *
     * @see ByCssWithText
     */
    public static ByCssWithText ByCssWithText(final String cssClassName, final String expectedText) {
        return new ByCssWithText(cssClassName, expectedText);
    }

    /**
     * By to locate elements that have the specified CSS class name and whose text contains the expected value
     * @param cssClassName the required css class
     * @param expectedText the expected text that should be contained within the element's text
     * @return the By instance
     *
     * @see ByCssContainingText
     */
    public static ByCssContainingText ByCssContainingText(final String cssClassName, final String expectedText) {
        return new ByCssContainingText(cssClassName, expectedText);
    }

    /**
     * By to locate elements using a CSS Selector that have the specified text
     * @param cssSelector the css selector
     * @param expectedText the required text
     * @return the By instance
     *
     * @see ByCssSelectorWithText
     */
    public static ByCssSelectorWithText ByCssSelectorWithText(final String cssSelector, final String expectedText) {
        return new ByCssSelectorWithText(cssSelector, expectedText);
    }

    /**
     * A By that finds elements with the specified id and text that matches a regex
     * @param id the id of the element
     * @param regEx the regex to use against the element's text value
     * @return the By instance
     *
     * @see ByIdWithTextMatchingRegex
     */
    public static ByIdWithTextMatchingRegex ByIdWithTextMatchingRegex(final String id, final String regEx) {
        return new ByIdWithTextMatchingRegex(id, regEx);
    }


     /**
     * convenience wrapper around the ByTagAndAttributesWithText constructor, converting a text parameter to a map
     * @param tagName the tagname
     * @param attributeString the attributes string of the form attribute1=value1,attribute2=value2
     * @param value the expected value
     * @return the constructed ByTagAndAttributesWithText instance
     * @see ByTagAndAttributesWithValue
     */
    public static ByTagAndAttributesWithValue ByTagAndAttributesWithValue(final String tagName,
                                                                          final String attributeString, final String value) {

        final Map<String, String> expectedAttributes = StepImplementationUtils.convertToMap(attributeString);

        return new ByTagAndAttributesWithValue(tagName, expectedAttributes, value);
    }

    /**
     * convenience wrapper around the ByTagAndAttributesWithText constructor, converting a text parameter to a map
     * @param tagName the tagname
     * @param attributeString the attributes string of the form attribute1=value1,attribute2=value2
     * @param text the expected text
     * @return the constructed ByTagAndAttributesWithText instance
     *
     * @see ByTagAndAttributesWithText
     * @see StepImplementationUtils
     */
    public static ByTagAndAttributesWithText ByTagAndAttributesWithText(final String tagName,
                                                                        final String attributeString, final String text) {

        final Map<String, String> expectedAttributes = StepImplementationUtils.convertToMap(attributeString);

        return new ByTagAndAttributesWithText(tagName, expectedAttributes, text);
    }


    static String equalsIgnoringCaseXPath(final String str1, final String str2) {
        return new StringBuilder().append("translate(").append(str1)
                .append(", 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')=").append(str2).toString();
    }


}
