package com.technophobia.webdriver.util;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitWebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * base class that extends the Selenium By, trapping StaleElementExceptions returning null instead and returning empty collections to satisfy HTMLUnit.
 * In the event that no elements are returned from the implementation, the By will be retried as part of the wait mechanism until a timeout.
 */
public abstract class BaseBy extends By {

    private static Logger logger = LoggerFactory.getLogger(BaseBy.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public final List<WebElement> findElements(final SearchContext context) {

        List<WebElement> matchingElems = null;
        try {
            matchingElems = findElementsBy(context);
        } catch (StaleElementReferenceException e) {
            logger.debug("StaleElementReferenceException looking for elements");
        }

        // NB. returning non null will prevent any wait from waiting.. HTML unit is a bit tricky in this respect as it expects an empty collection,
        // not compatible with any waits...

        if (matchingElems == null && context instanceof HtmlUnitWebElement) {
            matchingElems = Collections.emptyList();
        }
        return matchingElems;
    }

    /**
     * return list of elements that this By matches
     * @param context the searchcontext in which to apply the By
     * @return the list of matching elements
     */
    public abstract List<WebElement> findElementsBy(final SearchContext context);

    /**
     * return the a description of the By.
     * @return a string desciption or representation of the By
     */
    @Override
    public abstract String toString();

    /**
     * need to override the default base class behaviour which uses toString for equivalence and hashcode
     * @return the computed hashcode
     */
    @Override
    public abstract int hashCode();

    /**
     * need to override the default base class behaviour which uses toString for equivalence and hashcode
     * @param other the other object to compare to this one
     * @return true if these Bys are considered equal
     */
    @Override
    public abstract boolean equals(Object other);
}
