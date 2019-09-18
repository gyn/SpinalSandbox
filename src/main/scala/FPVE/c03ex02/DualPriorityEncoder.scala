package FPVE.c03ex02

import spinal.core._

case class PriorityEncoderMux(width: Int) extends Component {
  var nbit = log2Up(width + 1)

  val io = new Bundle {
    val request = in  Bits (width bits)
    val encode  = out UInt (nbit bits)
    val mask    = out Bits ((width - 1) bits)
  }

  val limit = 1 << width

  def ffs(x : Int) : Int = 32 - Integer.numberOfLeadingZeros(x)

  def mask(x : Int) : Int = if (x > 0) ((1 << (x - 1)) - 1) else 0

  //
  //
  // This works when width is less than 12 but fails otherwise with SpinalHDL 1.3.6
  //
  // io.encode := io.request.muxList(for (index <- 0 until limit) yield (index, U(ffs(index)))).asBits
  //
  // Following is a workaround
  //
  io.encode := Vec(for (index <- 0 until limit) yield U(ffs(index))).read(U(io.request))
  io.mask := Vec(for (index <- 0 until limit) yield U(mask(ffs(index)))).read(U(io.request)).asBits
}

class PriorityEncoder12b extends Component {
  val width = 12
  var nbit = log2Up(width + 1)

  val io = new Bundle {
    val request = in  Bits (width bits)
    val encode  = out UInt (nbit bits)
    val mask    = out Bits ((width - 1) bits)
  }

  when (io.request(11)) {
    io.encode := 12
    io.mask := B"111_1111_1111"
  } elsewhen (io.request(10)) {
    io.encode := 11
    io.mask := B"011_1111_1111"
  } elsewhen (io.request(9)) {
    io.encode := 10
    io.mask := B"001_1111_1111"
  } elsewhen (io.request(8)) {
    io.encode := 9
    io.mask := B"000_1111_1111"
  } elsewhen (io.request(7)) {
    io.encode := 8
    io.mask := B"000_0111_1111"
  } elsewhen (io.request(6)) {
    io.encode := 7
    io.mask := B"000_0011_1111"
  } elsewhen (io.request(5)) {
    io.encode := 6
    io.mask := B"000_0001_1111"
  } elsewhen (io.request(4)) {
    io.encode := 5
    io.mask := B"000_0000_1111"
  } elsewhen (io.request(3)) {
    io.encode := 4
    io.mask := B"000_0000_0111"
  } elsewhen (io.request(2)) {
    io.encode := 3
    io.mask := B"000_0000_0011"
  } elsewhen (io.request(1)) {
    io.encode := 2
    io.mask := B"000_0000_0001"
  } elsewhen (io.request(0)) {
    io.encode := 1
    io.mask := B"000_0000_0000"
  } otherwise {
    io.encode.clearAll()
    io.mask.clearAll()
  }
}

case class DualPriorityEncoderMux(width: Int) extends Component {
  var nbit = log2Up(width + 1)

  val io = new Bundle {
    val request = in  Bits (width bits)
    val first   = out UInt (nbit bits)
    val second  = out UInt (nbit bits)
  }

  var priorityEncoderFirst = PriorityEncoderMux(width = width)
  priorityEncoderFirst.io.request := io.request

  io.first := priorityEncoderFirst.io.encode

  var priorityEncoderSecond = PriorityEncoderMux(width = width)
  priorityEncoderSecond.io.request := io.request & priorityEncoderFirst.io.mask.resized

  io.second := priorityEncoderSecond.io.encode
}

class DualPriorityEncoder12b extends Component {
  val width = 12
  var nbit = log2Up(width + 1)

  val io = new Bundle {
    val request = in  Bits (width bits)
    val first   = out UInt (nbit bits)
    val second  = out UInt (nbit bits)
  }

  var priorityEncoderFirst = new PriorityEncoder12b
  priorityEncoderFirst.io.request := io.request

  io.first := priorityEncoderFirst.io.encode

  var priorityEncoderSecond = new PriorityEncoder12b
  priorityEncoderSecond.io.request := io.request & priorityEncoderFirst.io.mask.resized

  io.second := priorityEncoderSecond.io.encode
}