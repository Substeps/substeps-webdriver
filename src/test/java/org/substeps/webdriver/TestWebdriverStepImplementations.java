package org.substeps.webdriver;

import com.technophobia.substeps.model.SubSteps;
import com.technophobia.webdriver.substeps.runner.DefaultExecutionSetupTearDown;
import org.junit.Assert;

import java.io.File;
import java.io.FilenameFilter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ian on 09/02/17.
 */
@SubSteps.StepImplementations(requiredInitialisationClasses = DefaultExecutionSetupTearDown.class)
public class TestWebdriverStepImplementations {

    @SubSteps.Step("AssertScreenshotFileExists \"([^\"]*)\" something")
    public void assertScreenShotFileCreated(String prefix){

        // a file should have been created

        File currentDir = new File(".");
        File[] foundFiles =
        currentDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith(prefix);
            }
        });
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmm");
        LocalDateTime now = LocalDateTime.now();

        boolean found = false;
        Pattern p = Pattern.compile(prefix + "_(\\d{10})\\.png");
        for (File f : foundFiles){

            Matcher m = p.matcher(f.getName());

            if (m.matches()){
                String dateString = m.group(1);
                LocalDateTime fileDate = LocalDateTime.parse(dateString, formatter);

                long minutes = fileDate.until( now, ChronoUnit.MINUTES);

                if (minutes < 1){
                    found = true;
                    break;
                }

            }
        }
        Assert.assertTrue("Expecting to have found screenshot file", found);


    }
}
