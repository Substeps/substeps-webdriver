package com.technophobia.webdriver.substeps.runner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.hamcrest.CoreMatchers.is;

import com.typesafe.config.Config;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.WebDriver;

import com.technophobia.substeps.model.Scope;
import com.technophobia.substeps.runner.ExecutionContext;
import com.technophobia.webdriver.util.WebDriverContext;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.substeps.webdriver.DriverFactory;
import org.substeps.webdriver.DriverFactoryKey;
import org.substeps.webdriver.FirefoxDriverFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Tests interaction of webdriver.shutdown, webdriver.reuse, and
 * visual.webdriver.close.on.fail on webdriver shutdown and restart
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultExecutionSetupTearDownTest {

    private DefaultExecutionSetupTearDown std;
    private  Method shutdownMethod;
    private Method shouldStartupMethod;

    @Mock
    private WebdriverSubstepsConfiguration config;

    @Mock
    private WebDriver webDriver;

    @Mock
    private WebDriver.Options options;

    @Mock
    DriverFactory factory;

    private WebDriverContext context;

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

    @Before
    public void initialiseDependencies() throws NoSuchMethodException {

        shutdownMethod = DefaultExecutionSetupTearDown.class.getDeclaredMethod("shouldShutdown", WebDriverContext.class);

        shutdownMethod.setAccessible(true);

        shouldStartupMethod = DefaultExecutionSetupTearDown.class.getDeclaredMethod("shouldStartup", WebDriverContext.class);

        shouldStartupMethod.setAccessible(true);

        this.std = new DefaultExecutionSetupTearDown(this.config);

        // initialise context with visual webdriver and set test state to failed
        this.context = new WebDriverContext(FirefoxDriverFactory.KEY, this.webDriver);

        ExecutionContext.put(Scope.SUITE, "webDriverFactory", this.context);

        // creating a new webdriver instance will use the factory.
        ExecutionContext.put(Scope.SUITE, DriverFactory.DRIVER_FACTORY_KEY, this.factory);
    }


    /**
     * Global shutdown enabled - should always shutdown
     */
    @Test
    public void shouldShutdownIfGlobalShutdownEnabledAndReuseEnabledAndCloseOnFailEnabled() throws InvocationTargetException, IllegalAccessException {
        when(this.config.shutDownWebdriver()).thenReturn(true);
        when(this.config.reuseWebDriver()).thenReturn(true);
        when(this.config.closeVisualWebDriveronFail()).thenReturn(true);

        when(this.webDriver.manage()).thenReturn(this.options);

        this.context.setFailed();

        boolean rtn = (Boolean)shutdownMethod.invoke(this.std, this.context);

        Assert.assertThat("shouldShutdownIfGlobalShutdownEnabledAndReuseEnabledAndCloseOnFailEnabled", rtn, is(true));

    }


    /**
     * Global shutdown enabled - should always shutdown
     */
    @Test
    public void shouldShutdownIfGlobalShutdownEnabledAndReuseDisabledAndCloseOnFailEnabled() throws InvocationTargetException, IllegalAccessException {
        when(this.config.shutDownWebdriver()).thenReturn(true);
        when(this.config.reuseWebDriver()).thenReturn(false);
        when(this.config.closeVisualWebDriveronFail()).thenReturn(true);

        when(this.webDriver.manage()).thenReturn(this.options);

        this.context.setFailed();

        boolean rtn = (Boolean)shutdownMethod.invoke(this.std, this.context);

        Assert.assertThat("shouldShutdownIfGlobalShutdownEnabledAndReuseDisabledAndCloseOnFailEnabled", rtn, is(true));
    }


    /**
     * Global shutdown enabled - should always shutdown
     */
    @Test
    public void shouldShutdownIfGlobalShutdownEnabledAndReuseEnabledAndCloseOnFailDisabled() throws InvocationTargetException, IllegalAccessException {
        when(this.config.shutDownWebdriver()).thenReturn(true);
        when(this.config.reuseWebDriver()).thenReturn(true);
        when(this.config.closeVisualWebDriveronFail()).thenReturn(false);

        when(this.webDriver.manage()).thenReturn(this.options);

        this.context.setFailed();

        boolean rtn = (Boolean)shutdownMethod.invoke(this.std, this.context);

        Assert.assertThat("shouldShutdownIfGlobalShutdownEnabledAndReuseEnabledAndCloseOnFailDisabled", rtn, is(true));

    }


    /**
     * Case where the user doesn't care about retaining browser instances for
     * failed tests, and wants to reuse instances in subsequent tests.
     */
    @Test
    public void shouldNotShutdownIfGlobalShutdownDisabledAndReuseEnabledAndCloseOnFailEnabled() throws InvocationTargetException, IllegalAccessException {
        when(this.config.shutDownWebdriver()).thenReturn(false);
        when(this.config.reuseWebDriver()).thenReturn(true);
        when(this.config.closeVisualWebDriveronFail()).thenReturn(true);

        when(this.webDriver.manage()).thenReturn(this.options);

        this.context.setFailed();

        boolean rtn = (Boolean)shutdownMethod.invoke(this.std, this.context);

        Assert.assertThat("shouldNotShutdownIfGlobalShutdownDisabledAndReuseEnabledAndCloseOnFailEnabled", rtn, is(false));

    }


    /**
     * Case where user doesn't care about retaining browser instances for failed
     * tests, but hasn't declared that they want to reuse them either - so
     * shutdown anyway.
     */
    @Test
    public void shouldShutdownIfGlobalShutdownDisabledAndReuseDisabledAndCloseOnFailEnabled() throws InvocationTargetException, IllegalAccessException {
        when(this.config.shutDownWebdriver()).thenReturn(false);
        when(this.config.reuseWebDriver()).thenReturn(false);
        when(this.config.closeVisualWebDriveronFail()).thenReturn(true);

        when(this.webDriver.manage()).thenReturn(this.options);

        this.context.setFailed();

        boolean rtn = (Boolean)shutdownMethod.invoke(this.std, this.context);

        Assert.assertThat("shouldShutdownIfGlobalShutdownDisabledAndReuseDisabledAndCloseOnFailEnabled", rtn, is(true));
    }


//    public static class TestWebDriverFactory implements WebDriverFactory{
//
//        @Override
//        public WebDriver createWebDriver() {
//            return new FirefoxDriver();
//        }
//
//
//        @Override
//        public void shutdownWebDriver(WebDriverContext webDriverContext) {
//
//        }
//
//        @Override
//        public boolean resetWebDriver(WebDriverContext webDriverContext) {
//            return false;
//        }
//    }






    @Test
    public void testShutdownConditions() throws InvocationTargetException, IllegalAccessException {

        //

        /**
         * Case where user wants to keep browser open for failed tests (possibly to
         * see page on failure), and doesn't want the browser to be reused - don't
         * shutdown.
         */
        when(this.config.shutDownWebdriver()).thenReturn(false);
        when(this.config.closeVisualWebDriveronFail()).thenReturn(false);
        when(this.config.reuseWebDriver()).thenReturn(false);

        this.context.setFailed();

        boolean rtn = (Boolean)shutdownMethod.invoke(this.std, this.context);

        Assert.assertThat("should Not Shutdown If Global Shutdown Disabled And Reuse Disabled And Close On Fail Disabled", rtn, is(false));


        /**
         * Case where user wants to keep browser open for failed tests (possibly to
         * see page on failure), but does want the browser to be reused - don't
         * shutdown because the test has failed
         */

        when(this.config.shutDownWebdriver()).thenReturn(false);
        when(this.config.reuseWebDriver()).thenReturn(true);
        when(this.config.closeVisualWebDriveronFail()).thenReturn(false);

        rtn = (Boolean)shutdownMethod.invoke(this.std, this.context);

        Assert.assertThat("should Not Shutdown If Global Shutdown Disabled And Reuse Enabled And Close On Fail Disabled", rtn, is(false));

    }





    /**
     * Case where user wants to keep browser open for failed tests (possibly to
     * see page on failure), but does want the browser to be reused - don't
     * shutdown because the test has passed and we want to reuse the webdriver
     * instance
     */
    @Test
    public void shouldNotShutdownIfGlobalShutdownDisabledAndReuseEnabledAndCloseOnFailDisabledAndTestPasses() throws InvocationTargetException, IllegalAccessException {
        when(this.config.shutDownWebdriver()).thenReturn(false);
        when(this.config.reuseWebDriver()).thenReturn(true);
        when(this.config.closeVisualWebDriveronFail()).thenReturn(false);


        boolean rtn = (Boolean)shutdownMethod.invoke(this.std, this.context);

        Assert.assertThat("shouldNotShutdownIfGlobalShutdownDisabledAndReuseEnabledAndCloseOnFailDisabledAndTestPasses", rtn, is(false));
    }


    /********* startup ***********/

    /**
     * Clean start - no webdriver context yet.
     */
    @Test
    public void shouldStartupOnCleanStartBeforeWebDriverContextIsInitialised() throws InvocationTargetException, IllegalAccessException {

        ExecutionContext.put(Scope.SUITE, "webDriverFactory", null);

        boolean rtn = (Boolean)shouldStartupMethod.invoke(this.std, this.context);

        Assert.assertThat("shouldStartupOnCleanStartBeforeWebDriverContextIsInitialised", rtn, is(true));

    }


    /**
     * Global shutdown enabled - should always startup
     */
    @Test
    public void shouldStartupIfGlobalShutdownEnabledAndReuseEnabledAndCloseOnFailEnabled() throws InvocationTargetException, IllegalAccessException {
        when(this.config.shutDownWebdriver()).thenReturn(true);
        when(this.config.closeVisualWebDriveronFail()).thenReturn(true);
        when(this.config.reuseWebDriver()).thenReturn(true);

        this.context.setFailed();

        boolean rtn = (Boolean)shouldStartupMethod.invoke(this.std, this.context);

        Assert.assertThat("shouldStartupIfGlobalShutdownEnabledAndReuseEnabledAndCloseOnFailEnabled", rtn, is(true));

    }


    /**
     * Global shutdown enabled - should always startup
     */
    @Test
    public void shouldStartupIfGlobalShutdownEnabledAndReuseDisabledAndCloseOnFailEnabled() throws InvocationTargetException, IllegalAccessException {
        when(this.config.shutDownWebdriver()).thenReturn(true);
        when(this.config.reuseWebDriver()).thenReturn(false);
        when(this.config.closeVisualWebDriveronFail()).thenReturn(true);

        this.context.setFailed();

        boolean rtn = (Boolean)shouldStartupMethod.invoke(this.std, this.context);

        Assert.assertThat("shouldStartupIfGlobalShutdownEnabledAndReuseDisabledAndCloseOnFailEnabled", rtn, is(true));


    }


    /**
     * Global shutdown enabled - should always startup
     */
    @Test
    public void shouldStartupIfGlobalShutdownEnabledAndReuseEnabledAndCloseOnFailDisabled() throws InvocationTargetException, IllegalAccessException {
        when(this.config.shutDownWebdriver()).thenReturn(true);
        when(this.config.reuseWebDriver()).thenReturn(true);
        when(this.config.closeVisualWebDriveronFail()).thenReturn(false);

        this.context.setFailed();

        boolean rtn = (Boolean)shouldStartupMethod.invoke(this.std, this.context);

        Assert.assertThat("shouldStartupIfGlobalShutdownEnabledAndReuseEnabledAndCloseOnFailDisabled", rtn, is(true));

    }


    /**
     * Case where the user doesn't care about retaining browser instances for
     * failed tests, and wants to reuse instances in subsequent tests - should
     * not startup because we'll reuse the existing instance.
     */
    @Test
    public void shouldNotStartupIfGlobalShutdownDisabledAndReuseEnabledAndCloseOnFailEnabled() throws InvocationTargetException, IllegalAccessException {
        when(this.config.shutDownWebdriver()).thenReturn(false);
        when(this.config.reuseWebDriver()).thenReturn(true);
        when(this.config.closeVisualWebDriveronFail()).thenReturn(true);

        this.context.setFailed();

        boolean rtn = (Boolean)shouldStartupMethod.invoke(this.std, this.context);

        Assert.assertThat("shouldNotStartupIfGlobalShutdownDisabledAndReuseEnabledAndCloseOnFailEnabled", rtn, is(false));

    }


    /**
     * Case where user doesn't care about retaining browser instances for failed
     * tests, but hasn't declared that they want to reuse them either - will
     * have shut down, so should restart.
     */
    @Test
    public void shouldStartupIfGlobalShutdownDisabledAndReuseDisabledAndCloseOnFailEnabled() {
        when(this.config.shutDownWebdriver()).thenReturn(false);
        when(this.config.reuseWebDriver()).thenReturn(false);
        when(this.config.closeVisualWebDriveronFail()).thenReturn(true);

        this.context.setFailed();
        this.std.basePreScenarioSetup();

        ArgumentCaptor<Config> argument = ArgumentCaptor.forClass(Config.class);
        verify(this.factory).create(argument.capture());
    }


    /**
     * Case where user wants to keep browser open for failed tests (possibly to
     * see page on failure), and doesn't want the browser to be reused - must
     * startup because previous test failed and browser is being kept open.
     */
    @Test
    public void shouldStartupIfGlobalShutdownDisabledAndReuseDisabledAndCloseOnFailDisabled() throws InvocationTargetException, IllegalAccessException {
        when(this.config.shutDownWebdriver()).thenReturn(false);
        when(this.config.closeVisualWebDriveronFail()).thenReturn(false);
        when(this.config.reuseWebDriver()).thenReturn(false);

        this.context.setFailed();

        boolean rtn = (Boolean)shouldStartupMethod.invoke(this.std, this.context);

        Assert.assertThat("shouldStartupIfGlobalShutdownDisabledAndReuseDisabledAndCloseOnFailDisabled", rtn, is(true));

    }


    /**
     * Case where user wants to keep browser open for failed tests (possibly to
     * see page on failure), but does also want the browser to be reused - must
     * startup because previous test failed and browser is being kept open.
     */
    @Test
    public void shouldStartupIfGlobalShutdownDisabledAndReuseEnabledAndCloseOnFailDisabled() throws InvocationTargetException, IllegalAccessException {
        when(this.config.shutDownWebdriver()).thenReturn(false);
        when(this.config.reuseWebDriver()).thenReturn(true);
        when(this.config.closeVisualWebDriveronFail()).thenReturn(false);

        this.context.setFailed();

        boolean rtn = (Boolean)shouldStartupMethod.invoke(this.std, this.context);

        Assert.assertThat("shouldStartupIfGlobalShutdownDisabledAndReuseEnabledAndCloseOnFailDisabled", rtn, is(true));
    }


    /**
     * Case where user wants to keep browser open for failed tests (possibly to
     * see page on failure), but does want the browser to be reused - don't
     * startup because the test has passed and we want to reuse the webdriver
     * instance
     */
    @Test
    public void shouldNotStartupIfGlobalShutdownDisabledAndReuseEnabledAndCloseOnFailDisabledAndTestPasses() throws InvocationTargetException, IllegalAccessException {
        when(this.config.shutDownWebdriver()).thenReturn(false);
        when(this.config.reuseWebDriver()).thenReturn(true);
        when(this.config.closeVisualWebDriveronFail()).thenReturn(false);

        boolean rtn = (Boolean)shouldStartupMethod.invoke(this.std, this.context);

        Assert.assertThat("shouldNotStartupIfGlobalShutdownDisabledAndReuseEnabledAndCloseOnFailDisabledAndTestPasses", rtn, is(false));
    }

}
