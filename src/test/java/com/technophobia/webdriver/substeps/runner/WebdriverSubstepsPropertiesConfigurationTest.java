/*
 *  Copyright Technophobia Ltd 2012
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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.technophobia.substeps.model.Configuration;
import com.typesafe.config.Config;
import org.junit.*;

/**
 * @author imoore
 */
public class WebdriverSubstepsPropertiesConfigurationTest {

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

    @Test
    public void testGithubAuthOverride(){

        Config cfg = Configuration.INSTANCE.getConfig();

        Assert.assertThat(cfg.hasPath("github.auth"), is(true));

        Assert.assertThat(cfg.getConfig("github.auth").getString("username"), is("iantmoore"));


        Assert.assertThat(cfg.getString("github.auth.username"), is("iantmoore"));

//        if (cfg.hasPath("github.token") && cfg.hasPath("github.token.secret")){
//
//            System.setProperty("wdm.gitHubTokenName", cfg.getString("github.token"));
//            System.setProperty("wdm.gitHubTokenSecret", cfg.getString("github.token.secret"));
//        }
    }




    @Ignore
    @Test
    public void checkOverrides() {
        // can't run this on jenkins as that sets environment to jenkins - can't
        // re-init the config..
        System.setProperty("environment", "localhost");
        Assert.assertFalse(WebdriverSubstepsPropertiesConfiguration.INSTANCE.closeVisualWebDriveronFail());
    }

    @Test
    public void testRelativeURLResolvesToFileProtocol() throws SecurityException, NoSuchMethodException,
            IllegalArgumentException, IllegalAccessException, InvocationTargetException {

        final WebdriverSubstepsPropertiesConfiguration config = WebdriverSubstepsPropertiesConfiguration.INSTANCE;

        final Method determineBaseURLMethod = config.getClass().getDeclaredMethod("determineBaseURL", String.class);
        determineBaseURLMethod.setAccessible(true);

        final String baseUrl = (String) determineBaseURLMethod.invoke(config, "src/web");

        Assert.assertThat(baseUrl, startsWith("file:/"));

        final String baseUrl2 = (String) determineBaseURLMethod.invoke(config, "./src/web");

        final File current = new File(".");

        Assert.assertThat(baseUrl2, is(current.toURI().toString() + "src/web"));

        final String baseUrl3 = (String) determineBaseURLMethod.invoke(config, "http://blah-blah.com/src/web");

        Assert.assertThat(baseUrl3, startsWith("http://"));

        final String baseUrl4 = (String) determineBaseURLMethod.invoke(config, "file://some-path/whatever");

        Assert.assertThat(baseUrl4, is("file://some-path/whatever"));

    }


    @Test
    public void testConstructor_DefaultProxySettings() throws NoSuchFieldException,
            SecurityException, IllegalArgumentException, IllegalAccessException {
        final WebdriverSubstepsPropertiesConfiguration config = WebdriverSubstepsPropertiesConfiguration.INSTANCE;

        Assert.assertThat(config.getHtmlUnitProxyHost(), is(""));
        Assert.assertThat(config.getHtmlUnitProxyPort(), is(8080));
        Assert.assertThat(config.getNetworkProxyHost(), is(""));
        Assert.assertThat(config.getNetworkProxyPort(), is(8080));
    }

}
