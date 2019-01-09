package sensorstatistics

import org.scalatest.FunSuite

class FileContentReaderTest extends FunSuite {

  val directory: String = "src/test/resources"

  val computation: FileContentReader = new FileContentReader(directory)
  val contents: Array[Iterator[String]] = computation.readContentsFromCSVFiles

  test("It reports how many files it processed") {
    assert(computation.countFilesInDirectory === 2)
  }

  test("Test for proper reading from CSV files") {
    assert(contents.length === 2)
  }
}
