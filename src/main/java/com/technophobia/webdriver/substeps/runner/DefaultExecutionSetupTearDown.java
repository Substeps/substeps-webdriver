/*
 *	Copyright Technophobia Ltd 2012
 *
 *   This file is part of Substeps.
 *
 *    Substeps is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    Substeps is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with Substeps.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.technophobia.webdriver.substeps.runner;

import com.technophobia.substeps.model.Configuration;
import com.technophobia.substeps.model.Scope;
import com.technophobia.substeps.runner.ExecutionContext;
import com.technophobia.substeps.runner.ExecutionContextSupplier;
import com.technophobia.substeps.runner.INotificationDistributor;
import com.technophobia.substeps.runner.MutableSupplier;
import com.technophobia.substeps.runner.setupteardown.Annotations;
import com.technophobia.substeps.runner.setupteardown.Annotations.AfterEveryScenario;
import com.technophobia.substeps.runner.setupteardown.Annotations.BeforeAllFeatures;
import com.technophobia.substeps.runner.setupteardown.Annotations.BeforeEveryScenario;
import com.technophobia.webdriver.util.WebDriverContext;
import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.substeps.webdriver.DriverFactory;
import org.substeps.webdriver.config.WebdriverSubstepsConfig;
import org.substeps.webdriver.runner.WebdriverReuseStategy;

public class DefaultExecutionSetupTearDown {

    private static final Logger logger = LoggerFactory.getLogger(DefaultExecutionSetupTearDown.class);


    private final MutableSupplier<WebDriverContext> webDriverContextSupplier = new ExecutionContextSupplier<WebDriverContext>(
            Scope.SUITE, WebDriverContext.EXECUTION_CONTEXT_KEY);



    private final MutableSupplier<DriverFactory> webDriverFactorySupplier  = new ExecutionContextSupplier(Scope.SUITE, DriverFactory.DRIVER_FACTORY_KEY);

    private final WebdriverSubstepsConfiguration configuration;
    private long startTimeMillis;



    public DefaultExecutionSetupTearDown() {
        this(WebdriverSubstepsPropertiesConfiguration.INSTANCE);
    }

    public DefaultExecutionSetupTearDown(WebdriverSubstepsConfiguration configuration) {
        this.configuration = configuration;
    }


    @BeforeAllFeatures
    public final void beforeAllFeaturesSetup() {

        // Initialise the webdriver factories
//        FactoryInitialiser.INSTANCE.toString();

        final INotificationDistributor notifier = (INotificationDistributor) ExecutionContext.get(Scope.SUITE,
                INotificationDistributor.NOTIFIER_DISTRIBUTOR_KEY);

        notifier.addListener(new TestFailureListener(webDriverContextSupplier));

        logger.info("beforeAllTestsSetup");

        String env = System.getProperty("environment");

        if (env == null) {
            logger.warn("\n\n\n****** NO ENVIRONMENT SET DEFAULTING TO LOCALHOST ADD -Denvironment=mycomputer\" TO OVERRIDE******* \n\n\n\n");
            env = "localhost";
            System.setProperty("environment", env);
        }

        logger.info("env prop: " + env);

        Config cfg = Configuration.INSTANCE.getConfig();
        DriverFactory driverFactory = WebdriverSubstepsConfig.getDriverFactory(cfg);

        this.webDriverFactorySupplier.set(driverFactory);

    }

    @Annotations.AfterAllFeatures
    public final void afterAllFeatures(){
        // a final tear down if we're reusing webdrivers - otherwise will be left hanging around if re-use

        final WebDriverContext webDriverContext = webDriverContextSupplier.get();
        if (webDriverContext != null){
            DriverFactory driverFactory = this.webDriverFactorySupplier.get();
            driverFactory.shutdownWebDriver(webDriverContext);
            webDriverContextSupplier.set(null);
        }
    }


    @BeforeEveryScenario
    public final void basePreScenarioSetup() {
        startTimeMillis = System.currentTimeMillis();

        final WebDriverContext webDriverContext = webDriverContextSupplier.get();

//        boolean createNewWebDriver = shouldStartup(webDriverContext);

        if (webDriverContext == null) {
            logger.debug("no webdriver in context, creating..");
            DriverFactory driverFactory = this.webDriverFactorySupplier.get();

            Config cfg = Configuration.INSTANCE.getConfig();

            webDriverContextSupplier.set(new WebDriverContext(driverFactory.getKey(), driverFactory.create(cfg)));
        }
        else {
            logger.debug("re-using webdriver from previous scenario");
        }
    }


    @AfterEveryScenario
    public final void basePostScenariotearDown() {


        final WebDriverContext webDriverContext = webDriverContextSupplier.get();

        if (webDriverContext != null) {
            WebdriverReuseStategy reuseStrategy = WebdriverReuseStategy.fromConfig(Configuration.INSTANCE.getConfig());//this.configuration.getReuseStrategy();

            DriverFactory factory = this.webDriverFactorySupplier.get();

            reuseStrategy.afterScenario(factory, webDriverContextSupplier);

        }
        logScenarioDuration();

    }

    private void logScenarioDuration() {
        // TODO put long test threshold in config
        // TODO should use a thread local for the timings too
        final long ticks = (System.currentTimeMillis() - this.startTimeMillis) / 1000;

        if (ticks > 30) {
            logger.warn(String.format("Test scenario took %s seconds", ticks));
        } else {
            logger.info(String.format("Test scenario took %s seconds", ticks));
        }
    }

}
