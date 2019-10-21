package FPVE.c06li04

import spinal.core._
import spinal.lib.fsm._

case class Fibonacci(limitN: Int, resultLimit: Int) extends Component {
  val widthN = log2Up(limitN)
  val widthResult = log2Up(resultLimit)

  val io = new Bundle {
    val start   = in  Bool
    val n       = in  UInt(widthN bits)
    val ready   = out Bool
    val done    = out Bool
    val result  = out UInt(widthResult bits)
  }

  val fibonacciFsm = new StateMachine {
    val stateIdle = new State with EntryPoint
    val stateOp = new State
    val stateDone = new State

    val nReg = Reg(UInt(widthN bits)) init(0)
    val t0Reg = Reg(UInt(widthResult bits)) init(0)
    val t1Reg = Reg(UInt(widthResult bits)) init(0)

    stateIdle
      .whenIsActive {
        when (io.start) {
          t0Reg := 0
          t1Reg := 1
          nReg := io.n

          goto(stateOp)
        }
      }

    stateOp
      .whenIsActive {
        when (nReg === 0) {
          t1Reg := 0

          goto(stateDone)
        } elsewhen (nReg === 1) {
          goto(stateDone)
        } otherwise {
          t1Reg := t1Reg + t0Reg
          t0Reg := t1Reg
          nReg := nReg - 1

          goto(stateOp)
        }
      }

    stateDone.whenIsActive {
      goto(stateIdle)
    }
  }

  io.ready := fibonacciFsm.isActive(fibonacciFsm.stateIdle)
  io.done := fibonacciFsm.isActive(fibonacciFsm.stateDone)
  io.result := fibonacciFsm.t1Reg
}