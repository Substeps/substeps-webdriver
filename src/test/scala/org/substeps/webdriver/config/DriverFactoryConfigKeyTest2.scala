
package org.substeps.webdriver.config

import java.io.File

import com.technophobia.substeps.model.exception.SubstepsConfigurationException
import com.technophobia.webdriver.util.WebDriverContext
import com.typesafe.config.{Config, ConfigFactory, ConfigValueFactory}
import org.scalatest.{FunSuite, Matchers}
import org.substeps.config.SubstepsConfigLoader
import org.substeps.webdriver.{DriverFactory, WebdriverSubstepsConfigurationKeys}

class TestDriverFactory extends DriverFactory{
  override def create(cfg: Config) = ???
  override def getKey = ???
  override def shutdownWebDriver(webDriverContext: WebDriverContext) = ???
  override def resetWebDriver(webDriverContext: WebDriverContext) = ???
}

class InvalidDriverFactory{}

class DriverFactoryConfigKeyTest2 extends FunSuite with Matchers with WebdriverSubstepsConfigurationKeys {

  test("test invalid drivers throw an excpetion"){

    val cfg = ConfigFactory.empty()
      .withValue("key", ConfigValueFactory.fromAnyRef("k"))
      .withValue("class", ConfigValueFactory.fromAnyRef("org.substeps.webdriver.config.InvalidDriverFactory"))

    assertThrows[SubstepsConfigurationException]{
      DriverFactoryConfigKey(cfg)
    }
  }

  test("test the overriding ability of driver factories"){

    val presetEnv = Option(System.getProperty("environment"))
    System.clearProperty("environment")

    System.setProperty("environment", "test")

    val cfg = SubstepsConfigLoader.loadResolvedConfig

    val driverFactories = WebdriverSubstepsConfig.getDriverFactories(cfg)

    driverFactories.size should be (5)

    driverFactories.get("CHROME").get should be (DriverFactoryConfigKey("CHROME", Class.forName("org.substeps.webdriver.config.TestDriverFactory").asSubclass(classOf[DriverFactory])))

    System.clearProperty("environment")
    if (presetEnv.isDefined && !presetEnv.get.isEmpty) {
      System.setProperty("environment", presetEnv.get)
    }

  }

  // local dev test
  ignore("can actually create a webdriver"){

    val cfg = SubstepsConfigLoader.loadResolvedConfig.withValue(WebdriverSubstepsConfigurationKeys.DRIVER_TYPE_KEY, ConfigValueFactory.fromAnyRef("CHROME"))


    val factory = WebdriverSubstepsConfig.getDriverFactory(cfg) //DriverFactoryRegistry.INSTANCE.getDriverFactory(cfg)

    val chrome = factory.create(cfg)

    chrome should not be (null)
//    chrome.close()
    chrome.quit()

    val cfg2 = SubstepsConfigLoader.loadResolvedConfig.withValue(WebdriverSubstepsConfigurationKeys.DRIVER_TYPE_KEY, ConfigValueFactory.fromAnyRef("FIREFOX"))

    val factory2 = WebdriverSubstepsConfig.getDriverFactory(cfg2) //DriverFactoryRegistry.INSTANCE.getDriverFactory(cfg2)

    val ff = factory2.create(cfg2)

    ff should not be (null)
//    ff.close()
    ff.quit()


  }

  test("resolving url properties correctly"){

    WebdriverSubstepsConfig.determineBaseURL("src/web") should startWith ("file:/")

    WebdriverSubstepsConfig.determineBaseURL("./src/web") should be (new File(".").toURI().toString() + "src/web")

    WebdriverSubstepsConfig.determineBaseURL("http://blah-blah.com/src/web")  should be ("http://blah-blah.com/src/web")

    WebdriverSubstepsConfig.determineBaseURL("file://some-path/whatever")  should be ("file://some-path/whatever")

    /*

            final String baseUrl2 = (String) determineBaseURLMethod.invoke(config, "./src/web");

        final File current = new File(".");

        Assert.assertThat(baseUrl2, is(current.toURI().toString() + "src/web"));

        final String baseUrl3 = (String) determineBaseURLMethod.invoke(config, "http://blah-blah.com/src/web");

        Assert.assertThat(baseUrl3, startsWith("http://"));

        final String baseUrl4 = (String) determineBaseURLMethod.invoke(config, "file://some-path/whatever");

        Assert.assertThat(baseUrl4, is("file://some-path/whatever"));


     */
  }

}
