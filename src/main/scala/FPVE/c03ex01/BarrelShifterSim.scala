package FPVE.c03ex01

import spinal.core._
import spinal.core.sim._

object BarrelShifterLeftSim {
  def main(args: Array[String]) {
    val width = 32
    SimConfig.withWave.doSim(BarrelShifterLeft(width = width)) { dut =>
      val limit = width

      val scalar = BigInt("00000001", 16)

      dut.io.a #= scalar

      for (a <- 0 until limit) {
        dut.io.amt #= a

        sleep(1)

        assert(dut.io.y.toBigInt == scalar << a, "Failed when %x != %x << %d".format(dut.io.y.toBigInt, scalar, a))
      }
    }
  }
}

object BarrelShifterRightSim {
  def main(args: Array[String]) {
    val width = 32
    SimConfig.withWave.doSim(BarrelShifterRight(width = width)) { dut =>
      val limit = width

      val scalar = BigInt("80000000", 16)

      dut.io.a #= scalar

      for (a <- 0 until limit) {
        dut.io.amt #= a

        sleep(1)

        assert(dut.io.y.toBigInt == scalar >> a, "Failed when %x != %x >> %d".format(dut.io.y.toBigInt, scalar, a))
      }
    }
  }
}

object BarrelShifterSim {
  def main(args: Array[String]) {
    val width = 32
    SimConfig.withWave.doSim(BarrelShifter(width = width)) { dut =>
      val limit = width

      //
      // For right-shift
      //
      val rscalar = BigInt("80000000", 16)

      dut.io.a #= rscalar
      dut.io.lr #= true

      for (a <- 0 until limit) {
        dut.io.amt #= a

        sleep(1)

        assert(dut.io.y.toBigInt == rscalar >> a, "Failed when %x != %x >> %d".format(dut.io.y.toBigInt, rscalar, a))
      }

      sleep(1)

      //
      // For left-shift
      //
      val lscalar = BigInt("00000001", 16)

      dut.io.a #= lscalar
      dut.io.lr #= false

      for (a <- 0 until limit) {
        dut.io.amt #= a

        sleep(1)

        assert(dut.io.y.toBigInt == lscalar << a, "Failed when %x != %x << %d".format(dut.io.y.toBigInt, lscalar, a))
      }

      sleep(1)
    }
  }
}

object BarrelShifterByReverseSim {
  def main(args: Array[String]) {
    val width = 32
    SimConfig.withWave.doSim(BarrelShifterByReverse(width = width)) { dut =>
      val limit = width

      //
      // For right-shift
      //
      val rscalar = BigInt("80000000", 16)

      dut.io.a #= rscalar
      dut.io.lr #= true

      for (a <- 0 until limit) {
        dut.io.amt #= a

        sleep(1)

        assert(dut.io.y.toBigInt == rscalar >> a, "Failed when %x != %x >> %d".format(dut.io.y.toBigInt, rscalar, a))
      }

      sleep(1)

      //
      // For left-shift
      //
      val lscalar = BigInt("00000001", 16)

      dut.io.a #= lscalar
      dut.io.lr #= false

      for (a <- 0 until limit) {
        dut.io.amt #= a

        sleep(1)

        assert(dut.io.y.toBigInt == lscalar << a, "Failed when %x != %x << %d".format(dut.io.y.toBigInt, lscalar, a))
      }

      sleep(1)
    }
  }
}