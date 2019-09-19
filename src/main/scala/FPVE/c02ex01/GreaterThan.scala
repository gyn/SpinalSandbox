package FPVE.c02ex01

import spinal.core._

//
//
//    \gt\    a
// b          00      01      10      11
// 00         0       1       1       1
// 01         0       0       1       1
// 10         0       0       0       1
// 11         0       0       0       0
//
class GreaterThan2b extends Component {
  val io = new Bundle {
    val gt  = out Bool
    val a   = in  Bits (2 bits)
    val b   = in  Bits (2 bits)
  }

  val p1 =  io.a(1) && !io.b(1)
  val p2 = !io.a(1) &&  io.a(0) && !io.b(1) && !io.b(0)
  val p3 =  io.a(1) &&  io.a(0) &&  io.b(1) && !io.b(0)

  io.gt := p1 || p2 || p3
}

//
//
//    \eq\    a
// b          00      01      10      11
// 00         1       0       0       0
// 01         0       1       0       0
// 10         0       0       1       0
// 11         0       0       0       1
//
class Equal2b extends Component {
  val io = new Bundle {
    val eq  = out Bool
    val a   = in  Bits (2 bits)
    val b   = in  Bits (2 bits)
  }

  val p1 = !io.a(1) && !io.a(0) && !io.b(1) && !io.b(0)
  val p2 = !io.a(1) &&  io.a(0) && !io.b(1) &&  io.b(0)
  val p3 =  io.a(1) && !io.a(0) &&  io.b(1) && !io.b(0)
  val p4 =  io.a(1) &&  io.a(0) &&  io.b(1) &&  io.b(0)

  io.eq := p1 || p2 || p3 || p4
}

//
//
//                          +------------------------+
//     a[3:0]      a[3:2]   |                        |
//    +---------+---------->+ .a                     |                   +-----+
//     b[3:0]   |  b[3:2]   |    greater_than_2b .gt +------------------>+     |  gt
//    +------+------------->+ .b                     |                   | OR  +------>
//           |  |           |                        |               +-->+     |
//           |  |           +------------------------+               |   +-----+
//           |  |                                                    |
//           |  |                                                    |
//           |  |           +------------------------+               |
//           |  |  a[3:2]   |                        |               |
//           |  +---------->+ .a                     |      +-----+  |
//           |  |  b[3:2]   |       equal_2b     .eq +----->+     |  |
//           +------------->+ .b                     |      | AND +--+
//           |  |           |                        |  +-->+     |
//           |  |           +------------------------+  |   +-----+
//           |  |                                       |
//           |  |                                       |
//           |  |           +------------------------+  |
//           |  |  a[1:0]   |                        |  |
//           |  +---------->+ .a                     |  |
//           |     b[1:0]   |    greater_than_2b .gt +--+
//           +------------->+ .b                     |
//                          |                        |
//                          +------------------------+
//
class GreaterThan4b extends Component {
  val io = new Bundle {
    val gt  = out Bool
    val a   = in  Bits (4 bits)
    val b   = in  Bits (4 bits)
  }

  val greaterThan2bHi = new GreaterThan2b
  greaterThan2bHi.io.a := io.a(3 downto 2)
  greaterThan2bHi.io.b := io.b(3 downto 2)

  val greaterThan2bLow = new GreaterThan2b
  greaterThan2bLow.io.a := io.a(1 downto 0)
  greaterThan2bLow.io.b := io.b(1 downto 0)

  val equal2bHi = new Equal2b
  equal2bHi.io.a := io.a(3 downto 2)
  equal2bHi.io.b := io.b(3 downto 2)

  val p1 = greaterThan2bHi.io.gt
  val p2 = equal2bHi.io.eq && greaterThan2bLow.io.gt

  io.gt := p1 || p2
}