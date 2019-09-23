package FPVE.c03ex04

import spinal.core._

class FPGreaterThan extends Component {
  val expBits = 4
  val fracBits = 8

  val io = new Bundle {
    val sign1 = in  Bool
    val exp1  = in  UInt (expBits bits)
    val frac1 = in  UInt (fracBits bits)
    val sign2 = in  Bool
    val exp2  = in  UInt (expBits bits)
    val frac2 = in  UInt (fracBits bits)
    val gt    = out Bool
  }

  //
  //  1st stage : fixed the sign bit for -0 to simplify the condition
  //
  val sign1Fixed = (io.exp1 ## io.frac1).orR ? io.sign1 | False
  val sign2Fixed = (io.exp2 ## io.frac2).orR ? io.sign2 | False
  //
  // And also check the relational of {exp1, frac1} and {exp2, frac2}
  //
  val eqWithoutSign = ((io.exp1 @@ io.frac1) === (io.exp2 @@ io.frac2)) ? True | False
  val gtWithoutSign = ((io.exp1 @@ io.frac1) > (io.exp2 @@ io.frac2)) ? True | False

  //
  // 2nd stage : check sign1Fixed, sign2Fixed, eqWithoutSign and gtWithoutSign
  //
  val sel = sign1Fixed ## sign2Fixed ## eqWithoutSign ## gtWithoutSign
  io.gt := sel.mux(
    0x0 -> False,           // positive, positive, n1 < n2 for 0
    0x1 -> True,            // positive, positive, n1 > n2 for 1
    0x2 -> False,           // positive, positive, n1 == n2 for 2,3
    0x3 -> False,           //
    0x4 -> True,            // positive, negative for 4,5,6,7
    0x5 -> True,            //
    0x6 -> True,            //
    0x7 -> True,            //
    0x8 -> False,           // negative, positive for 8,9,10,11
    0x9 -> False,           //
    0xa -> False,           //
    0xb -> False,           //
    0xc -> True,            // negative, negative, n1 < n2 for 12
    0xd -> False,           // negative, negative, n1 > n2 for 13
    0xe -> False,           // negative, negative, n1 = n2 for 14, 15
    0xf -> False            //
  )
}