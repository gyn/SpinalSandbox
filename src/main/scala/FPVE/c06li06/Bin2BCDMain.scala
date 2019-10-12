package FPVE.c06li06

import spinal.core._

object Bin2BCDMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl").generateVerilog(Bin2BCD(5))
  }
}