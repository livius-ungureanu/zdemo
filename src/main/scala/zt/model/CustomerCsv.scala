package zt.model
import zio.Chunk

// Pojos
case class CustomerCsv(
  phoneNum: String,
  details: String
)


object CustomerCsv {
  case class CsvLine(phoneNum: String, detail: String)
  def parseCsvLine(line: String): CustomerCsv = {
    val parts = line.split(",")
    CustomerCsv(parts(0), parts(1))
  }
}