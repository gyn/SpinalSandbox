package FPVE.c05ex02

import spinal.core._

object DebouncerMain {
  def main(args: Array[String]) {
    val systemClock = 50 MHz
    val systemInterval = 20 ms

    val systemCycles = (systemClock * systemInterval).toInt

    SpinalConfig(targetDirectory = "rtl").generateVerilog(Debouncer(cycles = systemCycles))
  }
}