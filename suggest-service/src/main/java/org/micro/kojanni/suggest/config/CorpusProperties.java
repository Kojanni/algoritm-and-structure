package org.micro.kojanni.suggest.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Конфигурация параметров обработки корпуса текстов
 */
@Component
@ConfigurationProperties(prefix = "corpus.processing")
@Getter
@Setter
public class CorpusProperties {
    
    /**
     * Минимальная длина токена
     */
    private int minTokenLength = 3;
    
    /**
     * Минимальная частота фразы для включения в индекс
     */
    private int minPhraseFrequency = 2;
    
    /**
     * Максимальная длина n-граммы
     */
    private int maxNgram = 3;
    
    /**
     * Стоп-слова для фильтрации униграмм
     */
    private List<String> stopWords;
    
    /**
     * Технические слова и сокращения для полной фильтрации
     */
    private List<String> technicalWords;
}
