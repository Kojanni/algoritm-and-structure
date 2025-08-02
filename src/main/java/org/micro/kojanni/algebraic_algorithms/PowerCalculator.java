package org.micro.kojanni.algebraic_algorithms;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PowerCalculator {

    /**
     * 1. Через обычные итерации.
     */
    public String iterativePower(String[] args) {
        double x = Double.parseDouble(args[0]);
        long y = Long.parseLong(args[1]);
        double result = iterativePower(x, y);

        // Округление до 11 знаков для тестов
        long factor = (long) Math.pow(10, 11);
        result = Math.round(result * factor) / (double) factor;

        return String.valueOf(result);
    }

    public double iterativePower(double x, long n) {
        if (n < 0) {
            throw new IllegalArgumentException("Степень должна быть >= 0 для O(N) реализации");
        }

        double result = 1.0;
        for (long i = 0; i < n; i++) {
            result *= x;
        }
        return result;
    }

    /**
     * Через двоичное разложение показателя степени
     */
    public String subtractPower(String[] args) {
        double x = Double.parseDouble(args[0]);
        long y = Long.parseLong(args[1]);
        double result = subtractPower(x, y);

        // Округление до 11 знаков для тестов
        long factor = (long) Math.pow(10, 11);
        result = Math.round(result * factor) / (double) factor;

        return String.valueOf(result);
    }

    public static double subtractPower(double x, long n) {
        if (n == 0) return 1;

        if (n % 2 == 0) {
            double half = subtractPower(x, n / 2);
            return half * half;
        } else {
            return x * subtractPower(x, n - 1);
        }
    }


    /**
     * 2. Через степень двойки с домножением.
     */
    private static final int SCALE = 11;
    private static final int SCALE_IN = 15;

    public String binPower(String[] args) {
        BigDecimal x = new BigDecimal(args[0]);
        long y = Long.parseLong(args[1]);
        BigDecimal result = binPower(x, y);

        return  result.setScale(SCALE, RoundingMode.HALF_UP)
                .stripTrailingZeros()
                .toPlainString();
    }

    static BigDecimal binPower(BigDecimal a, long n) {
        if (n == 0) return BigDecimal.ONE;
        if (n < 0) return BigDecimal.ONE.divide(binPower(a, -n), SCALE_IN, RoundingMode.HALF_UP);

        BigDecimal result = BigDecimal.ONE;
        BigDecimal currentMultiplier = a;

        while (n > 0) {
            if (n % 2 == 1) {
                result = result.multiply(currentMultiplier).setScale(SCALE_IN, RoundingMode.HALF_UP);
            }
            currentMultiplier = currentMultiplier.multiply(currentMultiplier).setScale(SCALE_IN, RoundingMode.HALF_UP);
            n /= 2;
        }

        return result;
    }


}
