package org.micro.kojanni.probabilistic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BloomFilterTest {
    
    private DatasetGenerator generator;
    
    @BeforeEach
    void setUp() {
        generator = new DatasetGenerator(42);
    }
    
    @Test
    @DisplayName("Базовый тест: добавление и проверка элементов")
    void testBasicOperations() {
        BloomFilter<String> filter = new BloomFilter<>(1000, 0.01);

        filter.add("test1");
        filter.add("test2");
        filter.add("test3");

        assertTrue(filter.mightContain("test1"));
        assertTrue(filter.mightContain("test2"));
        assertTrue(filter.mightContain("test3"));
        
        assertEquals(3, filter.getElementCount());
    }
    
    @Test
    @DisplayName("Тест: элементы, которых нет, не должны находиться (с учетом false positives)")
    void testNoFalseNegatives() {
        BloomFilter<String> filter = new BloomFilter<>(1000, 0.01);
        
        Set<String> addedElements = new HashSet<>();
        for (int i = 0; i < 500; i++) {
            String element = "element_" + i;
            filter.add(element);
            addedElements.add(element);
        }

        for (String element : addedElements) {
            assertTrue(filter.mightContain(element), 
                "Bloom Filter не должен давать false negatives: " + element);
        }
    }
    
    @Test
    @DisplayName("Оценка false positive rate на малом датасете")
    void testFalsePositiveRateSmall() {
        int expectedElements = 1000;
        double targetFPR = 0.01;
        
        BloomFilter<String> filter = new BloomFilter<>(expectedElements, targetFPR);

        List<String> dataset = generator.generateEmails(expectedElements, expectedElements);
        Set<String> addedElements = new HashSet<>(dataset);
        
        for (String email : addedElements) {
            filter.add(email);
        }

        int falsePositives = 0;
        int testCount = 10000;
        
        for (int i = 0; i < testCount; i++) {
            String testEmail = "nonexistent_" + i + "@test.com";
            if (!addedElements.contains(testEmail) && filter.mightContain(testEmail)) {
                falsePositives++;
            }
        }
        
        double actualFPR = (double) falsePositives / testCount;
        
        System.out.println("=== Bloom Filter: Small Dataset Test ===");
        System.out.println("Expected elements: " + expectedElements);
        System.out.println("Target FPR: " + (targetFPR * 100) + "%");
        System.out.println("Actual FPR: " + (actualFPR * 100) + "%");
        System.out.println("Theoretical FPR: " + (filter.getCurrentFalsePositiveProbability() * 100) + "%");
        System.out.println("Filter info: " + filter);
        System.out.println();

        assertTrue(actualFPR < targetFPR * 3, 
            "Actual FPR (" + actualFPR + ") should be close to target (" + targetFPR + ")");
    }
    
    @Test
    @DisplayName("Оценка false positive rate на большом датасете")
    void testFalsePositiveRateLarge() {
        int expectedElements = 100000;
        double targetFPR = 0.01;
        
        BloomFilter<String> filter = new BloomFilter<>(expectedElements, targetFPR);

        List<String> dataset = generator.generateUrls(expectedElements, expectedElements);
        Set<String> addedElements = new HashSet<>(dataset);
        
        for (String url : addedElements) {
            filter.add(url);
        }

        int falsePositives = 0;
        int testCount = 50000;
        
        for (int i = 0; i < testCount; i++) {
            String testUrl = "https://nonexistent" + i + ".com/test";
            if (!addedElements.contains(testUrl) && filter.mightContain(testUrl)) {
                falsePositives++;
            }
        }
        
        double actualFPR = (double) falsePositives / testCount;
        
        System.out.println("=== Bloom Filter: Large Dataset Test ===");
        System.out.println("Expected elements: " + expectedElements);
        System.out.println("Target FPR: " + (targetFPR * 100) + "%");
        System.out.println("Actual FPR: " + (actualFPR * 100) + "%");
        System.out.println("Theoretical FPR: " + (filter.getCurrentFalsePositiveProbability() * 100) + "%");
        System.out.println("False positives: " + falsePositives + " out of " + testCount);
        System.out.println("Filter info: " + filter);
        System.out.println();
        
        assertTrue(actualFPR < targetFPR * 2, 
            "Actual FPR should be reasonably close to target");
    }
    
    @Test
    @DisplayName("Сравнение различных параметров Bloom Filter")
    void testDifferentParameters() {
        int elementCount = 10000;
        List<String> dataset = generator.generateIpAddresses(elementCount, elementCount);
        Set<String> addedElements = new HashSet<>(dataset);
        
        double[] targetFPRs = {0.001, 0.01, 0.05}; // 0.1%, 1%, 5%
        
        System.out.println("=== Bloom Filter: Parameter Comparison ===");
        System.out.println("Dataset size: " + elementCount);
        System.out.println();
        
        for (double targetFPR : targetFPRs) {
            BloomFilter<String> filter = new BloomFilter<>(elementCount, targetFPR);

            for (String ip : addedElements) {
                filter.add(ip);
            }

            int falsePositives = 0;
            int testCount = 10000;
            
            for (int i = 0; i < testCount; i++) {
                String testIp = "255.255." + (i / 256) + "." + (i % 256);
                if (!addedElements.contains(testIp) && filter.mightContain(testIp)) {
                    falsePositives++;
                }
            }
            
            double actualFPR = (double) falsePositives / testCount;
            
            System.out.printf("Target FPR: %.2f%% | Actual FPR: %.2f%% | Size: %d bits | Hash functions: %d | Memory: %.2f KB%n",
                targetFPR * 100, actualFPR * 100, filter.getSize(), 
                filter.getHashFunctionCount(), filter.getSize() / 8192.0);
        }
        System.out.println();
    }
    
    @Test
    @DisplayName("Тест производительности Bloom Filter")
    void testPerformance() {
        int elementCount = 1000000;
        BloomFilter<String> filter = new BloomFilter<>(elementCount, 0.01);
        
        List<String> dataset = generator.generateUserIds(elementCount, elementCount);

        long startAdd = System.nanoTime();
        for (String userId : dataset) {
            filter.add(userId);
        }
        long endAdd = System.nanoTime();

        long startCheck = System.nanoTime();
        int found = 0;
        for (String userId : dataset) {
            if (filter.mightContain(userId)) {
                found++;
            }
        }
        long endCheck = System.nanoTime();
        
        double addTimeMs = (endAdd - startAdd) / 1_000_000.0;
        double checkTimeMs = (endCheck - startCheck) / 1_000_000.0;
        
        System.out.println("=== Bloom Filter: Performance Test ===");
        System.out.println("Elements: " + elementCount);
        System.out.println("Add time: " + addTimeMs + " ms (" + (addTimeMs / elementCount * 1000) + " μs per element)");
        System.out.println("Check time: " + checkTimeMs + " ms (" + (checkTimeMs / elementCount * 1000) + " μs per element)");
        System.out.println("Memory usage: " + (filter.getSize() / 8192.0) + " KB");
        System.out.println("Found elements: " + found + " (should be " + elementCount + ")");
        System.out.println();
        
        assertEquals(elementCount, found, "All added elements should be found");
    }
    
    @Test
    @DisplayName("Тест очистки фильтра")
    void testClear() {
        BloomFilter<String> filter = new BloomFilter<>(100, 0.01);
        
        filter.add("test1");
        filter.add("test2");
        
        assertTrue(filter.mightContain("test1"));
        assertEquals(2, filter.getElementCount());
        
        filter.clear();
        
        assertEquals(0, filter.getElementCount());
        assertFalse(filter.mightContain("test1"));
        assertFalse(filter.mightContain("test2"));
    }
}
