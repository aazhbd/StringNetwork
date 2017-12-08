struct matrixMulElements{
        int r1;
        int c1;
        int matA[100];
        int c2;
        int matB[100];
        int matC[100];
};

struct sortElement{
        int element[1000];
        int len;
};
program RPC_PROG{
        version RPC_VERS{
                string  get_path(void) = 1;
                sortElement  get_sort(sortElement) = 2;
                string  get_echo(string) = 3;
                string  get_check(string) = 4;
                matrixMulElements get_matMul(matrixMulElements) = 5;
        } = 1;
} = 0x31111111;

