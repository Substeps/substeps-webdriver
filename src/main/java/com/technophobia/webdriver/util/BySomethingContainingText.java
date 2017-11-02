package com.technophobia.webdriver.util;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ByChained;

import java.util.ArrayList;
import java.util.List;

/**
 * A generic By that will look for elements using the specifed By and whose text values contain the specified text
 *
 * @see WebDriverSubstepsBy#ByXpathContainingText(String, String)
 */
// TODO - replace and create a By for ContainingText and chain it..?
class BySomethingContainingText extends BaseBy {

    protected final String text;
    protected final By by;

    BySomethingContainingText(final By by, final String text) {
        this.by = by;
        this.text = text;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WebElement> findElementsBy(final SearchContext context) {

        ByChained chain = new ByChained(by, ByTextMatcher.equalTo(text));
        return context.findElements(chain);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BySomethingContainingText that = (BySomethingContainingText) o;

        if (!text.equals(that.text)) return false;
        return by.equals(that.by);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = text.hashCode();
        result = 31 * result + by.hashCode();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "BySomethingContainingText{" +
                "text='" + text + '\'' +
                ", by=" + by +
                '}';
    }
}
