package FPVE.c04ex03

import spinal.core._

object RotatingLEDBannerMain {
  def main(args: Array[String]) {
    val systemClock = 50 MHz
    val systemInterval = 1 sec
    val systemCycles = (systemClock * systemInterval).toInt
    SpinalConfig(targetDirectory = "rtl").generateVerilog(RotatingLEDBanner(interval = systemCycles))
  }
}