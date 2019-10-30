package FPGA4Fun.p08

import spinal.core._
import spinal.core.sim._

object DebouncerSim {
  def main(args: Array[String]) {
    val systemClock = 50 MHz
    val resetPeriod = 36

    val simulationPeriod = ((1 GHz) / systemClock).toInt

    val width = 17

    SimConfig.withWave.doSim(Debouncer(width)) { dut =>
      dut.clockDomain.forkStimulus(period = simulationPeriod)

      //
      // Reset when level is low
      //
      dut.clockDomain.assertReset()
      dut.io.button #= false
      sleep(resetPeriod)
      dut.clockDomain.deassertReset()

      dut.clockDomain.waitSampling(80)

      dut.io.button #= true
      dut.clockDomain.waitSampling(80000)

      dut.io.button #= false
      dut.clockDomain.waitSampling(80000)
    }
  }
}