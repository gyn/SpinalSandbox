package FPVE.c03ex03

import spinal.core._

class BCDCell extends Component {
  val NIBBLE = 4

  val io = new Bundle {
    val hex = in  UInt (NIBBLE bits)
    val ci  = in  Bool
    val bcd = out UInt (NIBBLE bits)
    val co  = out Bool
  }

  val sum = io.hex.resize(NIBBLE + 1) + io.ci.asUInt

  when (sum < 10) {
    io.bcd := sum(NIBBLE - 1 downto 0)
    io.co  := False
  } otherwise {
    io.bcd := sum(NIBBLE - 1 downto 0) + 6
    io.co  := True
  }
}

case class BCDIncrementor(Nr : Int) extends Component {
  val NIBBLE = 4

  val io = new Bundle {
    val hex   = in  Vec(UInt(NIBBLE bits), Nr)
    val bcd   = out Vec(UInt(NIBBLE bits), Nr)
    val co    = out Bool
  }

  val BCDCellArray = Array.fill(Nr)(new BCDCell)
  var coBit = True

  BCDCellArray(0).io.hex := io.hex(0)
  BCDCellArray(0).io.ci := coBit
  io.bcd(0) := BCDCellArray(0).io.bcd
  coBit \= BCDCellArray(0).io.co

  for (index <- 1 until Nr) {
    BCDCellArray(index).io.hex := io.hex(index)
    BCDCellArray(index).io.ci := coBit
    io.bcd(index) := BCDCellArray(index).io.bcd
    coBit \= BCDCellArray(index).io.co
  }

  io.co := coBit
}