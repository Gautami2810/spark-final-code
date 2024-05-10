package org.example.sparktask.processing

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.example.sparktask.processing.DataProcessing.getCurrentDate

object dataIO {


  /** Read function : It takes four parameters which are sparkSession, input_path ,format in which Data is present and options eg. header-> true */
  def readFile(spark: SparkSession, inputConfig: Map[String, Any], input_options: Map[String, String] = Map()): DataFrame = {

    val dataFrame = spark.read
      .format(inputConfig("input.format").toString)
      .options(input_options)
      .load(inputConfig("input.file_path").toString)

    dataFrame
  }




  /** Write function : It takes four parameters which are sparkSession, output_path ,format in which Data should be written and partitionColumns for partitioning */
  def writeFile(spark: SparkSession, dataframe: DataFrame, outputConfig: Map[String, Any], partitionColumns: Seq[String]=Seq()): Unit = {
    val currentDate = getCurrentDate("Asia/Kolkata", "yyyy-MM-dd_HH-mm-ss")
    val file_path = outputConfig("output.file_path").toString
    val output_file_path = s"$file_path$currentDate"
    val writer = dataframe.write
      .mode("overwrite")
      .format(outputConfig("output.format").toString)

    if (partitionColumns.nonEmpty) writer.partitionBy(partitionColumns: _*)

    writer.save(output_file_path)

  }
}
