package org.micro.kojanni.suggest.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Сервис для сохранения файлов в файловую систему
 * Реализация по умолчанию - сохранение в директорию corpus
 */
@Service
public class FileStorageService implements IFileStorageService {

    private final ResourceLoader resourceLoader;

    public FileStorageService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * Сохранение файла в директорию corpus с конвертацией в UTF-8
     */
    public File saveToCorpus(MultipartFile file) throws IOException {
        File corpusDir = getCorpusDirectory();
        File targetFile = new File(corpusDir, file.getOriginalFilename());
        
        convertAndSaveFile(file, targetFile);
        
        System.out.println("Файл сохранен в UTF-8: " + targetFile.getAbsolutePath());
        return targetFile;
    }

    /**
     * Получение директории corpus в src/main/resources
     */
    protected File getCorpusDirectory() throws IOException {
        Resource corpusResource = resourceLoader.getResource("classpath:corpus");
        File targetClassesDir = corpusResource.getFile();
        
        String targetPath = targetClassesDir.getAbsolutePath();
        String sourcePath = targetPath.replace(
            File.separator + "target" + File.separator + "classes",
            File.separator + "src" + File.separator + "main" + File.separator + "resources"
        );
        
        File corpusDir = new File(sourcePath);
        if (!corpusDir.exists()) {
            corpusDir.mkdirs();
        }
        
        return corpusDir;
    }

    /**
     * Конвертация и сохранение файла в UTF-8
     */
    protected void convertAndSaveFile(MultipartFile file, File targetFile) throws IOException {
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
    }

    /**
     * Определяет кодировку файла (упрощенная версия)
     * Пытается определить: UTF-8, Windows-1251, или использует системную кодировку
     */
    protected Charset detectCharset(MultipartFile file) throws IOException {
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
