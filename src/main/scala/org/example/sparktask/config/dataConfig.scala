package org.example.sparktask.config

import com.typesafe.config.{Config, ConfigFactory}

import scala.collection.JavaConverters.asScalaSetConverter

object dataConfig {
/** Loading the config file */
  private lazy val config: Config = ConfigFactory.load("application.conf")

  /** Converting config file into map (key,value pair)*/
  def getConfigAsMap(config: Config): Map[String, Any] = {
    config.entrySet().asScala.map { entry =>
      val key = entry.getKey
      val value = entry.getValue.unwrapped() match {
        case nestedConfig: Config => getConfigAsMap(nestedConfig)
        case other => other
      }
      key -> value
    }.toMap
  }

  /** configuration in form of Map */
  val configMap: Map[String, Any] = getConfigAsMap(config)

}
