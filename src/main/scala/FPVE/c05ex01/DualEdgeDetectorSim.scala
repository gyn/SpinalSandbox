package FPVE.c05ex01

import spinal.core._
import spinal.core.sim._

object DualEdgeDetectorMooreSim {
  def main(args: Array[String]) {
    val systemClock = 50 MHz
    val resetPeriod = 36

    val simulationPeriod = ((1 GHz) / systemClock).toInt

    SimConfig.withWave.doSim(new DualEdgeDetectorMoore) { dut =>
      dut.clockDomain.forkStimulus(period = simulationPeriod)

      //
      // Reset with level is low
      //
      dut.clockDomain.assertReset()
      dut.io.level #= false
      sleep(resetPeriod)
      dut.clockDomain.deassertReset()

      // enter stateInit and then stateLevelLow
      dut.clockDomain.waitSampling(3)
      // enter statePosedge and then stateLevelHigh
      sleep(3)
      dut.io.level #= true
      dut.clockDomain.waitSampling(3)
      // enter stateNegedge and then stateLevelLow
      dut.io.level #= false
      dut.clockDomain.waitSampling(3)
      //  enter statePosedge and then stateLevelHigh again
      dut.io.level #= true
      dut.clockDomain.waitSampling(3)
      // enter stateNegedge
      dut.io.level #= false
      dut.clockDomain.waitSampling(1)

      //
      // Reset with level is high
      //
      dut.clockDomain.assertReset()
      dut.io.level #= true
      sleep(resetPeriod)
      dut.clockDomain.deassertReset()

      // enter stateInit and then stateLevelHigh
      dut.clockDomain.waitSampling(3)
      // enter stateNegedge and then stateLevelLow
      sleep(3)
      dut.io.level #= false
      dut.clockDomain.waitSampling(3)
      //  enter statePosedge and then stateLevelHigh
      dut.io.level #= true
      dut.clockDomain.waitSampling(3)
    }
  }
}

object DualEdgeDetectorMealySim {
  def main(args: Array[String]) {
    val systemClock = 50 MHz
    val resetPeriod = 36

    val simulationPeriod = ((1 GHz) / systemClock).toInt

    SimConfig.withWave.doSim(new DualEdgeDetectorMealy) { dut =>
      dut.clockDomain.forkStimulus(period = simulationPeriod)

      //
      // Reset with level is low
      //
      dut.clockDomain.assertReset()
      dut.io.level #= false
      sleep(resetPeriod)
      dut.clockDomain.deassertReset()

      // enter stateInit and then stateLevelLow
      dut.clockDomain.waitSampling(3)
      // enter then stateLevelHigh
      sleep(3)
      dut.io.level #= true
      dut.clockDomain.waitSampling(3)
      // enter stateLevelLow
      dut.io.level #= false
      dut.clockDomain.waitSampling(3)
      //  enter stateLevelHigh again
      dut.io.level #= true
      dut.clockDomain.waitSampling(3)
      // enter stateLevelLow again
      dut.io.level #= false
      dut.clockDomain.waitSampling(1)

      //
      // Reset with level is high
      //
      dut.clockDomain.assertReset()
      dut.io.level #= true
      sleep(resetPeriod)
      dut.clockDomain.deassertReset()

      // enter stateInit and then stateLevelHigh
      dut.clockDomain.waitSampling(3)
      // enter stateLevelLow
      sleep(3)
      dut.io.level #= false
      dut.clockDomain.waitSampling(3)
      //  enter stateLevelHigh
      dut.io.level #= true
      dut.clockDomain.waitSampling(3)
    }
  }
}