package queue;

import java.util.Objects;

public class LinkedQueue extends AbstractQueue {
    private Node first;
    private Node last;
    @Override
    protected Object elementImpl() {
        return first.value;
    }

    @Override
    protected void clearImpl() {
        first = null;
        last = null;
    }

    @Override
    protected void enqueueImpl(Object element) {
        Node temp = new Node(element, null);
        if (isEmpty()) {
            first = temp;
        } else {
            last.prev = temp;
        }
        last = temp;
    }

    @Override
    protected void dequeueImpl() {
        first = first.prev;
        if (size == 1) {
            last = null;
        }
    }

    private static class Node {
        private final Object value;
        private Node prev;
        public Node(Object value, Node prev) {
            assert value != null;
            this.value = value;
            this.prev = prev;
        }
    }
}


