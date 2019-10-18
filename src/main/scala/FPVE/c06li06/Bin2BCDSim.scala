package FPVE.c06li06

import spinal.core._
import spinal.core.sim._

import scala.collection.mutable.ListBuffer

object Bin2BCDSim {
  def main(args: Array[String]) {
    val systemClock = 50 MHz
    val resetPeriod = 36

    val simulationPeriod = ((1 GHz) / systemClock).toInt

    val width = 13

    SimConfig.withWave.doSim(Bin2BCD(width)) { dut =>
      dut.clockDomain.forkStimulus(period = simulationPeriod)

      //
      // Reset when level is low
      //
      dut.clockDomain.assertReset()
      sleep(resetPeriod)
      dut.clockDomain.deassertReset()

      def Int2BCD(number: Int) : Int = {
        val numberList = new ListBuffer[Int]()
        var n = number
        while (n != 0) {
          numberList.prepend(n % 10)
          n = n/10
        }
        numberList.fold(0)((acc, s) => acc * 16 + s)
      }

      for (i <- 0 until 1 << width) {
        dut.io.n #= i
        dut.clockDomain.waitSampling(1)
        dut.io.start #= true

        dut.clockDomain.waitSampling(width + 2)

        assert(dut.io.done.toBoolean)
        val errorMessage = f"n = ${i}, output 0x${dut.io.result.toInt}%x"
        assert(Int2BCD(i) == dut.io.result.toInt, errorMessage)

        dut.io.start #= false
        dut.clockDomain.waitSampling(1)
      }
    }
  }
}