package FPGA4Fun.p08

import spinal.core._
import spinal.lib.BufferCC

case class Debouncer(width: Int) extends Component {
  val io = new Bundle {
    val button  = in  Bool
    val state   = out Bool
    val up      = out Bool
    val down    = out  Bool
  }
  val buttonSync = BufferCC(io.button)

  val stateReg = Reg(Bool)
  val counterReg = Reg(UInt(width bits))

  val buttonIdle = stateReg === buttonSync
  counterReg := counterReg + 1
  when (buttonIdle) {
    counterReg := 0
  }

  val buttonCounterMax = counterReg.andR
  when (buttonCounterMax) {
    stateReg := !stateReg
  }

  io.state := stateReg
  io.up := !buttonIdle && buttonCounterMax && stateReg
  io.down := !buttonIdle && buttonCounterMax && !stateReg
}