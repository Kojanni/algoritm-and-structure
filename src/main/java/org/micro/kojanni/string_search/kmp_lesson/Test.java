package org.micro.kojanni.string_search.kmp_lesson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.micro.kojanni.string_search.kmp_lesson.PrefixFunction.fastPrefixFunction;
import static org.micro.kojanni.string_search.kmp_lesson.PrefixFunction.slowPrefixFunction;

public class Test {
    public static void main(String[] args) {
        String pattern = "ABABC";
        String text = "ABABABCCBBABABCAB";

        System.out.println("Образец: \"" + pattern + "\"");
        System.out.println("Текст  : \"" + text + "\"\n");

        // Конечный автомат
        FSM automaton = new FSM(pattern);
        List<Integer> autoRes = automaton.search(text);
        System.out.println("Конечный автомат: вхождения на позициях " + autoRes);

        // Медленная префикс-функция
        String combined = pattern + "#" + text;
        int[] slowPi = slowPrefixFunction(combined);
        System.out.print("Медленная префикс-функция для \"" + combined + "\": ");
        System.out.println(Arrays.toString(slowPi));
        // Поиск вхождений через медленную префикс-функцию (аналогично КМП)
        List<Integer> slowKMP = findWithPi(combined, pattern, slowPi);
        System.out.println("  -> вхождения по медленной π: " + slowKMP);

        // Быстрая префикс-функция
        int[] fastPi = fastPrefixFunction(pattern);
        System.out.println("Быстрая префикс-функция для образца: " + Arrays.toString(fastPi));

        // КМП с быстрой префикс-функцией
        KMP kmp = new KMP(pattern);
        List<Integer> kmpRes = kmp.search(text);
        System.out.println("КМП (быстрый): вхождения на позициях " + kmpRes);
    }

    /** Вспомогательный метод: извлекает вхождения из префикс-функции строки pattern#text */
    private static List<Integer> findWithPi(String combined, String pattern, int[] pi) {
        List<Integer> res = new ArrayList<>();
        int m = pattern.length();
        for (int i = m + 1; i < combined.length(); i++) {
            if (pi[i] == m) {
                res.add(i - 2 * m);
            }
        }
        return res;
    }
}