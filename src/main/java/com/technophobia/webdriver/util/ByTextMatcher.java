package com.technophobia.webdriver.util;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * A By that uses a generic String matcher to determine if an element should be returned.  The search context first uses an xpath to case insensitively
 * return all sub elements that contain the specified text, before applying the Matcher to each one.  Intended for use with chained Bys, but could be used standalone.
 */
class ByTextMatcher extends BaseBy{

    private final Matcher<String> stringMatcher;
    private final String theString;

    /**
     * convenience factory method that uses Matchers.equalTo
     * @param expected the expected string value
     * @return the constructed By
     */
    public static ByTextMatcher equalTo(String expected){
        return new ByTextMatcher(Matchers.equalTo(expected), expected);
    }

    /**
     * convenience factory method to create this By with a Matcher.containsString
     * @param substring the substring to match against an elements text
     * @return the constructed By
     */
    public static ByTextMatcher contains(String substring){
        return new ByTextMatcher(Matchers.containsString(substring), substring);
    }


    ByTextMatcher(Matcher<String> stringMatcher, String theString){
        this.stringMatcher = stringMatcher;
        this.theString = theString;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WebElement> findElementsBy(final SearchContext context) {

        List<WebElement> matchingElems = null;

        final List<WebElement> elemsContainingTheText =
                context.findElements(By.xpath("//*[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), " +
                        "translate('" + theString + "', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'))]"));

        for (final WebElement e : elemsContainingTheText) {

            if (this.stringMatcher.matches(e.getText())){
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
    public String toString() {
        return "ByTextMatcher{" +
                "stringMatcher=" + stringMatcher +
                ", theString='" + theString + '\'' +
                '}';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ByTextMatcher that = (ByTextMatcher) o;

        if (!stringMatcher.equals(that.stringMatcher)) return false;
        return theString.equals(that.theString);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = stringMatcher.hashCode();
        result = 31 * result + theString.hashCode();
        return result;
    }
}
