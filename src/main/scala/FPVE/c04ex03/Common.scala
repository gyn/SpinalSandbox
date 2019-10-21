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

  val counter = Reg(UInt(width bits)) init(0)
  when (io.tick) {
    when (io.up) {
      counter := (counter === U(limit - 1)) ? U(0) | (counter + 1)
    } otherwise {
      counter := (counter === U(0)) ? U(limit - 1) | (counter - 1)
    }
  }

  io.value := counter
}

object SSegPattern {
  val blank = B"8'b1111_1111"
  val full = B"8'0000_0000"

  val upperSquare = B"8'b1001_1100"
  val lowerSquare = B"8'b1110_0010"

  val leftBar = B"8'b1111_1001"
  val rightBar = B"8'b1100_1111"

  val Nr0 = B"8'b1000_0001"
  val Nr1 = B"8'b1100_1111"
  val Nr2 = B"8'b1001_0010"
  val Nr3 = B"8'b1000_0110"
  val Nr4 = B"8'b1100_1100"
  val Nr5 = B"8'b1010_0100"
  val Nr6 = B"8'b1010_0000"
  val Nr7 = B"8'b1000_1111"
  val Nr8 = B"8'b1000_0000"
  val Nr9 = B"8'b1000_0100"
  val NrA = B"8'b1000_1000"
  val NrB = B"8'b1110_0000"
  val NrC = B"8'b1011_0001"
  val NrD = B"8'b1100_0010"
  val NrE = B"8'b1011_0000"
  val NrF = B"8'b1011_1000"
}

object BCDSSeg {
  def apply(input: UInt): Bits = {
    val ret = Bits(8 bits)
    switch (input) {
      is(0x0) {
        ret := SSegPattern.Nr0
      }
      is(0x1) {
        ret := SSegPattern.Nr1
      }
      is(0x2) {
        ret := SSegPattern.Nr2
      }
      is(0x3) {
        ret := SSegPattern.Nr3
      }
      is(0x4) {
        ret := SSegPattern.Nr4
      }
      is(0x5) {
        ret := SSegPattern.Nr5
      }
      is(0x6) {
        ret := SSegPattern.Nr6
      }
      is(0x7) {
        ret := SSegPattern.Nr7
      }
      is(0x8) {
        ret := SSegPattern.Nr8
      }
      is(0x9) {
        ret := SSegPattern.Nr9
      }
      default {
        ret := SSegPattern.blank
      }
    }
    ret
  }
}

object HexSSeg {
  def apply(input: UInt): Bits = {
    val ret = Bits(8 bits)
    switch (input) {
      is(0x0) {
        ret := SSegPattern.Nr0
      }
      is(0x1) {
        ret := SSegPattern.Nr1
      }
      is(0x2) {
        ret := SSegPattern.Nr2
      }
      is(0x3) {
        ret := SSegPattern.Nr3
      }
      is(0x4) {
        ret := SSegPattern.Nr4
      }
      is(0x5) {
        ret := SSegPattern.Nr5
      }
      is(0x6) {
        ret := SSegPattern.Nr6
      }
      is(0x7) {
        ret := SSegPattern.Nr7
      }
      is(0x8) {
        ret := SSegPattern.Nr8
      }
      is(0x9) {
        ret := SSegPattern.Nr9
      }
      is(0xA) {
        ret := SSegPattern.NrA
      }
      is(0xB) {
        ret := SSegPattern.NrB
      }
      is(0xC) {
        ret := SSegPattern.NrC
      }
      is(0xD) {
        ret := SSegPattern.NrD
      }
      is(0xE) {
        ret := SSegPattern.NrE
      }
      is(0xF) {
        ret := SSegPattern.NrF
      }
      default {
        ret := SSegPattern.blank
      }
    }
    ret
  }
}