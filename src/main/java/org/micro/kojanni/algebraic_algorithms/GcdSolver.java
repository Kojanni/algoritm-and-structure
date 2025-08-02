package org.micro.kojanni.algebraic_algorithms;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 1. Алгоритм Евклида через вычитание
 * 2. Алгоритм Евклида через остаток
 * 3. Алгоритм Стейнца через битовые операции
 */
public class GcdSolver {

    public String subtraction(String[] args) {
        BigInteger x = new BigInteger(args[0]);
        BigInteger y = new BigInteger(args[1]);
        return String.valueOf(processSubtraction(x, y));
    }

    private BigInteger processSubtraction(BigInteger x, BigInteger y) {
        x = x.abs();
        y = y.abs();

        if (x.equals(BigInteger.ZERO)) return y;
        if (y.equals(BigInteger.ZERO)) return x;

        while (!x.equals(y)) {
            if (x.compareTo(y) > 0) {
                x = x.subtract(y);
            } else {
                y = y.subtract(x);
            }
        }

        return x;
    }

    public String remainder(String[] args) {
        BigInteger x = new BigInteger(args[0]);
        BigInteger y = new BigInteger(args[1]);
        return String.valueOf(processRemainder(x, y));
    }

    private BigInteger processRemainder(BigInteger x, BigInteger y) {
        x = x.abs();
        y = y.abs();

        if (x.equals(BigInteger.ZERO)) return y;
        if (y.equals(BigInteger.ZERO)) return x;
        if (x.equals(y)) return x;

        return x.compareTo(y) < 0 ?
                processSubtraction(x, y.mod(x)) :
                processSubtraction(y, x.mod(y));
    }

    /**
     * Алгоритм Стейнца через битовые операции
     */
    public String bitOperation(String[] args) {
        BigInteger x = new BigInteger(args[0]);
        BigInteger y = new BigInteger(args[1]);
        return String.valueOf(processBitOperation(x, y));
    }

    private BigInteger processBitOperation(BigInteger x, BigInteger y) {
        x = x.abs();
        y = y.abs();

        if (x.equals(BigInteger.ZERO)) return y;
        if (y.equals(BigInteger.ZERO)) return x;
        if (x.equals(y)) return x;

        if (x.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO) && y.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) {
            return BigInteger.valueOf(2).multiply(processBitOperation(x.divide(BigInteger.valueOf(2)), y.divide(BigInteger.valueOf(2))));
        } else if (x.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) {
            return processBitOperation(x.divide(BigInteger.valueOf(2)), y);
        } else if (y.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) {
            return processBitOperation(x, y.divide(BigInteger.valueOf(2)));
        } else if (x.compareTo(y) > 0) {
            return processBitOperation((x.subtract(y)).divide(BigInteger.valueOf(2)), y);
        } else {
            return processBitOperation((y.subtract(x)).divide(BigInteger.valueOf(2)), x);
        }
    }
}
