package FPVE.c04ex03

import spinal.core._

case class RotatingSquare(interval: Int) extends Component {
  val ssegNr = 4
  val ssegWidth = 8
  val patternNr = 8
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
  counter.io.up := io.cw

  //
  // Pattern register
  //
  val patternReg = Reg(Bits(patternWidth bits)) init (B((patternWidth - 1 downto 0) -> true))
  patternReg := counter.io.value.mux(
    0 -> SSegPattern.upperSquare ## SSegPattern.blank ## SSegPattern.blank ## SSegPattern.blank,
    1 -> SSegPattern.blank ## SSegPattern.upperSquare ## SSegPattern.blank ## SSegPattern.blank,
    2 -> SSegPattern.blank ## SSegPattern.blank ## SSegPattern.upperSquare ## SSegPattern.blank,
    3 -> SSegPattern.blank ## SSegPattern.blank ## SSegPattern.blank ## SSegPattern.upperSquare,
    4 -> SSegPattern.blank ## SSegPattern.blank ## SSegPattern.blank ## SSegPattern.lowerSquare,
    5 -> SSegPattern.blank ## SSegPattern.blank ## SSegPattern.lowerSquare ## SSegPattern.blank,
    6 -> SSegPattern.blank ## SSegPattern.lowerSquare ## SSegPattern.blank ## SSegPattern.blank,
    7 -> SSegPattern.lowerSquare ## SSegPattern.blank ## SSegPattern.blank ## SSegPattern.blank
  )

  io.pattern := patternReg
}