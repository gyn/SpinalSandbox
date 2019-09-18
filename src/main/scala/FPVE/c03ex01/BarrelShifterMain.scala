package FPVE.c03ex01

import spinal.core._

object BarrelShifterLeftMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl").generateVerilog(BarrelShifterLeft(width = 32))
  }
}

object BarrelShifterRightMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl").generateVerilog(BarrelShifterRight(width = 32))
  }
}

object BarrelShifterMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl").generateVerilog(BarrelShifter(width = 32))
  }
}

object BarrelShifterByReverseMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl").generateVerilog(BarrelShifterByReverse(width = 32))
  }
}