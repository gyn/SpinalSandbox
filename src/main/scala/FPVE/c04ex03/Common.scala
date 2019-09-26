package FPVE.c04ex03

import spinal.core._

case class ModNTimer(limit: Int) extends Component {
  val width = log2Up(limit)

  val io = new Bundle {
    val tick  = out Bool
  }

  val counter = Reg(UInt(width bits)) init (0)
  counter := (counter === U(limit - 1)) ? U(0) | (counter + 1)

  io.tick := counter === U(limit - 1)
}

case class ModNCounter(limit: Int) extends Component {
  val width = log2Up(limit)

  val io = new Bundle {
    val tick  = in  Bool
    val up    = in  Bool
    val value = out UInt(width bits)
  }

  val counter = Reg(UInt(width bits)) init (0)
  when (io.tick) {
    when (io.up) {
      counter := (counter === U(limit - 1)) ? U(0) | (counter + 1)
    } otherwise {
      counter := (counter === U(0)) ? U(limit - 1) | (counter - 1)
    }
  }

  io.value := counter
}

object SSeg {
  val patternBlank = B"8'b1111_1111"
  val patternFull = B"8'0000_0000"

  val patternUpperSquare = B"8'b1001_1100"
  val patternLowerSquare = B"8'b1110_0010"

  val patternLeftBar = B"8'b1111_1001"
  val patternRightBar = B"8'b1100_1111"

  val patternNr0 = B"8'b1000_0001"
  val patternNr1 = B"8'b1100_1111"
  val patternNr2 = B"8'b1001_0010"
  val patternNr3 = B"8'b1000_0110"
  val patternNr4 = B"8'b1100_1100"
  val patternNr5 = B"8'b1010_0100"
  val patternNr6 = B"8'b1010_0000"
  val patternNr7 = B"8'b1000_1111"
  val patternNr8 = B"8'b1000_0000"
  val patternNr9 = B"8'b1000_0100"
  val patternNrA = B"8'b1000_1000"
  val patternNrB = B"8'b1110_0000"
  val patternNrC = B"8'b1011_0001"
  val patternNrD = B"8'b1100_0010"
  val patternNrE = B"8'b1011_0000"
  val patternNrF = B"8'b1011_1000"
}