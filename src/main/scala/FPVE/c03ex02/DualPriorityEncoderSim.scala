package FPVE.c03ex02

import spinal.core._
import spinal.core.sim._

object Utils {
  def ffs(x: Int): Int = 32 - Integer.numberOfLeadingZeros(x)

  def mask(x: Int): Int = if (x > 0) (1 << (x - 1)) - 1 else 0
}

object PriorityEncoder12bSim {
  def main(args: Array[String]) {
    SimConfig.withWave.doSim(new PriorityEncoder12b) { dut =>
      val limit = 1 << 12 - 1

      for (i <- 0 to limit) {
        dut.io.request #= i

        sleep(1)

        val expectedEncode = Utils.ffs(i)
        val expectedMask = Utils.mask(expectedEncode)
        val errorMessage = "Failed when request = %x, expected %d and %x, output %d and %x".format(
          i, expectedEncode, expectedMask, dut.io.encode.toInt, dut.io.mask.toInt)
        assert(dut.io.encode.toInt == expectedEncode && dut.io.mask.toInt == expectedMask, errorMessage)
      }

      sleep(1)
    }
  }
}

object PriorityEncoder12bMuxSim {
  def main(args: Array[String]) {
    SimConfig.withWave.doSim(PriorityEncoderMux(width = 12)) { dut =>
      val limit = 1 << 12 - 1

      for (i <- 0 to limit) {
        dut.io.request #= i

        sleep(1)

        val expectedEncode = Utils.ffs(i)
        val expectedMask = Utils.mask(expectedEncode)
        val errorMessage = "Failed when request = %x, expected %d and %x, output %d and %x".format(
          i, expectedEncode, expectedMask, dut.io.encode.toInt, dut.io.mask.toInt)
        assert(dut.io.encode.toInt == expectedEncode && dut.io.mask.toInt == expectedMask)
      }

      sleep(1)
    }
  }
}

object DualPriorityEncoderSim {
  def main(args: Array[String]) {
    SimConfig.withWave.doSim(new DualPriorityEncoder12b) { dut =>
      val limit = 1 << 12 - 1

      for (i <- 0 to limit) {
        dut.io.request #= i

        sleep(1)

        val expectedFirst = Utils.ffs(i)
        val expectedSecond = Utils.ffs(i & Utils.mask(expectedFirst))
        val errorMessage = "Failed when request = %x, expected %d and %d, output %d and %d".format(
          i, expectedFirst, expectedSecond, dut.io.first.toInt, dut.io.second.toInt)
        assert(dut.io.first.toInt == expectedFirst && dut.io.second.toInt == expectedSecond)
      }

      sleep(1)
    }
  }
}

object DualPriorityEncoderMuxSim {
  def main(args: Array[String]) {
    SimConfig.withWave.doSim(DualPriorityEncoderMux(width = 12)) { dut =>
      val limit = 1 << 12 - 1

      for (i <- 0 to limit) {
        dut.io.request #= i

        sleep(1)

        val expectedFirst = Utils.ffs(i)
        val expectedSecond = Utils.ffs(i & Utils.mask(expectedFirst))
        val errorMessage = "Failed when request = %x, expected %d and %d, output %d and %d".format(
          i, expectedFirst, expectedSecond, dut.io.first.toInt, dut.io.second.toInt)
        assert(dut.io.first.toInt == expectedFirst && dut.io.second.toInt == expectedSecond)
      }

      sleep(1)
    }
  }
}