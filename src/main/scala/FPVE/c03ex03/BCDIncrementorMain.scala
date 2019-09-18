package FPVE.c03ex03

import spinal.core._

object BCDCellMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl").generateVerilog(new BCDCell)
  }
}

object BCDIncrementorMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl").generateVerilog(BCDIncrementor(Nr = 3))
  }
}