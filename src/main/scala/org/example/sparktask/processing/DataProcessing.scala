package org.example.sparktask.processing

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._

import java.time.format.DateTimeFormatter
import java.time.{ZoneId, ZonedDateTime}

object DataProcessing {

  /** processDateColumn : Converts the dataframe timestamp column in a particular format extracting year,month,day of the timestamp*/
  def processDateColumn(df: DataFrame, dateColumn: String,format:String): DataFrame = {

    // Convert the date column to a timestamp column
    val dfWithTimestamp = df.withColumn("timestamp", to_timestamp((df(dateColumn)),format))

    // Extract year, month, and day from the timestamp column
    val dfWithDateColumns = dfWithTimestamp.withColumn("year", year(col("timestamp")))
      .withColumn("month", month(col("timestamp")))
      .withColumn("day", dayofmonth(col("timestamp")))


    dfWithDateColumns
  }

  /** getCurrentDate : Function takes timezone and pattern and produces a dateTime string with the timezone mentioned with the pattern*/
  def getCurrentDate(timezone: String, pattern: String): String = {
    ZonedDateTime.now(ZoneId.of(timezone)).format(DateTimeFormatter.ofPattern(pattern))
  }



}
