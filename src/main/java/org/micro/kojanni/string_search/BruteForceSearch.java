package org.micro.kojanni.string_search;

/**
 * Алгоритм полного перебора: последовательно сравнивает каждый символ текста с паттерном без оптимизаций.
 */
public class BruteForceSearch {

    /**
     * Ищет первое вхождение подстроки pattern в тексте text с помощью полного перебора.
     * @param text текст для поиска
     * @param pattern подстрока для поиска
     * @return индекс первого вхождения или -1, если не найдено
     */
    public static int search(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();

        if (m == 0) return 0;
        if (n < m) return -1;

        for (int i = 0; i <= n - m; i++) {
            int j;
            for (j = 0; j < m; j++) {
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    break;
                }
            }
            if (j == m) {
                return i;
            }
        }
        return -1;
    }
}
