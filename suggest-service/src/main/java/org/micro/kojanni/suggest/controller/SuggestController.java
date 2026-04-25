package org.micro.kojanni.suggest.controller;

import org.micro.kojanni.suggest.service.TrieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/suggest")
public class SuggestController {

    @Autowired
    private TrieService trieService;

    @GetMapping
    public List<String> suggest(@RequestParam String prefix,
                                @RequestParam(defaultValue = "5") int limit) {
        if (limit <= 0 || limit > 20) limit = 5;

        return trieService.getSuggestions(prefix, limit);
    }

    @DeleteMapping
    public String clear() {
        trieService.clear();
        return "Trie cleared successfully";
    }
}