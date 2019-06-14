package org.substeps.webdriver;

import com.technophobia.webdriver.util.WebDriverContext;
import com.typesafe.config.Config;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ian on 12/12/16.
 */
public class ChromeDriverFactory extends BaseDriverFactory implements DriverFactory {

    private static final Logger log = LoggerFactory.getLogger(ChromeDriverFactory.class);

    public static final DriverFactoryKey KEY = new DriverFactoryKey("CHROME", true, ChromeDriverFactory.class);

    private static final String CHROMEDRIVER_VERSION_KEY = "chromedriver.version";

    /**
     * {@inheritDoc}
     */
    @Override
    protected WebDriver createInternal(Config cfg) {

        log.debug("creating chrome driver");

        if (cfg.hasPath(CHROMEDRIVER_VERSION_KEY)) {
            WebDriverManager.chromedriver().version(cfg.getString(CHROMEDRIVER_VERSION_KEY)).setup();
        } else {
            WebDriverManager.chromedriver().setup();
        }

        final DesiredCapabilities chromeCapabilities = DesiredCapabilities.chrome();
        WebDriverFactoryUtils.setNetworkCapabilities(chromeCapabilities, cfg);

        WebDriverFactoryUtils.setLoggingPreferences(chromeCapabilities, cfg);

//        Map<String, String> mobileEmulation = new HashMap<>();
//        mobileEmulation.put("deviceName", "Nexus 5");


        ChromeOptions chromeOptions = new ChromeOptions().merge(chromeCapabilities);
//        chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);

        return new ChromeDriver(chromeOptions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DriverFactoryKey getKey() {
        return KEY;
    }


    @Override
    public void shutdownWebDriver(WebDriverContext webDriverContext) {
        super.shutdownWebDriver(webDriverContext);
    }
}
