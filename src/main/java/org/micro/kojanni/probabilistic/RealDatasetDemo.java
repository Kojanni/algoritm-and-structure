package org.micro.kojanni.probabilistic;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Демонстрация работы вероятностных алгоритмов на реальных датасетах.
 * 
 * Генерирует большой датасет (1-10 миллионов записей) и применяет к нему
 * Bloom Filter и HyperLogLog, сравнивая результаты с точными методами.
 */
public class RealDatasetDemo {
    
    private static final String DATASET_DIR = "datasets";
    private static final String WEB_LOGS_FILE = DATASET_DIR + "/web_server_logs.txt";
    private static final String IP_ADDRESSES_FILE = DATASET_DIR + "/ip_addresses.txt";
    private static final String URLS_FILE = DATASET_DIR + "/urls.txt";
    
    public static void main(String[] args) throws IOException {
        Files.createDirectories(Paths.get(DATASET_DIR));
        
        demoWebServerLogs();
        System.out.println();
        
        demoUniqueIPAddresses();
        System.out.println();
        
        demoMaliciousURLFilter();
    }
    
    private static void demoWebServerLogs() throws IOException {
        System.out.println("=== Анализ логов веб-сервера ===");
        
        int totalRequests = 5_000_000;
        int uniqueVisitors = 500_000;
        
        System.out.println("Генерация: " + totalRequests + " запросов, " + uniqueVisitors + " уникальных IP");
        DatasetGenerator generator = new DatasetGenerator(42);
        List<String> ipAddresses = generator.generateIpAddresses(totalRequests, uniqueVisitors);
        saveToFile(ipAddresses, WEB_LOGS_FILE);
        System.out.println("Сохранено в: " + WEB_LOGS_FILE);
        
        System.out.println("\nHashSet (точный подсчет):");
        long startExact = System.nanoTime();
        Set<String> uniqueIPs = new HashSet<>(ipAddresses);
        int exactCount = uniqueIPs.size();
        long endExact = System.nanoTime();
        double exactTimeMs = (endExact - startExact) / 1_000_000.0;
        long exactMemory = estimateMemory(exactCount);
        
        System.out.printf("  Результат: %d, Время: %.2f мс, Память: %d KB%n", 
            exactCount, exactTimeMs, exactMemory / 1024);
        
        System.out.println("\nHyperLogLog:");
        HyperLogLog<String> hll = new HyperLogLog<>(14);
        long startHLL = System.nanoTime();
        for (String ip : ipAddresses) {
            hll.add(ip);
        }
        long estimate = hll.estimate();
        long endHLL = System.nanoTime();
        double hllTimeMs = (endHLL - startHLL) / 1_000_000.0;
        double error = Math.abs((double) exactCount - estimate) / exactCount * 100;
        
        System.out.printf("  Результат: %d, Время: %.2f мс, Память: %.1f KB, Ошибка: %.2f%%%n",
            estimate, hllTimeMs, hll.getMemoryUsage() / 1024.0, error);
        System.out.printf("  Ускорение: %.1fx, Экономия памяти: %.0fx%n",
            exactTimeMs / hllTimeMs, exactMemory / (double) hll.getMemoryUsage());
    }
    
    private static void demoUniqueIPAddresses() throws IOException {
        System.out.println("=== Подсчет уникальных IP ===");
        
        int totalRecords = 10_000_000;
        int uniqueIPs = 1_000_000;
        
        DatasetGenerator generator = new DatasetGenerator(123);
        List<String> ips = generator.generateIpAddresses(totalRecords, uniqueIPs);
        saveToFile(ips, IP_ADDRESSES_FILE);
        long fileSize = new File(IP_ADDRESSES_FILE).length() / 1024 / 1024;
        System.out.println("Датасет: " + totalRecords + " записей, " + fileSize + " MB");
        
        long startExact = System.nanoTime();
        Set<String> uniqueSet = new HashSet<>(ips);
        int exactCount = uniqueSet.size();
        long endExact = System.nanoTime();
        double exactTimeMs = (endExact - startExact) / 1_000_000.0;
        System.out.printf("HashSet: %d уникальных, %.2f мс%n%n", exactCount, exactTimeMs);
        
        System.out.println("HyperLogLog (разные precision):");
        
        int[] precisions = {10, 12, 14, 16};
        for (int precision : precisions) {
            HyperLogLog<String> hll = new HyperLogLog<>(precision);
            long start = System.nanoTime();
            for (String ip : ips) {
                hll.add(ip);
            }
            long estimate = hll.estimate();
            long end = System.nanoTime();
            double timeMs = (end - start) / 1_000_000.0;
            double error = Math.abs((double) exactCount - estimate) / exactCount * 100;
            
            System.out.printf("  p=%2d: %8d (ошибка %.2f%%), %.2f мс, %d KB%n",
                precision, estimate, error, timeMs, hll.getMemoryUsage() / 1024);
        }
    }
    
    private static void demoMaliciousURLFilter() {
        System.out.println("=== Фильтрация вредоносных URL ===");
        
        int blacklistSize = 1_000_000;
        int testURLs = 10_000_000;
        
        DatasetGenerator generator = new DatasetGenerator(456);
        List<String> blacklist = generator.generateUrls(blacklistSize, blacklistSize);
        Set<String> blacklistSet = new HashSet<>(blacklist);
        System.out.println("Черный список: " + blacklistSize + " URL");
        
        double[] targetFPRs = {0.001, 0.01, 0.05};
        System.out.println("\nТестирование с разными FPR:");
        
        for (double targetFPR : targetFPRs) {
            BloomFilter<String> bloomFilter = new BloomFilter<>(blacklistSize, targetFPR);
            
            long startAdd = System.nanoTime();
            for (String url : blacklist) {
                bloomFilter.add(url);
            }
            long endAdd = System.nanoTime();
            double addTimeMs = (endAdd - startAdd) / 1_000_000.0;
            
            List<String> testUrls = generator.generateUrls(testURLs, testURLs - 1000);
            
            long startCheck = System.nanoTime();
            int falsePositives = 0;
            int truePositives = 0;
            int trueNegatives = 0;
            
            for (String url : testUrls) {
                boolean inBlacklist = blacklistSet.contains(url);
                boolean bloomSays = bloomFilter.mightContain(url);
                
                if (bloomSays && !inBlacklist) {
                    falsePositives++;
                } else if (bloomSays && inBlacklist) {
                    truePositives++;
                } else if (!bloomSays && !inBlacklist) {
                    trueNegatives++;
                }
            }
            long endCheck = System.nanoTime();
            double checkTimeMs = (endCheck - startCheck) / 1_000_000.0;
            double actualFPR = (double) falsePositives / (falsePositives + trueNegatives) * 100;
            
            System.out.printf("\nFPR %.1f%%: %d KB, %d хеш-функций%n",
                targetFPR * 100, bloomFilter.getSize() / 8 / 1024, bloomFilter.getHashFunctionCount());
            System.out.printf("  Добавление: %.2f мс, Проверка %dM URL: %.2f мс (%.0f URL/сек)%n",
                addTimeMs, testURLs / 1_000_000, checkTimeMs, testURLs / checkTimeMs * 1000);
            System.out.printf("  TP: %d, FP: %d, Факт. FPR: %.3f%%, Теор. FPR: %.3f%%%n",
                truePositives, falsePositives, actualFPR, 
                bloomFilter.getCurrentFalsePositiveProbability() * 100);
        }
    }
    
    private static void saveToFile(List<String> data, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String item : data) {
                writer.write(item);
                writer.newLine();
            }
        }
    }
    
    private static long estimateMemory(int elements) {
        return elements * 50L + 1024;
    }
}
