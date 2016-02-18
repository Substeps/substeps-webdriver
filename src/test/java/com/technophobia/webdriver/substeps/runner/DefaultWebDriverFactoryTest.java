package com.technophobia.webdriver.substeps.runner;

import com.technophobia.webdriver.util.WebDriverContext;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.WebDriver;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by ian on 18/02/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultWebDriverFactoryTest {

    @Mock
    private WebdriverSubstepsConfiguration config;

    @Mock
    private WebDriverContext context;

    @Mock
    private WebDriver webdriver;

    @Mock
    private WebDriver.Options webdriverOptions;

    private DefaultWebDriverFactory factory;

    @Before
    public void setupFactory(){
        factory = new DefaultWebDriverFactory(config);

        when(this.context.getWebDriver()).thenReturn(webdriver);
        when(this.webdriver.manage()).thenReturn(webdriverOptions);
    }

    @Ignore
    @Test
    public void testCreateWebDriver(){

        // TODO
        //     WebDriver createWebDriver();

    }

    @Test
    public void testShutdownWebDriver(){
        factory.shutdownWebDriver(context);
        verify(this.webdriverOptions).deleteAllCookies();
        verify(this.webdriver).quit();
    }

    @Test
    public void testResetWebDriver(){

        factory.resetWebDriver(context);
        verify(this.webdriverOptions).deleteAllCookies();
    }

}
