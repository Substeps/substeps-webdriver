package org.substeps.webdriver.runner;

import com.technophobia.substeps.runner.MutableSupplier;
import com.technophobia.webdriver.util.WebDriverContext;
import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.substeps.webdriver.DriverFactory;

/**
 * Created by ian on 26/03/17.
 */
public enum WebdriverReuseStategy {


    /**
     * After each scenario, shut the webdriver down, start a new webdriver for each scenario - this is the safest, but slowest option.
     */
    SHUTDOWN_AND_CREATE_NEW{
        @Override
        public void afterScenario(DriverFactory factory, MutableSupplier<WebDriverContext> webDriverContextSupplier) {

            WebDriverContext webDriverContext = webDriverContextSupplier.get();

            factory.shutdownWebDriver(webDriverContext);

            webDriverContextSupplier.set(null);

        }
    },
    /**
     * After each scenario, reset the webdriver, unless there's an error, in which case terminate it (unless it's a visual webdriver,
     * in which case eave the webdriver open for debugging purposes
     */
    REUSE_UNLESS_ERROR_KEEP_VISUALS_IN_ERROR{   // if error and visual, leave and create new. if no error reset
        @Override
        public void afterScenario(DriverFactory factory, MutableSupplier<WebDriverContext> webDriverContextSupplier) {

            WebDriverContext webDriverContext = webDriverContextSupplier.get();

            if (webDriverContext.hasFailed()){

                if (webDriverContext.getDriverType().isVisual()){
                    // don't call shutdown, keep the browser
                }
                else {
                    factory.shutdownWebDriver(webDriverContext);

                }
                webDriverContextSupplier.set(null);

            }
            else {
                factory.resetWebDriver(webDriverContext);
            }
        }
    },

    /**
     * After each scenario, reset the webdriver, unless there's an error, in which case terminate it
     */
    REUSE_UNLESS_ERROR{    // if error, shutdown and create new, if no error reset
        @Override
        public void afterScenario(DriverFactory factory, MutableSupplier<WebDriverContext> webDriverContextSupplier) {

            WebDriverContext webDriverContext = webDriverContextSupplier.get();

            if (webDriverContext.hasFailed()){
                factory.shutdownWebDriver(webDriverContext);

                // call shutdown, discard the browser
                webDriverContextSupplier.set(null);

            }
            else {
                factory.resetWebDriver(webDriverContext);
            }
        }
    },

    /**
     * After each scenario, leave the webdriver open, but create a new one for the next scenario.  Useful for debugging, if you need to inspect anything in the console but previous tests have passed.
     */
    LEAVE_AND_CREATE_NEW{
        @Override
        public void afterScenario(DriverFactory factory, MutableSupplier<WebDriverContext> webDriverContextSupplier) {

            WebDriverContext webDriverContext = webDriverContextSupplier.get();

            // we're not going to call shutdown, just leave the webdriver hanging around

            webDriverContextSupplier.set(null);


        }
    };

    public static WebdriverReuseStategy fromConfig(Config cfg){

        WebdriverReuseStategy rtn = SHUTDOWN_AND_CREATE_NEW; // default
        if (cfg.hasPath("org.substeps.webdriver.reuse-strategy")){
            String strat = cfg.getString("org.substeps.webdriver.reuse-strategy");

            try {
                rtn = WebdriverReuseStategy.valueOf(strat.replaceAll("-", "_").toUpperCase());
            }
            catch (IllegalArgumentException e){

                if (e.getMessage().startsWith("No enum constant")){
                    logger.error("Value in properties files for reuse-strategy does not map to an enum value in WebdriverReuseStategy");
                }
                throw e;
            }
        }
        return rtn;
    }
    private static final Logger logger = LoggerFactory.getLogger(WebdriverReuseStategy.class);

    public abstract void afterScenario(DriverFactory factory, MutableSupplier<WebDriverContext> webDriverContextSupplier);
}
