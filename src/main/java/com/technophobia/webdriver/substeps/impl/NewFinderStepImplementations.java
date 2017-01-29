package com.technophobia.webdriver.substeps.impl;

import com.google.common.io.Files;
import com.technophobia.substeps.model.Configuration;
import com.technophobia.substeps.model.SubSteps;
import com.technophobia.webdriver.substeps.runner.DefaultExecutionSetupTearDown;
import com.technophobia.webdriver.util.WebDriverSubstepsBy;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ian on 21/01/17.
 */

@SubSteps.StepImplementations(requiredInitialisationClasses = DefaultExecutionSetupTearDown.class)
public class NewFinderStepImplementations extends AbstractWebDriverSubStepImplementations {

    private static final Logger logger = LoggerFactory.getLogger(NewFinderStepImplementations.class);

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmm");


    // xpath query //*[contains(@id, 'f41_txt')]
    // $x("//*[@id='sogei_mapping_left_container']")

    // $x("//*[@id='sogei_mapping_left_container']//div[contains(text(),'BANDY')]")  selects the item

    // Handy little ! xpath to get to the chevron to do the whatsit
    // $x("//*[@id='sogei_mapping_left_container']//div[contains(text(),'BASKET')]/ancestor::div[contains(@class, 'mapperItem draggable')]//div[contains(@class, 'chevron')]")


    @SubSteps.Step("FindByXpath2 (.*)$")
    public WebElement findByXpath2(String xpath) {

        logger.debug("Looking for item with xpath " + xpath);
        this.webDriverContext().setCurrentElement(null);
        WebElement elem = this.webDriverContext().waitForElement(By.xpath(xpath));
        Assert.assertNotNull("expecting an element with xpath " + xpath, elem);
        this.webDriverContext().setCurrentElement(elem);

        return elem;

    }

    @SubSteps.Step("FindById \"([^\"]*)\" with text matching regex (.*)$")
    public WebElement findByIdWithRegex(String id, String regex) {

        logger.debug("Looking for id with text matching regex " + regex);
        this.webDriverContext().setCurrentElement(null);
        WebElement elem = this.webDriverContext().waitForElement(WebDriverSubstepsBy.ByIdWithTextMatchingRegex(id, regex));
        Assert.assertNotNull("expecting an element with id " + id + " and text that matches regex: " + regex, elem);
        this.webDriverContext().setCurrentElement(elem);

        return elem;

    }

    /**
     *
     * @example FindByXpath with token replacement $x("//*[@id='sogei_mapping_left_container']//div[contains(@class, 'label drilldown') and text() ='%1']") "CALCIO" "fred"
     *
     * @param xpath
     * @param tokens the tokens if any to be substitued into the xpath string using String format specifiers.  NB. If calling this directly in code, the tokens need to be quoted
     * @return the web element
     */
    @SubSteps.Step("FindByXpath with token replacement \\$x\\(\"([^\"]*)\"\\)(.*)$")
    public WebElement findByXpathWithTokenReplacement(String xpath, String tokens) {

        String finalXpath = null;
        if (!tokens.isEmpty()){

            Pattern matcherPattern = Pattern.compile("\"([^\"]*)\"");

            Matcher matcher = matcherPattern.matcher(tokens);

            ArrayList<String> tokenValues = new ArrayList<>();
            while(matcher.find()){

                tokenValues.add( matcher.group(1));
            }

            finalXpath = String.format(xpath, tokenValues.toArray());
        }
        else {
            finalXpath = xpath;
        }

        logger.debug("Looking for item with xpath " + finalXpath);
        this.webDriverContext().setCurrentElement(null);
        WebElement elem = this.webDriverContext().waitForElement(By.xpath(finalXpath));
        Assert.assertNotNull("expecting an element with xpath " + finalXpath, elem);
        this.webDriverContext().setCurrentElement(elem);

        return elem;

    }

    @SubSteps.Step("TakeScreenshot with prefix \"([^\"]*)\"")
    public void takeScreenshot(String filePrefix){

        logger.debug("taking screenshot..");

        LocalDateTime timePoint = LocalDateTime.now();

        timePoint.getMinute();

        String formattedDate = timePoint.format(formatter);

        File out = new File(filePrefix + "_" + formattedDate + ".png");

        try {
            Files.write(getScreenshotBytes(), out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @SubSteps.Step("Select \"([^\"]*)\" option in class \"([^\"]*)\"")
    public void selectOptionInClass(String optionText, String cssClassName) {

        By by = new By.ByClassName(cssClassName);
        WebElement selectElement = waitFor(by, "expecting an element with class ", cssClassName);

        final Select select = new Select(selectElement);
        select.selectByVisibleText(optionText);
    }


    @SubSteps.Step("Select \"([^\"]*)\" option in Id \"([^\"]*)\"")
    public WebElement selectOptionInId(String optionText, String id) throws InterruptedException {

        String xpathString = String.format("//*[@id='%s']//option[ text() ='%s']/..", id, optionText);

        logger.debug("looking for option with xpath: " + xpathString);

        By by = new By.ByXPath(xpathString);

        WebElement selectElement = waitFor(by, "expecting an element with Id ", id);

        final Select select = new Select(selectElement);
        select.selectByVisibleText(optionText);

        int attempt = 0;

        // Make sure text is displayed after the item is selected from the dropdown list
        while (attempt < 3) {
            try {
                WebElement option = select.getFirstSelectedOption();
                String test = option.getText();

                if (test.equals(optionText)) {
                    break;
                }
            }
            catch (NotFoundException e) {
                logger.debug("element not found " + e);
            }

            // Try to select again, sometimes it doesn't do it properly
            logger.debug("Trying to click again");
            select.selectByVisibleText(optionText);

            Thread.sleep(500);
            attempt++;
        }

        return selectElement;
    }

    @SubSteps.Step("ExecuteJavascript (.*)$")
    public void executeJavaScript(String js){
        ((JavascriptExecutor) webDriver()).executeScript(js);

    }

    // TODO - need an example of this
    @SubSteps.Step("Execute substituted Javascript ~([^~]*)~ \"([^\"]*)\"")
    public void executeJavaScript(String js, String filePropertyName){

        String fileName = Configuration.INSTANCE.getString(filePropertyName);

        logger.debug("csv filename: " + fileName);

        File csvFile = new File(fileName);

        String finalJs = String.format(js, csvFile.getAbsolutePath());

        logger.debug("formatted js expression: " + finalJs);

        ((JavascriptExecutor) webDriver()).executeScript(finalJs);
    }

    @SubSteps.Step("WaitFor id \"([^\"]*)\" to hide")
    public void waitForElementToHide(String id) {

        waitUntil(ExpectedConditions.invisibilityOfElementLocated(By.id(id)));


    }



    @SubSteps.Step("WaitFor id \"([^\"]*)\" to be visible")
    public void waitForElementToBeVisible(String id) {

        waitUntil(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
    }






    @SubSteps.Step("NotImplemented")
    public void notImplementedNotFailing() {
        logger.warn("silent test success...");
    }


    @SubSteps.Step("FindByTag \"([^\"]*)\" with text \"([^\"]*)\"")
    public void findByTagAndText(final String tag, String text) {

        final By by = WebDriverSubstepsBy.ByTagAndWithText(tag, text);

        waitFor(by, "expecting an element with tag", tag, "and text", text);
    }

    @SubSteps.Step("FindByTag \"([^\"]*)\" containing text \"([^\"]*)\"")
    public void findByTagContainingText(final String tag, String text) {

        final By by = WebDriverSubstepsBy.ByTagContainingText(tag, text);

        waitFor(by, "expecting an element with tag", tag, "and text", text);
    }

}
