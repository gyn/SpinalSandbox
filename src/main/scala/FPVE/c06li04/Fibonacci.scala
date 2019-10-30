package FPVE.c06li04

import spinal.core._
import spinal.lib.fsm._

case class Fibonacci(limitN: Int, resultLimit: Int) extends Component {
  val widthN = log2Up(limitN)
  val widthResult = log2Up(resultLimit)

  val io = new Bundle {
    val start  = in  Bool
    val n      = in  UInt(widthN bits)
    val ready  = out Bool
    val done   = out Bool
    val result = out UInt(widthResult bits)
  }

  val fibonacciFsm = new StateMachine {
    val stateIdle = new State with EntryPoint
    val stateOp   = new State
    val stateDone = new State

    val nRegNext  = UInt(widthN bits)
    val nReg      = RegNext(nRegNext) init(0)
    val t0RegNext = UInt(widthResult bits)
    val t0Reg     = RegNext(t0RegNext) init(0)
    val t1RegNext = UInt(widthResult bits)
    val t1Reg     = RegNext(t1RegNext) init(0)

    nRegNext  := nReg
    t0RegNext := t0Reg
    t1RegNext := t1Reg

    stateIdle
      .whenIsActive {
        when(io.start) {
          t0RegNext := 0
          t1RegNext := 1
          nRegNext  := io.n

          goto(stateOp)
        }
      }

    stateOp
      .whenIsActive {
        when(nReg === 0) {
          t1RegNext := 0

          goto(stateDone)
        } elsewhen (nReg === 1) {
          goto(stateDone)
        } otherwise {
          t1RegNext := t1Reg + t0Reg
          t0RegNext := t1Reg
          nRegNext  := nReg - 1

          goto(stateOp)
        }
      }

    stateDone.whenIsActive {
      goto(stateIdle)
    }
  }

  io.ready  := fibonacciFsm.isActive(fibonacciFsm.stateIdle)
  io.done   := fibonacciFsm.isActive(fibonacciFsm.stateDone)
  io.result := fibonacciFsm.t1Reg
}