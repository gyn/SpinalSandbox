package FPVE.c04ex02

import spinal.core._

object PWMLEDDimmerMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl").generateVerilog(PWMLEDDimmer(width = 4))
  }
}
