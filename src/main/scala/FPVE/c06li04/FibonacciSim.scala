package FPVE.c06li04

import spinal.core._
import spinal.core.sim._

object FibonacciSim {
  def main(args: Array[String]) {
    val systemClock = 50 MHz
    val resetPeriod = 36

    val simulationPeriod = ((1 GHz) / systemClock).toInt

    SimConfig.withWave.doSim(Fibonacci(1<<5, 1<<20)) { dut =>
      dut.clockDomain.forkStimulus(period = simulationPeriod)

      //
      // Reset when level is low
      //
      dut.clockDomain.assertReset()
      sleep(resetPeriod)
      dut.clockDomain.deassertReset()

      dut.io.n #= 7
      dut.io.start #= true

      dut.clockDomain.waitSampling(10)

      dut.io.start #= false
      dut.clockDomain.waitSampling(1)

      dut.io.n #= 13
      dut.io.start #= true

      dut.clockDomain.waitSampling(20)
    }
  }
}