package FPVE.c04ex06

import spinal.core._

case class StopWatchCell(limit: Int) extends Component {
  val width = log2Up(limit + 1)

  val io = new Bundle {
    val tick    = in  Bool
    val hold    = in  Bool
    val up      = in  Bool
    val clear   = in  Bool
    val tickOut = out Bool
    val holdOut = out Bool
    val count   = out UInt(width bits)
  }

  val countReg = Reg(UInt(width bits)) init(0)
  when (io.clear) {
    countReg := 0
  } elsewhen (io.tick) {
    when (io.up) {
      when (countReg === limit) {
        when (io.hold) {
          countReg := countReg
        } otherwise {
          countReg := 0
        }
      } otherwise {
        countReg := countReg + 1
      }
    } otherwise {
      when (countReg === 0) {
        when (io.hold) {
          countReg := countReg
        } otherwise {
          countReg := limit
        }
      } otherwise {
        countReg := countReg - 1
      }
    }
  } otherwise {
    countReg := countReg
  }

  io.count := countReg

  when (io.up) {
    io.tickOut := io.tick && (countReg === limit)
    io.holdOut := io.hold && (countReg === limit)
  } otherwise {
    io.tickOut := io.tick && (countReg === 0)
    io.holdOut := io.hold && (countReg === 0)
  }
}

case class StopWatch(interval: Int) extends Component {
  val number = 4
  val nibble = 4
  val width = log2Up(interval)

  val io = new Bundle {
    val up    = in  Bool
    val clear = in  Bool
    val go    = in  Bool
    val hex   = out Vec(UInt(nibble bits), number)
  }

  val counter = Reg(UInt(width bits)) init (0)
  counter := (counter === U(interval - 1)) ? U(0) | (counter + 1)

  val tick = counter === U(interval - 1) && io.go

  val stopWatchCellArray = Array(
    StopWatchCell(limit = 9),
    StopWatchCell(limit = 9),
    StopWatchCell(limit = 5),
    StopWatchCell(limit = 9))

  var tickOut = tick
  var holdOut = True

  for (index <- 0 until number) {
    stopWatchCellArray(index).io.tick := tickOut
    stopWatchCellArray(index).io.up := io.up
    stopWatchCellArray(index).io.clear := io.clear

    io.hex(index) := stopWatchCellArray(index).io.count.resized

    tickOut \= stopWatchCellArray(index).io.tickOut
  }

  for (index <- number - 1 downto 0) {
    stopWatchCellArray(index).io.hold := holdOut

    holdOut \= stopWatchCellArray(index).io.holdOut
  }
}