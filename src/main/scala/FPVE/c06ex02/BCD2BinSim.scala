package FPVE.c06ex02

import spinal.core._
import spinal.core.sim._

object BCD2BinSim {
  def main(args: Array[String]) {
    val systemClock = 50 MHz
    val resetPeriod = 36

    val simulationPeriod = ((1 GHz) / systemClock).toInt

    val NIBBLE = 4
    val width = 12

    SimConfig.withWave.doSim(BCD2Bin(width)) { dut =>
      dut.clockDomain.forkStimulus(period = simulationPeriod)

      //
      // Reset when level is low
      //
      dut.clockDomain.assertReset()
      sleep(resetPeriod)
      dut.clockDomain.deassertReset()

      for (i <- 0 until 10; j <- 0 until 10; k <- 0 until 10) {
        dut.io.n #= (i << 8) + (j << 4) + k
        dut.clockDomain.waitSampling(1)
        dut.io.start #= true

        dut.clockDomain.waitSampling(width / NIBBLE + 2)

        assert(dut.io.done.toBoolean)

        def BCD3toInt(i: Int, j: Int, k: Int): Int = {
          i * 100 + j * 10 + k
        }

        val errorMessage = f"n = 0x${dut.io.n.toInt}%x, output ${dut.io.result.toInt}"
        assert(dut.io.result.toInt == BCD3toInt(i, j, k), errorMessage)

        dut.io.start #= false
        dut.clockDomain.waitSampling(1)
      }
    }
  }
}