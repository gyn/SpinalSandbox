package FPVE.c04ex06

import spinal.core._

object StopWatchMain {
  def main(args: Array[String]) {
    val systemClock = 50 MHz
    val systemInterval = 100 ms

    val systemCycles = (systemClock * systemInterval).toInt

    SpinalConfig(targetDirectory = "rtl").generateVerilog(StopWatch(interval = systemCycles))
  }
}