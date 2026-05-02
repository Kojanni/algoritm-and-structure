package org.micro.kojanni.suggest.controller;

import org.micro.kojanni.suggest.service.TrieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SuggestController {

    @Autowired
    private TrieService trieService;
    
    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping("/api/suggest")
    public List<String> suggest(@RequestParam String prefix,
                                @RequestParam(defaultValue = "5") int limit) {
        if (limit <= 0 || limit > 20) limit = 5;

        return trieService.getSuggestions(prefix, limit);
    }

    @DeleteMapping("/api/suggest")
    public String clear() {
        trieService.clear();
        return "Trie cleared successfully";
    }

    @PostMapping("/api/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (file.isEmpty()) {
                response.put("success", false);
                response.put("message", "Файл пустой");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (!file.getOriginalFilename().endsWith(".txt")) {
                response.put("success", false);
                response.put("message", "Поддерживаются только .txt файлы");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Получаем путь к директории corpus в исходниках (не в target)
            Resource corpusResource = resourceLoader.getResource("classpath:corpus");
            File targetClassesDir = corpusResource.getFile();
            
            // Преобразуем путь из target/classes в src/main/resources
            String targetPath = targetClassesDir.getAbsolutePath();
            String sourcePath = targetPath.replace(
                File.separator + "target" + File.separator + "classes",
                File.separator + "src" + File.separator + "main" + File.separator + "resources"
            );
            
            File corpusDir = new File(sourcePath);
            if (!corpusDir.exists()) {
                corpusDir.mkdirs();
            }
            
            File targetFile = new File(corpusDir, file.getOriginalFilename());
            Files.copy(file.getInputStream(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            
            System.out.println("Файл сохранен: " + targetFile.getAbsolutePath());
            
            // Добавляем в Trie (читаем заново из сохраненного файла)
            int phrasesAdded = trieService.addFromFile(Files.newInputStream(targetFile.toPath()));
            
            response.put("success", true);
            response.put("message", "Файл успешно загружен и сохранен");
            response.put("phrasesAdded", phrasesAdded);
            response.put("filename", file.getOriginalFilename());
            response.put("savedTo", targetFile.getAbsolutePath());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Ошибка при загрузке файла: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}