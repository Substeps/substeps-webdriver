package com.technophobia.webdriver.util;

import java.util.List;
        import org.junit.Assert;
        import org.openqa.selenium.WebElement;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;

public class WebDriverUtils {
    private static final Logger logger = LoggerFactory.getLogger(WebDriverUtils.class);


    public static WebElement checkForOneMatchingElement(String msg, List<WebElement> matchingElems) {
        WebElement rtn = null;
        if(matchingElems != null && matchingElems.size() > 1) {
            Assert.fail("Found too many elements that meet this criteria");
        } else if(matchingElems != null) {
            rtn = (WebElement)matchingElems.get(0);
        }

        Assert.assertNotNull(msg, rtn);
        return rtn;
    }
}