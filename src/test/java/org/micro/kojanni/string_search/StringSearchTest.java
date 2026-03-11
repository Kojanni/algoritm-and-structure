package org.micro.kojanni.string_search;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StringSearchTest {

    @Test
    public void testBruteForce() {
        assertEquals(0, BruteForceSearch.search("hello", "hel"));
        assertEquals(1, BruteForceSearch.search("hello", "ell"));
        assertEquals(-1, BruteForceSearch.search("hello", "xyz"));
        assertEquals(0, BruteForceSearch.search("", ""));
        assertEquals(-1, BruteForceSearch.search("a", "ab"));
    }

    @Test
    public void testBadCharacter() {
        assertEquals(0, KMPSearch.search("hello", "hel"));
        assertEquals(1, KMPSearch.search("hello", "ell"));
        assertEquals(-1, KMPSearch.search("hello", "xyz"));
        assertEquals(0, KMPSearch.search("", ""));
        assertEquals(-1, KMPSearch.search("a", "ab"));
    }

    @Test
    public void testSuffixShiftSearch() {
        assertEquals(0, SuffixShiftSearch.search("hello", "hel"));
        assertEquals(1, SuffixShiftSearch.search("hello", "ell"));
        assertEquals(-1, SuffixShiftSearch.search("hello", "xyz"));
        assertEquals(0, SuffixShiftSearch.search("", ""));
        assertEquals(-1, SuffixShiftSearch.search("a", "ab"));
    }

    @Test
    public void testBoyerMoore() {
        assertEquals(0, BoyerMooreSearch.search("hello", "hel"));
        assertEquals(1, BoyerMooreSearch.search("hello", "ell"));
        assertEquals(-1, BoyerMooreSearch.search("hello", "xyz"));
        assertEquals(0, BoyerMooreSearch.search("", ""));
        assertEquals(-1, BoyerMooreSearch.search("a", "ab"));
    }

    @Test
    public void performanceTest() {
        StringBuilder textBuilder = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            textBuilder.append("abcdefghijklmnopqrstuvwxyz");
        }
        String text = textBuilder.toString();

        String pattern = "xyz";

        List<String> algorithms = Arrays.asList("BruteForceSearch", "KMPSearch", "SuffixShiftSearch", "BoyerMooreSearch");

        int T = 100; // Количество прогонов

        for (String alg : algorithms) {
            long totalTime = 0;
            for (int i = 0; i < T; i++) {
                long start = System.nanoTime();
                switch (alg) {
                    case "BruteForceSearch":
                        BruteForceSearch.search(text, pattern);
                        break;
                    case "KMPSearch":
                        KMPSearch.search(text, pattern);
                        break;
                    case "SuffixShiftSearch":
                        SuffixShiftSearch.search(text, pattern);
                        break;
                    case "BoyerMooreSearch":
                        BoyerMooreSearch.search(text, pattern);
                        break;
                }
                long end = System.nanoTime();
                totalTime += (end - start);
            }
            double avgTime = totalTime / (double) T;
            System.out.println(alg + " average time: " + avgTime + " ns");
        }
    }

    @Test
    public void benchmark() {
        Benchmark.runBenchmarks();
    }
}
