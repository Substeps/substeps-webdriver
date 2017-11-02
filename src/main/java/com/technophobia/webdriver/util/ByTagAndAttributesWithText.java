package com.technophobia.webdriver.util;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * By that locates elements using tag name, required attributes and text value
 */
public class ByTagAndAttributesWithText extends XPathBy {

    private static final Logger logger = LoggerFactory.getLogger(ByTagAndAttributesWithText.class);

    private final String tagName;
    private final Map<String, String> requiredAttributes;
    private final String text;
    private final int minimumExpected;

    ByTagAndAttributesWithText(final String tagName, final Map<String, String> requiredAttributes, final String text) {
        this(tagName, requiredAttributes, text, 1);
    }

    ByTagAndAttributesWithText(final String tagName, final Map<String, String> requiredAttributes,
                               final String text, final int nth) {
        this.tagName = tagName;
        this.requiredAttributes = requiredAttributes;
        this.text = text;
        this.minimumExpected = nth;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void buildXPath(final StringBuilder xpathBuilder) {
        xpathBuilder.append(".//").append(tagName);

        final boolean hasAttributes = !requiredAttributes.isEmpty();
        final boolean hasText = StringUtils.isNotEmpty(text);

        if (hasAttributes || hasText) {
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

            if (hasText) {
                if (!firstOne) {
                    xpathBuilder.append(" and ");
                }
                xpathBuilder.append("text()='").append(text).append("'");
            }

            xpathBuilder.append("]");
        }

        logger.debug("returning by xpath string: " + xpathBuilder.toString());
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

        ByTagAndAttributesWithText that = (ByTagAndAttributesWithText) o;

        if (minimumExpected != that.minimumExpected) return false;
        if (!tagName.equals(that.tagName)) return false;
        if (!requiredAttributes.equals(that.requiredAttributes)) return false;
        return text.equals(that.text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = tagName.hashCode();
        result = 31 * result + requiredAttributes.hashCode();
        result = 31 * result + text.hashCode();
        result = 31 * result + minimumExpected;
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ByTagAndAttributesWithText{" +
                "tagName='" + tagName + '\'' +
                ", requiredAttributes=" + requiredAttributes +
                ", text='" + text + '\'' +
                ", minimumExpected=" + minimumExpected +
                '}';
    }
}
