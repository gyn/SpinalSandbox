package FPVE.c04ex06

import spinal.core._
import spinal.core.sim._

import scala.util.Random

object StopWatchSim {
  def main(args: Array[String]) {
    val systemClock = 50 MHz
    val simulationInterval = 100 ns
    val resetPeriod = 10

    val simulationCycles = (systemClock * simulationInterval).toInt
    val simulationPeriod = ((1 GHz) / systemClock).toInt

    SimConfig.withWave.doSim(StopWatch(interval = simulationCycles)) { dut =>
      dut.clockDomain.forkStimulus(period = simulationPeriod)

      //
      // Reset with up equals true, clear equals false and go equals true
      //
      dut.clockDomain.assertReset()
      dut.io.up #= true
      dut.io.clear #= false
      dut.io.go #= true
      sleep(resetPeriod)
      dut.clockDomain.deassertReset()

      val clearInterval = 1000 + Random.nextInt(20000)

      dut.clockDomain.waitSampling(clearInterval)
      dut.io.clear #= true
      dut.clockDomain.waitSampling(1000)
      // and then continue
      dut.io.clear #= false

      val stopInterval = 1000 + Random.nextInt(20000)

      dut.clockDomain.waitSampling(stopInterval)
      // stop for a while
      dut.io.go #= false
      dut.clockDomain.waitSampling(1000)
      // and then continue
      dut.io.go #= true
      dut.clockDomain.waitSampling(35000 - stopInterval)

      //
      // counting down
      //
      dut.io.up #= false
      dut.clockDomain.waitSampling(35000)
    }
  }
}