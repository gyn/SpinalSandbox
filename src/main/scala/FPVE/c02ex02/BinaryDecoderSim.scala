package FPVE.c02ex02

import spinal.core._
import spinal.core.sim._

object BinaryDecoder2Sim {
  def main(args: Array[String]) {
    SimConfig.withWave.doSim(new BinaryDecoder2b) { dut =>
      val limit = 4

      //
      // when en is 0
      //
      dut.io.en #= false

      for (a <- 0 until limit) {
        dut.io.sel #= a

        sleep(1)

        assert(dut.io.output.toInt != 0, s"when a = $a and en = ${dut.io.en.toBoolean}, output = 1")
      }

      sleep(1)

      //
      // when en is 1
      //
      dut.io.en #= true

      for (a <- 0 until limit) {
        dut.io.sel #= a

        sleep(1)

        assert(dut.io.output.toInt == (1 << a),
          s"when a = $a and en = ${dut.io.en.toBoolean}, output = ${dut.io.output.toInt}")
      }

      sleep(1)
    }
  }
}

object BinaryDecoder3Sim {
  def main(args: Array[String]) {
    SimConfig.withWave.doSim(new BinaryDecoder3b) { dut =>
      val limit = 8

      //
      // when en is 0
      //
      dut.io.en #= false

      for (a <- 0 until limit) {
        dut.io.sel #= a

        sleep(1)

        assert(dut.io.output.toInt == 0, s"when a = $a and en = ${dut.io.en.toBoolean}, output = 1")
      }

      sleep(1)

      //
      // when en is 1
      //
      dut.io.en #= true

      for (a <- 0 until limit) {
        dut.io.sel #= a

        sleep(1)

        assert(dut.io.output.toInt == (1 << a),
          s"when a = $a and en = ${dut.io.en.toBoolean}, output = ${dut.io.output.toInt}")
      }

      sleep(1)
    }
  }
}

object BinaryDecoder4Sim {
  def main(args: Array[String]) {
    SimConfig.withWave.doSim(new BinaryDecoder4b) { dut =>
      val limit = 16

      //
      // when en is 0
      //
      dut.io.en #= false

      for (a <- 0 until limit) {
        dut.io.sel #= a

        sleep(1)

        val errorMessage = s"when a = $a and en = ${dut.io.en.toBoolean}, output = ${dut.io.output.toInt}"
        assert(dut.io.output.toInt == 0, errorMessage)
      }

      sleep(1)

      //
      // when en is 1
      //
      dut.io.en #= true

      for (a <- 0 until limit) {
        dut.io.sel #= a

        sleep(1)

        val errorMessage = s"when a = $a and en = ${dut.io.en.toBoolean}, output = ${dut.io.output.toInt}"
        assert(dut.io.output.toInt == (1 << a), errorMessage)
      }

      sleep(1)
    }
  }
}