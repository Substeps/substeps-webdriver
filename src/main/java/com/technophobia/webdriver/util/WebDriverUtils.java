package com.technophobia.webdriver.util;

import org.junit.Assert;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Utility class, static methods relating to a webdriver, finding elements etc
 */
public abstract class WebDriverUtils {

    private static final Logger logger = LoggerFactory.getLogger(WebDriverUtils.class);

    /**
     * prevent construction
     */
    private WebDriverUtils(){}

    /**
     * check that the supplied list of elements contains only one element, using the supplied message in any assertion
     * @param msg the potential assertion message
     * @param matchingElems the list of elements to check, can be null
     * @return the single matched element or null if none or multiple found
     */
    public static WebElement checkForOneMatchingElement(String msg, List<WebElement> matchingElems) {
        WebElement rtn = null;
        if (matchingElems != null && matchingElems.size() > 1) {
            Assert.fail("Found too many elements that meet this criteria");
        } else if (matchingElems != null) {
            rtn = matchingElems.get(0);
        }

        Assert.assertNotNull(msg, rtn);
        return rtn;
    }


    /**
     * convenience method to check that at least n elements are contained within the supplied list
     * @param matchingElems the list of elements located by the By
     * @param minimumExpected the minimum number of expected elements
     * @return the list of elements if there are at least the minimum required, otherwise null
     */
    public static List<WebElement> checkAtLeastNMatchingElements(List<WebElement> matchingElems, int minimumExpected){

        if (matchingElems != null && matchingElems.size() < minimumExpected) {
            logger.info("expecting at least " + minimumExpected + " matching elems, found only "
                    + matchingElems.size() + " this time around");
            // we haven't found enough, clear out

            return null;
        }

        return matchingElems;
    }
}