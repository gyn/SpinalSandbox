package FPVE.c06li06

import spinal.core._
import spinal.core.sim._

object Bin2BCDSim {
  def main(args: Array[String]) {
    val systemClock = 50 MHz
    val resetPeriod = 36

    val simulationPeriod = ((1 GHz) / systemClock).toInt

    val width = 13

    SimConfig.withWave.doSim(Bin2BCD(width)) { dut =>
      dut.clockDomain.forkStimulus(period = simulationPeriod)

      //
      // Reset when level is low
      //
      dut.clockDomain.assertReset()
      sleep(resetPeriod)
      dut.clockDomain.deassertReset()

      for (i <- 0 until 1 << width) {
        dut.io.n #= i
        dut.clockDomain.waitSampling(1)
        dut.io.start #= true

        dut.clockDomain.waitSampling(width + 2)

        assert(dut.io.done.toBoolean)

        dut.io.start #= false
        dut.clockDomain.waitSampling(1)
      }
    }
  }
}