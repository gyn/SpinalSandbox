package FPVE.c04ex02

import spinal.core._

case class PWMLEDDimmer(width: Int) extends Component {
  val io = new Bundle {
    val w       = in  UInt (width bits)
    val signal  = out Bool
  }

  val counterReg = Reg(UInt(width bits)) init (0)
  counterReg := counterReg + 1

  val outReg = Reg(Bool) init (False)
  outReg := (counterReg < io.w) ? True | False

  io.signal := outReg
}