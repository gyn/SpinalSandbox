package FPVE.c04ex01

import spinal.core._

object SquareWaveGeneratorMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl").generateVerilog(SquareWaveGenerator(configBits = 4))
  }
}