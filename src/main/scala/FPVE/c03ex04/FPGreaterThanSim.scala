package FPVE.c03ex04

import spinal.core._
import spinal.core.sim._

import scala.util.Random

object FPGreaterThanSim {
  def main(args: Array[String]) {
    SimConfig.withWave.doSim(new FPGreaterThan) { dut =>
      //
      // check 0 and 0 with same or different sign
      //
      dut.io.exp1 #= 0
      dut.io.frac1 #= 0
      dut.io.exp2 #= 0
      dut.io.frac2 #= 0

      val signLimit = 16
      var signIndex = 0
      while (signIndex < signLimit) {
        dut.io.sign1 #= Random.nextBoolean()
        dut.io.sign2 #= Random.nextBoolean()

        sleep(1)

        val errorMessage = "when 0s with sign1 = %b, sign2 = %b, output = %b".format(
          dut.io.sign1.toBoolean, dut.io.sign2.toBoolean, dut.io.gt.toBoolean)
        assert(!dut.io.gt.toBoolean, errorMessage)

        signIndex += 1
      }

      sleep(1)

      //
      // Random test
      //
      // maximum case number is 2 * 2 ^ 7 * 2 ^ 4 * 2 * 2 ^ 7 * 2 ^ 4 = 2 ^ 24
      //
      val randomLimit = 1 << (24 + 1)
      var randomIndex = 0
      while (randomIndex < randomLimit) {
        val sign1 = Random.nextBoolean()
        val r1 = Random.nextInt(128)
        val frac1 = if (r1 != 0) 0x80 + r1 else 0
        val exp1 = if (r1 != 0) Random.nextInt(15) else 0
        val sign2 = Random.nextBoolean()
        val r2 = Random.nextInt(128)
        val frac2 = if (r2 != 0) 0x80 + r2 else 0
        val exp2 = if (r2 != 0) Random.nextInt(15) else 0

        dut.io.sign1 #= sign1
        dut.io.exp1 #= exp1
        dut.io.frac1 #= frac1
        dut.io.sign2 #= sign2
        dut.io.exp2 #= exp2
        dut.io.frac2 #= frac2

        sleep(1)

        val fp1 = if (sign1) -1 * frac1 * (1 << exp1) else frac1 * (1 << exp1)
        val fp2 = if (sign2) -1 * frac2 * (1 << exp2) else frac2 * (1 << exp2)
        val errorMessage = "when sign = %b, frac = %d, exp = %d, sign = %b, frac = %d, exp = %d, output = %b".format(
          sign1, frac1, exp1, sign2, frac2, exp2, dut.io.gt.toBoolean)
        assert(dut.io.gt.toBoolean == (fp1 > fp2), errorMessage)

        randomIndex += 1
      }

      sleep(1)
    }
  }
}