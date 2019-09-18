package FPVE.c03ex04

import spinal.core._
import spinal.core.sim._

import scala.util.Random

object FPGreaterThanSim {
  def main(args: Array[String]) {
    SimConfig.withWave.doSim(new FPGreaterThan) { dut =>
      //
      // 0 and 0
      //
      dut.io.exp1 #= 0
      dut.io.frac1 #= 0
      dut.io.exp2 #= 0
      dut.io.frac2 #= 0

      val signLimit = 16
      for (i <- 0 to signLimit) {
        dut.io.sign1 #= Random.nextBoolean()
        dut.io.sign2 #= Random.nextBoolean()

        sleep(1)

        assert(dut.io.gt.toBoolean == false)
      }

      sleep(1)

      //
      // Random
      //
      val limit = 1000000
      for (i <- 0 to limit) {
        val sign1 = Random.nextBoolean()
        val r1    = Random.nextInt(128)
        val frac1 = if (r1 != 0) 0x80 + r1 else 0
        val exp1  = if (r1 != 0) Random.nextInt(15) else 0
        val sign2 = Random.nextBoolean()
        val r2    = Random.nextInt(128)
        val frac2 = if (r2 != 0) 0x80 + r2 else 0
        val exp2  = if (r2 != 0) Random.nextInt(15) else 0

        dut.io.sign1 #= sign1
        dut.io.exp1 #= exp1
        dut.io.frac1 #= frac1
        dut.io.sign2 #= sign2
        dut.io.exp2 #= exp2
        dut.io.frac2 #= frac2

        sleep(1)

        val fp1 = if (sign1) -1 * frac1 * (1<<exp1) else frac1 * (1<<exp1)
        val fp2 = if (sign2) -1 * frac2 * (1<<exp2) else frac2 * (1<<exp2)

        assert(dut.io.gt.toBoolean == (fp1 > fp2))
      }

      sleep(1)
    }
  }
}