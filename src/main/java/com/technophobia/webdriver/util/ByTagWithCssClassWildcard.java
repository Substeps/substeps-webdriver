package com.technophobia.webdriver.util;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A By to locate elements by tag name and apply a regex to include and optionally exclude against the elements css classes
 * @see WebDriverSubstepsBy#ByTagWithCssClassWildcard(String, String)
 * @see WebDriverSubstepsBy#ByTagWithCssClassWildcard(String, String, String)
 */
class ByTagWithCssClassWildcard extends BaseBy {

    private final String tagName;
    private final Pattern cssClassRegEx;
    private final Pattern cssClassExcludesRegEx;

    private static Logger logger = LoggerFactory.getLogger(ByTagWithCssClassWildcard.class);

    ByTagWithCssClassWildcard(final String tagName, final String cssClassRegEx, final String cssClassExcludesRegEx) {
        this.tagName = tagName;
        this.cssClassRegEx = Pattern.compile(cssClassRegEx);

        if (cssClassExcludesRegEx != null) {
            this.cssClassExcludesRegEx = Pattern.compile(cssClassExcludesRegEx);
        } else {
            this.cssClassExcludesRegEx = null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WebElement> findElementsBy(final SearchContext context) {

        List<WebElement> matchingElems = null;
        boolean done = false;
        while (!done) {
            try {
                final List<WebElement> tagElements = context.findElements(By.tagName(this.tagName));

                for (final WebElement e : tagElements) {

                    String classString = e.getAttribute("class");

                    if ((cssClassRegEx.matcher(classString).matches() && cssClassExcludesRegEx == null) ||
                            (cssClassRegEx.matcher(classString).matches() && cssClassExcludesRegEx != null && !cssClassExcludesRegEx.matcher(classString).matches())) {

                        if (matchingElems == null) {
                            matchingElems = new ArrayList<>();
                        }
                        matchingElems.add(e);
                    }
                }
                done = true;

            } catch (StaleElementReferenceException e) {
                logger.debug("got a stale element exception");
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

        ByTagWithCssClassWildcard that = (ByTagWithCssClassWildcard) o;

        if (!tagName.equals(that.tagName)) return false;
        if (!cssClassRegEx.equals(that.cssClassRegEx)) return false;
        return cssClassExcludesRegEx != null ? cssClassExcludesRegEx.equals(that.cssClassExcludesRegEx) : that.cssClassExcludesRegEx == null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = tagName.hashCode();
        result = 31 * result + cssClassRegEx.hashCode();
        result = 31 * result + (cssClassExcludesRegEx != null ? cssClassExcludesRegEx.hashCode() : 0);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ByTagWithCssClassWildcard{" +
                "tagName='" + tagName + '\'' +
                ", cssClassRegEx=" + cssClassRegEx +
                ", cssClassExcludesRegEx=" + cssClassExcludesRegEx +
                '}';
    }
}
