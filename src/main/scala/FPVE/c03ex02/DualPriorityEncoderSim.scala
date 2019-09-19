package FPVE.c03ex02

import spinal.core._
import spinal.core.sim._

object PriorityEncoder12bSim {
  def ffs(x : Int) : Int = 32 - Integer.numberOfLeadingZeros(x)

  def mask(x : Int) : Int = if (x > 0) (1 << (x - 1)) - 1 else 0

  def main(args: Array[String]) {
    SimConfig.withWave.doSim(new PriorityEncoder12b){dut =>
      val limit = 1<<12 - 1

      for (i <- 0 to limit) {
        dut.io.request #= i

        sleep(1)

        assert(dut.io.encode.toInt == ffs(i))
        assert(dut.io.mask.toInt == mask(ffs(i)))
      }

      sleep(1)
    }
  }
}

object PriorityEncoder12bMuxSim {
  def ffs(x : Int) : Int = 32 - Integer.numberOfLeadingZeros(x)

  def mask(x : Int) : Int = if (x > 0) (1 << (x - 1)) - 1 else 0

  def main(args: Array[String]) {
    SimConfig.withWave.doSim(PriorityEncoderMux(width = 12)){dut =>
      val limit = 1<<12 - 1

      for (i <- 0 to limit) {
        dut.io.request #= i

        sleep(1)

        assert(dut.io.encode.toInt == ffs(i))
        assert(dut.io.mask.toInt == mask(ffs(i)))
      }

      sleep(1)
    }
  }
}

object DualPriorityEncoderSim {
  def ffs(x : Int) : Int = 32 - Integer.numberOfLeadingZeros(x)

  def mask(x : Int) : Int = if (x > 0) (1 << (x - 1)) - 1 else 0

  def main(args: Array[String]) {
    SimConfig.withWave.doSim(new DualPriorityEncoder12b){dut =>
      val limit = 1<<12 - 1

      for (i <- 0 to limit) {
        dut.io.request #= i

        sleep(1)

        assert(dut.io.first.toInt == ffs(i))
        assert(dut.io.second.toInt == ffs(i & mask(ffs(i))))
      }

      sleep(1)
    }
  }
}

object DualPriorityEncoderMuxSim {
  def ffs(x : Int) : Int = 32 - Integer.numberOfLeadingZeros(x)

  def mask(x : Int) : Int = if (x > 0) (1 << (x - 1)) - 1 else 0

  def main(args: Array[String]) {
    SimConfig.withWave.doSim(DualPriorityEncoderMux(width = 12)){dut =>
      val limit = 1<<12 - 1

      for (i <- 0 to limit) {
        dut.io.request #= i

        sleep(1)

        assert(dut.io.first.toInt == ffs(i))
        assert(dut.io.second.toInt == ffs(i & mask(ffs(i))))
      }

      sleep(1)
    }
  }
}