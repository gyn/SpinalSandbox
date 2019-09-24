package FPVE.c04ex01

import spinal.core._

case class SquareWaveGenerator(configBits: Int) extends Component {
  val scalar = 5

  val io = new Bundle {
    val m       = in UInt (configBits bits)
    val n       = in UInt (configBits bits)
    val signal  = out Bool
  }

  val width = log2Up(scalar * 2 * ((1 << configBits) - 1))

  //
  // limitM = m * 5 = m << 2 + m
  // limitSum = (m + n) * 5 = (m + n) << 2 + (m + n)
  //
  val sum = io.m.resize(configBits + 1) + io.n
  val limitSum = (sum @@ U"2'0").resize(width bits) + sum
  val limitM = (io.m @@ U"2'0").resize(width bits) + io.m

  val counterReg = Reg(UInt(width bits)) init (0)
  counterReg := (counterReg === (limitSum - 1)) ? U(0) | counterReg + 1

  //
  // m determines the signal, which means the signal would be always 0 when m is 0
  //
  val signalReg = Reg(Bool) init (False)
  signalReg := (counterReg < limitM) ? True | False

  io.signal := signalReg
}