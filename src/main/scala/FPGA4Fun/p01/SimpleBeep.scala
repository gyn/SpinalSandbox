package FPGA4Fun.p01

import spinal.core._

case class SimpleBeep(limit: Int) extends Component {
  val width = log2Up(limit)

  val io = new Bundle {
    val speaker = out Bool
  }

  val counterReg = Reg(UInt(width bits)) init(limit - 1)
  counterReg := counterReg - 1
  when(counterReg === 0) {
    counterReg := limit - 1
  }

  val speakerReg = Reg(Bool) init(False)
  when(counterReg === 0) {
    speakerReg := !speakerReg
  }

  io.speaker := speakerReg
}