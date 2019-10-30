package FPGA4Fun.p08

import spinal.core._

object DebouncerMain {
  def main(args: Array[String]) {
    val width = 17
    SpinalConfig(targetDirectory = "rtl").generateVerilog(Debouncer(width))
  }
}