package org.substeps.webdriver;

import com.technophobia.substeps.model.Configuration;
import com.technophobia.webdriver.util.WebDriverContext;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.substeps.config.SubstepsConfigLoader;
import org.substeps.runner.NewSubstepsExecutionConfig;
import org.substeps.webdriver.config.WebdriverSubstepsConfig;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;

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

    @Ignore("manual test")
    @Test
    public void testWebdriverManagerOverride(){
        Config cfg = SubstepsConfigLoader.loadResolvedConfig();

        HTMLUnitDriverFactory factory = new HTMLUnitDriverFactory();

        factory.create(cfg);

        String wdmProps = System.getProperty("wdm.properties");

        Assert.assertThat(wdmProps, is("/substeps-webdrivermanager.properties"));
    }


  //  @Ignore
    @Test
    public void testDriverCreation(){

        Config cfg = SubstepsConfigLoader.loadResolvedConfig();

        System.out.println(SubstepsConfigLoader.render(cfg));

        List<Config> splits = SubstepsConfigLoader.splitMasterConfig(cfg);

        DriverFactory driverFactory = WebdriverSubstepsConfig.getDriverFactory(splits.get(0));

        WebDriver webDriver = driverFactory.create(cfg);

        webDriver.quit();
    }


    @Ignore("this is a local dev test only")
    @Test
    public void testWindowSizing(){
        WebDriver cd = null;
        try {
            WebDriverManager.chromedriver().setup();
            WebDriverManager.firefoxdriver().setup();

            final DesiredCapabilities firefoxCapabilities = DesiredCapabilities.firefox();
            FirefoxProfile fp = new FirefoxProfile();

            FirefoxOptions options = new FirefoxOptions();

            firefoxCapabilities.setCapability(FirefoxDriver.PROFILE, fp);


            cd = new FirefoxDriver(options);

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
