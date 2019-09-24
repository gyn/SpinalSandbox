package FPVE.c04ex02

import spinal.core._
import spinal.core.sim._

object PWMLEDDimmerSim {
  def main(args: Array[String]) {
    SimConfig.withWave.doSim(PWMLEDDimmer(width = 4)) { dut =>
      dut.clockDomain.forkStimulus(period = 20)

      val limit = 16
      val times = 4
      val resetPeriod = 36
      for (i <- 0 until limit) {
        dut.clockDomain.assertReset()
        dut.io.w #= i
        sleep(resetPeriod)
        dut.clockDomain.deassertReset()

        val highPeriod = i
        val totalPeriod = 16

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
        } else {
          expectedHighs = highPeriod * times
          expectedToggles = 2 * times
        }

        val errorMessage = s"when w = $i"
        assert(highs == expectedHighs && toggles == expectedToggles, errorMessage)
      }
    }
  }
}