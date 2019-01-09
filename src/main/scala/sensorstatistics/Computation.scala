package sensorstatistics

object Computation {

  val valueNaN: String = "NaN"

  def sortByAvg(statistics: Map[String, Statistics]): Seq[(String, Statistics)] = {
    val stats: Stream[(String, Statistics)] = statistics.toStream
    val (naNValues, notNaNValues) = stats.partition { case (_, value) => value.avg == valueNaN }
    notNaNValues.sortBy(_._2.avg).reverse #::: naNValues
  }

  def getStatistics(groupedData: Map[String, Stream[String]]): Map[String, Statistics] = {
    groupedData.map { case (key, value) =>
      (key, calculateStatistics(value.filterNot(value => value.equals(valueNaN)).map(_.toLong)))
    }
  }

  private def calculateStatistics(values: Stream[Long]): Statistics = {
    values match {
      case value if value.isEmpty => Statistics(valueNaN, valueNaN, valueNaN)
      case value =>
        Statistics(value.min.toString, value.max.toString, (value.foldLeft(0.0)(_ + _) / value.length).toString)
    }
  }

  def groupBySensor(fileContent: Array[Stream[String]]): Map[String, Stream[String]] = {
    val data: Array[Scheme] = validData(fileContent).flatten
    data.groupBy(_.sensorId).map(elem => (elem._1, elem._2.toStream.map(_.humidity)))
  }

  def reportsHowManyMeasurementsItProcessed(fileContent: Array[Stream[String]]): Long = {
    validData(fileContent).map(_.size).sum
  }

  def reportsHowManyMeasurementsFailed(fileContent: Array[Stream[String]]): Long = {
    validData(fileContent).map(content =>
      DataValidation.filterMeasurement(content, (c: Scheme) => c.humidity.equals(valueNaN)).size).sum
  }

  private def validData(contentFiles: Array[Stream[String]]): Array[Stream[Scheme]] = {
    contentFiles.map(content => DataValidation.useSchemeForMeasurement(content))
  }
}
