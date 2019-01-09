package sensorstatistics

import sensorstatistics.Computation.{groupBySensor, _}

object Main extends App {

  override def main(args: Array[String]): Unit = {

//    "src/main/resources"
    if (args.length == 0 || args.length > 1) throw new IllegalArgumentException("You didn't set directory to CSV files " +
      "or you set more than one argument in program arguments.")
    val directory: String = args(0)

    val fileContentReader: FileContentReader = new FileContentReader(directory)
    val fileContents: Array[Stream[String]] = fileContentReader.readContentsFromCSVFiles.map(_.toStream)

    val numOfAProcessedFiles = fileContentReader.countFilesInDirectory
    val numOfProcessedMeasurement = reportsHowManyMeasurementsItProcessed(fileContents)
    val numOfFailedMeasurement = reportsHowManyMeasurementsFailed(fileContents)
    val calculatedStatistics = getStatistics(groupBySensor(fileContents))
    val sortedStatistics = sortByAvg(calculatedStatistics)

    val formattedOutput = sortedStatistics.map { case (key, value) =>
      (key, Statistics.unapply(value).getOrElse(0, 0, 0.0))
    }.mkString("\n\t").filterNot(c => c == '(' || c == ')' || c == ' ')


    println(
      s"""
  Num of processed files: $numOfAProcessedFiles
  Num of processed measurements: $numOfProcessedMeasurement
  Num of failed measurements: $numOfFailedMeasurement

  Sensors with highest avg humidity:

  sensor-id,min,max,avg \n\t$formattedOutput
    """
    )
  }
}
