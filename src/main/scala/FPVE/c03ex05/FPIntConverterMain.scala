package FPVE.c03ex05

import spinal.core._

object Int2FPMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl").generateVerilog(new Int2FP)
  }
}

object FP2IntMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl").generateVerilog(new FP2Int)
  }
}
