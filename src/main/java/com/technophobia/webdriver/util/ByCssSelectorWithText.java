package com.technophobia.webdriver.util;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ByChained;

import java.util.ArrayList;
import java.util.List;

/**
 * By to locate elements using a CSS Selector that have the specified text
 *
 * @see WebDriverSubstepsBy#ByCssSelectorWithText(String, String)
 */
public class ByCssSelectorWithText extends BaseBy {

    private final String cssSelector;
    private final String expectedText;


    ByCssSelectorWithText(final String cssSelector, final String expectedText) {
        this.cssSelector = cssSelector;
        this.expectedText = expectedText;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WebElement> findElementsBy(final SearchContext context) {

        ByChained chain = new ByChained(new ByClassName(this.cssSelector), ByTextMatcher.equalTo(expectedText));
        return context.findElements(chain);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ByCssSelectorWithText that = (ByCssSelectorWithText) o;

        if (!cssSelector.equals(that.cssSelector)) return false;
        return expectedText.equals(that.expectedText);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = cssSelector.hashCode();
        result = 31 * result + expectedText.hashCode();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ByCssSelectorWithText{" +
                "cssSelector='" + cssSelector + '\'' +
                ", expectedText='" + expectedText + '\'' +
                '}';
    }
}
