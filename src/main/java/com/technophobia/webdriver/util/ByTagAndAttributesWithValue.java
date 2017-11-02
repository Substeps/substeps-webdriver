package com.technophobia.webdriver.util;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A by that locates elements using tag name, required attributes and expected value
 */
public class ByTagAndAttributesWithValue extends ByTagAndAttributes {

    private final String value;

    ByTagAndAttributesWithValue(final String tagName, final Map<String, String> requiredAttributes,
                                final String value) {
        super(tagName, requiredAttributes);
        this.value = value;

    }

    ByTagAndAttributesWithValue(final String tagName, final Map<String, String> requiredAttributes,
                                final String value, final int nth) {
        super(tagName, requiredAttributes, nth);

        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WebElement> findElementsBy(final SearchContext searchContext) {

        final List<WebElement> initialMatchingElems = super.findElementsBy(searchContext);

        List<WebElement> matchingElems = null;
        if (initialMatchingElems != null) {
            for (final WebElement e : initialMatchingElems) {
                final String val = e.getAttribute("value");
                if (val != null && val.compareTo(this.value) == 0) {

                    if (matchingElems == null) {
                        matchingElems = new ArrayList<>();
                    }
                    matchingElems.add(e);
                }
            }
        }

        return matchingElems;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ByTagAndAttributesWithValue that = (ByTagAndAttributesWithValue) o;

        return value.equals(that.value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ByTagAndAttributesWithValue{" +
                "value='" + value + '\'' +
                '}';
    }
}
