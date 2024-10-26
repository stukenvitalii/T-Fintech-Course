package edu.tbank.hw3;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CustomLinkedListImplTest {

    @Test
    void getElementAtIndex() {
        CustomLinkedListImpl<Integer> list = new CustomLinkedListImpl<>();
        list.add(1);
        list.add(2);
        list.add(3);
        assertEquals(2, list.get(1));
    }

    @Test
    void getElementAtNegativeIndexThrowsException() {
        CustomLinkedListImpl<Integer> list = new CustomLinkedListImpl<>();
        list.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
    }

    @Test
    void getElementFromEmptyListThrowsException() {
        CustomLinkedListImpl<Integer> list = new CustomLinkedListImpl<>();
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
    }

    @Test
    void addElementIncreasesSize() {
        CustomLinkedListImpl<Integer> list = new CustomLinkedListImpl<>();
        list.add(1);
        assertEquals(1, list.size());
    }

    @Test
    void removeElementAtIndex() {
        CustomLinkedListImpl<Integer> list = new CustomLinkedListImpl<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.remove(1);
        assertEquals(2, list.size());
        assertEquals(3, list.get(1));
    }

    @Test
    void removeElementAtNegativeIndexThrowsException() {
        CustomLinkedListImpl<Integer> list = new CustomLinkedListImpl<>();
        list.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
    }

    @Test
    void removeElementFromEmptyListThrowsException() {
        CustomLinkedListImpl<Integer> list = new CustomLinkedListImpl<>();
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));
    }

    @Test
    void containsElement() {
        CustomLinkedListImpl<Integer> list = new CustomLinkedListImpl<>();
        list.add(1);
        list.add(2);
        assertTrue(list.contains(2));
    }

    @Test
    void doesNotContainElement() {
        CustomLinkedListImpl<Integer> list = new CustomLinkedListImpl<>();
        list.add(1);
        list.add(2);
        assertFalse(list.contains(3));
    }

    @Test
    void addAllElementsFromList() {
        CustomLinkedListImpl<Integer> list = new CustomLinkedListImpl<>();
        list.addAll(List.of(1, 2, 3));
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test
    void convertEmptyStreamToCustomLinkedList() {
        Stream<Integer> stream = Stream.empty();
        CustomLinkedListImpl<Integer> list = CustomLinkedListImpl.toCustomLinkedList(stream);
        assertEquals(0, list.size());
    }

    @Test
    void convertStreamWithNullElementsToCustomLinkedList() {
        Stream<Integer> stream = Stream.of(1, null, 3);
        CustomLinkedListImpl<Integer> list = CustomLinkedListImpl.toCustomLinkedList(stream);
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertNull(list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test
    void iteratorNext_ReturnsCorrectElements_whenCalled() {
        CustomLinkedListImpl<Integer> list = new CustomLinkedListImpl<>();
        list.add(1);
        list.add(2);

        CustomIterator<Integer> iterator = list.iterator();

        Integer first = iterator.next();
        assertEquals(1, first);

        Integer second = iterator.next();
        assertEquals(2, second);
    }

    @Test
    void iteratorHasNext_ReturnsFalse_OnTheEndOfList() {
        CustomLinkedListImpl<Integer> list = new CustomLinkedListImpl<>();
        list.add(1);
        list.add(2);

        CustomIterator<Integer> iterator = list.iterator();
        iterator.next();
        iterator.next();

        boolean result = iterator.hasNext();

        assertFalse(result);
    }

    @Test
    void iteratorNext_ThrowsNoSuchElementException_WhenNoElementsLeft() {
        CustomLinkedListImpl<Integer> list = new CustomLinkedListImpl<>();
        list.add(1);
        list.add(2);

        CustomIterator<Integer> iterator = list.iterator();
        iterator.next();
        iterator.next();

        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void forEachRemaining_ShouldProcessRemainingElements_AfterNextIsCalled() {
        CustomLinkedListImpl<String> list = new CustomLinkedListImpl<>();
        list.add("first");
        list.add("second");
        list.add("third");

        List<String> collectedElements = new ArrayList<>();

        CustomIterator<String> iterator = list.iterator();
        iterator.next();

        iterator.forEachRemaining(collectedElements::add);

        assertEquals(List.of("second", "third"), collectedElements);
    }
}