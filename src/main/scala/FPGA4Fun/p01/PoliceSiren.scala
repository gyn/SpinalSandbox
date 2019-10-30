package FPGA4Fun.p01

import spinal.core._

case class PoliceSiren(limit: Int) extends Component {
  val width = log2Up(limit)
  //
  // Since the system clock is 50MHz, while 25MHz is used in the fpga4fun examples
  //
  val toneWidth = 23 + 1

  val io = new Bundle {
    val speaker = out Bool
  }

  val toneReg = Reg(UInt(toneWidth bits)) init(0)
  toneReg := toneReg + 1

  val ramp         = toneReg.msb ? toneReg(22 downto 16) | ~toneReg(22 downto 16)
  val clockDivider = U"2'01" @@ ramp @@ U"7'b0000000"

  val counterReg = Reg(UInt(width bits)) init(limit - 1)
  counterReg := counterReg - 1
  when(counterReg === 0) {
    counterReg := clockDivider
  }

  val speakerReg = Reg(Bool) init(False)
  when(counterReg === 0) {
    speakerReg := !speakerReg
  }

  io.speaker := speakerReg
}