//
// A wrapper for BinaryDecoder4b
//
module binary_decoder_4b16b
    (
    input logic     [3:0]   sel,
    input logic             en,
    output logic    [15:0]  out
    );

    BinaryDecoder4b BinaryDecoder4b_u0 (
        .io_output  ( out   ),
        .io_en      ( en    ),
        .io_sel     ( sel   )
    );
endmodule