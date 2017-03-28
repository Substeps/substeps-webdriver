package com.technophobia.webdriver.substeps.runner;

import org.substeps.webdriver.runner.WebdriverReuseStategy;

/**
 * User: dmoss
 * Date: 20/05/13
 * Time: 21:58
 */
public interface WebdriverSubstepsConfiguration {
    String baseURL();

    String driverLocale();


    boolean isJavascriptDisabledWithHTMLUnit();

    long defaultTimeout();

    /**
     * @return the html unit proxy
     * @deprecated use getNetworkProxyHost instead */
    @Deprecated
    String getHtmlUnitProxyHost();

    /**
     * @return the html unit proxy port

     * @deprecated use getNetworkProxyPort instead */
    @Deprecated
    Integer getHtmlUnitProxyPort();

    String getNetworkProxyHost();
    
    int getNetworkProxyPort();


    WebdriverReuseStategy getReuseStrategy();
}
