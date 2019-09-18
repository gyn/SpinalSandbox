package FPVE.c02ex02

import spinal.core._

object BinaryDecoder2bMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl").generateVerilog(new BinaryDecoder2b)
  }
}

object BinaryDecoder3bMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl").generateVerilog(new BinaryDecoder3b)
  }
}

object BinaryDecoder4bMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl").generateVerilog(new BinaryDecoder4b)
  }
}