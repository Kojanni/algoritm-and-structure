package org.micro.kojanni.suggest.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileStorageServiceTest {

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private Resource resource;

    @TempDir
    File tempDir;
    private FileStorageService fileStorageService;

    @BeforeEach
    void setUp() throws IOException {
        fileStorageService = new FileStorageService(resourceLoader);
    }

    @Test
    void testSaveToCorpus_UTF8File() throws IOException {
        File corpusDir = new File(tempDir, "corpus");
        corpusDir.mkdirs();
        when(resourceLoader.getResource(anyString())).thenReturn(resource);
        when(resource.getFile()).thenReturn(corpusDir);
        
        String content = "Привет, мир!";
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.txt",
            "text/plain",
            content.getBytes(StandardCharsets.UTF_8)
        );

        File savedFile = fileStorageService.saveToCorpus(file);

        assertTrue(savedFile.exists());
        
        String savedContent = Files.readString(savedFile.toPath(), StandardCharsets.UTF_8).trim();
        assertEquals(content, savedContent);
    }

    @Test
    void testSaveToCorpus_Windows1251File() throws IOException {
        File corpusDir = new File(tempDir, "corpus");
        corpusDir.mkdirs();
        when(resourceLoader.getResource(anyString())).thenReturn(resource);
        when(resource.getFile()).thenReturn(corpusDir);
        
        String content = "Привет, мир!";
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.txt",
            "text/plain",
            content.getBytes(Charset.forName("Windows-1251"))
        );

        File savedFile = fileStorageService.saveToCorpus(file);

        assertTrue(savedFile.exists());
        
        String savedContent = Files.readString(savedFile.toPath(), StandardCharsets.UTF_8).trim();
        assertEquals(content, savedContent);
    }

    @Test
    void testDetectCharset_UTF8WithBOM() throws IOException {
        byte[] bom = new byte[]{(byte)0xEF, (byte)0xBB, (byte)0xBF};
        byte[] content = "test".getBytes(StandardCharsets.UTF_8);
        byte[] withBom = new byte[bom.length + content.length];
        System.arraycopy(bom, 0, withBom, 0, bom.length);
        System.arraycopy(content, 0, withBom, bom.length, content.length);
        
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.txt",
            "text/plain",
            withBom
        );

        Charset charset = fileStorageService.detectCharset(file);
        
        assertEquals(StandardCharsets.UTF_8, charset);
    }

    @Test
    void testDetectCharset_UTF8() throws IOException {
        String content = "Hello, world!";
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.txt",
            "text/plain",
            content.getBytes(StandardCharsets.UTF_8)
        );

        Charset charset = fileStorageService.detectCharset(file);
        
        assertEquals(StandardCharsets.UTF_8, charset);
    }

    @Test
    void testDetectCharset_Windows1251() throws IOException {
        String content = "Привет";
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.txt",
            "text/plain",
            content.getBytes(Charset.forName("Windows-1251"))
        );

        Charset charset = fileStorageService.detectCharset(file);

        assertEquals(Charset.forName("Windows-1251"), charset);
    }

    @Test
    void testConvertAndSaveFile_MultipleLines() throws IOException {
        String content = "Строка 1\nСтрока 2\nСтрока 3";
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.txt",
            "text/plain",
            content.getBytes(StandardCharsets.UTF_8)
        );

        File targetFile = new File(tempDir, "output.txt");
        fileStorageService.convertAndSaveFile(file, targetFile);

        assertTrue(targetFile.exists());
        
        String savedContent = Files.readString(targetFile.toPath(), StandardCharsets.UTF_8);
        String[] expectedLines = content.split("\\n");
        String[] actualLines = savedContent.split("\\r?\\n");
        assertEquals(expectedLines.length, actualLines.length);
        for (int i = 0; i < expectedLines.length; i++) {
            assertEquals(expectedLines[i], actualLines[i]);
        }
    }

    @Test
    void testGetCorpusDirectory_CreatesIfNotExists() throws IOException {
        File nonExistentDir = new File(tempDir, "new-corpus");
        when(resourceLoader.getResource(anyString())).thenReturn(resource);
        when(resource.getFile()).thenReturn(nonExistentDir);
        
        File corpusDir = fileStorageService.getCorpusDirectory();
        
        assertTrue(corpusDir.exists());
        assertTrue(corpusDir.isDirectory());
    }

    @Test
    void testSaveToCorpus_PreservesFilename() throws IOException {
        File corpusDir = new File(tempDir, "corpus");
        corpusDir.mkdirs();
        when(resourceLoader.getResource(anyString())).thenReturn(resource);
        when(resource.getFile()).thenReturn(corpusDir);
        
        String filename = "my-custom-file.txt";
        MockMultipartFile file = new MockMultipartFile(
            "file",
            filename,
            "text/plain",
            "content".getBytes(StandardCharsets.UTF_8)
        );

        File savedFile = fileStorageService.saveToCorpus(file);

        assertEquals(filename, savedFile.getName());
    }

    @Test
    void testSaveToCorpus_EmptyFile() throws IOException {
        File corpusDir = new File(tempDir, "corpus");
        corpusDir.mkdirs();
        when(resourceLoader.getResource(anyString())).thenReturn(resource);
        when(resource.getFile()).thenReturn(corpusDir);
        
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "empty.txt",
            "text/plain",
            new byte[0]
        );

        File savedFile = fileStorageService.saveToCorpus(file);

        assertTrue(savedFile.exists());
        assertEquals(0, savedFile.length());
    }
}
