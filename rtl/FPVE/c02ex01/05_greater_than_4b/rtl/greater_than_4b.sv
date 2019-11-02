//
// A wrapper for GreaterThan4b
//
module greater_than_4b
    (
    input logic     [3:0]   a,
    input logic     [3:0]   b,
    output logic            gt
    );

    GreaterThan4b GreaterThan4b_u0 (
        .io_gt  ( gt    ),
        .io_a   ( a     ),
        .io_b   ( b     )
    );
endmodule