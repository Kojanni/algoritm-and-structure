package org.micro.kojanni.suggest.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.micro.kojanni.suggest.config.CorpusProperties;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CorpusLoaderTest {

    private CorpusLoader corpusLoader;
    private CorpusProperties properties;

    @BeforeEach
    void setUp() {
        properties = new CorpusProperties();
        properties.setMinTokenLength(3);
        properties.setMinPhraseFrequency(2);
        properties.setMaxNgram(3);
        properties.setStopWords(Arrays.asList("и", "в", "на", "с", "это", "все", "вот", "этого", "этой", "был", "была", "было", "были"));
        properties.setTechnicalWords(Arrays.asList("франц", "англ", "http"));
        
        corpusLoader = new CorpusLoader(properties);
    }

    @Test
    void testBuildFrequencyMap_FilterShortTokens() throws IOException {
        String text = "кот пес мышь ко пе\nкот пес мышь ко пе";
        InputStream inputStream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        
        Map<String, Long> freqMap = corpusLoader.buildFrequencyMapFromStream(inputStream);

        assertTrue(freqMap.containsKey("кот"), "Should contain 'кот'");
        assertTrue(freqMap.containsKey("пес"), "Should contain 'пес'");
        assertTrue(freqMap.containsKey("мышь"), "Should contain 'мышь'");
        assertFalse(freqMap.containsKey("ко"), "Should not contain 'ко' - too short");
        assertFalse(freqMap.containsKey("пе"), "Should not contain 'пе' - too short");
    }

    @Test
    void testBuildFrequencyMap_FilterStopWords() throws IOException {
        String text = "война и мир\nвойна и мир";
        InputStream inputStream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        
        Map<String, Long> freqMap = corpusLoader.buildFrequencyMapFromStream(inputStream);
        
        assertFalse(freqMap.containsKey("и"));
        assertTrue(freqMap.containsKey("война"));
        assertTrue(freqMap.containsKey("мир"));
    }

    @Test
    void testBuildFrequencyMap_StopWordsNotAtEnd() throws IOException {
        String text = "время и мир\nвремя и мир\nвремя и мир";
        InputStream inputStream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        
        Map<String, Long> freqMap = corpusLoader.buildFrequencyMapFromStream(inputStream);

        assertFalse(freqMap.containsKey("время и"));
        assertTrue(freqMap.containsKey("время и мир"));
    }

    @Test
    void testBuildFrequencyMap_FilterStopWordsAtEnd() throws IOException {
        String text = "пьер в\nпьер и\nпьер это\nпьер все\nпьер вот";
        InputStream inputStream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        
        Map<String, Long> freqMap = corpusLoader.buildFrequencyMapFromStream(inputStream);

        assertFalse(freqMap.containsKey("пьер в"));
        assertFalse(freqMap.containsKey("пьер и"));
        assertFalse(freqMap.containsKey("пьер это"));
        assertFalse(freqMap.containsKey("пьер вот"));
    }

    @Test
    void testBuildFrequencyMap_FilterTechnicalWords() throws IOException {
        String text = "книга автор сайт\nкнига автор сайт";
        InputStream inputStream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        
        Map<String, Long> freqMap = corpusLoader.buildFrequencyMapFromStream(inputStream);
        
        assertTrue(freqMap.containsKey("книга"), "Should contain 'книга'");
        assertTrue(freqMap.containsKey("автор"), "Should contain 'автор'");
        assertTrue(freqMap.containsKey("сайт"), "Should contain 'сайт'");
        assertFalse(freqMap.containsKey("франц"), "Should not contain 'франц' - technical word");
        assertFalse(freqMap.containsKey("англ"), "Should not contain 'англ' - technical word");
        assertFalse(freqMap.containsKey("http"), "Should not contain 'http' - technical word");
    }

    @Test
    void testBuildFrequencyMap_CountFrequency() throws IOException {
        String text = "java программирование\njava программирование\njava разработка";
        InputStream inputStream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        
        Map<String, Long> freqMap = corpusLoader.buildFrequencyMapFromStream(inputStream);

        assertFalse(freqMap.containsKey("один два три"));
    }

    @Test
    void testBuildFrequencyMap_ShortWordNotAtEnd() throws IOException {
        String text = "там был\nтам был\nтам был";
        InputStream inputStream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        
        Map<String, Long> freqMap = corpusLoader.buildFrequencyMapFromStream(inputStream);
        
        assertFalse(freqMap.containsKey("там был"), "Should not contain 'там был' - ends with stop word");
    }
}
