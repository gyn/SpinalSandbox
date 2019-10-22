package FPVE.c06ex04

import spinal.core._

object FibonacciBCDMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl").generateVerilog(new FibonacciBCD)
  }
}