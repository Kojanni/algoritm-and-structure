package org.micro.kojanni.suggest.controller;

import org.micro.kojanni.suggest.service.TrieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
    private ResourceLoader resourceLoader;

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
            
            // Сохраняем файл с конвертацией в UTF-8
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(file.getInputStream(), detectCharset(file)));
                 BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(targetFile), StandardCharsets.UTF_8))) {
                
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                    writer.newLine();
                }
            }
            
            System.out.println("Файл сохранен в UTF-8: " + targetFile.getAbsolutePath());
            
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
    
    /**
     * Определяет кодировку файла (упрощенная версия)
     * Пытается определить: UTF-8, Windows-1251, или использует системную кодировку
     */
    private Charset detectCharset(MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        
        // Проверяем BOM для UTF-8
        if (bytes.length >= 3 && 
            bytes[0] == (byte)0xEF && 
            bytes[1] == (byte)0xBB && 
            bytes[2] == (byte)0xBF) {
            return StandardCharsets.UTF_8;
        }
        
        // Пробуем декодировать как UTF-8
        try {
            String test = new String(bytes, StandardCharsets.UTF_8);
            // Проверяем на наличие replacement characters
            if (!test.contains("\uFFFD")) {
                return StandardCharsets.UTF_8;
            }
        } catch (Exception e) {
            // UTF-8 не подошла
        }
        
        // Для русских текстов чаще всего Windows-1251
        return Charset.forName("Windows-1251");
    }
}