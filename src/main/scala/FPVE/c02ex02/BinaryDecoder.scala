package FPVE.c02ex02

import spinal.core._

//
//
//  \output\  sel
// en         00      01      10      11
//  0         0000    0000    0000    0000
//  1         0001    0010    0100    1000
//
class BinaryDecoder2b extends Component {
  val io = new Bundle {
    val output  = out Bits(4 bits)
    val en      = in  Bool
    val sel     = in  Bits(2 bits)
  }

  io.output(3) := io.en &&  io.sel(1) &&  io.sel(0)
  io.output(2) := io.en &&  io.sel(1) && !io.sel(0)
  io.output(1) := io.en && !io.sel(1) &&  io.sel(0)
  io.output(0) := io.en && !io.sel(1) && !io.sel(0)
}

//
//
//     en
//    +---------------------+
//                          |
//     sel[2:0]  sel[1:0]   |               +------------------------------+
//    +---------+-----------------------+-->+ .sel                         |
//              |           |           |   |                              | out[7:4]   out[7:0]
//              |           |  +-----+  |   |     binary_decoder_2b4b .out +----------+---------->
//              |           +--+     |  |   |                              |          ^
//              |sel[2]     |  | AND +----->+ .en                          |          |
//              +-------+------+     |  |   +------------------------------+          |
//                      |   |  +-----+  |                                             |
//                      |   |           |                                             |
//                      |   |           |                                             |
//                    +-+-+ |           |   +------------------------------+          |
//                    |NOT| |           +-->+ .sel                         |          |
//                    +-+-+ |               |                              | out[3:0] |
//                      |   |  +-----+      |     binary_decoder_2b4b .out +----------+
//                      |   +--+     |      |                              |
//                      |      | AND +----->+ .en                          |
//                      +------+     |      +------------------------------+
//                             +-----+
//
//
class BinaryDecoder3b extends Component {
  val io = new Bundle {
    val output  = out Bits(8 bits)
    val en      = in  Bool
    val sel     = in  Bits(3 bits)
  }

  val binaryDecoder2bHi = new BinaryDecoder2b
  binaryDecoder2bHi.io.sel := io.sel(1 downto 0)
  binaryDecoder2bHi.io.en := io.en && io.sel(2)

  val binaryDecoder2bLow = new BinaryDecoder2b
  binaryDecoder2bLow.io.sel := io.sel(1 downto 0)
  binaryDecoder2bLow.io.en := io.en && !io.sel(2)

  io.output := binaryDecoder2bHi.io.output ## binaryDecoder2bLow.io.output
}

//
//  sel[3:0]                              sel[1:0]                +------------------------------+
//  +--------+------------------------------------------------+-->+ .sel                         |
//           |                                                |   |                              | out[15:12] out[15:0]
//           |                                                |   |     binary_decoder_2b4b .out +----------+---------->
//           |                                        out[3]  |   |                              |          ^
//           |sel[3:2]                              +------------>+ .en                          |          |
//           |                                      |         |   +------------------------------+          |
//           |                                      |         |                                             |
//           |    +------------------------------+  |         |                                             |
//           +--->+ .sel                         |  |         |   +------------------------------+          |
//                |                              |  |         +-->+ .sel                         |          |
//                |     binary_decoder_2b4b .out +--+         |   |                              | out[11:8]|
//  en            |                              |  |         |   |     binary_decoder_2b4b .out +----------+
//  +------------>+ .en                          |  | out[2]  |   |                              |          |
//                +------------------------------+  +------------>+ .en                          |          |
//                                                  |         |   +------------------------------+          |
//                                                  |         |                                             |
//                                                  |         |                                             |
//                                                  |         |   +------------------------------+          |
//                                                  |         +-->+ .sel                         |          |
//                                                  |         |   |                              | out[7:4] |
//                                                  |         |   |     binary_decoder_2b4b .out +----------+
//                                                  | out[1]  |   |                              |          |
//                                                  +------------>+ .en                          |          |
//                                                  |         |   +------------------------------+          |
//                                                  |         |                                             |
//                                                  |         |                                             |
//                                                  |         |   +------------------------------+          |
//                                                  |         +-->+ .sel                         |          |
//                                                  |             |                              | out[3:0] |
//                                                  |             |     binary_decoder_2b4b .out +----------+
//                                                  | out[0]      |                              |
//                                                  +------------>+ .en                          |
//                                                                +------------------------------+
//
class BinaryDecoder4b extends Component {
  val io = new Bundle {
    val output  = out Bits(16 bits)
    val en      = in  Bool
    val sel     = in  Bits(4 bits)
  }

  val binaryDecoder2bSel = new BinaryDecoder2b
  binaryDecoder2bSel.io.sel := io.sel(3 downto 2)
  binaryDecoder2bSel.io.en := io.en

  val enSel = binaryDecoder2bSel.io.output

  val width = 4
  val BinaryDecoder2bArray = Array.fill(width)(new BinaryDecoder2b)
  for (index <- 0 until width) {
    BinaryDecoder2bArray(index).io.sel := io.sel(1 downto 0)
    BinaryDecoder2bArray(index).io.en := enSel(index)

    val base = width * index
    io.output(base + 3 downto base) := BinaryDecoder2bArray(index).io.output
  }
}