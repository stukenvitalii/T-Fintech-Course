package edu.tbank.hw3;

import java.util.List;

public interface CustomLinkedList<T> {
    void add(T t);
    T get(int index);
    void remove(int index);
    int size();
    boolean contains(T t);
    void addAll(List<T> list);
}
