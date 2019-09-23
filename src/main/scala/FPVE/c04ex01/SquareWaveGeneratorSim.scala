package FPVE.c04ex01

import spinal.core._
import spinal.core.sim._

object SquareWaveGeneratorSim {
  def main(args: Array[String]) {
    SimConfig.withWave.doSim(SquareWaveGenerator(configBits = 4)) { dut =>
      dut.clockDomain.forkStimulus(period = 20)

      val limit = 16
      val times = 4
      val resetPeroid = 36
      for (i <- 0 until limit; j <- 0 until limit) {
        dut.clockDomain.assertReset()
        dut.io.m #= i
        dut.io.n #= j
        sleep(resetPeroid)
        dut.clockDomain.deassertReset()

        val highPeroid = i * 5
        val totalPeroid = if (i == 0 && j == 0) 5 else (i + j) * 5

        var highs = 0
        var toggles = 0
        var last = false
        var index = 0
        while (index <= totalPeroid * times) {
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
          expectedHighs = totalPeroid * times
          expectedToggles = 1
        } else {
          expectedHighs = highPeroid * times
          expectedToggles = 2 * times
        }

        val errorMessage = "Failed when m = %d, n = %d".format(i, j)
        assert(highs == expectedHighs && toggles == expectedToggles, errorMessage)
      }

      sleep(1)
    }
  }
}