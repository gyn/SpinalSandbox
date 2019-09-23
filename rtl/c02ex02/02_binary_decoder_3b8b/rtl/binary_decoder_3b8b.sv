//
// A wrapper for BinaryDecoder3b
//
module binary_decoder_3b8b
    (
    input logic     [2:0]   sel,
    input logic             en,
    output logic    [7:0]   out
    );

    BinaryDecoder3b BinaryDecoder3b_u0 (
        .io_output  ( out   ),
        .io_en      ( en    ),
        .io_sel     ( sel   )
    );
endmodule