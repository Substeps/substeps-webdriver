package org.substeps.webdriver;

import com.technophobia.substeps.model.Scope;
import com.technophobia.substeps.runner.ExecutionContextSupplier;
import com.technophobia.substeps.runner.MutableSupplier;
import com.technophobia.webdriver.substeps.runner.DriverType;
import org.substeps.webdriver.runner.WebdriverReuseStategy;
import com.technophobia.webdriver.util.WebDriverContext;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.mockito.Mockito.*;

/**
 * Created by ian on 25/03/17.
 */
@RunWith(Parameterized.class)
public class WebdriverReuseStrategyShutdownTest {

    //  is this actually a post scenario reuse policy

    // 1. shutdown and create new
    // 2. if error and visual, leave and create new. if no error reset
    // 3. if error, shutdown and create new, if no error reset
    // 4. leave and create new



    private final WebdriverReuseStategy strategy;
    private final boolean hasFailed;
    private final boolean isVisualDriver;
    private final boolean driverShutdown;
    private final boolean driverReusable;
    private final String scenario;


    public WebdriverReuseStrategyShutdownTest(String scenario, WebdriverReuseStategy strategy, boolean hasFailed, boolean isVisualDriver, boolean driverShutdown, boolean driverReusable){
        this.scenario = scenario;
        this.strategy = strategy;
        this.hasFailed = hasFailed;
        this.isVisualDriver = isVisualDriver;
        this.driverShutdown = driverShutdown;
        this.driverReusable = driverReusable;
    }

    @Test
    public void runTheTest(){

        DriverFactory driverFactory = mock(DriverFactory.class);

        MutableSupplier<WebDriverContext> webDriverContextSupplier = new ExecutionContextSupplier<>(
                Scope.SUITE, WebDriverContext.EXECUTION_CONTEXT_KEY);

        WebDriverContext webdriverContext = mock(WebDriverContext.class);

        when(webdriverContext.hasFailed()).thenReturn(hasFailed);

        DriverType driverType = mock(DriverType.class);

        when (webdriverContext.getDriverType()).thenReturn(driverType);

        when (driverType.isVisual()).thenReturn(isVisualDriver);

        webDriverContextSupplier.set(webdriverContext);

        strategy.afterScenario(driverFactory, webDriverContextSupplier);

        if (driverShutdown){
            verify(driverFactory, times(1)).shutdownWebDriver(webdriverContext);
        }
        else {
            verify(driverFactory, never()).shutdownWebDriver(webdriverContext);
        }

        if (driverReusable){
            // shouldnt have called shutdown
            verify(driverFactory, never()).shutdownWebDriver(webdriverContext);

            // reset should have been called
            verify(driverFactory, times(1)).resetWebDriver(webdriverContext);

            Assert.assertNotNull(webDriverContextSupplier.get());
        }
        else {
            // can't re-use the webdriver, then the webdriver context should have been nulled out
            Assert.assertNull(webDriverContextSupplier.get());
        }


    }


    @Parameterized.Parameters(name ="{index}: {0}")
    public static Collection<Object[]> data() {

        return Arrays.asList(new Object[][] {

                // failed?  visual ?     driver shutdown ?  driver reusable ?
            { "global shutdown no fail, visual", WebdriverReuseStategy.SHUTDOWN_AND_CREATE_NEW, false, true,true, false},
            { "global shutdown failures, visual", WebdriverReuseStategy.SHUTDOWN_AND_CREATE_NEW, true, true,true, false},
            { "global shutdown no fail, non visual", WebdriverReuseStategy.SHUTDOWN_AND_CREATE_NEW, false, false,true, false},
            { "global shutdown failures, non visual", WebdriverReuseStategy.SHUTDOWN_AND_CREATE_NEW, true, false,true, false},



            { "Re-use unless errors, keeping failed visual browsers open, no fail, visual", WebdriverReuseStategy.REUSE_UNLESS_ERROR_KEEP_VISUALS_IN_ERROR, false, true,false, true},
            { "Re-use unless errors, keeping failed visual browsers open, failures, visual", WebdriverReuseStategy.REUSE_UNLESS_ERROR_KEEP_VISUALS_IN_ERROR, true, true,false, false},
            { "Re-use unless errors, keeping failed visual browsers open, no fail, non visual", WebdriverReuseStategy.REUSE_UNLESS_ERROR_KEEP_VISUALS_IN_ERROR, false, false,false, true},
            { "Re-use unless errors, keeping failed visual browsers open, failures, non visual", WebdriverReuseStategy.REUSE_UNLESS_ERROR_KEEP_VISUALS_IN_ERROR, true, false,true, false},

            { "Re-use unless errors, no fail, visual", WebdriverReuseStategy.REUSE_UNLESS_ERROR, false, true,false, true},
            { "Re-use unless errors, failures, visual", WebdriverReuseStategy.REUSE_UNLESS_ERROR, true, true,true, false},
            { "Re-use unless errors, no fail, non visual", WebdriverReuseStategy.REUSE_UNLESS_ERROR, false, false,false, true},
            { "Re-use unless errors, failures, non visual", WebdriverReuseStategy.REUSE_UNLESS_ERROR, true, false,true, false},


            { "No Re-use, always keep browsers open, no fail, visual", WebdriverReuseStategy.LEAVE_AND_CREATE_NEW, false, true,false, false},
            { "No Re-use, always keep browsers open, failures, visual", WebdriverReuseStategy.LEAVE_AND_CREATE_NEW, true, true,false, false},
            { "No Re-use, always keep browsers open, no fail, non visual", WebdriverReuseStategy.LEAVE_AND_CREATE_NEW, false, false,false, false},
            { "No Re-use, always keep browsers open, failures, non visual", WebdriverReuseStategy.LEAVE_AND_CREATE_NEW, true, false,false, false}
        });
    }

}
