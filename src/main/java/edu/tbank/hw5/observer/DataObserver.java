package edu.tbank.hw5.observer;

import java.util.List;

public interface DataObserver<T> {
    void update(List<T> data);
}
