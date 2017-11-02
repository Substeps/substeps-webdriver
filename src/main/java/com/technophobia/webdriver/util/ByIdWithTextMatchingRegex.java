package com.technophobia.webdriver.util;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Pattern;

/**
 * A By that finds elements with the specified id and text that matches a regex
 * @see WebDriverSubstepsBy#ByIdWithTextMatchingRegex(String, String)
 */
public class ByIdWithTextMatchingRegex extends BaseBy {
    private static Logger logger = LoggerFactory.getLogger(ByIdWithTextMatchingRegex.class);
    protected final Pattern pattern;
    protected final String id;

    ByIdWithTextMatchingRegex(String id, String regEx) {
        this.id = id;
        this.pattern = Pattern.compile(regEx);
    }


    public List<WebElement> findElementsBy(SearchContext context) {
        List<WebElement> elems = context.findElements(By.id(this.id));

        if (elems != null) {
            if (elems.size() == 1) {
                String text = elems.get(0).getText();

                if (pattern.matcher(text).matches()) {
                    return elems;
                } else {
                    logger.debug("no reg ex match on text: " + text + " for regex: " + pattern.pattern());
                }
            } else {
                logger.error("To many elements found for Id: " + id);
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ByIdWithTextMatchingRegex that = (ByIdWithTextMatchingRegex) o;

        if (!pattern.equals(that.pattern)) return false;
        return id.equals(that.id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = pattern.hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ByIdWithTextMatchingRegex{" +
                "pattern=" + pattern +
                ", id='" + id + '\'' +
                '}';
    }
}
