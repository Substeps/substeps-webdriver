package org.substeps.webdriver;

import com.typesafe.config.Config;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
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
            ChromeDriverManager.getInstance().setup(cfg.getString(CHROMEDRIVER_VERSION_KEY));
        } else {
            ChromeDriverManager.getInstance().setup();
        }

        final DesiredCapabilities chromeCapabilities = DesiredCapabilities.chrome();
        WebDriverFactoryUtils.setNetworkCapabilities(chromeCapabilities, cfg);

        WebDriverFactoryUtils.setLoggingPreferences(chromeCapabilities, cfg);

        return new ChromeDriver(chromeCapabilities);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DriverFactoryKey getKey() {
        return KEY;
    }
}
