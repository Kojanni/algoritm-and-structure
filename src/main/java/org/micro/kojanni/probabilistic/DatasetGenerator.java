package org.micro.kojanni.probabilistic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * Генератор датасетов для тестирования вероятностных алгоритмов.
 */
public class DatasetGenerator {
    
    private final Random random;
    
    public DatasetGenerator() {
        this.random = new Random(42); // фиксированный seed для воспроизводимости
    }
    
    public DatasetGenerator(long seed) {
        this.random = new Random(seed);
    }
    
    /**
     * Генерирует список email адресов.
     * 
     * @param totalCount общее количество элементов
     * @param uniqueCount количество уникальных элементов
     * @return список email адресов
     */
    public List<String> generateEmails(int totalCount, int uniqueCount) {
        if (uniqueCount > totalCount) {
            throw new IllegalArgumentException("uniqueCount cannot be greater than totalCount");
        }
        
        // Генерируем уникальные email'ы
        Set<String> uniqueEmails = new HashSet<>();
        String[] domains = {"gmail.com", "yahoo.com", "hotmail.com", "outlook.com", "mail.ru", "yandex.ru"};
        
        while (uniqueEmails.size() < uniqueCount) {
            String name = generateRandomString(5, 12);
            String domain = domains[random.nextInt(domains.length)];
            uniqueEmails.add(name + "@" + domain);
        }
        
        // Создаем список с повторениями
        List<String> emails = new ArrayList<>(uniqueEmails);
        List<String> result = new ArrayList<>(totalCount);
        
        for (int i = 0; i < totalCount; i++) {
            result.add(emails.get(random.nextInt(emails.size())));
        }
        
        return result;
    }
    
    /**
     * Генерирует список URL адресов.
     */
    public List<String> generateUrls(int totalCount, int uniqueCount) {
        if (uniqueCount > totalCount) {
            throw new IllegalArgumentException("uniqueCount cannot be greater than totalCount");
        }
        
        Set<String> uniqueUrls = new HashSet<>();
        String[] protocols = {"http://", "https://"};
        String[] domains = {"example.com", "test.org", "demo.net", "sample.io", "website.ru"};
        String[] paths = {"/api/users", "/products", "/blog", "/about", "/contact", "/search", "/admin"};
        
        while (uniqueUrls.size() < uniqueCount) {
            String protocol = protocols[random.nextInt(protocols.length)];
            String domain = domains[random.nextInt(domains.length)];
            String path = paths[random.nextInt(paths.length)];
            String params = random.nextBoolean() ? "?id=" + random.nextInt(10000) : "";
            uniqueUrls.add(protocol + domain + path + params);
        }
        
        List<String> urls = new ArrayList<>(uniqueUrls);
        List<String> result = new ArrayList<>(totalCount);
        
        for (int i = 0; i < totalCount; i++) {
            result.add(urls.get(random.nextInt(urls.size())));
        }
        
        return result;
    }
    
    /**
     * Генерирует список IP адресов.
     */
    public List<String> generateIpAddresses(int totalCount, int uniqueCount) {
        if (uniqueCount > totalCount) {
            throw new IllegalArgumentException("uniqueCount cannot be greater than totalCount");
        }
        
        Set<String> uniqueIps = new HashSet<>();
        
        while (uniqueIps.size() < uniqueCount) {
            int a = random.nextInt(256);
            int b = random.nextInt(256);
            int c = random.nextInt(256);
            int d = random.nextInt(256);
            uniqueIps.add(a + "." + b + "." + c + "." + d);
        }
        
        List<String> ips = new ArrayList<>(uniqueIps);
        List<String> result = new ArrayList<>(totalCount);
        
        for (int i = 0; i < totalCount; i++) {
            result.add(ips.get(random.nextInt(ips.size())));
        }
        
        return result;
    }
    
    /**
     * Генерирует список пользовательских ID.
     */
    public List<String> generateUserIds(int totalCount, int uniqueCount) {
        if (uniqueCount > totalCount) {
            throw new IllegalArgumentException("uniqueCount cannot be greater than totalCount");
        }
        
        Set<String> uniqueIds = new HashSet<>();
        
        while (uniqueIds.size() < uniqueCount) {
            uniqueIds.add("user_" + UUID.randomUUID().toString());
        }
        
        List<String> ids = new ArrayList<>(uniqueIds);
        List<String> result = new ArrayList<>(totalCount);
        
        for (int i = 0; i < totalCount; i++) {
            result.add(ids.get(random.nextInt(ids.size())));
        }
        
        return result;
    }
    
    /**
     * Генерирует список случайных строк.
     */
    public List<String> generateRandomStrings(int totalCount, int uniqueCount, int minLength, int maxLength) {
        if (uniqueCount > totalCount) {
            throw new IllegalArgumentException("uniqueCount cannot be greater than totalCount");
        }
        
        Set<String> uniqueStrings = new HashSet<>();
        
        while (uniqueStrings.size() < uniqueCount) {
            uniqueStrings.add(generateRandomString(minLength, maxLength));
        }
        
        List<String> strings = new ArrayList<>(uniqueStrings);
        List<String> result = new ArrayList<>(totalCount);
        
        for (int i = 0; i < totalCount; i++) {
            result.add(strings.get(random.nextInt(strings.size())));
        }
        
        return result;
    }
    
    /**
     * Генерирует случайную строку заданной длины.
     */
    private String generateRandomString(int minLength, int maxLength) {
        int length = minLength + random.nextInt(maxLength - minLength + 1);
        StringBuilder sb = new StringBuilder(length);
        
        for (int i = 0; i < length; i++) {
            char c = (char) ('a' + random.nextInt(26));
            sb.append(c);
        }
        
        return sb.toString();
    }
    
    /**
     * Сохраняет датасет в файл.
     */
    public void saveToFile(List<String> data, Path filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            for (String item : data) {
                writer.write(item);
                writer.newLine();
            }
        }
    }
    
    /**
     * Создает статистику по датасету.
     */
    public DatasetStats getStats(List<String> data) {
        Set<String> unique = new HashSet<>(data);
        
        Map<String, Integer> frequency = new HashMap<>();
        for (String item : data) {
            frequency.merge(item, 1, Integer::sum);
        }
        
        int maxFrequency = frequency.values().stream().mapToInt(Integer::intValue).max().orElse(0);
        int minFrequency = frequency.values().stream().mapToInt(Integer::intValue).min().orElse(0);
        double avgFrequency = frequency.values().stream().mapToInt(Integer::intValue).average().orElse(0);
        
        return new DatasetStats(
            data.size(),
            unique.size(),
            minFrequency,
            maxFrequency,
            avgFrequency
        );
    }
    
    /**
     * Статистика датасета.
     */
    public static class DatasetStats {
        public final int totalElements;
        public final int uniqueElements;
        public final int minFrequency;
        public final int maxFrequency;
        public final double avgFrequency;
        
        public DatasetStats(int totalElements, int uniqueElements, int minFrequency, 
                          int maxFrequency, double avgFrequency) {
            this.totalElements = totalElements;
            this.uniqueElements = uniqueElements;
            this.minFrequency = minFrequency;
            this.maxFrequency = maxFrequency;
            this.avgFrequency = avgFrequency;
        }
        
        @Override
        public String toString() {
            return String.format(
                "Dataset Stats:\n" +
                "  Total elements: %d\n" +
                "  Unique elements: %d\n" +
                "  Min frequency: %d\n" +
                "  Max frequency: %d\n" +
                "  Avg frequency: %.2f",
                totalElements, uniqueElements, minFrequency, maxFrequency, avgFrequency
            );
        }
    }
}
