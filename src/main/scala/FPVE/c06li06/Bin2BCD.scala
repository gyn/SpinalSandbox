package FPVE.c06li06

import spinal.core._
import spinal.lib.fsm._

case class Bin2BCD(widthN: Int) extends Component {
  val NIBBLE = 4
  val widthCounter = log2Up(widthN)

  def BCDNibble(binWidth: Int) = {
    var n = 1 << binWidth
    var i = 0
    while (n != 0) {
      i += 1
      n = n / 10
    }
    i
  }

  val nibbleResult = BCDNibble(widthN)
  val widthResult = nibbleResult * NIBBLE

  val io = new Bundle {
    val start   = in  Bool
    val n       = in  UInt(widthN bits)
    val ready   = out Bool
    val done    = out Bool
    val result  = out UInt(widthResult bits)
  }

  val bin2bcdFsm = new StateMachine {
    val stateIdle = new State with EntryPoint
    val stateOp = new State
    val stateDone = new State

    val nRegNext = UInt(widthCounter bits)
    val nReg = RegNext(nRegNext) init(0)
    val psRegNext = UInt(widthN bits)
    val psReg = RegNext(psRegNext) init(0)
    val bcdRegNext = UInt(widthResult bits)
    val bcdReg = RegNext(bcdRegNext) init(0)

    nRegNext := nReg
    psRegNext := psReg
    bcdRegNext := bcdReg

    stateIdle
      .whenIsActive {
        when(io.start) {
          psRegNext := io.n
          bcdRegNext := 0
          nRegNext := widthN

          goto(stateOp)
        }
      }

    stateOp
      .whenIsActive {
        psRegNext := psReg |<< 1

        val bcdRegFixed = UInt(widthResult bits)
        for (i <- 0 until nibbleResult) {
          val base = i * NIBBLE
          val nibble = bcdReg(base + 3 downto base)
          bcdRegFixed(base + 3 downto base) := (nibble > 4) ? (nibble + 3) | nibble
        }

        bcdRegNext := bcdRegFixed(widthResult - 2 downto 0) @@ psReg.msb

        nRegNext := nReg - 1
        when (nRegNext === 0) {
          goto(stateDone)
        }
      }

    stateDone.whenIsActive {
      goto(stateIdle)
    }
  }

  io.ready := bin2bcdFsm.isActive(bin2bcdFsm.stateIdle)
  io.done := bin2bcdFsm.isActive(bin2bcdFsm.stateDone)
  io.result := bin2bcdFsm.bcdReg
}