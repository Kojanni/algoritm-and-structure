package org.micro.kojanni.dynamic_programming;

import org.micro.kojanni.utils.Test;
import java.util.Stack;

/**
 * Этап 4. Большой сарай.
 * Находит максимальную площадь прямоугольника из свободных клеток.
 * Объединяет решения этапов 2 и 3:
 * 1. Вычисляет высоты для каждой клетки (этап 2)
 * 2. Для каждой строки находит максимальный прямоугольник в гистограмме (этап 3)
 *
 * Вход:
 * - Первая строка: N M (N - строк, M - столбцов, от 1 до 1000)
 * - Вторая строка: T (количество занятых клеток, от 0 до 10000)
 * - Следующие T строк: X Y (координаты занятых клеток, 0 <= X < N, 0 <= Y < M)
 *
 * Выход:
 * - Максимальная площадь прямоугольника из свободных клеток
 */
public class Stage4BigBarn {

    public static String run(String[] input) {
        String[] dimensions = input[0].trim().split("\\s+");
        int rows = Integer.parseInt(dimensions[0]);    // N - количество строк
        int cols = Integer.parseInt(dimensions[1]);    // M - количество столбцов

        int t = Integer.parseInt(input[1].trim());

        // матрица занятости: true - занято, false - свободно
        boolean[][] blocked = new boolean[rows][cols];

        for (int i = 0; i < t; i++) {
            String[] coords = input[i + 2].trim().split("\\s+");
            int x = Integer.parseInt(coords[0]); // строка
            int y = Integer.parseInt(coords[1]); // столбец
            blocked[x][y] = true;
        }

        // вычисляем высоты для каждой клетки
        int[][] heights = calculateHeights(blocked, rows, cols);

        // находим максимальную площадь прямоугольника
        int maxArea = findMaxRectangle(heights, rows, cols);

        return String.valueOf(maxArea);
    }

    /**
     * Вычисляет для каждой клетки количество свободных клеток вверх (включая саму клетку).
     * Если клетка занята, высота = 0.
     *
     * @param blocked матрица занятости (rows x cols)
     * @param rows количество строк
     * @param cols количество столбцов
     * @return матрица высот (rows x cols)
     */
    private static int[][] calculateHeights(boolean[][] blocked, int rows, int cols) {
        int[][] heights = new int[rows][cols];

        for (int col = 0; col < cols; col++) {
            int cnt = 0;
            for (int row = 0; row < rows; row++) {
                if (blocked[row][col]) {
                    cnt = 0;
                    heights[row][col] = 0;
                } else {
                    cnt++;
                    heights[row][col] = cnt;
                }
            }
        }
        return heights;
    }

    /**
     * Находит максимальную площадь прямоугольника в матрице высот.
     * Для каждой строки применяет алгоритм поиска максимального прямоугольника в гистограмме.
     *
     * @param heights матрица высот (rows x cols)
     * @param rows количество строк
     * @param cols количество столбцов
     * @return максимальная площадь
     */
    private static int findMaxRectangle(int[][] heights, int rows, int cols) {
        int maxArea = 0;
        for (int row = 0; row < rows; row++) {
            int area = maxRectangleInHistogram(heights[row], cols);
            if (area > maxArea) {
                maxArea = area;
            }
        }
        return maxArea;
    }

    /**
     * Находит максимальную площадь прямоугольника в гистограмме.
     * Использует стек для эффективного поиска за O(n).
     *
     * @param heights массив высот (длина n)
     * @param n длина массива
     * @return максимальная площадь прямоугольника в гистограмме
     */
    private static int maxRectangleInHistogram(int[] heights, int n) {
        Stack<Integer> stack = new Stack<>();
        int maxArea = 0;

        for (int i = 0; i < n; i++) {
            // Пока текущая высота меньше высоты на вершине стека
            while (!stack.isEmpty() && heights[i] < heights[stack.peek()]) {
                int h = heights[stack.pop()];
                int width = stack.isEmpty() ? i : i - stack.peek() - 1;
                maxArea = Math.max(maxArea, h * width);
            }
            stack.push(i);
        }

        // Обрабатываем оставшиеся элементы в стеке
        while (!stack.isEmpty()) {
            int h = heights[stack.pop()];
            int width = stack.isEmpty() ? n : n - stack.peek() - 1;
            maxArea = Math.max(maxArea, h * width);
        }

        return maxArea;
    }

    public static void main(String[] args) {
        Test test = new Test(Stage4BigBarn::run);
        test.run("src/test/java/org/micro/kojanni/dynamic_programming/Большой_сарай/");
    }
}