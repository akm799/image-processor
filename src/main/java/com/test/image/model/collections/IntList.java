package com.test.image.model.collections;

public interface IntList {

    void add(int value);

    void clear();

    boolean contains(int value);

    int get(int index);

    int indexOf(int value);

    int lastIndexOf(int value);

    boolean isEmpty();

    IntIterator iterator();

    void set(int index, int value);

    int size();

    int[] toArray();
}
