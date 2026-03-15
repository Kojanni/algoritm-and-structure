package org.micro.kojanni.string_search.kmp_lesson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BaseSearchBenchmark {

    public static void main(String[] args) {
        List<TestCase> tests = new ArrayList<>();

        // Тест 1: случайные строки (короткие)
        tests.add(new TestCase("Random short", randomString(1000), randomString(5)));

        // Тест 2: случайные строки (длинные)
        tests.add(new TestCase("Random long", randomString(10000), randomString(5)));

        // Тест 3: много повторений (частые вхождения)
        String repeatedA = "a".repeat(10000);
        tests.add(new TestCase("Frequent pattern", repeatedA, "a".repeat(10)));

        // Тест 4: редкие вхождения в повторяющемся тексте
        tests.add(new TestCase("Rare pattern", repeatedA, "a".repeat(100)));

        // Тест 5: реальный текст (отрывок)
        String lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.";
        tests.add(new TestCase("Real text", lorem, "dolor"));

        // Тест 6: граничные случаи (пустой образец)
        tests.add(new TestCase("Empty pattern", lorem, ""));

        // Тест 7: образец отсутствует
        tests.add(new TestCase("No match", lorem, "abcdefgh"));

        System.out.println("Сравнительное тестирование алгоритмов поиска подстроки\n");
        System.out.printf("%-25s %12s %12s %12s %12s %10s%n",
                "Тест", "Naive(мкс)", "Automaton(мкс)", "KMP fast(мкс)", "KMP slow(мкс)", "Вхождений");
        System.out.println("----------------------------------------------------------------------------------------");

        for (TestCase test : tests) {
            List<Integer> expected = NaiveSearcher.search(test.text, test.pattern);
            List<Integer> autoRes = new FSM(test.pattern).search(test.text);
            List<Integer> kmpFastRes = new KMP(test.pattern).search(test.text);
            List<Integer> kmpSlowRes = new KMPSlow(test.pattern).search(test.text);

            if (!expected.equals(autoRes) || !expected.equals(kmpFastRes) || !expected.equals(kmpSlowRes)) {
                System.err.println("Ошибка: результаты не совпадают для теста " + test.name);
                continue;
            }

            int T = getIterationCount(test.text.length());
            long timeNaive = measure(() -> NaiveSearcher.search(test.text, test.pattern), T);
            long timeAuto = measure(() -> new FSM(test.pattern).search(test.text), T);
            long timeKmpFast = measure(() -> new KMP(test.pattern).search(test.text), T);
            long timeKmpSlow = measure(() -> new KMPSlow(test.pattern).search(test.text), T);

            System.out.printf("%-25s %12.3f %12.3f %12.3f %12.3f %10d%n",
                    test.name,
                    timeNaive / 1000.0,  // в микросекундах
                    timeAuto / 1000.0,
                    timeKmpFast / 1000.0,
                    timeKmpSlow / 1000.0,
                    expected.size());
        }
    }

    static class NaiveSearcher {
        static List<Integer> search(String text, String pattern) {
            List<Integer> result = new ArrayList<>();
            if (pattern.isEmpty()) {
                for (int i = 0; i <= text.length(); i++) result.add(i);
                return result;
            }
            int m = pattern.length();
            int n = text.length();
            for (int i = 0; i <= n - m; i++) {
                int j = 0;
                while (j < m && text.charAt(i + j) == pattern.charAt(j)) j++;
                if (j == m) result.add(i);
            }
            return result;
        }
    }

    record TestCase(String name, String text, String pattern) {

    }

    private static String randomString(int length) {
        Random rnd = new Random(123);
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append((char) ('A' + rnd.nextInt(26)));
        }
        return sb.toString();
    }

    private static int getIterationCount(int textLength) {
        if (textLength <= 1000) return 100_000;
        if (textLength <= 10_000) return 10_000;
        return 1000;
    }

    private static long measure(Runnable task, int iterations) {
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            task.run();
        }
        long end = System.nanoTime();
        return (end - start) / iterations;
    }

    interface Runnable {
        void run();
    }
}
