package org.micro.kojanni.string_search.kmp_lesson;

/**
 * Алгоритм Кнута-Морриса-Пратта
 * Использует префикс-функцию для строки pattern + "#" + text.
 */
public class KMP extends KMPBase{

    public KMP(String pattern) {
        super(pattern);
    }

    @Override
    int[] getPi(String pattern) {
        return PrefixFunction.fastPrefixFunction(pattern);
    }
}