package FPVE.c03ex05

import spinal.core.sim._

import scala.util.Random

object Int2FPSim {
  def main(args: Array[String]) {
    SimConfig.withWave.doSim(new Int2FP) { dut =>
      var index = -128
      val limit = 128
      while (index < limit) {
        dut.io.value #= index

        sleep(1)

        val number = (dut.io.frac.toInt * (1 << dut.io.exp.toInt)) >> 8
        val result = if (dut.io.sign.toBoolean) -1 * number else number
        val errorMessage = "when value = %d, sign = %b frac = 0x%x exp = %d".format(
          index, dut.io.sign.toBoolean, dut.io.frac.toInt, dut.io.exp.toInt)
        assert(index == result, errorMessage)

        index += 1
      }

      sleep(1)
    }
  }
}

object FP2IntSim {
  def main(args: Array[String]) {
    SimConfig.withWave.doSim(new FP2Int) { dut =>
      //
      // Random test
      //
      // maximum case number is 2 * 2 ^ 7 * 2 ^ 4 = 2 ^ 12
      //
      val randomLimit = 1 << (12 + 1)
      var randomIndex = 0
      while (randomIndex < randomLimit) {
        val sign = Random.nextBoolean()
        val r1 = Random.nextInt(128)
        val frac = if (r1 != 0) 0x80 + r1 else 0
        val exp = if (r1 != 0) Random.nextInt(15) else 0

        dut.io.sign #= sign
        dut.io.frac #= frac
        dut.io.exp #= exp

        sleep(1)

        val number = frac * (1 << exp)
        val result = if (sign) -1 * number else number
        val value = result.toFloat / (1 << 8)

        val expectedOverflow = (value > 127) || (value < -128)
        val expectedUnderflow = (result != 0) && (value.abs < 1)
        val expectedValue = value.toByte

        val errorMessage = "when sign = %b, frac = %x, exp = %d, output value = %x, overflow = %b, underflow = %b".format(
          sign, frac, exp, dut.io.value.toInt, dut.io.overflow.toBoolean, dut.io.underflow.toBoolean)
        assert(dut.io.overflow.toBoolean == expectedOverflow &&
          dut.io.underflow.toBoolean == expectedUnderflow &&
          dut.io.value.toInt == expectedValue, errorMessage)

        randomIndex += 1
      }
    }
  }
}

object Int2IntTesterSim {
  def main(args: Array[String]) {
    SimConfig.withWave.doSim(new Int2IntTester) { dut =>
      var index = -128
      val limit = 128
      while (index < limit) {
        dut.io.inValue #= index

        sleep(1)

        val errorMessage = "when value = %d".format(index)
        assert(index == dut.io.outValue.toInt, errorMessage)

        index += 1
      }

      sleep(1)
    }
  }
}