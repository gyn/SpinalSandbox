//
// A wrapper for BinaryDecoder2b
//
module binary_decoder_2b4b
    (
    input logic     [1:0]   sel,
    input logic             en,
    output logic    [3:0]   out
    );

    BinaryDecoder2b BinaryDecoder2b_u0 (
        .io_output  ( out   ),
        .io_en      ( en    ),
        .io_sel     ( sel   )
    );
endmodule