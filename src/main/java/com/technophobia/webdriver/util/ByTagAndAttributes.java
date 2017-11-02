package com.technophobia.webdriver.util;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * By to locate an element using tag name and a map of expected attributes.  optionally can look for multiple matching elements
 */
public class ByTagAndAttributes extends XPathBy {

    private static final Logger logger = LoggerFactory.getLogger(ByTagAndAttributes.class);

    private final String tagName;
    private final Map<String, String> requiredAttributes;
    private final int minimumExpected;

    ByTagAndAttributes(final String tagName, final Map<String, String> requiredAttributes) {
        this.tagName = tagName;
        this.requiredAttributes = requiredAttributes;
        this.minimumExpected = 1;
    }

    ByTagAndAttributes(final String tagName, final Map<String, String> requiredAttributes, final int nth) {
        this.tagName = tagName;
        this.requiredAttributes = requiredAttributes;
        this.minimumExpected = nth;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void buildXPath(final StringBuilder xpathBuilder) {
        xpathBuilder.append(".//").append(tagName);

        if (!requiredAttributes.isEmpty()) {
            xpathBuilder.append("[");

            boolean firstOne = true;

            for (final Map.Entry<String, String> requiredAttribute : requiredAttributes.entrySet()) {

                if (!firstOne) {
                    xpathBuilder.append(" and ");
                }

                xpathBuilder.append("@").append(requiredAttribute.getKey()).append(" = '")
                        .append(requiredAttribute.getValue()).append("'");

                firstOne = false;
            }

            xpathBuilder.append("]");
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WebElement> findElementsBy(final SearchContext searchContext) {

        final List<WebElement> matchingElems = super.findElementsBy(searchContext);

        return WebDriverUtils.checkAtLeastNMatchingElements(matchingElems, this.minimumExpected);
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ByTagAndAttributes that = (ByTagAndAttributes) o;

        if (minimumExpected != that.minimumExpected) return false;
        if (!tagName.equals(that.tagName)) return false;
        return requiredAttributes.equals(that.requiredAttributes);
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public int hashCode() {
        int result = tagName.hashCode();
        result = 31 * result + requiredAttributes.hashCode();
        result = 31 * result + minimumExpected;
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ByTagAndAttributes{" +
                "tagName='" + tagName + '\'' +
                ", requiredAttributes=" + requiredAttributes +
                ", minimumExpected=" + minimumExpected +
                '}';
    }
}
