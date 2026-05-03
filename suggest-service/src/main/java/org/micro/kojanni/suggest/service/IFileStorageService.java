package org.micro.kojanni.suggest.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Интерфейс для сервиса сохранения файлов
 * Позволяет переопределять реализацию (например, для сохранения в облако, БД и т.д.)
 */
public interface IFileStorageService {
    
    /**
     * Сохранение файла в хранилище
     * @param file загружаемый файл
     * @return сохраненный файл
     * @throws IOException при ошибке сохранения
     */
    File saveToCorpus(MultipartFile file) throws IOException;
}
