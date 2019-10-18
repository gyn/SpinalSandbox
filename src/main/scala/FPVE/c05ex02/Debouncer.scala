package FPVE.c05ex02

import spinal.core._
import spinal.lib.fsm._

case class Debouncer(cycles: Int) extends Component {
  val io = new Bundle {
    val level   = in  Bool
    val output  = out Bool
  }

  val limit = cycles
  val width = log2Up(limit)
  val counter = Reg(UInt(width bits)) init (0)
  counter := (counter === U(limit - 1)) ? U(0) | (counter + 1)
  val tick = counter === U(limit - 1)

  val debouncerFsm = new StateMachine {
    val stateInit = new State with EntryPoint
    val stateLevelHigh = new State
    val stateNegedgeDelay = new State
    val stateLevelLow = new State
    val statePosedgeDelay = new State

    stateInit
      .whenIsActive {
        when (io.level) {
          goto(stateLevelHigh)
        } otherwise {
          goto(stateLevelLow)
        }
      }

    stateLevelHigh
      .whenIsActive {
        when (!io.level) {
          goto(stateNegedgeDelay)
        }
      }

    stateNegedgeDelay
      .whenIsActive {
        when (tick) {
          when(io.level) {
            goto(stateLevelHigh)
          } otherwise {
            goto(stateLevelLow)
          }
        }
      }

    stateLevelLow
      .whenIsActive {
        when (io.level) {
          goto(statePosedgeDelay)
        }
      }

    statePosedgeDelay
      .whenIsActive {
        when (tick) {
          when (io.level) {
            goto(stateLevelHigh)
          } otherwise {
            goto(stateLevelLow)
          }
        }
      }
  }

  io.output := debouncerFsm.isActive(debouncerFsm.stateLevelHigh) ||
    debouncerFsm.isActive(debouncerFsm.statePosedgeDelay)
}