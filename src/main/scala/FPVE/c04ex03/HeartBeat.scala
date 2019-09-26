package FPVE.c04ex03

import spinal.core._

case class HeartBeat(interval: Int) extends Component {
  val ssegNr = 4
  val ssegWidth = 8
  val patternNr = 3
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
    0 -> SSeg.patternBlank ## SSeg.patternRightBar ## SSeg.patternLeftBar ## SSeg.patternBlank,
    1 -> SSeg.patternBlank ## SSeg.patternLeftBar ## SSeg.patternRightBar ## SSeg.patternBlank,
    2 -> SSeg.patternLeftBar ## SSeg.patternBlank ## SSeg.patternBlank ## SSeg.patternRightBar,
    default -> SSeg.patternFull ## SSeg.patternFull ## SSeg.patternFull ## SSeg.patternFull
  )

  io.pattern := patternReg
}