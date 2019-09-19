package FPVE.c04ex01

import spinal.core._

case class SquareWaveGenerator(Scalar: Int, WBits: Int) extends Component {
  val width = log2Up(Scalar) + WBits + 1

  val io = new Bundle {
    val m       = in UInt (WBits bits)
    val n       = in UInt (WBits bits)
    val signal  = out Bool
  }

  //
  // limit = (m + n) * 5 = (m + n) << 2 + (m + n)
  //
  val sum = io.m.resize(WBits + 1) + io.n
  val limitSum = (sum @@ U"2'0").resize(width bits) + sum - 1

  val counterReg = Reg(UInt(width bits)) init (0)
  when (counterReg === limitSum) {
    counterReg := 0
  } otherwise {
    counterReg := counterReg + 1
  }

  //
  // limitM = m * 5 = m << 2 + m
  //
  val limitM = (io.m @@ U"2'0").resize(width bits) + io.m

  //
  // m determines the signal, which means the signal would be always 0 when m is 0
  //
  val signalReg = Reg(Bool) init (False)
  when (counterReg < limitM) {
    signalReg := True
  } otherwise {
    signalReg := False
  }

  io.signal := signalReg
}