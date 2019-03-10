package com.test.image.model.collections;

public interface IntCollection {

    void add(int value);

    void addAll(IntCollection intCollection);

    void clear();

    boolean contains(int value);

    boolean isEmpty();

    IntIterator iterator();

    int size();
}