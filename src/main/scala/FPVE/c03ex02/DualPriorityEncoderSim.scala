package FPVE.c03ex02

import spinal.core._
import spinal.core.sim._

object Utils {
  def ffs(x : Int) : Int = 32 - Integer.numberOfLeadingZeros(x)

  def mask(x : Int) : Int = if (x > 0) (1 << (x - 1)) - 1 else 0
}

object PriorityEncoder12bSim {
  def main(args: Array[String]) {
    SimConfig.withWave.doSim(new PriorityEncoder12b){dut =>
      val limit = 1<<12 - 1

      for (i <- 0 to limit) {
        dut.io.request #= i

        sleep(1)

        assert(dut.io.encode.toInt == Utils.ffs(i))
        assert(dut.io.mask.toInt == Utils.mask(Utils.ffs(i)))
      }

      sleep(1)
    }
  }
}

object PriorityEncoder12bMuxSim {
  def main(args: Array[String]) {
    SimConfig.withWave.doSim(PriorityEncoderMux(width = 12)){dut =>
      val limit = 1<<12 - 1

      for (i <- 0 to limit) {
        dut.io.request #= i

        sleep(1)

        assert(dut.io.encode.toInt == Utils.ffs(i))
        assert(dut.io.mask.toInt == Utils.mask(Utils.ffs(i)))
      }

      sleep(1)
    }
  }
}

object DualPriorityEncoderSim {
  def main(args: Array[String]) {
    SimConfig.withWave.doSim(new DualPriorityEncoder12b){dut =>
      val limit = 1<<12 - 1

      for (i <- 0 to limit) {
        dut.io.request #= i

        sleep(1)

        assert(dut.io.first.toInt == Utils.ffs(i))
        assert(dut.io.second.toInt == Utils.ffs(i & Utils.mask(Utils.ffs(i))))
      }

      sleep(1)
    }
  }
}

object DualPriorityEncoderMuxSim {
  def main(args: Array[String]) {
    SimConfig.withWave.doSim(DualPriorityEncoderMux(width = 12)){dut =>
      val limit = 1<<12 - 1

      for (i <- 0 to limit) {
        dut.io.request #= i

        sleep(1)

        assert(dut.io.first.toInt == Utils.ffs(i))
        assert(dut.io.second.toInt == Utils.ffs(i & Utils.mask(Utils.ffs(i))))
      }

      sleep(1)
    }
  }
}