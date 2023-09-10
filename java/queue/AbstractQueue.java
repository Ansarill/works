package queue;


import java.util.Objects;

public abstract class AbstractQueue implements Queue{
    protected int size = 0;

    public Object element() {
        assert size >= 1;
        return elementImpl();
    }
    protected abstract Object elementImpl();

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        size = 0;
        clearImpl();
    }

    protected abstract void clearImpl();

    public void enqueue(Object element) {
        enqueueImpl(element);
        size++;
    }

    protected abstract void enqueueImpl(Object element);

    public Object dequeue() {
        assert size >= 1;
        var result = element();
        dequeueImpl();
        size--;
        return result;
    }
    @Override
    public boolean removeFirstOccurrence(Object element) {
        boolean found = false;
        for (int j = 0, n = size; j < n; j++) {
            if (!found && Objects.equals(element(), element)) {
                dequeue();
                found = true;
            } else {
                enqueue(dequeue());
            }
        }
        return found;
    }

    @Override
    public boolean contains(Object element) {
        boolean found = false;
        for (int j = 0, n = size; j < n; j++) {
            if (!found && Objects.equals(element, element())) {
                found = true;
            }
            enqueue(dequeue());
        }
        return found;
    }

    protected abstract void dequeueImpl();

}

