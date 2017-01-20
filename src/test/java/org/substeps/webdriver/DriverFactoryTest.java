package org.substeps.webdriver;

import com.technophobia.substeps.model.Configuration;
import com.technophobia.webdriver.substeps.runner.WebdriverSubstepsPropertiesConfiguration;
import com.typesafe.config.Config;
import org.junit.*;
import org.openqa.selenium.WebDriver;

/**
 * Created by ian on 12/12/16.
 */
public class DriverFactoryTest {

    private static String presetEnv;

    @BeforeClass
    public static void captureEnvVar(){

        presetEnv = System.getProperty("environment");
        System.clearProperty("environment");
    }

    @AfterClass
    public static void resetEnvVar(){

        if (presetEnv != null) {
            System.setProperty("environment", presetEnv);
        }
    }

    @Test
    public void testWebDriverFactoryCreate() {

        FactoryInitialiser.INSTANCE.toString();

        Config cfg = Configuration.INSTANCE.getConfig();

        WebDriver htmlUnit = DriverFactoryRegistry.INSTANCE.create("HTMLUNIT", cfg);

        Assert.assertNotNull(htmlUnit);
        htmlUnit.quit();
    }

        @Test
    @Ignore("this can only run where chrome and firefox installed, dev only")
    public void testConstructors(){

        FactoryInitialiser.INSTANCE.toString();

        Config cfg = Configuration.INSTANCE.getConfig();

        WebDriver chrome = DriverFactoryRegistry.INSTANCE.create("CHROME", cfg);

        Assert.assertNotNull(chrome);
        chrome.close();

        WebDriver firefox = DriverFactoryRegistry.INSTANCE.create("FIREFOX", cfg);

        Assert.assertNotNull(firefox);
        firefox.close();

        firefox.quit();

    }
}
