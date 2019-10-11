package FPVE.c06li04

import spinal.core._

object FibonacciMain {
  def main(args: Array[String]) {
    SpinalConfig(targetDirectory = "rtl").generateVerilog(Fibonacci(5, 20))
  }
}