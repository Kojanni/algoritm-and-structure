package org.micro.kojanni.suggest.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.micro.kojanni.suggest.service.IFileStorageService;
import org.micro.kojanni.suggest.service.TrieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SuggestControllerTest {

    @Mock
    private TrieService trieService;

    @Mock
    private IFileStorageService fileStorageService;

    @InjectMocks
    private SuggestController suggestController;

    @TempDir
    File tempDir;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testSuggest_ValidPrefix() {
        List<String> expectedSuggestions = Arrays.asList("java", "javascript", "java programming");
        when(trieService.getSuggestions("jav", 5)).thenReturn(expectedSuggestions);

        List<String> result = suggestController.suggest("jav", 5);

        assertEquals(expectedSuggestions, result);
        verify(trieService, times(1)).getSuggestions("jav", 5);
    }

    @Test
    void testSuggest_DefaultLimit() {
        List<String> expectedSuggestions = Arrays.asList("python", "programming");
        when(trieService.getSuggestions(anyString(), anyInt())).thenReturn(expectedSuggestions);

        List<String> result = suggestController.suggest("py", 5);

        assertNotNull(result);
        verify(trieService, times(1)).getSuggestions("py", 5);
    }

    @Test
    void testSuggest_LimitTooLow() {
        List<String> expectedSuggestions = Arrays.asList("test");
        when(trieService.getSuggestions(anyString(), eq(5))).thenReturn(expectedSuggestions);

        List<String> result = suggestController.suggest("test", 0);

        verify(trieService, times(1)).getSuggestions("test", 5);
    }

    @Test
    void testSuggest_LimitTooHigh() {
        List<String> expectedSuggestions = Arrays.asList("test");
        when(trieService.getSuggestions(anyString(), eq(5))).thenReturn(expectedSuggestions);

        List<String> result = suggestController.suggest("test", 100);

        verify(trieService, times(1)).getSuggestions("test", 5);
    }

    @Test
    void testSuggest_ValidLimit() {
        List<String> expectedSuggestions = Arrays.asList("test1", "test2");
        when(trieService.getSuggestions("test", 10)).thenReturn(expectedSuggestions);

        List<String> result = suggestController.suggest("test", 10);

        assertEquals(expectedSuggestions, result);
        verify(trieService, times(1)).getSuggestions("test", 10);
    }

    @Test
    void testClear_Success() {
        String result = suggestController.clear();

        assertEquals("Trie cleared successfully", result);
        verify(trieService, times(1)).clear();
    }

    @Test
    void testUploadFile_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.txt",
            "text/plain",
            "test content".getBytes()
        );

        File testFile = new File(tempDir, "test.txt");
        Files.write(testFile.toPath(), "test content".getBytes());
        
        when(fileStorageService.saveToCorpus(file)).thenReturn(testFile);
        when(trieService.addFromFile(any(InputStream.class))).thenReturn(10);

        ResponseEntity<Map<String, Object>> response = suggestController.uploadFile(file);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals(10, response.getBody().get("phrasesAdded"));
        assertEquals("test.txt", response.getBody().get("filename"));
        
        verify(fileStorageService, times(1)).saveToCorpus(file);
        verify(trieService, times(1)).addFromFile(any(InputStream.class));
    }

    @Test
    void testUploadFile_EmptyFile() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.txt",
            "text/plain",
            new byte[0]
        );

        ResponseEntity<Map<String, Object>> response = suggestController.uploadFile(file);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse((Boolean) response.getBody().get("success"));
        assertEquals("Файл пустой", response.getBody().get("message"));
        
        verify(fileStorageService, never()).saveToCorpus(any());
        verify(trieService, never()).addFromFile(any());
    }

    @Test
    void testUploadFile_InvalidFileType() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.pdf",
            "application/pdf",
            "test content".getBytes()
        );

        ResponseEntity<Map<String, Object>> response = suggestController.uploadFile(file);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse((Boolean) response.getBody().get("success"));
        assertEquals("Поддерживаются только .txt файлы", response.getBody().get("message"));
        
        verify(fileStorageService, never()).saveToCorpus(any());
        verify(trieService, never()).addFromFile(any());
    }

    @Test
    void testUploadFile_IOExceptionDuringSave() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.txt",
            "text/plain",
            "test content".getBytes()
        );

        when(fileStorageService.saveToCorpus(file)).thenThrow(new IOException("Disk full"));

        ResponseEntity<Map<String, Object>> response = suggestController.uploadFile(file);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse((Boolean) response.getBody().get("success"));
        assertTrue(response.getBody().get("message").toString().contains("Ошибка при загрузке файла"));
        
        verify(fileStorageService, times(1)).saveToCorpus(file);
        verify(trieService, never()).addFromFile(any());
    }

    @Test
    void testUploadFile_IOExceptionDuringTrieAdd() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.txt",
            "text/plain",
            "test content".getBytes()
        );

        File testFile = new File(tempDir, "test.txt");
        Files.write(testFile.toPath(), "test content".getBytes());
        
        when(fileStorageService.saveToCorpus(file)).thenReturn(testFile);
        when(trieService.addFromFile(any(InputStream.class))).thenThrow(new IOException("Parse error"));

        ResponseEntity<Map<String, Object>> response = suggestController.uploadFile(file);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse((Boolean) response.getBody().get("success"));
        assertTrue(response.getBody().get("message").toString().contains("Ошибка при загрузке файла"));
        
        verify(fileStorageService, times(1)).saveToCorpus(file);
        verify(trieService, times(1)).addFromFile(any(InputStream.class));
    }

    @Test
    void testUploadFile_ValidTxtFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "corpus.txt",
            "text/plain",
            "война и мир\nанна каренина".getBytes()
        );

        File testFile = new File(tempDir, "corpus.txt");
        Files.write(testFile.toPath(), "война и мир\nанна каренина".getBytes());
        
        when(fileStorageService.saveToCorpus(file)).thenReturn(testFile);
        when(trieService.addFromFile(any(InputStream.class))).thenReturn(5);

        ResponseEntity<Map<String, Object>> response = suggestController.uploadFile(file);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertEquals("Файл успешно загружен и сохранен", response.getBody().get("message"));
        assertEquals(5, response.getBody().get("phrasesAdded"));
    }
}
