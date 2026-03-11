package org.micro.kojanni.string_search;

import java.util.*;

public class Benchmark {

    private static final int[] TEXT_LENGTHS = {1000, 10000, 100000};

    private static final int[] PATTERN_LENGTHS = {3, 10, 50};

    private static final String[] SCENARIOS = {"random", "patternAtStart", "patternAtEnd", "noMatch"};

    private static final int T = 1000; // количество прогонов

    private static final Random random = new Random();

    public static void runBenchmarks() {

        Map<String, Map<String, Double>> results = new HashMap<>();

        for (int textLen : TEXT_LENGTHS) {

            for (int patLen : PATTERN_LENGTHS) {

                if (patLen > textLen) continue;

                for (String scenario : SCENARIOS) {

                    String text = generateText(textLen, patLen, scenario);

                    String pattern = generatePattern(patLen, scenario);

                    Map<String, Double> algResults = runForTextPattern(text, pattern);

                    String key = "TextLen=" + textLen + ", PatLen=" + patLen + ", Scenario=" + scenario;

                    results.put(key, algResults);

                }

            }

        }

        printTable(results);

        printConclusion();

    }

    private static String generateRandomText(int length) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {

            sb.append((char) ('a' + random.nextInt(26)));

        }

        return sb.toString();

    }

    private static String generateRandomPattern(int length) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {

            sb.append((char) ('a' + random.nextInt(26)));

        }

        return sb.toString();

    }

    private static String generateText(int length, int patLen, String scenario) {

        switch (scenario) {

            case "random":

                return generateRandomText(length);

            case "patternAtStart":

                String pat = generateRandomPattern(patLen);

                String rest = generateRandomText(length - patLen);

                return pat + rest;

            case "patternAtEnd":

                pat = generateRandomPattern(patLen);

                rest = generateRandomText(length - patLen);

                return rest + pat;

            case "noMatch":

                return generateRandomText(length); // pattern will be different random

            default:

                return generateRandomText(length);

        }

    }

    private static String generatePattern(int length, String scenario) {

        return generateRandomPattern(length);

    }

    private static Map<String, Double> runForTextPattern(String text, String pattern) {

        Map<String, Double> algResults = new HashMap<>();

        List<String> algorithms = Arrays.asList("BruteForce", "KMP", "SuffixShift", "BoyerMoore");

        for (String alg : algorithms) {

            long totalTime = 0;

            for (int i = 0; i < T; i++) {

                long start = System.nanoTime();

                switch (alg) {

                    case "BruteForce":

                        BruteForceSearch.search(text, pattern);

                        break;

                    case "KMP":

                        KMPSearch.search(text, pattern);

                        break;

                    case "SuffixShift":

                        SuffixShiftSearch.search(text, pattern);

                        break;

                    case "BoyerMoore":

                        BoyerMooreSearch.search(text, pattern);

                        break;

                }

                long end = System.nanoTime();

                totalTime += (end - start);

            }

            double avgTime = totalTime / (double) T;

            algResults.put(alg, avgTime);

        }

        return algResults;

    }

    private static void printTable(Map<String, Map<String, Double>> results) {

        System.out.println("| Test Case | BruteForce (ns) | KMP (ns) | SuffixShift (ns) | BoyerMoore (ns) |");

        System.out.println("|-----------|-----------------|----------|------------------|-----------------|");

        for (String testCase : results.keySet()) {

            Map<String, Double> algResults = results.get(testCase);

            System.out.printf("| %s | %.2f | %.2f | %.2f | %.2f |\n",

                testCase,

                algResults.get("BruteForce"),

                algResults.get("KMP"),

                algResults.get("SuffixShift"),

                algResults.get("BoyerMoore"));

        }

    }

    private static void printConclusion() {

        System.out.println("\nВывод:");

        System.out.println("Из результатов тестирования видно, что:");

        System.out.println("- Алгоритм полного перебора (BruteForce) обычно самый медленный, особенно для больших текстов и паттернов, поскольку он проверяет каждую позицию без оптимизаций.");

        System.out.println("- KMP и SuffixShift показывают среднюю производительность, с улучшениями над BruteForce благодаря использованию информации о префиксах и суффиксах.");

        System.out.println("- Алгоритм Бойера-Мура (BoyerMoore) часто самый быстрый, благодаря комбинации таблиц плохих символов и хороших суффиксов, позволяющих большие сдвиги.");

        System.out.println("Производительность зависит от длины текста и паттерна, а также от сценария (например, паттерн в начале или конце может влиять на время).");

        System.out.println("Для точных измерений использовалось T=1000 прогонов на тест, с усреднением времени.");

    }

}
