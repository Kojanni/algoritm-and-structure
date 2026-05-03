package org.micro.kojanni.suggest.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrieNodeTest {

    private TrieNode trieNode;

    @BeforeEach
    void setUp() {
        trieNode = new TrieNode();
    }

    @Test
    void testAddSuggestion_SingleSuggestion() {
        trieNode.addSuggestion("java", 100L);

        List<Suggestion> suggestions = trieNode.getSuggestions();
        assertEquals(1, suggestions.size());
        assertEquals("java", suggestions.get(0).getPhrase());
        assertEquals(100L, suggestions.get(0).getWeight());
    }

    @Test
    void testAddSuggestion_MultipleSuggestions() {
        trieNode.addSuggestion("java", 100L);
        trieNode.addSuggestion("javascript", 80L);
        trieNode.addSuggestion("python", 90L);

        List<Suggestion> suggestions = trieNode.getSuggestions();
        assertEquals(3, suggestions.size());
    }

    @Test
    void testSortSuggestions_DescendingOrder() {
        trieNode.addSuggestion("java", 100L);
        trieNode.addSuggestion("javascript", 80L);
        trieNode.addSuggestion("python", 90L);

        trieNode.sortSuggestions();

        List<Suggestion> suggestions = trieNode.getSuggestions();
        assertEquals("java", suggestions.get(0).getPhrase());
        assertEquals("python", suggestions.get(1).getPhrase());
        assertEquals("javascript", suggestions.get(2).getPhrase());
    }

    @Test
    void testSortSuggestions_AlreadySorted() {
        trieNode.addSuggestion("java", 100L);
        trieNode.addSuggestion("python", 90L);
        trieNode.addSuggestion("javascript", 80L);

        trieNode.sortSuggestions();

        List<Suggestion> suggestions = trieNode.getSuggestions();
        assertEquals("java", suggestions.get(0).getPhrase());
        assertEquals("python", suggestions.get(1).getPhrase());
        assertEquals("javascript", suggestions.get(2).getPhrase());
    }

    @Test
    void testSortSuggestions_EqualWeights() {
        trieNode.addSuggestion("java", 100L);
        trieNode.addSuggestion("python", 100L);
        trieNode.addSuggestion("javascript", 100L);

        trieNode.sortSuggestions();

        List<Suggestion> suggestions = trieNode.getSuggestions();
        assertEquals(3, suggestions.size());

        assertTrue(suggestions.stream().anyMatch(s -> s.getPhrase().equals("java")));
        assertTrue(suggestions.stream().anyMatch(s -> s.getPhrase().equals("python")));
        assertTrue(suggestions.stream().anyMatch(s -> s.getPhrase().equals("javascript")));
    }

    @Test
    void testSortSuggestions_EmptyList() {
        trieNode.sortSuggestions();

        List<Suggestion> suggestions = trieNode.getSuggestions();
        assertTrue(suggestions.isEmpty());
    }

    @Test
    void testGetChildren_EmptyInitially() {
        assertTrue(trieNode.getChildren().isEmpty());
    }

    @Test
    void testGetChildren_AddChild() {
        TrieNode child = new TrieNode();
        trieNode.getChildren().put('a', child);

        assertEquals(1, trieNode.getChildren().size());
        assertSame(child, trieNode.getChildren().get('a'));
    }

    @Test
    void testGetChildren_MultipleChildren() {
        TrieNode child1 = new TrieNode();
        TrieNode child2 = new TrieNode();
        TrieNode child3 = new TrieNode();

        trieNode.getChildren().put('a', child1);
        trieNode.getChildren().put('b', child2);
        trieNode.getChildren().put('c', child3);

        assertEquals(3, trieNode.getChildren().size());
        assertSame(child1, trieNode.getChildren().get('a'));
        assertSame(child2, trieNode.getChildren().get('b'));
        assertSame(child3, trieNode.getChildren().get('c'));
    }

    @Test
    void testAddSuggestion_ZeroWeight() {
        trieNode.addSuggestion("test", 0L);

        List<Suggestion> suggestions = trieNode.getSuggestions();
        assertEquals(1, suggestions.size());
        assertEquals(0L, suggestions.get(0).getWeight());
    }

    @Test
    void testAddSuggestion_NegativeWeight() {
        trieNode.addSuggestion("test", -10L);

        List<Suggestion> suggestions = trieNode.getSuggestions();
        assertEquals(1, suggestions.size());
        assertEquals(-10L, suggestions.get(0).getWeight());
    }

    @Test
    void testSortSuggestions_MixedWeights() {
        trieNode.addSuggestion("low", 10L);
        trieNode.addSuggestion("high", 1000L);
        trieNode.addSuggestion("medium", 100L);
        trieNode.addSuggestion("very-low", 1L);

        trieNode.sortSuggestions();

        List<Suggestion> suggestions = trieNode.getSuggestions();
        assertEquals("high", suggestions.get(0).getPhrase());
        assertEquals("medium", suggestions.get(1).getPhrase());
        assertEquals("low", suggestions.get(2).getPhrase());
        assertEquals("very-low", suggestions.get(3).getPhrase());
    }
}
