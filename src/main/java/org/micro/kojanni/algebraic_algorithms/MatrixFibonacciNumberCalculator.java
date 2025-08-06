package org.micro.kojanni.algebraic_algorithms;

import java.math.BigInteger;

public class MatrixFibonacciNumberCalculator {
    private static final BigInteger[][] FIB_MATRIX = {
            {BigInteger.ONE, BigInteger.ONE},
            {BigInteger.ONE, BigInteger.ZERO}
    };

    public static BigInteger[][] multiply(BigInteger[][] a, BigInteger[][] b) {
        BigInteger[][] result = new BigInteger[2][2];
        result[0][0] = a[0][0].multiply(b[0][0]).add(a[0][1].multiply(b[1][0]));
        result[0][1] = a[0][0].multiply(b[0][1]).add(a[0][1].multiply(b[1][1]));
        result[1][0] = a[1][0].multiply(b[0][0]).add(a[1][1].multiply(b[1][0]));
        result[1][1] = a[1][0].multiply(b[0][1]).add(a[1][1].multiply(b[1][1]));
        return result;
    }

    public static BigInteger[][] power(BigInteger[][] matrix, int exponent) {
        BigInteger[][] result = identityMatrix();
        while (exponent > 0) {
            if ((exponent & 1) == 1) {
                result = multiply(result, matrix);
            }
            matrix = multiply(matrix, matrix);
            exponent >>= 1;
        }
        return result;
    }

    private static BigInteger[][] identityMatrix() {
        return new BigInteger[][] {
                {BigInteger.ONE, BigInteger.ZERO},
                {BigInteger.ZERO, BigInteger.ONE}
        };
    }

    public BigInteger fibonacci(int n) {
        if (n == 0) return BigInteger.ZERO;
        if (n == 1) return BigInteger.ONE;

        BigInteger[][] result = power(FIB_MATRIX, n - 1);
        return result[0][0];
    }

    public String fibonacci(String[] args) {
        int x = Integer.parseInt(args[0]);
        return String.valueOf(fibonacci(x));
    }
}