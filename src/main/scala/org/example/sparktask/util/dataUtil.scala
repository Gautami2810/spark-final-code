package org.example.sparktask.util

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.example.sparktask.config.dataConfig

object dataUtil {

  /** Loading configuration map form from config*/
  val configMap = dataConfig.configMap


  def createSparkSession(appname: String): SparkSession = {

    /** Filtering only spark configuration*/
    val sparkConfig = configMap.filterKeys(_.startsWith("spark"))

    /** AWS Credentials loaded from the system */
   // val awsCredentials = new DefaultAWSCredentialsProviderChain()
   //   .getCredentials()

    /** Using sparkConf object to set configuration*/
    val sparkConf = new SparkConf()
    for ((k, v) <- sparkConfig) {
      sparkConf.set(k, v.toString)
    }

    /** Creating spark session*/
    val spark = SparkSession.builder
      .appName(appname)
      .config(sparkConf)
     // .config("spark.hadoop.fs.s3a.access.key", awsCredentials.getAWSAccessKeyId)
      //.config("spark.hadoop.fs.s3a.secret.key", awsCredentials.getAWSSecretKey)
      .getOrCreate()

    spark
  }

  /** Input configuration loading[filtering input configuration]*/
  def loadInputConfig(): Map[String, Any] = configMap.filterKeys(_.startsWith("input"))

  /** Ouptput configuration loading [filtering output configuration]*/
  def loadOutputConfig(): Map[String, Any] = configMap.filterKeys(_.startsWith("output"))

}
