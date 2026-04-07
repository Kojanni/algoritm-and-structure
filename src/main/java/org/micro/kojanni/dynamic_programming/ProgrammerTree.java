package org.micro.kojanni.dynamic_programming;

import org.micro.kojanni.utils.Test;

public class ProgrammerTree {

    public static String run(String[] input) {
        int n = Integer.parseInt(input[0].trim());
        int[][] tree = new int[n][];
        
        for (int i = 0; i < n; i++) {
            String[] nums = input[i + 1].trim().split("\\s+");
            tree[i] = new int[i + 1];
            for (int j = 0; j <= i; j++) {
                tree[i][j] = Integer.parseInt(nums[j]);
            }
        }
        
        // DP: максимальная сумма до каждой позиции
        int[][] dp = new int[n][];
        for (int i = 0; i < n; i++) {
            dp[i] = new int[i + 1];
        }
        
        dp[0][0] = tree[0][0];
        
        for (int i = 1; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                int maxPrev = Integer.MIN_VALUE;
                
                // Можем прийти из позиции j-1 или j предыдущего уровня
                if (j > 0) {
                    maxPrev = Math.max(maxPrev, dp[i - 1][j - 1]);
                }
                if (j < i) {
                    maxPrev = Math.max(maxPrev, dp[i - 1][j]);
                }
                
                dp[i][j] = tree[i][j] + maxPrev;
            }
        }
        
        // Находим максимум на последнем уровне
        int maxSum = Integer.MIN_VALUE;
        for (int j = 0; j < n; j++) {
            maxSum = Math.max(maxSum, dp[n - 1][j]);
        }
        
        return String.valueOf(maxSum);
    }

    public static void main(String[] args) {
        Test test = new Test(ProgrammerTree::run);
        test.run("src/test/java/org/micro/kojanni/dynamic_programming/Елочка_программиста/");
    }
}
