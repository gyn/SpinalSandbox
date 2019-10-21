package FPVE.c05ex01

import spinal.core._
import spinal.lib.fsm._

class DualEdgeDetectorMoore extends Component {
  val io = new Bundle {
    val level   = in  Bool
    val posedge = out Bool
    val negedge = out Bool
  }

  val detectorFsm = new StateMachine {
    val stateInit = new State with EntryPoint
    val stateLevelHigh = new State
    val stateNegedge = new State
    val stateLevelLow = new State
    val statePosedge = new State

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
          goto(stateNegedge)
        }
      }

    stateNegedge.whenIsActive (goto(stateLevelLow))

    stateLevelLow
      .whenIsActive {
        when (io.level) {
          goto(statePosedge)
        }
      }

    statePosedge.whenIsActive (goto(stateLevelHigh))
  }

  io.negedge := detectorFsm.isActive(detectorFsm.stateNegedge)
  io.posedge := detectorFsm.isActive(detectorFsm.statePosedge)
}

class DualEdgeDetectorMealy extends Component {
  val io = new Bundle {
    val level   = in  Bool
    val posedge = out Bool
    val negedge = out Bool
  }

  val detectorFsm = new StateMachine {
    val stateInit = new State with EntryPoint
    val stateLevelHigh = new State
    val stateLevelLow = new State

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
          goto(stateLevelLow)
        }
      }

    stateLevelLow
      .whenIsActive {
        when (io.level) {
          goto(stateLevelHigh)
        }
      }
  }

  io.negedge := detectorFsm.isActive(detectorFsm.stateLevelHigh) && !io.level
  io.posedge := detectorFsm.isActive(detectorFsm.stateLevelLow) && io.level
}

class DualEdgeDetectorGate extends Component {
  val io = new Bundle {
    val level   = in  Bool
    val posedge = out Bool
    val negedge = out Bool
  }

  val delayReg = Reg(Bool) init(False)

  delayReg := io.level

  io.negedge := delayReg && !io.level
  io.posedge := !delayReg && io.level
}