package edu.tbank.hw3;

import java.util.List;

public class CustomLinkedListImpl<T> implements CustomLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    public CustomLinkedListImpl() {
        head = null;
        tail = null;
        size = 0;
    }

    private boolean isEmpty() {
        return head == null;
    }

    @Override
    public T get(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index cannot be negative");
        }
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("List is empty");
        }
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            if (current == null) {
                throw new IndexOutOfBoundsException("Index is out of bounds");
            }
            current = current.getNext();
        }
        return current.getData();
    }

    @Override
    public void add(T t) {
        Node<T> newNode = new Node<>(t, null);
        if (isEmpty()) {
            head = newNode;
        } else {
            tail.setNext(newNode);
        }
        tail = newNode;
        size++;
    }

    @Override
    public void remove(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index cannot be negative");
        }
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("List is empty");
        }
        if (index == 0) {
            head = head.getNext();
            size--;
            return;
        }
        Node<T> current = head;
        for (int i = 0; i < index - 1; i++) {
            if (current == null) {
                throw new IndexOutOfBoundsException("Index is out of bounds");
            }
            current = current.getNext();
        }
        if (current.getNext() == null) {
            throw new IndexOutOfBoundsException("Index is out of bounds");
        }
        current.setNext(current.getNext().getNext());
        size--;
    }

    @Override
    public int size() {
        return size;
    }

    public boolean contains(T t) {
        Node<T> current = head;
        while (current != null) {
            if (current.getData().equals(t)) {
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    @Override
    public void addAll(List<T> list) {
        for (T t : list) {
            add(t);
        }
    }
}
