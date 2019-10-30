package FPGA4Fun.p01

import spinal.core._
import spinal.core.sim._

object SimpleBeepSim {
  def main(args: Array[String]) {
    val systemClock = 50 MHz
    val resetPeriod = 36

    val simulationPeriod = ((1 GHz) / systemClock).toInt

    val targetHz = 440 Hz
    val width = (systemClock / targetHz / 2).toInt

    SimConfig.withWave.doSim(SimpleBeep(width)) { dut =>
      dut.clockDomain.forkStimulus(period = simulationPeriod)

      //
      // Reset when level is low
      //
      dut.clockDomain.assertReset()
      sleep(resetPeriod)
      dut.clockDomain.deassertReset()

      dut.clockDomain.waitSampling(300000)
    }
  }
}