package org.micro.kojanni.algebraic_algorithms;

public class FibonacciNumberCalculator {

    public static int recursive(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Степень должна быть >= 0");
        }
        if (n == 0) return 0;
        if (n == 1 || n == 2) {
            return 1;
        }
        return recursive(n - 1) + recursive(n - 2);
    }

    public static int iterative(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Степень должна быть >= 0");
        }
        if (n == 0) return 0;
        if (n == 1 || n == 2) {
            return 1;
        }

        int f1 = 1;
        int f2 = 1;
        for (int i = 3; i <= n; i++) {
            int sum = f1 + f2;
            f1 = f2;
            f2 = sum;
        }

        return f2;
    }

    public static int goldenSection(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Степень должна быть >= 0");
        }
        if (n == 0) return 0;
        if (n == 1 || n == 2) {
            return 1;
        }

        double f = (1 + Math.sqrt(5)) / 2;

        return (int) Math.round(Math.pow(f, n) / Math.sqrt(5) - 1/2);
    }

    public static void main(String[] args) {
        System.out.println(recursive(2));
        System.out.println(recursive(5));
        System.out.println(recursive(8));

        System.out.println(iterative(2));
        System.out.println(iterative(5));
        System.out.println(iterative(8));

        System.out.println(goldenSection(2));
        System.out.println(goldenSection(5));
        System.out.println(goldenSection(8));
    }
}
