package FPVE.c06ex04

import FPVE.c04ex03.BCDSSeg
import spinal.core._
import spinal.lib.fsm._

class FibonacciBCD extends Component {
  val bcdLimit = 20
  val fibonacciOutputLimit = 6765

  val bcdNr  = 2
  val ssegNr = 4
  val NIBBLE = 4
  val BYTE   = 8

  val ssegWidth   = ssegNr * NIBBLE
  val bcdWidth    = bcdNr * NIBBLE
  val outputWidth = ssegNr * BYTE

  val io = new Bundle {
    val start  = in  Bool
    val n      = in  UInt(bcdWidth bits)
    val result = out Bits(outputWidth bits)
  }

  val fibonacciBCDFsm = new StateMachine {
    val stateIdle      = new State with EntryPoint
    val stateBin2BCD   = new State
    val stateFibonacci = new State
    val stateBCD2Bin   = new State
    val stateDone      = new State

    val nRegNext = UInt(log2Up(bcdLimit) bits)
    val nReg     = RegNext(nRegNext) init(0)

    nRegNext := nReg

    val t0RegNext = UInt(log2Up(fibonacciOutputLimit) bits)
    val t0Reg     = RegNext(t0RegNext) init(0)
    val t1RegNext = UInt(log2Up(fibonacciOutputLimit) bits)
    val t1Reg     = RegNext(t1RegNext) init(0)

    t0RegNext := t0Reg
    t1RegNext := t1Reg

    val ssegRegNext   = UInt(ssegWidth bits)
    val ssegReg       = RegNext(ssegRegNext) init (0)
    val resultRegNext = UInt(ssegWidth bits)
    val resultReg     = RegNext(resultRegNext) init (0)

    ssegRegNext   := ssegReg
    resultRegNext := resultReg

    val nibbleHigh = io.n(7 downto 4)
    val nibbleLow  = io.n(3 downto 0)

    stateIdle
      .whenIsActive {
        when(io.start) {
          when(nibbleHigh < 2 || (nibbleHigh === 2 && nibbleLow === 0)) {
            goto(stateBCD2Bin)
          } otherwise {
            val ssegOverflowLimit = 0x9999

            ssegRegNext := ssegOverflowLimit

            goto(stateIdle)
          }
        }
      }

    stateBCD2Bin
      .whenIsActive {
        //
        // bcdResult is io.n(7 downto 4) * 10 + io.n(3 downto 0), which is nibbleHigh * 10 + nibbleLow.
        // and bcdResult is always less than 21, so io.n(7 downto 6), which is nibbleHigh(3 downto 2) could be ignored
        //
        nRegNext := nibbleHigh(1 downto 0) @@ U"3'0" + nibbleHigh(1 downto 0) @@ U"1'0" + nibbleLow

        // Prepare for stateFibonacci
        t0RegNext := 0
        t1RegNext := 1

        goto(stateFibonacci)
      }

    stateFibonacci
      .whenIsActive {
        when(nReg === 0) {
          t1RegNext := 0

          // Prepare for stateBin2BCD
          resultRegNext := 0
          nRegNext      := log2Up(fibonacciOutputLimit)

          goto(stateBin2BCD)
        } elsewhen (nReg === 1) {
          // Prepare for stateBin2BCD
          resultRegNext := 0
          nRegNext      := log2Up(fibonacciOutputLimit)

          goto(stateBin2BCD)
        } otherwise {
          t1RegNext := t1Reg + t0Reg
          t0RegNext := t1Reg
          nRegNext  := nReg - 1

          goto(stateFibonacci)
        }
      }

    stateBin2BCD
      .whenIsActive {
        t1RegNext := t1Reg |<< 1

        val bcdRegFixed = UInt(ssegWidth bits)
        for (i <- 0 until ssegNr) {
          val base = i * NIBBLE
          val nibble = resultReg(base + 3 downto base)

          bcdRegFixed(base + 3 downto base) := (nibble > 4) ? (nibble + 3) | nibble
        }

        resultRegNext := bcdRegFixed(ssegWidth - 2 downto 0) @@ t1Reg.msb

        nRegNext := nReg - 1
        when(nRegNext === 0) {
          ssegRegNext := resultRegNext

          goto(stateDone)
        }
      }

    stateDone
      .whenIsActive {
        goto(stateIdle)
      }
  }

  val ssegReg = fibonacciBCDFsm.ssegReg.subdivideIn(NIBBLE bits)
  val result  = Vec(Bits(BYTE bits), ssegNr)
  for (i <- 0 until ssegNr) {
    result(i) := BCDSSeg(ssegReg(i))
  }

  io.result := result.asBits
}