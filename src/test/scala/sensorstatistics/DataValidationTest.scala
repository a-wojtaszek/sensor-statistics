package sensorstatistics

import org.scalatest.FunSuite

class DataValidationTest extends FunSuite {

  val sampleData: List[String] = List("sensor-id,humidity", "s1,10")

  test("Measurement line has sensor id and the humidity value") {
    DataValidation.useSchemeForMeasurement(sampleData.toStream).foreach(row =>
      assert(row.humidity == "10" && row.sensorId == "s1"))
  }
}
