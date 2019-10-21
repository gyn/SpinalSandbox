package FPVE.c06li04

import spinal.core._
import spinal.core.sim._

object FibonacciSim {
  def main(args: Array[String]) {
    val systemClock = 50 MHz
    val resetPeriod = 36

    val simulationPeriod = ((1 GHz) / systemClock).toInt

    val width = 5
    val widthResult = 21
    val limitN = 1 << width
    val limitResult = 1 << widthResult
    SimConfig.withWave.doSim(Fibonacci(limitN, limitResult)) { dut =>
      dut.clockDomain.forkStimulus(period = simulationPeriod)

      //
      // Reset when level is low
      //
      dut.clockDomain.assertReset()
      sleep(resetPeriod)
      dut.clockDomain.deassertReset()

      for (i <- 0 until 1 << width) {
        dut.io.n #= i
        dut.clockDomain.waitSampling(1)
        dut.io.start #= true

        dut.clockDomain.waitSampling(i match { case 0 | 1 => 3; case _ => i + 2 })
        assert(dut.io.done.toBoolean)

        def fibonacci(n: Int): Int = {
          @scala.annotation.tailrec
          def fibonacciTail(n: Int, a: Int, b: Int): Int = n match {
            case 0 => a
            case _ => fibonacciTail(n - 1, b, a + b)
          }

          fibonacciTail(n, 0, 1)
        }

        val errorMessage = f"n = $i%d, expected ${fibonacci(i)}%d, output ${dut.io.result.toInt}"
        assert(dut.io.result.toInt == fibonacci(i), errorMessage)

        dut.io.start #= false
        dut.clockDomain.waitSampling(1)
      }
    }
  }
}