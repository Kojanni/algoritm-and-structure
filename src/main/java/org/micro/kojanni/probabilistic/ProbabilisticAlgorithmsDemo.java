package org.micro.kojanni.probabilistic;

import java.util.*;

/**
 * Демонстрация работы вероятностных алгоритмов.
 */
public class ProbabilisticAlgorithmsDemo {
    
    public static void main(String[] args) {
        demoBloomFilter();
        System.out.println("\n" + "=".repeat(70) + "\n");
        demoHyperLogLog();
        System.out.println("\n" + "=".repeat(70) + "\n");
        demoComparison();
    }
    
    /**
     * Демонстрация Bloom Filter.
     */
    private static void demoBloomFilter() {
        System.out.println("📊 BLOOM FILTER - Проверка принадлежности элемента множеству");
        System.out.println("─".repeat(70));
        
        // Создаем фильтр для 10000 элементов с вероятностью ложных срабатываний 1%
        BloomFilter<String> filter = new BloomFilter<>(10000, 0.01);
        
        System.out.println("Параметры фильтра:");
        System.out.println("  • Размер битового массива: " + filter.getSize() + " бит");
        System.out.println("  • Количество хеш-функций: " + filter.getHashFunctionCount());
        System.out.println("  • Память: " + (filter.getSize() / 8192.0) + " KB");
        System.out.println();
        
        // Генерируем датасет
        DatasetGenerator generator = new DatasetGenerator(42);
        List<String> emails = generator.generateEmails(10000, 8000);
        Set<String> uniqueEmails = new HashSet<>(emails);
        
        System.out.println("Добавляем " + uniqueEmails.size() + " уникальных email адресов...");
        for (String email : uniqueEmails) {
            filter.add(email);
        }
        
        System.out.println("✓ Добавлено элементов: " + filter.getElementCount());
        System.out.println("  Заполненность: " + String.format("%.2f%%", filter.getFillRatio() * 100));
        System.out.println("  Теоретическая вероятность FP: " + 
            String.format("%.4f%%", filter.getCurrentFalsePositiveProbability() * 100));
        System.out.println();
        
        // Проверяем элементы
        System.out.println("Проверка элементов:");
        
        // Проверяем существующие элементы
        int foundExisting = 0;
        for (String email : uniqueEmails) {
            if (filter.mightContain(email)) {
                foundExisting++;
            }
        }
        System.out.println("  • Найдено существующих: " + foundExisting + " из " + uniqueEmails.size() + 
            " (должно быть 100%)");
        
        // Проверяем несуществующие элементы
        int falsePositives = 0;
        int testCount = 5000;
        for (int i = 0; i < testCount; i++) {
            String testEmail = "nonexistent_" + i + "@test.com";
            if (filter.mightContain(testEmail)) {
                falsePositives++;
            }
        }
        
        double actualFPR = (double) falsePositives / testCount * 100;
        System.out.println("  • False Positives: " + falsePositives + " из " + testCount + 
            " (" + String.format("%.2f%%", actualFPR) + ")");
        
        System.out.println();
        System.out.println("💡 Вывод: Bloom Filter не дает false negatives (все существующие элементы найдены),");
        System.out.println("   но может давать false positives (~" + String.format("%.2f%%", actualFPR) + 
            " в данном случае).");
    }
    
    /**
     * Демонстрация HyperLogLog.
     */
    private static void demoHyperLogLog() {
        System.out.println("📊 HYPERLOGLOG - Подсчет уникальных элементов");
        System.out.println("─".repeat(70));
        
        // Создаем HyperLogLog с точностью 14
        HyperLogLog<String> hll = new HyperLogLog<>(14);
        
        System.out.println("Параметры HyperLogLog:");
        System.out.println("  • Точность (precision): " + hll.getPrecision());
        System.out.println("  • Количество регистров: " + hll.getRegisterCount());
        System.out.println("  • Память: " + (hll.getMemoryUsage() / 1024.0) + " KB");
        System.out.println("  • Теоретическая ошибка: ±" + 
            String.format("%.2f%%", hll.getStandardError() * 100));
        System.out.println();
        
        // Генерируем датасет с дубликатами
        DatasetGenerator generator = new DatasetGenerator(42);
        int totalElements = 1000000;
        int uniqueElements = 100000;
        
        System.out.println("Генерируем датасет:");
        System.out.println("  • Всего элементов: " + totalElements);
        System.out.println("  • Уникальных элементов: " + uniqueElements);
        
        List<String> urls = generator.generateUrls(totalElements, uniqueElements);
        Set<String> actualUnique = new HashSet<>(urls);
        
        System.out.println("  • Фактически уникальных: " + actualUnique.size());
        System.out.println();
        
        // Добавляем элементы в HyperLogLog
        System.out.println("Добавляем элементы в HyperLogLog...");
        long startTime = System.nanoTime();
        for (String url : urls) {
            hll.add(url);
        }
        long endTime = System.nanoTime();
        double timeMs = (endTime - startTime) / 1_000_000.0;
        
        long estimate = hll.estimate();
        int actual = actualUnique.size();
        double error = Math.abs((double) actual - estimate) / actual * 100;
        
        System.out.println("✓ Обработано за " + String.format("%.2f", timeMs) + " мс");
        System.out.println();
        
        System.out.println("Результаты:");
        System.out.println("  • Фактическое количество уникальных: " + actual);
        System.out.println("  • Оценка HyperLogLog: " + estimate);
        System.out.println("  • Абсолютная ошибка: " + Math.abs(actual - estimate));
        System.out.println("  • Относительная ошибка: " + String.format("%.2f%%", error));
        System.out.println();
        
        // Сравнение с точным подсчетом
        System.out.println("Сравнение с точным подсчетом (HashSet):");
        
        long exactMemory = actual * 50; // примерная оценка
        long hllMemory = hll.getMemoryUsage();
        
        System.out.println("  • Память HashSet: ~" + (exactMemory / 1024) + " KB");
        System.out.println("  • Память HyperLogLog: " + (hllMemory / 1024.0) + " KB");
        System.out.println("  • Экономия памяти: " + String.format("%.0fx", exactMemory / (double) hllMemory));
        System.out.println();
        
        System.out.println("💡 Вывод: HyperLogLog дает приближенную оценку (ошибка ~" + 
            String.format("%.2f%%", error) + "),");
        System.out.println("   но использует в " + String.format("%.0f", exactMemory / (double) hllMemory) + 
            " раз меньше памяти!");
    }
    
    /**
     * Сравнение алгоритмов.
     */
    private static void demoComparison() {
        System.out.println("📊 СРАВНЕНИЕ АЛГОРИТМОВ");
        System.out.println("─".repeat(70));
        
        DatasetGenerator generator = new DatasetGenerator(42);
        int datasetSize = 500000;
        int uniqueCount = 100000;
        
        List<String> dataset = generator.generateIpAddresses(datasetSize, uniqueCount);
        Set<String> actualUnique = new HashSet<>(dataset);
        
        System.out.println("Датасет: " + datasetSize + " элементов, " + actualUnique.size() + " уникальных");
        System.out.println();
        
        // HashSet (точный подсчет)
        long startExact = System.nanoTime();
        Set<String> exactSet = new HashSet<>(dataset);
        int exactCount = exactSet.size();
        long endExact = System.nanoTime();
        long exactMemory = exactCount * 50;
        
        // HyperLogLog
        long startHLL = System.nanoTime();
        HyperLogLog<String> hll = new HyperLogLog<>(14);
        for (String ip : dataset) {
            hll.add(ip);
        }
        long hllEstimate = hll.estimate();
        long endHLL = System.nanoTime();
        
        // Bloom Filter (для проверки принадлежности)
        long startBF = System.nanoTime();
        BloomFilter<String> bf = new BloomFilter<>(uniqueCount, 0.01);
        for (String ip : actualUnique) {
            bf.add(ip);
        }
        long endBF = System.nanoTime();
        
        System.out.println("┌─────────────────────┬──────────────┬──────────────┬──────────────┐");
        System.out.println("│ Метод               │ HashSet      │ HyperLogLog  │ Bloom Filter │");
        System.out.println("├─────────────────────┼──────────────┼──────────────┼──────────────┤");
        System.out.printf("│ Время обработки     │ %8.2f мс │ %8.2f мс │ %8.2f мс │%n",
            (endExact - startExact) / 1_000_000.0,
            (endHLL - startHLL) / 1_000_000.0,
            (endBF - startBF) / 1_000_000.0);
        System.out.printf("│ Память              │ ~%7d KB │ %8.2f KB │ %8.2f KB │%n",
            exactMemory / 1024,
            hll.getMemoryUsage() / 1024.0,
            bf.getSize() / 8192.0);
        System.out.printf("│ Результат           │ %12d │ %12d │ %12s │%n",
            exactCount, hllEstimate, "N/A");
        System.out.printf("│ Ошибка              │ %12s │ %11.2f%% │ %11.2f%% │%n",
            "0%",
            Math.abs((double) exactCount - hllEstimate) / exactCount * 100,
            bf.getCurrentFalsePositiveProbability() * 100);
        System.out.println("└─────────────────────┴──────────────┴──────────────┴──────────────┘");
        System.out.println();
        
        System.out.println("📌 Когда использовать каждый алгоритм:");
        System.out.println();
        System.out.println("HashSet:");
        System.out.println("  ✓ Когда нужна 100% точность");
        System.out.println("  ✓ Когда достаточно памяти");
        System.out.println("  ✓ Для небольших датасетов");
        System.out.println();
        System.out.println("HyperLogLog:");
        System.out.println("  ✓ Подсчет уникальных посетителей сайта");
        System.out.println("  ✓ Анализ больших логов");
        System.out.println("  ✓ Распределенные системы (можно сливать)");
        System.out.println("  ✓ Когда допустима погрешность ~2%");
        System.out.println();
        System.out.println("Bloom Filter:");
        System.out.println("  ✓ Проверка наличия элемента (быстрая фильтрация)");
        System.out.println("  ✓ Защита от спама/вредоносных URL");
        System.out.println("  ✓ Кеширование (проверка перед обращением к БД)");
        System.out.println("  ✓ Когда false positives допустимы, но false negatives - нет");
    }
}
