package FPVE.c06ex02

import spinal.core._
import spinal.lib.fsm._

case class BCD2Bin(widthN: Int) extends Component {
  val NIBBLE = 4

  require((widthN & (NIBBLE - 1)) == 0)
  val widthCounter = widthN / NIBBLE

  def binWidth(bcdWidth: Int) = {
    val limit = scala.math.pow(10, widthCounter).toInt - 1
    log2Up(limit)
  }

  val widthResult = binWidth(widthN)

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
    val psReg = RegNext(psRegNext) init (0)
    val binRegNext = UInt(widthResult bits)
    val binReg = RegNext(binRegNext) init (0)

    nRegNext := nReg
    psRegNext := psReg
    binRegNext := binReg

    stateIdle
      .whenIsActive {
        when(io.start) {
          psReg := io.n
          binRegNext := 0
          nRegNext := widthCounter

          goto(stateOp)
        }
      }

    stateOp
      .whenIsActive {
        psRegNext := psReg |<< 4

        //
        // binReg * 10 + psReg(psReg.high downto (psReg.high - 3))
        //
        binRegNext := (binReg |<< 3) + (binReg |<< 1) + psReg(widthN - 1 downto widthN - 4)

        nRegNext := nReg - 1
        when (nRegNext === 0) {
          goto(stateDone)
        }
      }

    stateDone.whenIsActive { goto(stateIdle) }
  }

  io.ready := bin2bcdFsm.isActive(bin2bcdFsm.stateIdle)
  io.done := bin2bcdFsm.isActive(bin2bcdFsm.stateDone)
  io.result := bin2bcdFsm.binRegNext
}