package FPVE.c02ex02

import spinal.core._
import spinal.core.sim._

object BinaryDecoder2Sim {
  def main(args: Array[String]) {
    SimConfig.withWave.doSim(new BinaryDecoder2b) { dut =>
      val limit = 4 - 1

      //
      dut.io.en #= false

      for (a <- 0 to limit) {
        dut.io.sel #= a

        sleep(1)

        assert(dut.io.output.toInt == 0, "Failed when a = %d and en is false".format(a))
      }

      sleep(1)

      //
      dut.io.en #= true

      for (a <- 0 to limit) {
        dut.io.sel #= a

        sleep(1)

        assert(dut.io.output.toInt == (1 << a), "Failed when a = %d and ouput is %x".format(a, dut.io.output.toInt))
      }

      sleep(1)
    }
  }
}

object BinaryDecoder3Sim {
  def main(args: Array[String]) {
    SimConfig.withWave.doSim(new BinaryDecoder3b) { dut =>
      val limit = 8 - 1

      //
      dut.io.en #= false

      for (a <- 0 to limit) {
        dut.io.sel #= a

        sleep(1)

        assert(dut.io.output.toInt == 0, "Failed when a = %d and en is false".format(a))
      }

      sleep(1)

      //
      dut.io.en #= true

      for (a <- 0 to limit) {
        dut.io.sel #= a

        sleep(1)

        assert(dut.io.output.toInt == (1 << a), "Failed when a = %d and ouput is %x".format(a, dut.io.output.toInt))
      }

      sleep(1)
    }
  }
}

object BinaryDecoder4Sim {
  def main(args: Array[String]) {
    SimConfig.withWave.doSim(new BinaryDecoder4b) { dut =>
      val limit = 16 - 1

      //
      dut.io.en #= false

      for (a <- 0 to limit) {
        dut.io.sel #= a

        sleep(1)

        assert(dut.io.output.toInt == 0, "Failed when a = %d and en is false".format(a))
      }

      sleep(1)

      //
      dut.io.en #= true

      for (a <- 0 to limit) {
        dut.io.sel #= a

        sleep(1)

        assert(dut.io.output.toInt == (1 << a), "Failed when a = %d and ouput is %x".format(a, dut.io.output.toInt))
      }

      sleep(1)
    }
  }
}