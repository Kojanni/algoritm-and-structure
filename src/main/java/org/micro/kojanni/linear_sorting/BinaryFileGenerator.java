package org.micro.kojanni.linear_sorting;

import lombok.experimental.UtilityClass;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

@UtilityClass
public class BinaryFileGenerator {

    /**
     * Генерирует бинарный файл с 16-битными числами
     *
     * @param filename имя файла
     * @param n        количество чисел
     */
    public static void generateBinaryFile(String filename, int n) {
        Random random = new Random();

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(filename))) {
            for (int i = 0; i < n; i++) {
                int value = random.nextInt(65536);
                dos.writeShort(value);
            }
            System.out.println("Файл успешно создан: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
