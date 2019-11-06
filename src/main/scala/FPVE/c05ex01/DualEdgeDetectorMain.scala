package FPVE.c05ex01

import spinal.core._

object DualEdgeDetectorMooreMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl").generateVerilog(new DualEdgeDetectorMoore)
  }
}

object DualEdgeDetectorMealyMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl").generateVerilog(new DualEdgeDetectorMealy)
  }
}

object DualEdgeDetectorGateMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl").generateVerilog(new DualEdgeDetectorGate)
  }
}

object DualEdgeDetectorMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl").generateVerilog(new DualEdgeDetector)
  }
}