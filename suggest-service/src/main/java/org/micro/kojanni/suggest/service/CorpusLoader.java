package org.micro.kojanni.suggest.service;

import org.micro.kojanni.suggest.config.CorpusProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

@Component
public class CorpusLoader {

    private static final Pattern WORD_BOUNDARY = Pattern.compile("[\\s\\p{Punct}]+");
    
    private final CorpusProperties properties;
    private final Set<String> stopWords;
    private final Set<String> technicalWords;

    public CorpusLoader(CorpusProperties properties) {
        this.properties = properties;
        this.stopWords = new HashSet<>(properties.getStopWords());
        this.technicalWords = new HashSet<>(properties.getTechnicalWords());
    }

    public Map<String, Long> buildFrequencyMap(String corpusResourcePattern) throws IOException {
        Map<String, Long> freqMap = new HashMap<>();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:" + corpusResourcePattern + "/*.txt");

        for (Resource resource : resources) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    processLine(line, freqMap);
                }
            }
        }

        // Фильтрация редких фраз
        freqMap.entrySet().removeIf(entry -> entry.getValue() < properties.getMinPhraseFrequency());

        return freqMap;
    }

    public Map<String, Long> buildFrequencyMapFromStream(InputStream inputStream) throws IOException {
        Map<String, Long> freqMap = new HashMap<>();
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                processLine(line, freqMap);
            }
        }
        
        // Фильтрация редких фраз
        freqMap.entrySet().removeIf(entry -> entry.getValue() < properties.getMinPhraseFrequency());
        
        return freqMap;
    }

    private void processLine(String line, Map<String, Long> freqMap) {
        String[] tokens = WORD_BOUNDARY.split(line.toLowerCase());
        
        // непустые, - технические слова
        List<String> allTokens = Arrays.stream(tokens)
                .filter(t -> !t.isEmpty() && !technicalWords.contains(t))
                .toList();

        //n-граммы
        for (int n = 1; n <= properties.getMaxNgram(); n++) {
            for (int i = 0; i <= allTokens.size() - n; i++) {
                List<String> ngramTokens = allTokens.subList(i, i + n);
                
                // Фильтрация по правилам:
                // 1. Для униграмм: пропускаем стоп-слова и короткие слова
                if (n == 1) {
                    String token = ngramTokens.get(0);
                    if (token.length() < properties.getMinTokenLength() || stopWords.contains(token)) {
                        continue;
                    }
                }
                
                // 2. Для биграмм и триграмм: 
                //    - Стоп-слова разрешены везде (даже короткие)
                //    - Короткие НЕ-стоп-слова разрешены, если НЕ в конце фразы
                //    - Последнее слово должно быть >= MIN_TOKEN_LEN (если не стоп-слово)
                if (n > 1) {
                    // Проверяем последнее слово
                    String lastToken = ngramTokens.get(ngramTokens.size() - 1);
                    if (lastToken.length() < properties.getMinTokenLength() && !stopWords.contains(lastToken)) {
                        continue; // Последнее слово короткое и не стоп-слово - пропускаем
                    }
                }
                
                String ngram = String.join(" ", ngramTokens);
                freqMap.merge(ngram, 1L, Long::sum);
            }
        }
    }
}