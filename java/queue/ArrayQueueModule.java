package queue;
    /*
    Model: a[1]...a[n], n >= 0
    Invariant: for i=1..n: a[i] != null

    Let immutable(s, t): for i=s..t: a'[i] = a[i]

    PRE: element != null
    POST: n' = n + 1 & immutable(1, n) & a[n'] = element
    enqueue(element)

    PRE: n >= 1
    POST: R = a[1] & immutable(1, n) & n' = n
    element()

    PRE: n >= 1
    POST: for i=1..n-1: a'[i] = a[i + 1] & n' = n-1 & R = a[1]
    dequeue()

    PRE: true
    POST: R = n & immutable(1, n) & n' = n
    size()

    PRE: true
    POST: immutable(1, n) & R = (n == 0) & n' = n
    isEmpty()

    PRE: true
    POST: n' = 0
    clear()

    */

public class ArrayQueueModule {
    private static Object[] elements;
    private static int start;
    private static int size;


    static {
        clear();
    }

//    PRE: n >= 1
//    POST: R = a[1] & immutable(1, n) & n' = n
    public static Object element() {
        assert size >= 1;
        return elements[start];
    }

//    PRE: true
//    POST: R = n & immutable(1, n) & n' = n
    public static int size() {
        return size;
    }

//    PRE: true
//    POST: immutable(1, n) & R = (n == 0) & n' = n
    public static boolean isEmpty() {
        return size == 0;
    }

//    PRE: true
//    POST: n' = 0
    public static void clear() {
        elements = new Object[10];
        start = 9;
        size = 0;
    }

//    PRE: element != null
//    POST: n' = n + 1 & immutable(1, n) & a[n'] = element
    public static void enqueue(final Object element) {
        ensureCapacity(size + 1);
        elements[end()] = element;
        size++;
    }

//    PRE: n >= 1
//    POST: for i=1..n-1: a'[i] = a[i + 1] & n' = n-1 & R = a[1]
    public static Object dequeue() {
        assert size >= 1;
        var result = elements[start];
        elements[start--] = null;
        if (start < 0) {
            start = elements.length - 1;
        }
        size--;
        return result;
    }

//    PRE: true
//    POST: immutable(1, n) & n' = n & R = a[1]..a[n]
    public static Object[] toArray() {
        var result = new Object[size];
        int i = start;
        int j = 0;
        while (j != size) {
            result[j++] = elements[i--];
            if (i < 0) {
                i = elements.length - 1;
            }
        }
        return result;
    }
//    PRE: element != null
//    POST: for i=1..n: a'[i + 1] = a[i] & a'[1] = element & n' = n
    public static void push(final Object element) {
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
    public static Object peek() {
        assert size >= 1;
        return elements[(end() + 1) % elements.length];
    }
//    PRE: n >= 1
//    POST: R = a[n] & n' = n - 1 & immutable(1, n')
    public static Object remove() {
        assert size >= 1;
        int last = (end() + 1) % elements.length;
        var result = elements[last];
        elements[last] = null;
        size--;
        return result;
    }
    private static void ensureCapacity(int size) {
        if (size > elements.length) {
            var temp = new Object[(size + 2) * 2];
            System.arraycopy(elements, 0, temp, 0, start + 1);
            if (start + 1 != elements.length) { // [start + 1, elements.length) -> [x, temp.length)
                System.arraycopy(elements, start + 1, temp,
                        temp.length - elements.length + start + 1, elements.length - start - 1);
            }
            elements = temp;
        }
    }
    private static int end() {
        return (elements.length + start - size) % elements.length;
    }
}
