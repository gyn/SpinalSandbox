`timescale 1ns/1ns

module greater_than_2b_tb
    #(
    parameter   N = 2,
    parameter   T = 20
    );
    logic [N - 1:0] a;
    logic [N - 1:0] b;
    logic           gt;

    greater_than_2b greater_than_2b_u0
        (
        .a      ( a     ),
        .b      ( b     ),
        .gt     ( gt    )
        );

    initial
    begin
        a = '0;
        b = '0;

        for (int i = 0; i < 2**N; i++)
        begin
            for (int j = 0; j < 2**N; j++)
            begin
                # T
                a = N'(j);
                b = N'(i);
            end
        end

        # T
        $stop;
    end
endmodule