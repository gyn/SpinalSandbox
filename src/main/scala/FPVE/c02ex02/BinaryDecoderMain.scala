package FPVE.c02ex02

import spinal.core._

object BinaryDecoder2bMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl/c02ex02/01_binary_decoder_2b4b/rtl/").generateVerilog(new BinaryDecoder2b)
  }
}

object BinaryDecoder3bMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl/c02ex02/02_binary_decoder_3b8b/rtl/").generateVerilog(new BinaryDecoder3b)
  }
}

object BinaryDecoder4bMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl/c02ex02/03_binary_decoder_4b16b/rtl/").generateVerilog(new BinaryDecoder4b)
  }
}