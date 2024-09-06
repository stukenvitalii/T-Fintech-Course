package edu.tbank.hw3;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Node<T> {
    private T data;
    private Node<T> next;
}
