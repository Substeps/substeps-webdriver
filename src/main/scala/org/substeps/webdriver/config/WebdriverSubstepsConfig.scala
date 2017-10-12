package org.substeps.webdriver.config


import java.io.File

import com.technophobia.substeps.model.exception.SubstepsConfigurationException
import com.typesafe.config.Config
import org.substeps.runner.NewSubstepsExecutionConfig
import org.substeps.webdriver.{DriverFactory, WebdriverSubstepsConfigurationKeys}

import scala.collection.JavaConverters._

//
case class DriverFactoryConfigKey(key : String, factoryClass : Class[_ <: DriverFactory]){

  def driverFactory() = try {
    factoryClass.newInstance
  } catch {
    case e: Exception =>
      throw new SubstepsConfigurationException("Failed to create Webdriver factory: " + factoryClass.toString, e)
  }
}

object DriverFactoryConfigKey{
  def apply(cfg : Config) : DriverFactoryConfigKey = {

    val className = cfg.getString("class")
    val clazz = Class.forName(className)

    if (!classOf[DriverFactory].isAssignableFrom(clazz)){
      throw new SubstepsConfigurationException(s"Webdriver factory class $className must implement org.substeps.webdriver.DriverFactory")
    }

    DriverFactoryConfigKey(cfg.getString("key"), clazz.asSubclass(classOf[DriverFactory]))
  }
}

object WebdriverSubstepsConfig {

  def getDriverFactories(cfg : Config): Map[String, DriverFactoryConfigKey] = {

    val defaultDriverFactories = cfg.getConfigList("org.substeps.webdriver.default.driverFactories").asScala.map(c => {

      val df = DriverFactoryConfigKey(c)
      (df.key -> df)
    }).toMap

    val driverFactories =
      if (cfg.hasPath("org.substeps.webdriver.driverFactories")){

      cfg.getConfigList("org.substeps.webdriver.driverFactories").asScala.map(c => {
        val df = DriverFactoryConfigKey(c)
        (df.key -> df)
      }).toMap

    } else Map()

    // take the defaults and overlay with any bespoke
    val result = defaultDriverFactories ++ driverFactories

    result
  }


  def getDriverFactory(cfg: Config): DriverFactory = {
    val key = cfg.getString(WebdriverSubstepsConfigurationKeys.DRIVER_TYPE_KEY).toUpperCase
    val driverFactories = WebdriverSubstepsConfig.getDriverFactories(cfg)

      driverFactories.get(key) match {
      case None => throw new SubstepsConfigurationException("No factory registered / initialised for key: " + key)
      case Some(driverFactoryConfigKey)  => driverFactoryConfigKey.driverFactory
    }
  }


  def determineBaseURL(baseUrlProperty: String) = {

    val property = removeTrailingSlash(baseUrlProperty)

    if (!property.startsWith("http") && !property.startsWith("file://")){

       removeTrailingSlash(new File(property).toURI.toString)
    }
    else property
  }


  def removeTrailingSlash(string: String): String = {
    if (string.endsWith("/")) return string.substring(0, string.length - 1)
    string
  }


}
