package org.micro.kojanni.algebraic_algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrimeNumber {

    public static int run(int n) {
        int count = 0;

        for (int i = 2; i <= n; i++) {
            if (isPrime(i)) {
                count++;
            }
        }
        return count;
    }

    private static boolean isPrime(int number) {
        for (int j = 2; j < number; j++) {
            if (number % j == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Реализовать алгоритм поиска простых чисел с оптимизациями поиска и делением только на простые числа, O(N * Sqrt(N) / Ln (N)).
     */
    public static List<Integer> getPrimes(int n) {
        List<Integer> primes = new ArrayList<>();
        if (n >= 2) primes.add(2);

        for (int num = 3; num <= n; num += 2) {//только нечет
            boolean isPrime = true;
            int sqrt = (int) Math.sqrt(num);
            for (int prime : primes) {
                if (prime > sqrt) break; //до корня из числа
                if (num % prime == 0) {
                    isPrime = false;
                    break;
                }
            }
            if (isPrime) primes.add(num);
        }

        return primes;
    }

    /**
     * . Реализовать алгоритм "Решето Эратосфена" для быстрого поиска простых чисел O(N Log Log N).
     */
    public static List<Integer> getPrimesSieveOfEratosthenes(int n) {
        boolean[] isPrime = new boolean[n + 1];
        Arrays.fill(isPrime, true);
        isPrime[0] = false;
        isPrime[1] = false;

        for (int i = 2; i * i <= n; i++) {
            if (isPrime[i]) {
                for (int j = i * i; j <= n; j += i) {
                    isPrime[j] = false;
                }
            }
        }

        List<Integer> primes = new ArrayList<>();
        for (int i = 2; i <= n; i++) {
            if (isPrime[i]) {
                primes.add(i);
            }
        }

        return primes;
    }

    /**
     * . Решето Эратосфена со сложностью O(N).
     */
    public static List<Integer> getPrimesSieveOfEratosthenesLinear(int n) {
        List<Integer> primes = new ArrayList<>();
        int[] smallestPrimeFactor = new int[n + 1];

        for (int i = 2; i <= n; i++) {
            if (smallestPrimeFactor[i] == 0) {
                smallestPrimeFactor[i] = i;
                primes.add(i);
            }

            for (int p : primes) {
                if (p > smallestPrimeFactor[i] || i * p > n) break;
                smallestPrimeFactor[i * p] = p;
            }
        }

        return primes;
    }

    public static void main(String[] args) {
        System.out.println(run(2));
        System.out.println(run(8));
        System.out.println(run(15));

        System.out.println(getPrimes(15));
        System.out.println(getPrimesSieveOfEratosthenes(15));
        System.out.println(getPrimesSieveOfEratosthenesLinear(20));
    }
}
