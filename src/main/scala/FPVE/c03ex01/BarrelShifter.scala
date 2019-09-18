package FPVE.c03ex01

import spinal.core._
import spinal.lib._

//
//
// width   i   Left-shift                                  Right-shift
// 1       0   {[N - 2**0 - 1 : 0], [N - 1 : N - 2**0]}    {[2**0 - 1 : 0], [N - 1: 2**0]}
// 2       1   {[N - 2**1 - 1 : 0], [N - 1 : N - 2**1]}    {[2**1 - 1 : 0], [N - 1: 2**1]}
// 4       2   {[N - 2**2 - 1 : 0], [N - 1 : N - 2**2]}    {[2**2 - 1 : 0], [N - 1: 2**2]}
// 8       3   {[N - 2**3 - 1 : 0], [N - 1 : N - 2**3]}    {[2**3 - 1 : 0], [N - 1: 2**3]}
// 16      4   {[N - 2**4 - 1 : 0], [N - 1 : N - 2**4]}    {[2**4 - 1 : 0], [N - 1: 2**4]}
// ...     ... {[       ...      ], [       ...      ]}    {[     ...    ], [    ...    ]}
// 2**i    i   {[N - 2**i - 1 : 0], [N - 1 : N - 2**i]}    {[2**i - 1 : 0], [N - 1: 2**i]}
//
//
case class BarrelShifterLeft(width: Int) extends Component {
  var nbit = log2Up(width)

  val io = new Bundle {
    val a   = in  Bits(width bits)
    val amt = in  Bits(nbit bits)
    val y   = out Bits(width bits)
  }

  var t = Vec(Bits(width bits), nbit + 1)

  t(0) := io.a

  for (i <- 0 until nbit) {
    when (io.amt(i)) {
      var limit = 1 << i
      t(i+1) := t(i)(width - limit - 1 downto 0) ## t(i)(width - 1 downto width - limit)
    } otherwise {
      t(i+1) := t(i)
    }
  }

  io.y := t(nbit)
}

case class BarrelShifterRight(width: Int) extends Component {
  var nbit = log2Up(width)

  val io = new Bundle {
    val a   = in  Bits(width bits)
    val amt = in  Bits(nbit bits)
    val y   = out Bits(width bits)
  }

  var t = Vec(Bits(width bits), nbit + 1)

  t(0) := io.a

  for (i <- 0 until nbit) {
    when (io.amt(i)) {
      var limit = 1 << i
      t(i+1) := t(i)(limit - 1 downto 0) ## t(i)(width - 1 downto limit)
    } otherwise {
      t(i+1) := t(i)
    }
  }

  io.y := t(nbit)
}

case class BarrelShifter(width: Int) extends Component {
  var nbit = log2Up(width)

  val io = new Bundle {
    val a   = in  Bits(width bits)
    val amt = in  Bits(nbit bits)
    var lr  = in  Bool
    val y   = out Bits(width bits)
  }

  var barrelShifterRight = BarrelShifterRight(width = width)
  barrelShifterRight.io.a := io.a
  barrelShifterRight.io.amt := io.amt

  var barrelShifterLeft = BarrelShifterLeft(width = width)
  barrelShifterLeft.io.a := io.a
  barrelShifterLeft.io.amt := io.amt

  when (io.lr) {
    io.y := barrelShifterRight.io.y
  } otherwise {
    io.y := barrelShifterLeft.io.y
  }
}

case class BarrelShifterByReverse(width: Int) extends Component {
  var nbit = log2Up(width)

  val io = new Bundle {
    val a   = in  Bits(width bits)
    val amt = in  Bits(nbit bits)
    var lr  = in  Bool
    val y   = out Bits(width bits)
  }
  var barrelShifterRight = BarrelShifterRight(width = width)
  barrelShifterRight.io.amt := io.amt

  when (io.lr) {
    barrelShifterRight.io.a := io.a
    io.y := barrelShifterRight.io.y
  } otherwise {
    barrelShifterRight.io.a := Reverse(io.a)
    io.y := Reverse(barrelShifterRight.io.y)
  }
}