package com.technophobia.webdriver.util;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ByChained;

import java.util.ArrayList;
import java.util.List;

/**
 * By to locate elements that have the specified CSS class name and whose text contains the expected value
 *
 * @see WebDriverSubstepsBy#ByCssContainingText(String, String)
 */
public class ByCssContainingText extends BaseBy {

    private final String cssClassName;
    private final String expectedText;

    ByCssContainingText(final String cssClassName, final String expectedText) {
        this.cssClassName = cssClassName;
        this.expectedText = expectedText;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WebElement> findElementsBy(final SearchContext context) {

        ByChained chain = new ByChained(new ByClassName(this.cssClassName), ByTextMatcher.contains(expectedText));
        return context.findElements(chain);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ByCssContainingText that = (ByCssContainingText) o;

        if (!cssClassName.equals(that.cssClassName)) return false;
        return expectedText.equals(that.expectedText);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = cssClassName.hashCode();
        result = 31 * result + expectedText.hashCode();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ByCssContainingText{" +
                "cssClassName='" + cssClassName + '\'' +
                ", expectedText='" + expectedText + '\'' +
                '}';
    }
}
