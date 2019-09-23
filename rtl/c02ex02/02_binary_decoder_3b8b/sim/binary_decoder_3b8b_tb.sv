`timescale 1ns/1ns


module binary_decoder_3b8b_tb
    #(
    parameter   N = 3,
    parameter   T = 20
    );
    logic   [N-1:0]     sel;
    logic               en;
    logic   [2**N-1:0]  out;

    binary_decoder_3b8b binary_decoder_3b8b_u0
        (
        .sel    ( sel ),
        .en     ( en  ),
        .out    ( out )
        );

    initial
    begin
        en  = 1'b0;
        sel = '0;

        for (int i = 0; i < 2**N; i++)
        begin
            # T
            en  = 1'b0;
            sel = N'(i);
        end

        for (int i = 0; i < 2**N; i++)
        begin
            # T
            en = 1'b1;
            sel = N'(i);
        end

        # T
        $stop;
    end
endmodule