package org.micro.kojanni.bitwise_arithmetic;

public class BitCount {
    private static final int[] BIT_COUNT_CACHE = new int[256];

    static {
        for (int i = 0; i < 256; i++) {
            BIT_COUNT_CACHE[i] = countIterative(i);
        }
    }

    // Алгоритм 1: итеративный
    public static int countIterative(long n) {
        int count = 0;
        for (int i = 0; i < 64; i++) {
            if ((n & (1L << i)) != 0) {
                count++;
            }
        }
        return count;
    }
    // Алгоритм 2: алгоритм Кернигана
    public static int countKernighan(long n) {
        int count = 0;
        long v = n;
        while (v != 0) {
            count++;
            v = v & (v - 1);
        }
        return count;
    }

    // Алгоритм 3: кэширование по байтам
    public static int countCached(long n) {
        int count = 0;
        final long MASK = 0xFFL;// Маска младшего байта

        for (int i = 0; i < 8; i++) {
            int byteValue = (int) ((n >>> (i * 8)) & MASK);
            count += BIT_COUNT_CACHE[byteValue];
        }
        return count;
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            long num = Long.parseLong(args[0]);
            System.out.println("Введенное число: " + num);
            System.out.println("Итеративный подсчет: " + countIterative(num));
            System.out.println("Алгоритм Кернигана: " + countKernighan(num));
            System.out.println("Кэшированный подсчет: " + countCached(num));
        } else {
            long[] testNumbers = {0L, 1L, 3L, 0b101010L, 0xFL, 0xFFL, 0xFFFFL, 0xFFFFFFFFL, 0xFFFFFFFFFFFFFFFFL,
                    0x5555555555555555L, 0xAAAAAAAAAAAAAAAAL};
            for (long number : testNumbers) {
                String hexString = Long.toHexString(number).toUpperCase();
                String number16 = "0x" + "0".repeat(16 - hexString.length()) + hexString + "L";
                System.out.println("Число: " + number16 + " (двоичное: " + Long.toBinaryString(number) + ")");
                System.out.println("  Итеративный: " + countIterative(number));
                System.out.println("  Кернигана: " + countKernighan(number));
                System.out.println("  Кэшированный: " + countCached(number));
            }
        }
    }
}
