package FPVE.c06ex03

import spinal.core._
import spinal.lib.fsm._
import FPVE.c06ex02.BCD2Bin
import FPVE.c06li04.Fibonacci
import FPVE.c06li06.Bin2BCD

class FibonacciBCD extends Component {
  val inputLimit = 99
  val fibonacciOutputLimit = 6765

  val NIBBLE = 4
  val bcdWidth = 2 * NIBBLE
  val ssegWidth = 4 * NIBBLE
  val resultWidth = log2Up(fibonacciOutputLimit)

  val io = new Bundle {
    val start   = in  Bool
    val n       = in  UInt(bcdWidth bits)
    val result  = out UInt(ssegWidth bits)
  }

  val bcd2Bin = BCD2Bin(bcdWidth)
  val fibonacci = Fibonacci(inputLimit, fibonacciOutputLimit)
  val bin2BCD = Bin2BCD(resultWidth)

  bcd2Bin.io.n := io.n
  bcd2Bin.io.start := False

  fibonacci.io.n := bcd2Bin.io.result
  fibonacci.io.start := False

  bin2BCD.io.n := fibonacci.io.result
  bin2BCD.io.start := False

  val fibonacciBCDFsm = new StateMachine {
    val stateIdle = new State with EntryPoint
    val stateBin2BCD = new State
    val stateFibonacci = new State
    val stateBCD2Bin = new State
    val stateDone = new State

    val ssegReg = Reg(UInt(ssegWidth bits)) init(0)

    stateIdle
      .whenIsActive {
        when (io.start) {
          goto(stateBCD2Bin)
        }
      }

    stateBCD2Bin
      .whenIsActive {
        bcd2Bin.io.start := True

        when(bcd2Bin.io.done) {
          goto(stateFibonacci)
        }
      }

    stateFibonacci
      .whenIsActive {
        val fibonacciInputLimit = 20
        val ssegOverflowLimit = 0x9999
        when (bcd2Bin.io.result > fibonacciInputLimit) {
          ssegReg := ssegOverflowLimit

          goto(stateIdle)
        } otherwise {
          fibonacci.io.start := True

          when(fibonacci.io.done) {
            goto(stateBin2BCD)
          }
        }
      }

    stateBin2BCD
      .whenIsActive {
        bin2BCD.io.start := True

        when(bin2BCD.io.done) {
          goto(stateDone)
        }
      }

    stateDone
      .whenIsActive {
        ssegReg := bin2BCD.io.result.resized

        goto(stateIdle)
      }
  }

  io.result := fibonacciBCDFsm.ssegReg
}