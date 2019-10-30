package FPVE.c06ex01

import spinal.core._
import spinal.lib.fsm._

case class Debouncer(cycles: Int) extends Component {
  val io = new Bundle {
    val level  = in  Bool
    val output = out Bool
  }

  val debouncerFsm = new StateMachine {
    val stateInit = new State with EntryPoint
    val stateLevelHigh    = new State
    val stateNegedgeDelay = new StateDelay(cyclesCount = cycles)
    val stateLevelLow     = new State
    val statePosedgeDelay = new StateDelay(cyclesCount = cycles)

    stateInit
      .whenIsActive {
        when(io.level) {
          goto(stateLevelHigh)
        } otherwise {
          goto(stateLevelLow)
        }
      }

    stateLevelHigh
      .whenIsActive {
        when(!io.level) {
          goto(stateNegedgeDelay)
        }
      }

    stateNegedgeDelay.whenCompleted {
      when(io.level) {
        goto(stateLevelHigh)
      } otherwise {
        goto(stateLevelLow)
      }
    }

    stateLevelLow
      .whenIsActive {
        when(io.level) {
          goto(statePosedgeDelay)
        }
      }

    statePosedgeDelay.whenCompleted {
      when(io.level) {
        goto(stateLevelHigh)
      } otherwise {
        goto(stateLevelLow)
      }
    }
  }

  io.output := debouncerFsm.isActive(debouncerFsm.stateLevelHigh) ||
    debouncerFsm.isActive(debouncerFsm.statePosedgeDelay)
}