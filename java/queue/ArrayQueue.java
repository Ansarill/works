package queue;

import java.util.Objects;

public class ArrayQueue extends AbstractQueue{
    private Object[] elements;
    private int start;
    public ArrayQueue() {
        super();
        elements = new Object[10];
        start = 9;
    }
    @Override
    protected Object elementImpl() {
        return elements[start];
    }

    @Override
    protected void clearImpl() {
        elements = new Object[10];
        start = 9;
    }

    @Override
    protected void enqueueImpl(Object element) {
        ensureCapacity(size + 1);
        elements[end()] = element;
    }

    @Override
    protected void dequeueImpl() {
        elements[start] = null;
        start = getByMod(start - 1);
    }
    private void ensureCapacity(int size) {
        if (size > elements.length) {
            changeCapacity(2 * size);
        }
    }
    private void changeCapacity(int newSize) {
        assert newSize >= size;
        var temp = new Object[newSize];
        if (end() < start) {
            System.arraycopy(elements, end() + 1, temp, 0, start - end());
            start = start - end() - 1;
        } else {
            System.arraycopy(elements, 0, temp, 0, start + 1);
            System.arraycopy(elements, end() + 1, temp, newSize - elements.length + 1 + end(), elements.length - 1 - end());

        }
        elements = temp;
    }

//    PRE: n >= 1
//    POST: R = a[n] & n' = n - 1 & immutable(1, n')
    public Object remove() {
        assert size >= 1;
        int last = getByMod(end() + 1);
        var result = elements[last];
        elements[last] = null;
        size--;
        return result;
    }

//    PRE: true
//    POST: immutable(1, n) & n' = n & R = a[1]..a[n]
    public Object[] toArray() {
        var result = new Object[size];
        int i = start;
        int j = 0;
        while (j != size) {
            result[j++] = elements[i];
            i = getByMod(i - 1);
        }
        return result;
    }

//    PRE: element != null
//    POST: for i=1..n: a'[i + 1] = a[i] & a'[1] = element & n' = n
    public void push(final Object element) {
        ensureCapacity(++size);
        if (start == elements.length - 1) {
            start = 0;
        } else {
            start++;
        }
        elements[start] = element;
    }

//    PRE: n >= 1
//    POST: R = a[n] & n' = n & immutable(1, n)
    public Object peek() {
        assert size >= 1;
        return elements[getByMod(end() + 1)];
    }

    private int end() {
        return getByMod(start - size);
    }

    private int getByMod(int a) {
        return (a + elements.length) % elements.length;
    }
}
