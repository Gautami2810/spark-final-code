package org.example.sparktask
import org.apache.log4j.Logger

import org.example.sparktask.processing.DataProcessing.{getCurrentDate, processDateColumn}
import org.example.sparktask.processing.dataIO._
import org.example.sparktask.util.dataUtil.{createSparkSession, loadInputConfig, loadOutputConfig}

object sparkMainJob {
  @transient lazy val logger: Logger = Logger.getLogger(getClass.getName)

  def main(args: Array[String]): Unit = {

    try {

      logger.info("Spark session creation...")
      val spark = createSparkSession("spark-emr-cluster")
      logger.info("Spark session created...")

      logger.info("Input configuration loaded ...")
      val inputConfig: Map[String, Any] = loadInputConfig()

      logger.info("Dataframe creation...")
      val input_options = Map("inferSchema" -> "true", "header" -> "true", "delimiter" -> ",")
      val dataframe = readFile(spark, inputConfig, input_options)
      logger.info("Count of records: " + dataframe.select("*").count())

      logger.info("Dataframe processing ....")


      val employee_dataframe = processDateColumn(dataframe, "new_joining_date", "dd-MM-yyyy")
      logger.info("Output configuration loaded...")
      val outputConfig = loadOutputConfig()
      val partition_columns = Seq("year", "month", "day")

      /** Repartitioning for fast and efficient processing of the dataframe */
      val numPartitions = 4 * spark.sparkContext.defaultParallelism
      val repartitionedDF = employee_dataframe.repartition(numPartitions, partition_columns.map(col): _*)


      logger.info("Output being written...")
      writeFile(spark, repartitionedDF, outputConfig, partition_columns)

      logger.info("Spark connection closing...")
      spark.close()

    } catch {
      case e => {
        logger.error("Error occured ")
        e.printStackTrace()
      }
    }


  }

}
