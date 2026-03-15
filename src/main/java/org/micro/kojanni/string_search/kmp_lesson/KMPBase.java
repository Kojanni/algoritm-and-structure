package org.micro.kojanni.string_search.kmp_lesson;

import java.util.ArrayList;
import java.util.List;

/**
 * Алгоритм Кнута-Морриса-Пратта
 * Использует префикс-функцию для строки pattern + "#" + text.
 */
public abstract class KMPBase extends BaseSearch{
    private final int[] pi;

    public KMPBase(String pattern) {
        super(pattern);
        this.pi = getPi(pattern);
    }

    abstract int[] getPi(String pattern);

    @Override
    public List<Integer> search(String text) {
        List<Integer> occurrences = new ArrayList<>();
        if (pattern.isEmpty()) {
            for (int i = 0; i <= text.length(); i++) occurrences.add(i);
            return occurrences;
        }

        int q = 0; // количество совпавших символов
        for (int i = 0; i < text.length(); i++) {
            while (q > 0 && pattern.charAt(q) != text.charAt(i)) {
                q = pi[q - 1];
            }
            if (pattern.charAt(q) == text.charAt(i)) {
                q++;
            }
            if (q == pattern.length()) {
                occurrences.add(i - pattern.length() + 1);
                q = pi[q - 1]; // продолжаем поиск следующих вхождений
            }
        }
        return occurrences;
    }
}