package com.technophobia.webdriver.util;

import org.hamcrest.Matcher;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A By to locate elements by tag name and apply a regex to include and exclude against the elements css classes and uses a Matcher to match the elements text value
 * @see WebDriverSubstepsBy#ByTagWithCssClassWildcardContainingText(String, String, String)
 */
class ByTagWithCssClassWildcardAndTextMatching extends BaseBy {

    private final String tagName;
    private final Pattern cssClassRegEx;
    private final Pattern cssClassExcludesRegEx;
    private final Matcher<String> stringMatcher;


    ByTagWithCssClassWildcardAndTextMatching(final String tagName, final String cssClassRegEx,
                                             final String cssClassExcludesRegEx, final Matcher<String> stringMatcher) {
        this.tagName = tagName;
        this.cssClassRegEx = Pattern.compile(cssClassRegEx);
        this.stringMatcher = stringMatcher;

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

        final List<WebElement> tagElements = context.findElements(By.tagName(this.tagName));

        for (final WebElement e : tagElements) {

            String classString = e.getAttribute("class");
            if (this.stringMatcher.matches(e.getText()) && ((cssClassRegEx.matcher(classString).matches() && cssClassExcludesRegEx == null) ||
                    (cssClassRegEx.matcher(classString).matches() && cssClassExcludesRegEx != null && !cssClassExcludesRegEx.matcher(classString).matches()))) {

                if (matchingElems == null) {
                    matchingElems = new ArrayList<>();
                }
                matchingElems.add(e);
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

        ByTagWithCssClassWildcardAndTextMatching that = (ByTagWithCssClassWildcardAndTextMatching) o;

        if (!tagName.equals(that.tagName)) return false;
        if (!cssClassRegEx.equals(that.cssClassRegEx)) return false;
        if (cssClassExcludesRegEx != null ? !cssClassExcludesRegEx.equals(that.cssClassExcludesRegEx) : that.cssClassExcludesRegEx != null)
            return false;
        return stringMatcher.equals(that.stringMatcher);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = tagName.hashCode();
        result = 31 * result + cssClassRegEx.hashCode();
        result = 31 * result + (cssClassExcludesRegEx != null ? cssClassExcludesRegEx.hashCode() : 0);
        result = 31 * result + stringMatcher.hashCode();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ByTagWithCssClassWildcardAndTextMatching{" +
                "tagName='" + tagName + '\'' +
                ", cssClassRegEx=" + cssClassRegEx +
                ", cssClassExcludesRegEx=" + cssClassExcludesRegEx +
                ", stringMatcher=" + stringMatcher +
                '}';
    }
}
