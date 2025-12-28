package org.micro.kojanni.visual;

import org.micro.kojanni.linear_sorting.Sorting;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SortingAlgorithmFactory {
    private static final String BASE_PACKAGE = "org.micro.kojanni.linear_sorting";
    private static Map<String, Class<? extends Sorting>> sortingAlgorithms;

    static {
        discoverSortingAlgorithms();
    }

    private static void discoverSortingAlgorithms() {
        sortingAlgorithms = new LinkedHashMap<>();

        try {
            Class<?>[] classes = {
                    Class.forName(BASE_PACKAGE + ".CountingSort"),
                    Class.forName(BASE_PACKAGE + ".RadixSort"),
                    Class.forName(BASE_PACKAGE + ".BucketSort")
            };

            for (Class<?> clazz : classes) {
                if (Sorting.class.isAssignableFrom(clazz) && clazz.isAnnotationPresent(SortingAlgorithm.class)) {
                    SortingAlgorithm annotation = clazz.getAnnotation(SortingAlgorithm.class);
                    sortingAlgorithms.put(annotation.name(), (Class<? extends Sorting>) clazz);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getAvailableAlgorithms() {
        return new ArrayList<>(sortingAlgorithms.keySet());
    }

    public static Sorting createSorter(String algorithmName) {
        try {
            Class<? extends Sorting> clazz = sortingAlgorithms.get(algorithmName);
            if (clazz != null) {
                return clazz.getDeclaredConstructor().newInstance();
            }
            throw new IllegalArgumentException("Unknown sorting algorithm: " + algorithmName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create sorter: " + algorithmName, e);
        }
    }
}
