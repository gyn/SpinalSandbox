package FPGA4Fun.p10

import spinal.core._
import spinal.core.sim._

object SimpleCounterSim {
  def main(args: Array[String]) {
    val systemClock = 50 MHz
    val resetPeriod = 36

    val simulationPeriod = ((1 GHz) / systemClock).toInt

    val width = 4
    SimConfig.withWave.doSim(SimpleCounter(width)) { dut =>
      dut.clockDomain.forkStimulus(period = simulationPeriod)

      //
      // Reset when level is low
      //
      dut.clockDomain.assertReset()
      sleep(resetPeriod)
      dut.clockDomain.deassertReset()

      dut.clockDomain.waitSampling(33)
    }
  }
}

object SimpleCounterAndSim {
  def main(args: Array[String]) {
    val systemClock = 50 MHz
    val resetPeriod = 36

    val simulationPeriod = ((1 GHz) / systemClock).toInt

    val width = 4
    SimConfig.withWave.doSim(SimpleCounterAnd(width)) { dut =>
      dut.clockDomain.forkStimulus(period = simulationPeriod)

      //
      // Reset when level is low
      //
      dut.clockDomain.assertReset()
      sleep(resetPeriod)
      dut.clockDomain.deassertReset()

      dut.clockDomain.waitSampling(33)
    }
  }
}

object SimpleCounterAltSim {
  def main(args: Array[String]) {
    val systemClock = 50 MHz
    val resetPeriod = 36

    val simulationPeriod = ((1 GHz) / systemClock).toInt

    val width = 4
    SimConfig.withWave.doSim(SimpleCounterAlt(width)) { dut =>
      dut.clockDomain.forkStimulus(period = simulationPeriod)

      //
      // Reset when level is low
      //
      dut.clockDomain.assertReset()
      sleep(resetPeriod)
      dut.clockDomain.deassertReset()

      dut.clockDomain.waitSampling(33)
    }
  }
}

object ModNCounterSim {
  def main(args: Array[String]) {
    val systemClock = 50 MHz
    val resetPeriod = 36

    val simulationPeriod = ((1 GHz) / systemClock).toInt

    val limit = 10
    SimConfig.withWave.doSim(ModNCounter(limit)) { dut =>
      dut.clockDomain.forkStimulus(period = simulationPeriod)

      //
      // Reset when level is low
      //
      dut.clockDomain.assertReset()
      sleep(resetPeriod)
      dut.clockDomain.deassertReset()

      dut.clockDomain.waitSampling(33)
    }
  }
}

object GrayCounterSim {
  def main(args: Array[String]) {
    val systemClock = 50 MHz
    val resetPeriod = 36

    val simulationPeriod = ((1 GHz) / systemClock).toInt

    def BinaryToGray(num: Int): Int = {
      num ^ (num >> 1)
    }

    val width = 4
    SimConfig.withWave.doSim(GrayCounter(width)) { dut =>
      dut.clockDomain.forkStimulus(period = simulationPeriod)

      //
      // Reset when level is low
      //
      dut.clockDomain.assertReset()
      sleep(resetPeriod)
      dut.clockDomain.deassertReset()

      val limit = 33
      for (i <- 0 until limit) {
        dut.clockDomain.waitSampling(1)

        assert(dut.io.count.toInt == BinaryToGray(i % 16))
      }
    }
  }
}