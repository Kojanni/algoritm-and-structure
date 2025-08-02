package org.micro.kojanni.algebraic_algorithms;

/**
 * Написать класс умножения матриц, реализовать алгоритм возведения матрицы в степень через двоичное разложение показателя степени,
 * реализовать алгоритм поиска чисел Фибоначчи O(LogN) через умножение матриц, используя созданный класс.
 */
public class MatrixFibonacciNumberCalculator {
    private static long[][] fibMatrix = {{1, 1}, {1, 0}};

    public static long[][] multiply(long[][] a, long[][] b) {
        int n = a.length;
        long[][] result = new long[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return result;
    }

    public static long[][] power(long[][] matrix, long exponent) {
        int n = matrix.length;
        long[][] result = identityMatrix(n);
        while (exponent > 0) {
            if ((exponent & 1) == 1) {
                result = multiply(result, matrix);
            }
            matrix = multiply(matrix, matrix);
            exponent >>= 1;
        }
        return result;
    }

    private static long[][] identityMatrix(int n) {
        long[][] identity = new long[n][n];
        for (int i = 0; i < n; i++) {
            identity[i][i] = 1;
        }
        return identity;
    }


    public static long fibonacci(int n) {
        if (n == 0) return 0;

        long[][] result = power(fibMatrix, n - 1);

        return result[0][0];
    }

    public static void main(String[] args) {
        System.out.println(fibonacci(2));
        System.out.println(fibonacci(5));
        System.out.println(fibonacci(8));
    }
}
