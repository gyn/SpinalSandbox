package FPGA4Fun.p01

import spinal.core._

case class AmbulanceSiren(limit: Int) extends Component {
  val width = log2Up(limit)
  //
  // Since the system clock is 50MHz, while 25MHz is used in the fpga4fun examples
  //
  val toneWidth = 24 + 1

  val io = new Bundle {
    val speaker = out Bool
  }

  val toneReg = Reg(UInt(toneWidth bits)) init(0)
  toneReg := toneReg + 1

  val counterReg = Reg(UInt(width bits)) init(limit - 1)
  counterReg := counterReg - 1
  when(counterReg === 0) {
    counterReg := toneReg.msb ? U(limit - 1) | U(limit / 2 - 1)
  }

  val speakerReg = Reg(Bool) init(False)
  when(counterReg === 0) {
    speakerReg := !speakerReg
  }

  io.speaker := speakerReg
}