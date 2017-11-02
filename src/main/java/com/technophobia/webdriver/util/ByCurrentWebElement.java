package com.technophobia.webdriver.util;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * A By for use with the current web element, to be chained with other Bys
 */
public class ByCurrentWebElement extends BaseBy {

    private final WebElement currentElement;

    /**
     * default constructor to construct a By that matches the specified element, for chaining with other Bys
     * @param elem the current element to match on
     */
    public ByCurrentWebElement(final WebElement elem) {
        this.currentElement = elem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WebElement> findElementsBy(final SearchContext context) {

        final List<WebElement> matchingElems = new ArrayList<>();
        matchingElems.add(this.currentElement);

        return matchingElems;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ByCurrentWebElement that = (ByCurrentWebElement) o;

        return currentElement.equals(that.currentElement);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return currentElement.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ByCurrentWebElement{" +
                "currentElement=" + currentElement +
                '}';
    }
}
