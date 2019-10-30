package FPGA4Fun.p01

import spinal.core._

class PlayingNotes extends Component {
  //
  // Since the system clock is 50MHz, while 25MHz is used in the fpga4fun examples
  //
  val toneWidth = 28 + 1

  val io = new Bundle {
    val speaker = out Bool
  }

  val toneReg = Reg(UInt(toneWidth bits)) init(0)
  toneReg := toneReg + 1

  val selNote = toneReg(28 downto 25)
  val octave = selNote.muxList(for (index <- 0 until 16) yield (index, U(index / 3)))
  val noteHigh = selNote.muxList(for (index <- 0 until 16) yield (index, U(index % 3)))
  val note = noteHigh @@ toneReg(24 downto 23)

  val clockDivider = note.mux(
    0 -> U(512 - 1),
    1 -> U(483 - 1),
    2 -> U(456 - 1),
    3 -> U(431 - 1),
    4 -> U(406 - 1),
    5 -> U(384 - 1),
    6 -> U(362 - 1),
    7 -> U(342 - 1),
    8 -> U(323 - 1),
    9 -> U(304 - 1),
    10 -> U(287 - 1),
    11 -> U(271 - 1),
    12 -> U(0),
    13 -> U(0),
    14 -> U(0),
    15 -> U(0)
  )

  val counterNoteReg = Reg(UInt(9 bits)) init(511)
  counterNoteReg := counterNoteReg - 1
  when (counterNoteReg === 0) {
    counterNoteReg := clockDivider
  }

  def octaveMux(i: Int): UInt = {
    val result = (1 << (8 - i)) - 1
    if (result < 7) 7 else result
  }

  val counterOctaveReg = Reg(UInt(8 bits)) init(255)
  counterOctaveReg := counterOctaveReg - 1
  when (counterOctaveReg === 0) {
    counterOctaveReg := octave.muxList(for (index <- 0 until 8) yield (index, octaveMux(index)))
  }

  val speakerReg = Reg(Bool) init(False)
  when (counterNoteReg === 0 && counterOctaveReg === 0) {
    speakerReg := !speakerReg
  }

  io.speaker := speakerReg
}