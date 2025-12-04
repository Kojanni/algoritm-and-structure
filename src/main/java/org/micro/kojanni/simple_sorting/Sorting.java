package org.micro.kojanni.simple_sorting;

import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class Sorting {

    private int[] array;
    private int size;

    public Sorting() {
    }

    public String processSort(String[] args) {
        int size = Integer.parseInt(args[0]);
        this.size = size;

        int[] array = Arrays.stream(args[1].split(" ")).mapToInt(Integer::parseInt).toArray();
        this.array = array;

        return Arrays.stream(sort(array))
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(" "));
    }

    abstract int[] sort(int[] array);


    public void print() {
        if (size == 0) {
            System.out.println("Входной массив пустой");
            return;
        }

        System.out.println(Arrays.stream(array)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(" ")));
    }
}