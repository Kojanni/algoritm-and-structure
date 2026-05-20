package org.micro.kojanni.dynamic_programming;

import org.micro.kojanni.utils.Test;

/**
 * Этап 1. Маленький сарай. Решение задачи простым перебором.
 * Находит максимальный прямоугольник из нулей в матрице размером до 30x30.
 * 
 * Вход:
 * - Первая строка: N M (размер матрицы, от 1 до 30)
 * - Следующие M строк: по N элементов (0 или 1)
 * 
 * Выход:
 * - Максимальная площадь прямоугольника из нулей
 */
public class Stage1SmallBarn {

    public static String run(String[] input) {
        String[] dimensions = input[0].trim().split("\\s+");
        int n = Integer.parseInt(dimensions[0]);
        int m = Integer.parseInt(dimensions[1]);
        
        int[][] matrix = new int[m][n];
        for (int i = 0; i < m; i++) {
            String[] row = input[i + 1].trim().split("\\s+");
            for (int j = 0; j < n; j++) {
                matrix[i][j] = Integer.parseInt(row[j]);
            }
        }
        
        int maxArea = findMaxRectangleBruteForce(matrix, n, m);
        return String.valueOf(maxArea);
    }
    
    /**
     * Перебор всех возможных прямоугольников.
     * Для каждой пары углов проверяем, состоит ли прямоугольник из нулей.
     */
    private static int findMaxRectangleBruteForce(int[][] matrix, int n, int m) {
        int maxArea = 0;
        
        // Перебираем все возможные верхние левые углы
        for (int r1 = 0; r1 < m; r1++) {
            for (int c1 = 0; c1 < n; c1++) {
                // Перебираем все возможные нижние правые углы
                for (int r2 = r1; r2 < m; r2++) {
                    for (int c2 = c1; c2 < n; c2++) {
                        // Проверяем, все ли клетки в прямоугольнике равны 0
                        if (isRectangleFree(matrix, r1, c1, r2, c2)) {
                            int area = (r2 - r1 + 1) * (c2 - c1 + 1);
                            maxArea = Math.max(maxArea, area);
                        }
                    }
                }
            }
        }
        
        return maxArea;
    }
    
    /**
     * Проверяет, все ли клетки в прямоугольнике равны 0.
     */
    private static boolean isRectangleFree(int[][] matrix, int r1, int c1, int r2, int c2) {
        for (int i = r1; i <= r2; i++) {
            for (int j = c1; j <= c2; j++) {
                if (matrix[i][j] == 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Test test = new Test(Stage1SmallBarn::run);
        test.run("src/test/java/org/micro/kojanni/dynamic_programming/Маленький_сарай/");
    }
}
