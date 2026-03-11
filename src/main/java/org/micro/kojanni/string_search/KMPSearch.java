package org.micro.kojanni.string_search;

/**
 * Оптимизация по префиксу шаблона: использует префиксную функцию для оптимизации поиска подстроки.
 * Кнута-Морриса-Пратта (KMP).
 */
public class KMPSearch {

    /**
     * Построение массива LPS (Longest Prefix Suffix)
     *
     * @param pattern подстрока
     * @return массив сдвигов для каждого символа
     */
    public static int[] buildTable(String pattern) {
        int m = pattern.length();
        int[] lps = new int[m];

        int len = 0;
        int i = 1;

        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }

        return lps;
    }

    /**
     * Оптимизированный поиск по префиксу шаблона
     *
     * @param text    текст
     * @param pattern паттерн
     * @return индекс первого вхождения или -1
     */
    public static int search(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();

        if (m == 0) return 0;
        if (n < m) return -1;

        int[] lps = buildTable(pattern);

        int i = 0;
        int j = 0;

        while (i < n) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
            }

            if (j == m) {
                return i - j;
            } else if (i < n && text.charAt(i) != pattern.charAt(j)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }

        return -1;
    }
}
