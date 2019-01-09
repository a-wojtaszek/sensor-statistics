package sensorstatistics

object DataValidation {

  def filterMeasurement(measurements: Stream[Scheme], predicate: Scheme => Boolean): Stream[Scheme] = {
    measurements.filter(predicate).map(data => Scheme(data.sensorId, data.humidity))
  }

  private [sensorstatistics] def useSchemeForMeasurement(data: Stream[String]): Stream[Scheme] = {
    val dataWithoutHeader: Stream[Array[String]] = data.drop(1).map(_.split(","))
    dataWithoutHeader.map(elem => Scheme(elem(0), elem(1)))
  }
}
