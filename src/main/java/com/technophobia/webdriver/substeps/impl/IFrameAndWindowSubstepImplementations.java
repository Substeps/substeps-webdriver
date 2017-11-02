package com.technophobia.webdriver.substeps.impl;

import com.technophobia.substeps.model.Scope;
import com.technophobia.substeps.model.SubSteps;
import com.technophobia.substeps.runner.ExecutionContextSupplier;
import com.technophobia.substeps.runner.MutableSupplier;
import com.technophobia.webdriver.substeps.runner.DefaultExecutionSetupTearDown;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Step implementations concerned with iframes and new windows, switching in and out from them.
 */

@SubSteps.StepImplementations(requiredInitialisationClasses = DefaultExecutionSetupTearDown.class)
public class IFrameAndWindowSubstepImplementations extends AbstractWebDriverSubStepImplementations {

    private static final Logger logger = LoggerFactory.getLogger(IFrameAndWindowSubstepImplementations.class);

    private static final MutableSupplier<String> webdriverPageInContext = new ExecutionContextSupplier<>(Scope.SCENARIO, "1");

    /**
     * Switches to the new window which has been opened
     *
     * @example Switch to new window
     * @section windows
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
     *
     * @example Close new window
     * @section windows
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
     *
     * @param iFrameCssSelector the CSS Selector to identify the iframe to switch to
     *                          NB the found iframe cannot be returned - once it has been switched to it will be stale and can't be referenced
     * @example Switch to new frame by CSS selector ".iframe-class"
     * @section windows
     */
    @SubSteps.Step("Switch to new frame by CSS selector \"([^\"]*)\"")
    public void switchToNewFrame(String iFrameCssSelector) {

        WebElement elem = waitFor(By.cssSelector(iFrameCssSelector), "Expecting an iframe with selector: " + iFrameCssSelector);
        webDriver().switchTo().frame(elem);

    }

    /**
     * Switches to the specified iFrame present on current page and store the webdriver element for the iFrame
     *
     * @param frameName the name attribute of the iframe to switch to
     *                  NB the found iframe cannot be returned - once it has been switched to it will be stale and can't be referenced
     * @example Switch to new frame by name "iframe-name"
     * @section windows
     */
    @SubSteps.Step("Switch to new frame by name \"([^\"]*)\"")
    public void switchToNewFrameByName(String frameName) {

        WebElement iframe = waitFor(By.name(frameName), "Expecting an iframe with name: " + frameName);

        logger.debug(" ** game name iframe @ x: " + iframe.getLocation().getX() + " y: " + iframe.getLocation().getY() + " h: " +
                iframe.getSize().getHeight() + " w: " + iframe.getSize().getWidth());

        webDriver().switchTo().frame(iframe);
    }

    /**
     * Switches back to the default content
     *
     * @example Switch to default content
     * @section Windows
     */
    @SubSteps.Step("Switch to default content")
    public void switchToDefaultContent() {

        webDriver().switchTo().defaultContent();
    }

    /**
     * Transfer the focus into the current element (set with a previous Find
     * method) which should be a frame or iframe
     *
     * @example SwitchFrameToCurrentElement
     * @section Windows
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
