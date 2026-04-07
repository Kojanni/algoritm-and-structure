package org.micro.kojanni.dynamic_programming;

import org.micro.kojanni.utils.Test;

public class IslandCounter {

    public static String run(String[] input) {
        int n = Integer.parseInt(input[0].trim());
        int[][] matrix = new int[n][n];
        
        for (int i = 0; i < n; i++) {
            String[] nums = input[i + 1].trim().split("\\s+");
            for (int j = 0; j < n; j++) {
                matrix[i][j] = Integer.parseInt(nums[j]);
            }
        }
        
        boolean[][] visited = new boolean[n][n];
        int islandCount = 0;
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 1 && !visited[i][j]) {
                    dfs(matrix, visited, i, j, n);
                    islandCount++;
                }
            }
        }
        
        return String.valueOf(islandCount);
    }
    
    private static void dfs(int[][] matrix, boolean[][] visited, int i, int j, int n) {
        if (i < 0 || i >= n || j < 0 || j >= n || visited[i][j] || matrix[i][j] == 0) {
            return;
        }
        
        visited[i][j] = true;
        
        // Проверяем 4 соседние клетки (вверх, вниз, влево, вправо)
        dfs(matrix, visited, i - 1, j, n);
        dfs(matrix, visited, i + 1, j, n);
        dfs(matrix, visited, i, j - 1, n);
        dfs(matrix, visited, i, j + 1, n);
    }

    public static void main(String[] args) {
        Test test = new Test(IslandCounter::run);
        test.run("src/test/java/org/micro/kojanni/dynamic_programming/Большой_остров/");
    }
}
