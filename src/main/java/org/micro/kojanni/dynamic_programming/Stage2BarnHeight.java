package org.micro.kojanni.dynamic_programming;

import org.micro.kojanni.utils.Test;

/**
 * Этап 2. Длина сарая.
 * Для каждой клетки вычисляет, сколько свободных клеток подряд находится "вверх" от неё.
 *
 * Вход:
 * - Первая строка: N M (размер матрицы, N строк, M столбцов, от 1 до 1000)
 * - Вторая строка: T (количество занятых клеток, от 0 до 10000)
 * - Следующие T строк: X Y (координаты занятых клеток, 0 <= X < N, 0 <= Y < M)
 *
 * Выход:
 * - Матрица N x M, где каждый элемент - количество свободных клеток вверх (включая текущую)
 * - N строк по M чисел, разделённых пробелами
 */
public class Stage2BarnHeight {

    public static String run(String[] input) {
        String[] dimensions = input[0].trim().split("\\s+");
        int n = Integer.parseInt(dimensions[0]); // количество строк
        int m = Integer.parseInt(dimensions[1]); // количество столбцов

        int t = Integer.parseInt(input[1].trim());

        // матрица занятости: true - занято, false - свободно
        boolean[][] blocked = new boolean[n][m];

        for (int i = 0; i < t; i++) {
            String[] coords = input[i + 2].trim().split("\\s+");
            int x = Integer.parseInt(coords[0]); // строка
            int y = Integer.parseInt(coords[1]); // столбец
            blocked[x][y] = true;
        }

        int[][] heights = new int[n][m];

        // Для каждого столбца считаем высоту
        for (int col = 0; col < m; col++) {
            int cnt = 0;
            for (int row = 0; row < n; row++) {
                if (blocked[row][col]) {
                    cnt = 0;
                    heights[row][col] = 0;
                } else {
                    cnt++;
                    heights[row][col] = cnt;
                }
            }
        }

        // Формируем вывод: n строк, в каждой m чисел
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < m; col++) {
                if (col > 0) sb.append(" ");
                sb.append(heights[row][col]);
            }
            if (row < n - 1) sb.append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Test test = new Test(Stage2BarnHeight::run);
        test.run("src/test/java/org/micro/kojanni/dynamic_programming/Длина_сарая/");
    }
}