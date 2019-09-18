package FPVE.c02ex01

import spinal.core._
import spinal.core.sim._

object GreaterThan2bSim {
  def main(args: Array[String]) {
    SimConfig.withWave.doSim(new GreaterThan2b){dut =>
      val limit = 4 - 1

      for (a <- 0 to limit) {
        for (b <- 0 to limit) {
          dut.io.a #= a
          dut.io.b #= b

          sleep(1)

          assert(dut.io.gt.toBoolean == (a > b))
        }
      }

      sleep(1)
    }
  }
}

object Equal2bSim {
  def main(args: Array[String]) {
    SimConfig.withWave.doSim(new Equal2b){dut =>
      val limit = 4 - 1

      for (a <- 0 to limit) {
        for (b <- 0 to limit) {
          dut.io.a #= a
          dut.io.b #= b

          sleep(1)

          assert(dut.io.eq.toBoolean == (a == b))
        }
      }

      sleep(1)
    }
  }
}

object GreaterThan4bSim {
  def main(args: Array[String]) {
    SimConfig.withWave.doSim(new GreaterThan4b){dut =>
      val limit = 16 - 1

      for (a <- 0 to limit) {
        for (b <- 0 to limit) {
          dut.io.a #= a
          dut.io.b #= b

          sleep(1)

          assert(dut.io.gt.toBoolean == (a > b))
        }
      }

      sleep(1)
    }
  }
}