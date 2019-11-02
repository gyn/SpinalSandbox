package FPGA4Fun.p10

import spinal.core._

object SimpleCounterMain {
  def main(args: Array[String]) {
    val width = 4
    SpinalConfig(targetDirectory = "rtl").generateVerilog(SimpleCounter(width))
  }
}

object SimpleCounterAndMain {
  def main(args: Array[String]) {
    val width = 4
    SpinalConfig(targetDirectory = "rtl").generateVerilog(SimpleCounterAnd(width))
  }
}

object SimpleCounterAltMain {
  def main(args: Array[String]) {
    val width = 4
    SpinalConfig(targetDirectory = "rtl").generateVerilog(SimpleCounterAlt(width))
  }
}

object ModNCounterMain {
  def main(args: Array[String]) {
    val limit = 10
    SpinalConfig(targetDirectory = "rtl").generateVerilog(ModNCounter(limit))
  }
}