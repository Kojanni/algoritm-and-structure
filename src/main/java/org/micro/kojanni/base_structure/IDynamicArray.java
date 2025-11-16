package org.micro.kojanni.base_structure;

public interface IDynamicArray<T> {

    void add(T item, int index);

    T remove(int index);

    int size();
}
