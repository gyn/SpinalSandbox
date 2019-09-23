package FPVE.c02ex01

import spinal.core._

object GreaterThan2bMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl/c02ex01/01_greater_than_2b/rtl").generateVerilog(new GreaterThan2b)
  }
}

object Equal2bMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl").generateVerilog(new Equal2b)
  }
}

object GreaterThan4bMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl/c02ex01/05_greater_than_4b/rtl").generateVerilog(new GreaterThan4b)
  }
}