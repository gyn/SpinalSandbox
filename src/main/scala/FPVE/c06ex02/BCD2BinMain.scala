package FPVE.c06ex02

import spinal.core._

object BCD2BinMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl").generateVerilog(BCD2Bin(8))
  }
}