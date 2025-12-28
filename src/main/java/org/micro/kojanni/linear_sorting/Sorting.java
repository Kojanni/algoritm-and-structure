package org.micro.kojanni.linear_sorting;

import org.micro.kojanni.visual.SortListener;

import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class Sorting {

    protected SortListener listener;
    protected int delayMs = 800;

    private int[] array;
    private int length;
    public abstract String getName();

    public Sorting() {
    }
    public void setListener(SortListener listener) {
        this.listener = listener;
    }

    public void setDelay(int delayMs) {
        this.delayMs = delayMs;
    }


    protected void step(int[] array, int i, int j) {
        if (listener != null) {
            listener.onStep(array.clone(), i, j);
            sleep();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException ignored) {}
    }

    public String processSort(String[] args) {
        int size = Integer.parseInt(args[0]);
        this.length = size;

        int[] array = Arrays.stream(args[1].split(" ")).mapToInt(Integer::parseInt).toArray();
        this.array = array;

        return Arrays.stream(sort(array))
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(" "));
    }

    public abstract int[] sort(int[] array);


    public void print() {
        if (length == 0) {
            System.out.println("Входной массив пустой");
            return;
        }

        System.out.println(Arrays.stream(array)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(" ")));
    }

    @Override
    public String toString() {
        return getName();
    }
}