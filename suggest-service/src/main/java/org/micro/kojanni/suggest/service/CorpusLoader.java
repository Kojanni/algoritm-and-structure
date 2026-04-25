package org.micro.kojanni.suggest.service;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

@Component
public class CorpusLoader {

    private static final Pattern WORD_BOUNDARY = Pattern.compile("[\\s\\p{Punct}]+");
    private static final Set<String> STOP_WORDS = Set.of("и", "в", "на", "с", "к", "у", "а", "но", "за", "по", "из", "о");
    private static final int MIN_TOKEN_LEN = 3;
    private static final int MIN_PHRASE_FREQ = 2;
    private static final int MAX_NGRAM = 3; // уни-, би-, триграммы

    public Map<String, Long> buildFrequencyMap(String corpusDirPath) throws IOException {
        Map<String, Long> freqMap = new HashMap<>();
        List<Path> txtFiles = Files.list(Paths.get(corpusDirPath))
                .filter(p -> p.toString().endsWith(".txt"))
                .toList();

        for (Path file : txtFiles) {
            try (BufferedReader reader = Files.newBufferedReader(file)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    processLine(line, freqMap);
                }
            }
        }

        // Фильтрация редких фраз
        freqMap.entrySet().removeIf(entry -> entry.getValue() < MIN_PHRASE_FREQ);

        return freqMap;
    }

    private void processLine(String line, Map<String, Long> freqMap) {
        String[] tokens = WORD_BOUNDARY.split(line.toLowerCase());
        List<String> filteredTokens = Arrays.stream(tokens)
                .filter(t -> t.length() >= MIN_TOKEN_LEN && !STOP_WORDS.contains(t))
                .toList();

        // Генерация n-грамм
        for (int n = 1; n <= MAX_NGRAM; n++) {
            for (int i = 0; i <= filteredTokens.size() - n; i++) {
                String ngram = String.join(" ", filteredTokens.subList(i, i + n));
                freqMap.merge(ngram, 1L, Long::sum);
            }
        }
    }
}