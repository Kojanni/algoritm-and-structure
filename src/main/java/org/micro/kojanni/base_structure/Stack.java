package org.micro.kojanni.base_structure;

public class Stack<T> {

    private Object[] data;
    private int size;
    private int capacity;

    public Stack() {
        this(10);
    }

    public Stack(int initialCapacity) {
        this.capacity = initialCapacity;
        this.data = new Object[capacity];
        this.size = 0;
    }

    /**
     * Добавление элемента на вершину стека.
     */
    public void push(T item) {
        if (size >= capacity) {
            resize();
        }
        data[size++] = item;
    }

    /**
     * Удаление элемента с вершины стека.
     */
    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        T item = (T) data[--size];
        data[size] = null;
        return item;
    }

    /**
     * Получение элемента с вершины стека без удаления.
     */
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return (T) data[size - 1];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Получение количества элементов в стеке.
     */
    public int size() {
        return size;
    }

    private void resize() {
        capacity *= 2;
        Object[] newData = new Object[capacity];
        System.arraycopy(data, 0, newData, 0, size);
        data = newData;
    }
}
