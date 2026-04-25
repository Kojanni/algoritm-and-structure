package org.micro.kojanni.suggest.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrieServiceTest {

    @Mock
    private CorpusLoader corpusLoader;

    private TrieService trieService;

    @BeforeEach
    void setUp() throws IOException {
        trieService = new TrieService(corpusLoader);

        Map<String, Long> mockFrequencyMap = new HashMap<>();
        mockFrequencyMap.put("java", 100L);
        mockFrequencyMap.put("javascript", 80L);
        mockFrequencyMap.put("python", 90L);
        mockFrequencyMap.put("programming", 70L);
        mockFrequencyMap.put("java programming", 60L);
        
        when(corpusLoader.buildFrequencyMap(anyString())).thenReturn(mockFrequencyMap);

        trieService.init();
    }

    @Test
    void testGetSuggestions_WithValidPrefix() {
        String prefix = "jav";
        int limit = 5;

        List<String> suggestions = trieService.getSuggestions(prefix, limit);

        assertNotNull(suggestions);
        assertEquals(3, suggestions.size());
        assertTrue(suggestions.contains("java"));
        assertTrue(suggestions.contains("javascript"));
        assertTrue(suggestions.contains("java programming"));
    }

    @Test
    void testGetSuggestions_OrderedByWeight() {
        String prefix = "jav";
        int limit = 5;

        List<String> suggestions = trieService.getSuggestions(prefix, limit);

        assertEquals("java", suggestions.get(0)); // weight 100
        assertEquals("javascript", suggestions.get(1)); // weight 80
        assertEquals("java programming", suggestions.get(2)); // weight 60
    }

    @Test
    void testGetSuggestions_WithLimit() {
        String prefix = "jav";
        int limit = 2;

        List<String> suggestions = trieService.getSuggestions(prefix, limit);

        assertEquals(2, suggestions.size());
        assertEquals("java", suggestions.get(0));
        assertEquals("javascript", suggestions.get(1));
    }

    @Test
    void testGetSuggestions_CaseInsensitive() {
        String prefix = "JAV";
        int limit = 5;

        List<String> suggestions = trieService.getSuggestions(prefix, limit);

        assertNotNull(suggestions);
        assertEquals(3, suggestions.size());
        assertTrue(suggestions.contains("java"));
    }

    @Test
    void testGetSuggestions_NoMatch() {
        String prefix = "xyz";
        int limit = 5;

        List<String> suggestions = trieService.getSuggestions(prefix, limit);

        assertNotNull(suggestions);
        assertTrue(suggestions.isEmpty());
    }

    @Test
    void testGetSuggestions_EmptyPrefix() {
        String prefix = "";
        int limit = 5;

        List<String> suggestions = trieService.getSuggestions(prefix, limit);

        assertNotNull(suggestions);
        assertTrue(suggestions.isEmpty());
    }

    @Test
    void testGetSuggestions_NullPrefix() {
        String prefix = null;
        int limit = 5;

        List<String> suggestions = trieService.getSuggestions(prefix, limit);

        assertNotNull(suggestions);
        assertTrue(suggestions.isEmpty());
    }

    @Test
    void testClear_RemovesAllData() {
        String prefix = "jav";
        int limit = 5;

        // Проверяем, что данные есть до очистки
        List<String> beforeClear = trieService.getSuggestions(prefix, limit);
        assertFalse(beforeClear.isEmpty());

        trieService.clear();

        List<String> afterClear = trieService.getSuggestions(prefix, limit);
        assertTrue(afterClear.isEmpty());
    }

    @Test
    void testClear_AllPrefixes() {
        List<String> javaSuggestions = trieService.getSuggestions("jav", 5);
        List<String> pythonSuggestions = trieService.getSuggestions("py", 5);
        
        assertFalse(javaSuggestions.isEmpty());
        assertFalse(pythonSuggestions.isEmpty());

        trieService.clear();

        assertTrue(trieService.getSuggestions("jav", 5).isEmpty());
        assertTrue(trieService.getSuggestions("py", 5).isEmpty());
        assertTrue(trieService.getSuggestions("pro", 5).isEmpty());
    }

    @Test
    void testGetSuggestions_PartialMatch() {
        String prefix = "p";
        int limit = 10;

        List<String> suggestions = trieService.getSuggestions(prefix, limit);

        assertNotNull(suggestions);
        assertEquals(2, suggestions.size());
        assertTrue(suggestions.contains("python"));
        assertTrue(suggestions.contains("programming"));
        assertEquals("python", suggestions.get(0));
    }
}
