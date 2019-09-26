package FPVE.c04ex03

import spinal.core._

case class RotatingLEDBanner(interval: Int) extends Component {
  val ssegNr = 4
  val ssegWidth = 8
  val patternNr = 10
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
    0 -> SSeg.patternNr0 ## SSeg.patternNr1 ## SSeg.patternNr2 ## SSeg.patternNr3,
    1 -> SSeg.patternNr1 ## SSeg.patternNr2 ## SSeg.patternNr3 ## SSeg.patternNr4,
    2 -> SSeg.patternNr2 ## SSeg.patternNr3 ## SSeg.patternNr4 ## SSeg.patternNr5,
    3 -> SSeg.patternNr3 ## SSeg.patternNr4 ## SSeg.patternNr5 ## SSeg.patternNr6,
    4 -> SSeg.patternNr4 ## SSeg.patternNr5 ## SSeg.patternNr6 ## SSeg.patternNr7,
    5 -> SSeg.patternNr5 ## SSeg.patternNr6 ## SSeg.patternNr7 ## SSeg.patternNr8,
    6 -> SSeg.patternNr6 ## SSeg.patternNr7 ## SSeg.patternNr8 ## SSeg.patternNr9,
    7 -> SSeg.patternNr7 ## SSeg.patternNr8 ## SSeg.patternNr9 ## SSeg.patternNr0,
    8 -> SSeg.patternNr8 ## SSeg.patternNr9 ## SSeg.patternNr0 ## SSeg.patternNr1,
    9 -> SSeg.patternNr9 ## SSeg.patternNr0 ## SSeg.patternNr1 ## SSeg.patternNr2,
    default -> SSeg.patternFull ## SSeg.patternFull ## SSeg.patternFull ## SSeg.patternFull
  )

  io.pattern := patternReg
}