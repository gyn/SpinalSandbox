package FPGA4Fun.p01

import spinal.core._

object SimpleBeepMain {
  def main(args: Array[String]) {
    val systemClock = 50 MHz
    val targetHz = 440 Hz
    val width = (systemClock / targetHz / 2).toInt
    SpinalConfig(targetDirectory = "rtl").generateVerilog(SimpleBeep(width))
  }
}