package com.technophobia.webdriver.substeps.impl;

import com.technophobia.substeps.model.Scope;
import com.technophobia.substeps.model.SubSteps;
import com.technophobia.substeps.runner.ExecutionContextSupplier;
import com.technophobia.substeps.runner.MutableSupplier;
import com.technophobia.webdriver.substeps.runner.DefaultExecutionSetupTearDown;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ian on 21/01/17.
 */

@SubSteps.StepImplementations(requiredInitialisationClasses = DefaultExecutionSetupTearDown.class)
public class IFrameAndWindowSubstepImplementations extends AbstractWebDriverSubStepImplementations {

    private static final Logger logger = LoggerFactory.getLogger(IFrameAndWindowSubstepImplementations.class);


    private static final MutableSupplier<WebDriver> webdriverInContext = new ExecutionContextSupplier<>(Scope.SCENARIO, "webdriver-in-context");
    private static final MutableSupplier<String> webdriverPageInContext = new ExecutionContextSupplier<>(Scope.SCENARIO, "1");

    /**
     * Switches to the new window which has been opened
     */
    @SubSteps.Step("Switch to new window")
    public void switchToNewWindow() {
        // Store the current window handle
        String winHandleBefore = webDriver().getWindowHandle();
        webdriverPageInContext.set(winHandleBefore);

        for (String newWindow : webDriver().getWindowHandles()) {
            webDriver().switchTo().window(newWindow);
        }
    }

    /**
     * Close the new window which was been opened and return the focus to the original window
     */
    @SubSteps.Step("Close new window")
    public void closeWebDriver() {

        // Close the new window, if that window no more required
        webDriver().close();

        // Switch back to original browser (first window)
        webDriver().switchTo().window(webdriverPageInContext.get());

    }

    /**
     * Switches to the new iFrame present on current page and store the webdriver element for the iFrame
     */
    @SubSteps.Step("Switch to new frame \"([^\"]*)\"")
    public void switchToNewFrame(String frameCss) throws InterruptedException {

        int timeout = 0;
        while (timeout < 10) {
            try {
                webdriverInContext.set(webDriver().switchTo().frame(webDriver().findElement(By.cssSelector(frameCss))));
                break;
            }
            catch (NoSuchElementException e) {
                logger.info("The element " + frameCss + " has not yet been found");
                Thread.sleep(1000);
                timeout++;
            }
        }
    }

    /**
     * Switches to the specified iFrame present on current page and store the webdriver element for the iFrame
     */
    @SubSteps.Step("Switch to new frame by name \"([^\"]*)\"")
    public void switchToNewFrameByName(String frameName) throws InterruptedException {

        int timeout = 0;
        while (timeout < 10) {
            try {
                webdriverInContext.set(webDriver().switchTo().frame(webDriver().findElement(By.name(frameName))));
                break;
            }
            catch (NoSuchElementException e) {
                logger.info("The element " + frameName + " has not yet been found");
                Thread.sleep(1000);
                timeout++;
            }
        }
    }

    /**
     * Switches back to the default content
     */
    @SubSteps.Step("Switch to default content")
    public void switchToDefaultContent() {

        WebDriver driver = webdriverInContext.get();
        driver = driver.switchTo().defaultContent();
        webdriverInContext.set(driver);
    }

    /**
     * Transfer the focus into the current element (set with a previous Find
     * method) which should be a frame or iframe
     *
     * @example SwitchFrameToCurrentElement
     * @section Location
     *
     */
    @SubSteps.Step("SwitchFrameToCurrentElement")
    public void switchFrameToCurrentElement() {

        final WebDriver.TargetLocator targetLocator = webDriver().switchTo();
        final WebDriver refocusedWebDriver = targetLocator.frame(webDriverContext().getCurrentElement());

        // yes I actually want to check these objects are the same!
        Assert.assertTrue(
                "Webdriver target locator has returned a different webdriver instance, some webdriver-substeps changes will be required to support this",
                refocusedWebDriver == webDriver());
    }

}
