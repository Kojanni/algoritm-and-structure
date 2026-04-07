package org.micro.kojanni.dynamic_programming;

import org.micro.kojanni.utils.Test;

public class FiveEight {

    public static String run(String[] input) {
        int n = Integer.parseInt(input[0].trim());
        
        if (n == 1) {
            return "2";
        }
        if (n == 2) {
            return "4";
        }
        
        long[][] dp = new long[n + 1][4];
        // Индексы: 0 = 5 (1 раз), 1 = 5 (2 раза), 2 = 8 (1 раз), 3 = 8 (2 раза)
        
        // Начальное состояние: числа длины 1
        dp[1][0] = 1; // 5
        dp[1][2] = 1; // 8
        
        for (int i = 2; i <= n; i++) {
            // Добавляем 5 к числам, заканчивающимся на 5 (1 раз)
            dp[i][1] = dp[i - 1][0]; // 5 -> 55 (2 раза)
            
            // Добавляем 5 к числам, заканчивающимся на 5 (2 раза) - НЕЛЬЗЯ (будет 3)
            // Добавляем 5 к числам, заканчивающимся на 8 (1 раз)
            dp[i][0] = dp[i - 1][2]; // 8 -> 85 (1 раз)
            
            // Добавляем 5 к числам, заканчивающимся на 8 (2 раза)
            dp[i][0] += dp[i - 1][3]; // 88 -> 885 (1 раз)
            
            // Добавляем 8 к числам, заканчивающимся на 8 (1 раз)
            dp[i][3] = dp[i - 1][2]; // 8 -> 88 (2 раза)
            
            // Добавляем 8 к числам, заканчивающимся на 8 (2 раза) - НЕЛЬЗЯ (будет 3)
            // Добавляем 8 к числам, заканчивающимся на 5 (1 раз)
            dp[i][2] = dp[i - 1][0]; // 5 -> 58 (1 раз)
            
            // Добавляем 8 к числам, заканчивающимся на 5 (2 раза)
            dp[i][2] += dp[i - 1][1]; // 55 -> 558 (1 раз)
        }
        
        long result = dp[n][0] + dp[n][1] + dp[n][2] + dp[n][3];
        return String.valueOf(result);
    }

    public static void main(String[] args) {
        Test test = new Test(FiveEight::run);
        test.run("src/test/java/org/micro/kojanni/dynamic_programming/Пятью_восемь/");
    }
}
