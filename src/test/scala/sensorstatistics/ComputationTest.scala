package sensorstatistics

import org.scalatest.{Assertion, FunSuite}
import sensorstatistics.Computation._

class ComputationTest extends FunSuite {

  val sampleData: Array[List[String]] = Array(
    List("sensor-id,humidity", "s1,10", "s2,88", "s1,NaN"),
    List("sensor-id,humidity", "s2,80", "s3,NaN", "s2,78", "s1,98"))

  test("It reports how many measurements it processed") {
    assert(reportsHowManyMeasurementsItProcessed(sampleData.map(_.toStream)) === 7)
  }

  test("It reports how many measurements failed.") {
    assert(reportsHowManyMeasurementsFailed(sampleData.map(_.toStream)) === 2)
  }

  test("Group data by sensor.") {
    assertForGroupBySensor("s1", List("10", "98", "NaN"))
    assertForGroupBySensor("s2", List("88", "80", "78"))
    assertForGroupBySensor("s3", List("NaN"))
  }

  test("For each sensor it calculates min humidity") {
    assert(getStatisticsForGroupedData("s1").min === "10")
    assert(getStatisticsForGroupedData("s2").min === "78")
  }

  test("For each sensor it calculates max humidity") {
    assert(getStatisticsForGroupedData("s1").max === "98")
    assert(getStatisticsForGroupedData("s2").max === "88")
  }

  test("For each sensor it calculates avg humidity") {
    assert(getStatisticsForGroupedData("s1").avg === "54.0")
    assert(getStatisticsForGroupedData("s2").avg === "82.0")
  }

  test("`Sensors with only `NaN` measurements have min/avg/max as `NaN/NaN/NaN`") {
    assert(getStatisticsForGroupedData("s3").min === "NaN")
  }

  test("Program sorts sensors by highest avg humidity (`NaN` values go last)") {
    assert(sortByAvg(getStatisticsForGroupedData).map(_._2.avg).toList == List("82.0", "54.0", "NaN"))
  }

  private def assertForGroupBySensor(key: String, values: List[String]): Assertion = {
    assert(groupBySensor(sampleData.map(_.toStream))(key).toList.sorted === values.sorted)
  }

  private def getStatisticsForGroupedData: Map[String, Statistics] = {
    Computation.getStatistics(groupBySensor(sampleData.map(_.toStream)))
  }
}
