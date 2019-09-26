package FPVE.c04ex03

import spinal.core._

object HeartBeatMain {
  def main(args: Array[String]) {
    val systemClock = 50 MHz
    val systemInterval = 1 sec
    val systemCycles = (systemClock * systemInterval).toInt
    SpinalConfig(targetDirectory = "rtl").generateVerilog(HeartBeat(interval = systemCycles))
  }
}