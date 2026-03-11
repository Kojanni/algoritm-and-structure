package org.micro.kojanni.string_search;

/**
 * Оптимизация по суффиксу текста (таблица хороших суффиксов):
 * если мы уже сравнили несколько символов и знаем, что часть текста совпала,
 * можно попытаться не начинать сравнение заново, а использовать суффикс.
 */
public class SuffixShiftSearch {

    public static int search(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();

        int i = 0;

        while (i <= n - m) {

            int j = 0;

            while (j < m && text.charAt(i + j) == pattern.charAt(j)) {
                j++;
            }

            if (j == m) {
                return i; // найдено
            }

            int shift = getSuffixShift(pattern, text, i, j);
            i += shift;
        }

        return -1;
    }

    private static int getSuffixShift(String pattern, String text, int start, int mismatchPos) {

        if (mismatchPos == 0) {
            return 1;
        }

        String matched = text.substring(start, start + mismatchPos);

        for (int len = mismatchPos - 1; len > 0; len--) {

            String suffix = matched.substring(mismatchPos - len);

            if (pattern.startsWith(suffix)) {
                return mismatchPos - len;
            }
        }

        return mismatchPos;
    }
}
