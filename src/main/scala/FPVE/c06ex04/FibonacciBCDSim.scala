package FPVE.c06ex04

import spinal.core._
import spinal.core.sim._

object FibonacciBCDSim {
  def main(args: Array[String]) {
    val systemClock = 50 MHz
    val resetPeriod = 36

    val simulationPeriod = ((1 GHz) / systemClock).toInt

    SimConfig.withWave.doSim(new FibonacciBCD) { dut =>
      dut.clockDomain.forkStimulus(period = simulationPeriod)

      //
      // Reset when level is low
      //
      dut.clockDomain.assertReset()
      sleep(resetPeriod)
      dut.clockDomain.deassertReset()

      for (i <- 0 until 10; j <- 0 until 10) {
        val n = (i << 4) + j
        val b = (i * 10) + j
        dut.io.n #= n
        dut.clockDomain.waitSampling(1)
        dut.io.start #= true

        dut.clockDomain.waitSampling(b match { case 0 | 1 => 14 + 3; case x if x > 20 => 2; case _ => 14 + b + 2 })

        dut.io.start #= false
        dut.clockDomain.waitSampling(1)
      }
    }
  }
}