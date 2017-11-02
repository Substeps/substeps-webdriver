package com.technophobia.webdriver.util;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Base by class used to extend By's that can be constructed using an xpath expression
 */
public abstract class XPathBy extends BaseBy {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WebElement> findElementsBy(final SearchContext context) {

        final StringBuilder xpathBuilder = new StringBuilder();

        buildXPath(xpathBuilder);

        return context.findElements(By.xpath(xpathBuilder.toString()));
    }

    protected abstract void buildXPath(StringBuilder xpathBuilder);
}
