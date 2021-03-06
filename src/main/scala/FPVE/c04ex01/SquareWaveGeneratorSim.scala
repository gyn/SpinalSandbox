package FPVE.c04ex01

import spinal.core._
import spinal.core.sim._

object SquareWaveGeneratorSim {
  def main(args: Array[String]) {
    val systemClock = 50 MHz
    val resetPeriod = 36

    val simulationPeriod = ((1 GHz) / systemClock).toInt

    SimConfig.withWave.doSim(SquareWaveGenerator(configBits = 4)) { dut =>
      dut.clockDomain.forkStimulus(period = simulationPeriod)

      val limit = 16
      val times = 4
      for (i <- 0 until limit; j <- 0 until limit) {
        dut.clockDomain.assertReset()
        dut.io.m #= i
        dut.io.n #= j
        sleep(resetPeriod)
        dut.clockDomain.deassertReset()

        val highPeriod = i * 5
        val totalPeriod = if (i == 0 && j == 0) 5 else (i + j) * 5

        var highs = 0
        var toggles = 0
        var last = false
        var index = 0
        while (index <= totalPeriod * times) {
          dut.clockDomain.waitSampling()

          if (dut.io.signal.toBoolean) {
            highs += 1
          }

          if (dut.io.signal.toBoolean != last) {
            toggles += 1
            last = dut.io.signal.toBoolean
          }

          index += 1
        }

        var expectedHighs = 0
        var expectedToggles = 0
        if (i == 0) {
          expectedHighs = 0
          expectedToggles = 0
        } else if (j == 0) {
          expectedHighs = totalPeriod * times
          expectedToggles = 1
        } else {
          expectedHighs = highPeriod * times
          expectedToggles = 2 * times
        }

        val errorMessage = s"when m = $i, n = $j"
        assert(highs == expectedHighs && toggles == expectedToggles, errorMessage)
      }

      sleep(1)
    }
  }
}