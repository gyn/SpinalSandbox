package FPVE.c03ex02

import spinal.core._

object PriorityEncoder12bMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl").generateVerilog(new PriorityEncoder12b)
  }
}

object PriorityEncoder12bMuxMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl").generateVerilog(PriorityEncoderMux(width = 12))
  }
}

object DualPriorityEncoderMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl").generateVerilog(new DualPriorityEncoder12b).printPruned()
  }
}

object DualPriorityEncoderMuxMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl").generateVerilog(DualPriorityEncoderMux(width = 12)).printPruned()
  }
}