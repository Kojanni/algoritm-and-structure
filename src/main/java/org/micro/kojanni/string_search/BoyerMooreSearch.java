package org.micro.kojanni.string_search;

import java.util.Arrays;

/**
 * Алгоритм Бойера-Мура: комбинирует таблицы плохих символов и хороших суффиксов для эффективного поиска подстроки.
 */
public class BoyerMooreSearch {

    private static final int ALPHABET_SIZE = 256;

    /**
     * Полный алгоритм Бойера-Мура с использованием таблиц плохих символов и хороших суффиксов.
     *
     * @param text    текст
     * @param pattern паттерн
     * @return индекс первого вхождения или -1
     */
    public static int search(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();
        if (m == 0) return 0;

        int[] badChar = preprocessBadCharacter(pattern);
        int[] goodSuffix = preprocessGoodSuffix(pattern);

        int s = 0;

        while (s <= n - m) {
            int j = m - 1;

            // сравнение справа налево
            while (j >= 0 && pattern.charAt(j) == text.charAt(s + j)) {
                j--;
            }

            if (j < 0) {
                return s;
            } else {
                int bcShift = j - badChar[text.charAt(s + j)];
                if (bcShift < 1) bcShift = 1;
                int gsShift = goodSuffix[j];
                s += Math.max(bcShift, gsShift);
            }
        }

        return -1;
    }


    // Предварительная обработка для "плохого символа"
    private static int[] preprocessBadCharacter(String pattern) {
        int[] badChar = new int[ALPHABET_SIZE];
        Arrays.fill(badChar, -1);
        for (int i = 0; i < pattern.length(); i++) {
            badChar[pattern.charAt(i)] = i;
        }
        return badChar;
    }

    // Предварительная обработка для "хорошего суффикса"
    private static int[] preprocessGoodSuffix(String pattern) {
        int m = pattern.length();
        int[] suff = new int[m];
        int[] goodSuffix = new int[m];

        // длины совпадающих суффиксов
        suff[m - 1] = m;
        for (int i = m - 2; i >= 0; i--) {
            int j = i;
            while (j >= 0 && pattern.charAt(j) == pattern.charAt(m - 1 - i + j)) {
                j--;
            }
            suff[i] = i - j;
        }

        // Инициализация значениями по умолчанию
        for (int i = 0; i < m; i++) {
            goodSuffix[i] = m;
        }

        // суффикс встретился раньше
        for (int i = m - 1; i >= 0; i--) {
            if (suff[i] == i + 1) {
                for (int j = 0; j < m - 1 - i; j++) {
                    if (goodSuffix[j] == m) {
                        goodSuffix[j] = m - 1 - i;
                    }
                }
            }
        }

        // суффикс совпадает с префиксом
        for (int i = 0; i < m - 1; i++) {
            goodSuffix[m - 1 - suff[i]] = m - 1 - i;
        }

        return goodSuffix;
    }
}
