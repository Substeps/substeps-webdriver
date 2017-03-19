package org.substeps.webdriver;

import com.technophobia.substeps.model.Configuration;
import com.technophobia.webdriver.substeps.runner.WebdriverSubstepsPropertiesConfiguration;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import org.junit.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import static org.hamcrest.CoreMatchers.*;

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

    @Ignore("this is a local dev test only")
    @Test
    public void testWindowSizing(){
        WebDriver cd = null;
        try {
            ChromeDriverManager.getInstance().setup();
            FirefoxDriverManager.getInstance().setup();

            final DesiredCapabilities firefoxCapabilities = DesiredCapabilities.firefox();
            FirefoxProfile fp = new FirefoxProfile();


            firefoxCapabilities.setCapability(FirefoxDriver.PROFILE, fp);


            cd = new FirefoxDriver(fp);
            //cd = new ChromeDriver();

            String cfgString = "org.substeps.webdriver{ window{ maximise=false, height=500, width=750 } }";

            Config cfg = ConfigFactory.parseString(cfgString);

            WebDriverFactoryUtils.setScreensize(cd, cfg);

            Dimension dim = cd.manage().window().getSize();

            Assert.assertThat("", dim.getHeight(), is(500));

            Assert.assertThat("", dim.getWidth(), is(750));
        }
        finally{
            if (cd != null){
                cd.close();
                cd.quit();
            }
        }
    }
}
