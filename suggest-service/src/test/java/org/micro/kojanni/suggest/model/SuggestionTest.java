package org.micro.kojanni.suggest.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SuggestionTest {

    @Test
    void testConstructor_AllArgs() {
        Suggestion suggestion = new Suggestion("java", 100L);

        assertEquals("java", suggestion.getPhrase());
        assertEquals(100L, suggestion.getWeight());
    }

    @Test
    void testConstructor_NoArgs() {
        Suggestion suggestion = new Suggestion();

        assertNull(suggestion.getPhrase());
        assertEquals(0L, suggestion.getWeight());
    }

    @Test
    void testBuilder() {
        Suggestion suggestion = Suggestion.builder()
                .phrase("python")
                .weight(90L)
                .build();

        assertEquals("python", suggestion.getPhrase());
        assertEquals(90L, suggestion.getWeight());
    }

    @Test
    void testSetters() {
        Suggestion suggestion = new Suggestion();
        suggestion.setPhrase("javascript");
        suggestion.setWeight(80L);

        assertEquals("javascript", suggestion.getPhrase());
        assertEquals(80L, suggestion.getWeight());
    }

    @Test
    void testEquals_SameValues() {
        Suggestion suggestion1 = new Suggestion("java", 100L);
        Suggestion suggestion2 = new Suggestion("java", 100L);

        assertEquals(suggestion1, suggestion2);
    }

    @Test
    void testEquals_DifferentPhrase() {
        Suggestion suggestion1 = new Suggestion("java", 100L);
        Suggestion suggestion2 = new Suggestion("python", 100L);

        assertNotEquals(suggestion1, suggestion2);
    }

    @Test
    void testEquals_DifferentWeight() {
        Suggestion suggestion1 = new Suggestion("java", 100L);
        Suggestion suggestion2 = new Suggestion("java", 90L);

        assertNotEquals(suggestion1, suggestion2);
    }

    @Test
    void testHashCode_SameValues() {
        Suggestion suggestion1 = new Suggestion("java", 100L);
        Suggestion suggestion2 = new Suggestion("java", 100L);

        assertEquals(suggestion1.hashCode(), suggestion2.hashCode());
    }

    @Test
    void testToString() {
        Suggestion suggestion = new Suggestion("java", 100L);

        String result = suggestion.toString();

        assertTrue(result.contains("java"));
        assertTrue(result.contains("100"));
    }

    @Test
    void testBuilder_PartialFields() {
        Suggestion suggestion = Suggestion.builder()
                .phrase("test")
                .build();

        assertEquals("test", suggestion.getPhrase());
        assertEquals(0L, suggestion.getWeight());
    }

    @Test
    void testSetPhrase_Null() {
        Suggestion suggestion = new Suggestion("java", 100L);
        suggestion.setPhrase(null);

        assertNull(suggestion.getPhrase());
        assertEquals(100L, suggestion.getWeight());
    }

    @Test
    void testSetWeight_Zero() {
        Suggestion suggestion = new Suggestion("java", 100L);
        suggestion.setWeight(0L);

        assertEquals("java", suggestion.getPhrase());
        assertEquals(0L, suggestion.getWeight());
    }

    @Test
    void testSetWeight_Negative() {
        Suggestion suggestion = new Suggestion("java", 100L);
        suggestion.setWeight(-50L);

        assertEquals("java", suggestion.getPhrase());
        assertEquals(-50L, suggestion.getWeight());
    }
}
