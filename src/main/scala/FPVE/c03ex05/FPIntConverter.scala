package FPVE.c03ex05

import spinal.core._

class FP2Int extends Component {
  val intBits  = 8
  val expBits  = 4
  val fracBits = 8

  val io = new Bundle {
    val sign      = in  Bool
    val exp       = in  UInt(expBits bits)
    val frac      = in  UInt(fracBits bits)
    val value     = out SInt(intBits bits)
    val overflow  = out Bool
    val underflow = out Bool
  }

  //
  // 1st stage : work out the number
  //
  val valueFixed = io.frac << io.exp
  val magnitude  = valueFixed(15 downto 8)

  //
  //  2nd stage : deal with output
  //
  io.value := io.sign ? (~magnitude + 1).asSInt | magnitude.asSInt
  //
  // Since the SInt is between -128 and 127, we should check valueFixed > (128 << 8) when io.sign is 1 and also
  // valueFixed > (127 << 8) when io.sign is 0.
  //
  // For 128, frac and exp should be 0x80 and 8, so the lower 8 bit could be ignore. While for 127, frac and exp is FE
  // and 7, the lower 7 bit could be ignored
  //
  io.overflow := io.sign ?
    (valueFixed(valueFixed.high downto 8) > 0x80) |
    (valueFixed(valueFixed.high downto 7) > (0x7F << 1))

  io.underflow := (io.frac =/= 0) && valueFixed < (1 << 8)
}

class Int2FP extends Component {
  val intBits = 8
  val expBits = 4
  val fracBits = 8

  val io = new Bundle {
    val value = in  SInt(intBits bits)
    val sign  = out Bool
    val exp   = out UInt(expBits bits)
    val frac  = out UInt(fracBits bits)
  }

  //
  // 1st stage : deal with the sign bit and the negative number
  //
  val valueFixed = io.value.msb ? ((~io.value(intBits - 2 downto 0)).asUInt.resize(intBits) + 1) | io.value.asUInt

  //
  // 2nd stage : deal with exp and normalize
  //
  def ffs(x: Int): Int = 32 - Integer.numberOfLeadingZeros(x)

  def amt(x: Int): Int = if (x > 0) 8 - x else 0

  val limit = 1 << intBits
  val shift = valueFixed.muxList(for (index <- 0 until limit) yield (index, U(amt(ffs(index)))))

  io.sign := io.value.msb
  io.frac := valueFixed |<< shift
  io.exp  := valueFixed.muxList(for (index <- 0 until limit) yield (index, U(ffs(index))))
}

class Int2IntTester extends Component {
  val intBits = 8

  val io = new Bundle {
    val inValue  = in  SInt(intBits bits)
    val outValue = out SInt(intBits bits)
  }

  val int2FP = new Int2FP
  int2FP.io.value := io.inValue

  val fp2Int = new FP2Int
  fp2Int.io.sign := int2FP.io.sign
  fp2Int.io.frac := int2FP.io.frac
  fp2Int.io.exp  := int2FP.io.exp

  io.outValue := fp2Int.io.value
}