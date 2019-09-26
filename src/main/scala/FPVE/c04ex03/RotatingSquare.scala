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
    0 -> SSeg.patternUpperSquare ## SSeg.patternBlank ## SSeg.patternBlank ## SSeg.patternBlank,
    1 -> SSeg.patternBlank ## SSeg.patternUpperSquare ## SSeg.patternBlank ## SSeg.patternBlank,
    2 -> SSeg.patternBlank ## SSeg.patternBlank ## SSeg.patternUpperSquare ## SSeg.patternBlank,
    3 -> SSeg.patternBlank ## SSeg.patternBlank ## SSeg.patternBlank ## SSeg.patternUpperSquare,
    4 -> SSeg.patternBlank ## SSeg.patternBlank ## SSeg.patternBlank ## SSeg.patternLowerSquare,
    5 -> SSeg.patternBlank ## SSeg.patternBlank ## SSeg.patternLowerSquare ## SSeg.patternBlank,
    6 -> SSeg.patternBlank ## SSeg.patternLowerSquare ## SSeg.patternBlank ## SSeg.patternBlank,
    7 -> SSeg.patternLowerSquare ## SSeg.patternBlank ## SSeg.patternBlank ## SSeg.patternBlank
  )

  io.pattern := patternReg
}