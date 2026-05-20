package org.micro.kojanni.probabilistic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class HyperLogLogTest {
    
    private DatasetGenerator generator;
    
    @BeforeEach
    void setUp() {
        generator = new DatasetGenerator(42);
    }
    
    @Test
    @DisplayName("Базовый тест: подсчет уникальных элементов")
    void testBasicCounting() {
        HyperLogLog<String> hll = new HyperLogLog<>(14);

        for (int i = 0; i < 1000; i++) {
            hll.add("element_" + i);
        }
        
        long estimate = hll.estimate();

        double error = Math.abs(1000.0 - estimate) / 1000.0;
        assertTrue(error < 0.05, "Error should be less than 5%");
    }
    
    @Test
    @DisplayName("Тест с дубликатами")
    void testWithDuplicates() {
        HyperLogLog<String> hll = new HyperLogLog<>(14);
        
        int uniqueCount = 5000;
        int totalCount = 50000;
        
        List<String> dataset = generator.generateEmails(totalCount, uniqueCount);
        
        for (String email : dataset) {
            hll.add(email);
        }
        
        long estimate = hll.estimate();
        double error = Math.abs((double) uniqueCount - estimate) / uniqueCount;

        assertTrue(error < hll.getStandardError() * 3, 
            "Error should be within 3 standard deviations");
    }
    
    @Test
    @DisplayName("Оценка точности на различных размерах датасета")
    void testAccuracyOnDifferentSizes() {
        int[] sizes = {100, 1000, 10000, 100000, 1000000};
        
        System.out.println("=== HyperLogLog: Accuracy on Different Sizes ===");
        System.out.println();
        
        for (int size : sizes) {
            HyperLogLog<String> hll = new HyperLogLog<>(14);
            
            List<String> dataset = generator.generateRandomStrings(size, size, 5, 20);
            Set<String> uniqueElements = new HashSet<>(dataset);
            
            for (String element : dataset) {
                hll.add(element);
            }
            
            long estimate = hll.estimate();
            int actualUnique = uniqueElements.size();
            double error = Math.abs((double) actualUnique - estimate) / actualUnique * 100;

            System.out.printf("Size: %7d | Actual: %7d | Estimated: %7d | Error: %6.2f%% | Memory: %5d bytes%n",
                size, actualUnique, estimate, error, hll.getMemoryUsage());
        }
        System.out.println();
    }
    
    @Test
    @DisplayName("Сравнение различных precision параметров")
    void testDifferentPrecisions() {
        int uniqueCount = 100000;
        List<String> dataset = generator.generateUrls(uniqueCount, uniqueCount);
        Set<String> uniqueElements = new HashSet<>(dataset);
        int actualUnique = uniqueElements.size();
        
        int[] precisions = {4, 8, 12, 14, 16};
        
        System.out.println("=== HyperLogLog: Precision Comparison ===");
        System.out.println("Actual unique elements: " + actualUnique);
        System.out.println();
        
        for (int precision : precisions) {
            HyperLogLog<String> hll = new HyperLogLog<>(precision);
            
            for (String url : dataset) {
                hll.add(url);
            }
            
            long estimate = hll.estimate();
            double error = Math.abs((double) actualUnique - estimate) / actualUnique * 100;
            
            System.out.printf("Precision: %2d | Registers: %5d | Memory: %6d B | Estimated: %7d | Error: %5.2f%% | Std Error: %5.2f%%%n",
                precision, hll.getRegisterCount(), hll.getMemoryUsage(), 
                estimate, error, hll.getStandardError() * 100);
        }
        System.out.println();
    }
    
    @Test
    @DisplayName("Тест слияния (merge) HyperLogLog")
    void testMerge() {
        HyperLogLog<String> hll1 = new HyperLogLog<>(14);
        HyperLogLog<String> hll2 = new HyperLogLog<>(14);
        HyperLogLog<String> hllCombined = new HyperLogLog<>(14);

        List<String> dataset1 = generator.generateIpAddresses(5000, 5000);
        List<String> dataset2 = generator.generateIpAddresses(5000, 5000);

        for (String ip : dataset1) {
            hll1.add(ip);
            hllCombined.add(ip);
        }
        
        for (String ip : dataset2) {
            hll2.add(ip);
            hllCombined.add(ip);
        }

        HyperLogLog<String> hllMerged = new HyperLogLog<>(14);
        for (String ip : dataset1) {
            hllMerged.add(ip);
        }
        hllMerged.merge(hll2);
        
        long estimateCombined = hllCombined.estimate();
        long estimateMerged = hllMerged.estimate();

        double difference = Math.abs(estimateCombined - estimateMerged);
        assertTrue(difference < estimateCombined * 0.05, 
            "Merged and combined estimates should be close");
    }
    
    @Test
    @DisplayName("Тест производительности HyperLogLog")
    void testPerformance() {
        int elementCount = 1000000;
        HyperLogLog<String> hll = new HyperLogLog<>(14);
        
        List<String> dataset = generator.generateUserIds(elementCount, elementCount / 2);
        Set<String> uniqueElements = new HashSet<>(dataset);

        long startAdd = System.nanoTime();
        for (String userId : dataset) {
            hll.add(userId);
        }
        long endAdd = System.nanoTime();
        long startEstimate = System.nanoTime();
        long estimate = hll.estimate();
        long endEstimate = System.nanoTime();
        
        double addTimeMs = (endAdd - startAdd) / 1_000_000.0;
        double estimateTimeMs = (endEstimate - startEstimate) / 1_000_000.0;
        
        int actualUnique = uniqueElements.size();
        double error = Math.abs((double) actualUnique - estimate) / actualUnique * 100;

        System.out.println("HyperLogLog: Performance Test");
        System.out.println("Total elements: " + elementCount);
        System.out.println("Actual unique: " + actualUnique);
        System.out.println("Estimated unique: " + estimate);
        System.out.println("Error: " + error + "%");
        System.out.println("Add time: " + addTimeMs + " ms (" + (addTimeMs / elementCount * 1000) + " μs per element)");
        System.out.println("Estimate time: " + estimateTimeMs + " ms");
        System.out.println("Memory usage: " + hll.getMemoryUsage() + " bytes (" + (hll.getMemoryUsage() / 1024.0) + " KB)");
        System.out.println();
        
        assertTrue(error < 5, "Error should be less than 5%");
    }
    
    @Test
    @DisplayName("Тест очистки HyperLogLog")
    void testClear() {
        HyperLogLog<String> hll = new HyperLogLog<>(14);
        
        for (int i = 0; i < 1000; i++) {
            hll.add("element_" + i);
        }
        
        assertTrue(hll.estimate() > 0);
        
        hll.clear();
        
        assertEquals(0, hll.estimate(), "Estimate should be 0 after clear");
    }
    
    @Test
    @DisplayName("Сравнение с точным подсчетом на реальных данных")
    void testComparisonWithExactCount() {
        int logEntries = 1000000;
        int uniqueIPs = 50000;
        
        List<String> logs = generator.generateIpAddresses(logEntries, uniqueIPs);

        long startExact = System.nanoTime();
        Set<String> exactSet = new HashSet<>(logs);
        int exactCount = exactSet.size();
        long endExact = System.nanoTime();

        long startHLL = System.nanoTime();
        HyperLogLog<String> hll = new HyperLogLog<>(14);
        for (String ip : logs) {
            hll.add(ip);
        }
        long estimate = hll.estimate();
        long endHLL = System.nanoTime();
        
        double exactTimeMs = (endExact - startExact) / 1_000_000.0;
        double hllTimeMs = (endHLL - startHLL) / 1_000_000.0;

        long exactMemory = exactSet.size() * 50; // примерная оценка для строк
        long hllMemory = hll.getMemoryUsage();
        
        double error = Math.abs((double) exactCount - estimate) / exactCount * 100;
        
        System.out.println("=== HyperLogLog vs Exact Count ===");
        System.out.println("Log entries: " + logEntries);
        System.out.println();
        System.out.println("Exact count: " + exactCount);
        System.out.println("Exact time: " + exactTimeMs + " ms");
        System.out.println("Exact memory: ~" + (exactMemory / 1024) + " KB");
        System.out.println();
        System.out.println("HLL estimate: " + estimate);
        System.out.println("HLL time: " + hllTimeMs + " ms");
        System.out.println("HLL memory: " + (hllMemory / 1024.0) + " KB");
        System.out.println();
        System.out.println("Error: " + error + "%");
        System.out.println("Time speedup: " + (exactTimeMs / hllTimeMs) + "x");
        System.out.println("Memory savings: " + (exactMemory / (double) hllMemory) + "x");
        System.out.println();
        
        assertTrue(error < 3, "Error should be less than 3%");
    }
}
