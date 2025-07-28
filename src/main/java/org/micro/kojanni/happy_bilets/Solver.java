package org.micro.kojanni.happy_bilets;

public class Solver {
    static final int MAX_N = 100;
    static final int MAX_S = 100;
    static long[][] memory = new long[MAX_N + 1][MAX_S + 1];

    public Solver() {
        super();
        //инициализация: -1 - не вычисленно
        for (int i = 0; i <= MAX_N; i++) {
            for (int j = 0; j <= MAX_S; j++) {
                memory[i][j] = -1L;
            }
        }

    }

    public String run(String[] args) {
        int n = Integer.parseInt(args[0]);
        return String.valueOf(tickets(n));
    }

    private Long tickets(int n) {
        long totalTickets = 0;

        int maxPossibleSum = 9 * n;
        for (int s = 0; s <= maxPossibleSum; s++) {
            long count = calc(n, s);
            totalTickets += count * count;  // Квадрат количества комбинаций
        }

        return totalTickets;
    }

    private long calc(int n, int s) {
        if (n < 0 || s < 0) {
            return 0;
        }
        if (memory[n][s] != -1) {
            return memory[n][s];
        }
        if (n == 0) {
            return (s == 0) ? 1 : 0;
        }

        long count = 0;
        for (int i = 0; i < 10; i++) {
            if (s - i >= 0) {
                count += calc(n - 1, s - i);
            }
        }
        memory[n][s] = count;

        return count;
    }

}
