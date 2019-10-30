package FPVE.c04ex03

import spinal.core._

case class RotatingLEDBanner(interval: Int) extends Component {
  val ssegNr       = 4
  val ssegWidth    = 8
  val patternNr    = 10

  val patternWidth = ssegNr * ssegWidth

  val io = new Bundle {
    val en      = in  Bool
    val cw      = in  Bool
    val pattern = out Bits(patternWidth bits)
  }

  //
  // timer
  //
  val timer = ModNTimer(limit = interval)

  //
  // Pattern maker
  //
  val counter = ModNCounter(limit = patternNr)
  counter.io.tick := io.en && timer.io.tick
  counter.io.up   := io.cw

  //
  // Pattern register
  //
  val patternReg = Reg(Bits(patternWidth bits)) init(B((patternWidth - 1 downto 0) -> true))
  patternReg := counter.io.value.mux(
    0 -> SSegPattern.Nr0 ## SSegPattern.Nr1 ## SSegPattern.Nr2 ## SSegPattern.Nr3,
    1 -> SSegPattern.Nr1 ## SSegPattern.Nr2 ## SSegPattern.Nr3 ## SSegPattern.Nr4,
    2 -> SSegPattern.Nr2 ## SSegPattern.Nr3 ## SSegPattern.Nr4 ## SSegPattern.Nr5,
    3 -> SSegPattern.Nr3 ## SSegPattern.Nr4 ## SSegPattern.Nr5 ## SSegPattern.Nr6,
    4 -> SSegPattern.Nr4 ## SSegPattern.Nr5 ## SSegPattern.Nr6 ## SSegPattern.Nr7,
    5 -> SSegPattern.Nr5 ## SSegPattern.Nr6 ## SSegPattern.Nr7 ## SSegPattern.Nr8,
    6 -> SSegPattern.Nr6 ## SSegPattern.Nr7 ## SSegPattern.Nr8 ## SSegPattern.Nr9,
    7 -> SSegPattern.Nr7 ## SSegPattern.Nr8 ## SSegPattern.Nr9 ## SSegPattern.Nr0,
    8 -> SSegPattern.Nr8 ## SSegPattern.Nr9 ## SSegPattern.Nr0 ## SSegPattern.Nr1,
    9 -> SSegPattern.Nr9 ## SSegPattern.Nr0 ## SSegPattern.Nr1 ## SSegPattern.Nr2,
    default -> SSegPattern.blank ## SSegPattern.blank ## SSegPattern.blank ## SSegPattern.blank
  )

  io.pattern := patternReg
}