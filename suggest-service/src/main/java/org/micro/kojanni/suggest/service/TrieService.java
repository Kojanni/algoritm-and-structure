package org.micro.kojanni.suggest.service;

import jakarta.annotation.PostConstruct;
import org.micro.kojanni.suggest.model.Suggestion;
import org.micro.kojanni.suggest.model.TrieNode;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class TrieService {

    private final TrieNode root = new TrieNode();
    private final CorpusLoader corpusLoader;

    public TrieService(CorpusLoader corpusLoader) {
        this.corpusLoader = corpusLoader;
    }

    @PostConstruct
    public void init() throws IOException {
        // 1. Загружаем сырую папку с текстами
        Map<String, Long> phraseFreq = corpusLoader.buildFrequencyMap("src/main/resources/corpus");

        // 2. Вставляем все фразы в Trie
        for (Map.Entry<String, Long> entry : phraseFreq.entrySet()) {
            insert(entry.getKey(), entry.getValue());
        }

        // 3. Сортируем подсказки во всех узлах
        sortAllNodeSuggestions(root);

        System.out.println("Trie built with " + phraseFreq.size() + " unique phrases.");
    }

    private void insert(String phrase, long weight) {
        TrieNode node = root;
        // Для каждого символа (включая последний) добавляем фразу в список узла
        for (char c : phrase.toCharArray()) {
            node = node.getChildren().computeIfAbsent(c, k -> new TrieNode());
            node.addSuggestion(phrase, weight);
        }
    }

    private void sortAllNodeSuggestions(TrieNode node) {
        for (TrieNode child : node.getChildren().values()) {
            sortAllNodeSuggestions(child);
        }
    }

    public List<String> getSuggestions(String prefix, int limit) {
        if (prefix == null || prefix.isEmpty()) return List.of();
        prefix = prefix.toLowerCase();
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            node = node.getChildren().get(c);
            if (node == null) return List.of();
        }
        return node.getSuggestions().stream()
                .limit(limit)
                .map(Suggestion::getPhrase)
                .toList();
    }

    public void clear() {
        root.getChildren().clear();
        root.getSuggestions().clear();
        System.out.println("Trie cleared.");
    }
}