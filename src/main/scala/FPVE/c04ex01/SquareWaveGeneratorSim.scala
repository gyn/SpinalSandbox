package FPVE.c04ex01

import spinal.core._
import spinal.core.sim._

import scala.util.Random

object SquareWaveGeneratorSim {
  def main(args: Array[String]) {
    SimConfig.withWave.doSim(SquareWaveGenerator(Scalar = 100/20, WBits = 4)) { dut =>
      dut.clockDomain.forkStimulus(period = 20)

      val limit = 16 - 1

      for (i <- 0 to limit) {
        for (j <- 0 to limit) {
          dut.clockDomain.waitRisingEdge(5)
          dut.clockDomain.assertReset()

          dut.clockDomain.waitRisingEdge(5)
          dut.io.m #= i
          dut.io.n #= j

          dut.clockDomain.waitRisingEdge(10)
          dut.clockDomain.deassertReset()

          dut.clockDomain.waitRisingEdge(380)
        }
      }

      dut.clockDomain.waitRisingEdge(10)
    }
  }
}