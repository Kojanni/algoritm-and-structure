package org.micro.kojanni.suggest.controller;

import org.micro.kojanni.suggest.service.IFileStorageService;
import org.micro.kojanni.suggest.service.TrieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SuggestController {

    @Autowired
    private TrieService trieService;
    
    @Autowired
    private IFileStorageService fileStorageService;

    @GetMapping("/suggest")
    public List<String> suggest(@RequestParam String prefix,
                                @RequestParam(defaultValue = "5") int limit) {
        if (limit <= 0 || limit > 20) limit = 5;

        return trieService.getSuggestions(prefix, limit);
    }

    @DeleteMapping("/suggest")
    public String clear() {
        trieService.clear();
        return "Trie cleared successfully";
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            validateFile(file, response);
            
            File targetFile = saveFileToCorpus(file);
            
            int phrasesAdded = addFileToTrie(targetFile);
            
            return buildSuccessResponse(response, file.getOriginalFilename(), targetFile, phrasesAdded);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Ошибка при загрузке файла: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * Валидация загружаемого файла
     */
    private void validateFile(MultipartFile file, Map<String, Object> response) {
        if (file.isEmpty()) {
            response.put("success", false);
            response.put("message", "Файл пустой");
            throw new IllegalArgumentException("Файл пустой");
        }
        
        if (!file.getOriginalFilename().endsWith(".txt")) {
            response.put("success", false);
            response.put("message", "Поддерживаются только .txt файлы");
            throw new IllegalArgumentException("Неверный формат файла");
        }
    }
    
    /**
     * Сохранение файла в директорию corpus с конвертацией в UTF-8
     */
    private File saveFileToCorpus(MultipartFile file) throws IOException {
        return fileStorageService.saveToCorpus(file);
    }
    
    /**
     * Добавление фраз из файла в Trie
     */
    private int addFileToTrie(File file) throws IOException {
        int phrasesAdded = trieService.addFromFile(Files.newInputStream(file.toPath()));
        System.out.println("Добавлено фраз в Trie: " + phrasesAdded);
        return phrasesAdded;
    }
    
    /**
     * Формирование успешного ответа
     */
    private ResponseEntity<Map<String, Object>> buildSuccessResponse(
            Map<String, Object> response, String filename, File targetFile, int phrasesAdded) {
        response.put("success", true);
        response.put("message", "Файл успешно загружен и сохранен");
        response.put("phrasesAdded", phrasesAdded);
        response.put("filename", filename);
        response.put("savedTo", targetFile.getAbsolutePath());
        
        return ResponseEntity.ok(response);
    }
    
}