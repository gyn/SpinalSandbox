package FPVE.c06ex01

import spinal.core._
import spinal.core.sim._

object DebouncerSim {
  def main(args: Array[String]) {
    val systemClock = 50 MHz
    val simulationInterval = 20 us
    val resetPeriod = 36

    val simulationCycles = (systemClock * simulationInterval).toInt
    val simulationPeriod = ((1 GHz) / systemClock).toInt

    SimConfig.withWave.doSim(Debouncer(cycles = simulationCycles)) { dut =>
      dut.clockDomain.forkStimulus(period = simulationPeriod)

      //
      // Reset when level is low
      //
      dut.clockDomain.assertReset()
      dut.io.level #= false
      sleep(resetPeriod)
      dut.clockDomain.deassertReset()

      // hold
      dut.clockDomain.waitSampling(3)

      // a rising edge
      sleep(3)
      dut.io.level #= true
      sleep(200)
      dut.io.level #= false
      sleep(200)
      dut.io.level #= true
      dut.clockDomain.waitSampling(1200)

      // a failing edge
      sleep(3)
      dut.io.level #= false
      sleep(200)
      dut.io.level #= true
      sleep(200)
      dut.io.level #= false
      dut.clockDomain.waitSampling(1200)
    }
  }
}