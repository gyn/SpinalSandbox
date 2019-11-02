package FPGA4Fun.p10

import spinal.core._

case class SimpleCounter(width: Int) extends Component {
  val io = new Bundle {
    val tick  = out Bool
  }

  val counterReg = Reg(UInt(width bits)) init(0)

  counterReg := counterReg + 1

  io.tick := counterReg === U(counterReg.range -> true)
}

case class SimpleCounterAnd(width: Int) extends Component {
  val io = new Bundle {
    val tick  = out Bool
  }

  val counterReg = Reg(UInt(width bits)) init(0)

  counterReg := counterReg + 1

  io.tick := counterReg.andR
}

case class SimpleCounterAlt(width: Int) extends Component {
  val io = new Bundle {
    val tick  = out Bool
  }

  val counterNext = UInt(width + 1 bits)
  val counterReg = Reg(UInt(width bits)) init(0)

  counterNext := counterReg.resize(width + 1) + 1
  counterReg := counterNext(counterNext.getWidth - 2 downto 0)

  io.tick := counterNext.msb
}

case class ModNCounter(limit: Int) extends Component {
  val width = log2Up(limit)

  val io = new Bundle {
    val tick  = out Bool
  }

  val counterNext = UInt(width bits)
  val counterReg = RegNext(counterNext) init(0)

  counterNext := (counterReg === limit - 1) ? U(0) | (counterReg + 1)

  io.tick := counterReg === (limit - 1)
}

case class GrayCounter(width: Int) extends Component {
  val io = new Bundle {
    val count = out UInt(width bits)
  }

  val counterReg = Reg(UInt(width bits)) init(0)
  counterReg := counterReg + 1

  io.count := counterReg ^ (counterReg |>> 1)
}