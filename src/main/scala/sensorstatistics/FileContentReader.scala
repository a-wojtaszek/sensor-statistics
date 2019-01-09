package sensorstatistics

import java.io.File

import scala.io.Source

class FileContentReader(directory: String){

  def readContentsFromCSVFiles: Array[Iterator[String]] = {
    getAllPathsToCSVFiles.map(path => Source.fromFile(path).getLines())
  }

  def countFilesInDirectory: Long = {
    getAllPathsToCSVFiles.length
  }

  private def getAllPathsToCSVFiles: Array[File] = {
    new File(directory).listFiles().filter(_.getPath.endsWith(".csv"))
  }
}
