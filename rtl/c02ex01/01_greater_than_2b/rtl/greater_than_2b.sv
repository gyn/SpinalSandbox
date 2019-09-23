//
// A wrapper for GreaterThan2b
//
module greater_than_2b
    (
    input logic     [1:0]   a,
    input logic     [1:0]   b,
    output logic            gt
    );

    GreaterThan2b GreaterThan2b_u0 (
        .io_gt  ( gt    ),
        .io_a   ( a     ),
        .io_b   ( b     )
    );
endmodule
