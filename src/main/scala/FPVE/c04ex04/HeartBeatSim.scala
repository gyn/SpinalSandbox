package FPVE.c04ex03

import spinal.core._
import spinal.core.sim._

object HeartBeatSim {
  def main(args: Array[String]) {
    val systemClock = 50 MHz
    val simulationInterval = 1 us
    val resetPeriod = 10

    val simulationCycles = (systemClock * simulationInterval).toInt
    val simulationPeriod = ((1 GHz) / systemClock).toInt

    SimConfig.withWave.doSim(HeartBeat(interval = simulationCycles)) { dut =>
      dut.clockDomain.forkStimulus(period = simulationPeriod)

      val limit = 22 * simulationCycles

      //
      // Reset with en equals false and cw equals false
      //
      dut.clockDomain.assertReset()
      dut.io.en #= false
      dut.io.cw #= false
      sleep(resetPeriod)
      dut.clockDomain.deassertReset()

      var index = 0
      while (index < limit) {
        dut.clockDomain.waitSampling()

        index += 1
      }

      sleep(1)

      //
      // Reset with en equals true and cw equals false
      //
      dut.clockDomain.assertReset()
      dut.io.en #= true
      dut.io.cw #= false
      sleep(resetPeriod)
      dut.clockDomain.deassertReset()

      index = 0
      while (index < limit) {
        dut.clockDomain.waitSampling()

        index += 1
      }

      sleep(1)

      //
      // Reset with en equals true and cw equals true
      //
      dut.clockDomain.assertReset()
      dut.io.en #= true
      dut.io.cw #= true
      sleep(resetPeriod)
      dut.clockDomain.deassertReset()

      index = 0
      while (index < limit) {
        dut.clockDomain.waitSampling()

        index += 1
      }

      sleep(1)

      //
      // switch cw to false on the fly without reset
      //
      dut.io.en #= true
      dut.io.cw #= false

      index = 0
      while (index < limit) {
        dut.clockDomain.waitSampling()

        index += 1
      }
    }
  }
}