package FPVE.c03ex03

import spinal.core._
import spinal.core.sim._

object BCDCellSim {
  def main(args: Array[String]) {
    SimConfig.withWave.doSim(new BCDCell){dut =>
      val limit = 1<<4 - 1

      dut.io.ci #= false

      for (i <- 0 to limit) {
        dut.io.hex #= i

        sleep(1)

        val sum = i

        if (i < 10) {
          assert(dut.io.bcd.toInt == sum)
          assert(!dut.io.co.toBoolean)
        } else {
          assert(dut.io.bcd.toInt == (sum - 10))
          assert(dut.io.co.toBoolean)
        }
      }

      sleep(1)

      dut.io.ci #= true

      for (i <- 0 to limit) {
        dut.io.hex #= i

        sleep(1)

        val sum = i + 1

        if (i < 10) {
          assert(dut.io.bcd.toInt == sum)
          assert(!dut.io.co.toBoolean)
        } else {
          assert(dut.io.bcd.toInt == (sum - 10))
          assert(dut.io.co.toBoolean)
        }
      }

      sleep(1)
    }
  }
}

object BCDIncrementorSim {
  def main(args: Array[String]) {
    SimConfig.withWave.doSim(BCDIncrementor(Nr = 3)){dut =>
      val limit = 10

      for (i <- 0 to limit) {
        for (j <- 0 to limit) {
          for (k <- 0 to limit) {
            dut.io.hex(0) #= k
            dut.io.hex(1) #= j
            dut.io.hex(2) #= i

            sleep(1)

            val sum = i * 100 + j * 10 + k + 1
            val n4 = sum / 1000
            val n3 = (sum - n4 * 1000) /100
            val n2 = (sum - n4 * 1000 - n3 * 100) / 10
            val n1 = sum % 10

            assert(dut.io.co.toBoolean == (n4 != 0) &&
              dut.io.bcd(2).toInt == n3 &&
              dut.io.bcd(1).toInt == n2 &&
              dut.io.bcd(0).toInt == n1)
          }
        }
      }

      sleep(1)
    }
  }
}