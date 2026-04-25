package org.micro.kojanni.suggest.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class TrieNode {

    private final Map<Character, TrieNode> children = new HashMap<>();
    private final List<Suggestion> suggestions = new ArrayList<>();

    public void addSuggestion(String phrase, long weight) {
        suggestions.add(new Suggestion(phrase, weight));
    }

    /**
     * Отсортировать кандидатов по убыванию веса
     */
    public void sortSuggestions() {
        suggestions.sort((a, b) -> Long.compare(b.getWeight(), a.getWeight()));
    }
}
