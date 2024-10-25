package edu.tbank.hw3;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

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

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index is out of bounds");
        }
    }

    @Override
    public T get(int index) {
        checkIndex(index);
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
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
        checkIndex(index);
        if (index == 0) {
            head = head.getNext();
            size--;
            return;
        }
        Node<T> current = head;
        for (int i = 0; i < index - 1; i++) {
            current = current.getNext();
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

    @Override
    public CustomIterator<T> iterator() {
        return new CustomLinkedListIterator();
    }

    public void addAll(CustomLinkedListImpl<T> list) {
        Node<T> current = list.head;
        while (current != null) {
            add(current.getData());
            current = current.getNext();
        }
    }

    public static <T> CustomLinkedListImpl<T> toCustomLinkedList(Stream<T> stream) {
        return stream.reduce(new CustomLinkedListImpl<>(), (list, element) -> {
            list.add(element);
            return list;
        }, (list1, list2) -> {
            list1.addAll(list2);
            return list1;
        });
    }

    @Setter
    @Getter
    @AllArgsConstructor
    private static class Node<T> {
        private T data;
        private Node<T> next;
    }

    private class CustomLinkedListIterator implements CustomIterator<T>{
        private Node<T> currentNode = head;

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T data = currentNode.getData();
            currentNode = currentNode.getNext();
            return data;
        }

        @Override
        public void forEachRemaining(Consumer<? super T> action) {
            Objects.requireNonNull(action);
            while (hasNext())
                action.accept(next());
        }
    }

}
