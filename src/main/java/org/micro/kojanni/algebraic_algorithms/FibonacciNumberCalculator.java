package org.micro.kojanni.algebraic_algorithms;

import java.math.BigInteger;

public class FibonacciNumberCalculator {

    public String recursive(String[] args) {
        BigInteger x = new BigInteger(args[0]);
        return String.valueOf(recursive(x));
    }

    private BigInteger recursive(BigInteger n) {
        if (n.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("Степень должна быть >= 0");
        }
        if (n.equals(BigInteger.ZERO)) {
            return BigInteger.ZERO;
        }
        if (n.equals(BigInteger.ONE) || n.equals(BigInteger.TWO)) {
            return BigInteger.ONE;
        }

        return recursive(n.subtract(BigInteger.ONE)).add(recursive(n.subtract(BigInteger.TWO)));
    }

    public String iterative(String[] args) {
        BigInteger x = new BigInteger(args[0]);
        return String.valueOf(iterative(x));
    }

    public static BigInteger iterative(BigInteger n) {
        if (n.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("Степень должна быть >= 0");
        }
        if (n.equals(BigInteger.ZERO)) return BigInteger.ZERO;
        if (n.equals(BigInteger.ONE) || n.equals(BigInteger.TWO)) return BigInteger.ONE;

        BigInteger f1 = BigInteger.ONE;
        BigInteger f2 = BigInteger.ONE;

        for (BigInteger i = BigInteger.valueOf(3);
             i.compareTo(n) <= 0;
             i = i.add(BigInteger.ONE)) {

            BigInteger sum = f1.add(f2);
            f1 = f2;
            f2 = sum;
        }

        return f2;
    }

    public String goldenSection(String[] args) {
        int x = Integer.parseInt(args[0]);
        return String.valueOf(goldenSection(x));
    }

    public static BigInteger goldenSection(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Степень должна быть >= 0");
        }
        if (n == 0) return BigInteger.ZERO;
        if (n == 1 || n == 2) return BigInteger.ONE;

        BigInteger a = BigInteger.ZERO;
        BigInteger b = BigInteger.ONE;
        BigInteger c;
        for (int i = 2; i <= n; i++) {
            c = a.add(b);
            a = b;
            b = c;
        }
        return b;
    }
}
