package FPVE.c03ex04

import spinal.core._

object FPGreaterThanMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl").generateVerilog(new FPGreaterThan)
  }
}