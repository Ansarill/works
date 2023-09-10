package queue;

public class ArrayQueueADT {
    private Object[] elements;
    private int start;
    private int size;

    public ArrayQueueADT() {
        elements = new Object[10];
        start = 9;
        size = 0;
    }

//    PRE: n >= 1
//    POST: R = a[1] & immutable(1, n) & n' = n
    public static Object element(final ArrayQueueADT q) {
        assert q.size >= 1;
        return q.elements[q.start];
    }

//    PRE: true
//    POST: R = n & immutable(1, n) & n' = n
    public static int size(final ArrayQueueADT q) {
        return q.size;
    }

//    PRE: true
//    POST: immutable(1, n) & R = (n == 0) & n' = n
    public static boolean isEmpty(final ArrayQueueADT q) {
        return q.size == 0;
    }

//    PRE: true
//    POST: n' = 0
    public static void clear(ArrayQueueADT q) {
        q.elements = new Object[10];
        q.size = 0;
        q.start = 9;
    }

//    PRE: element != null
//    POST: n' = n + 1 & immutable(1, n) & a[n'] = element
    public static void enqueue(final ArrayQueueADT q, Object element) {
        ensureCapacity(q, q.size + 1);
        q.elements[end(q)] = element;
        q.size++;
    }

    //    PRE: n >= 1
//    POST: for i=1..n-1: a'[i] = a[i + 1] & n' = n-1 & R = a[1]
    public static Object dequeue(final ArrayQueueADT q) {
        assert q.size >= 1;
        var result = q.elements[q.start];
        q.elements[q.start--] = null;
        if (q.start < 0) {
            q.start = q.elements.length - 1;
        }
        q.size--;
        return result;
    }

//    PRE: true
//    POST: immutable(1, n) & n' = n & R = a[1]..a[n]
    public static Object[] toArray(final ArrayQueueADT q) {
        Object[] a = new Object[q.size];
        for (int i = 0; i < q.size; i++) {
            a[i] = dequeue(q);
            enqueue(q, a[i]);
        }
        return a;
    }
//    PRE: element != null
//    POST: for i=1..n: a'[i + 1] = a[i] & a'[1] = element & n' = n
    public static void push(final ArrayQueueADT q, final Object element) {
        ensureCapacity(q, ++q.size);
        if (q.start == q.elements.length - 1) {
            q.start = 0;
        } else {
            q.start++;
        }
        q.elements[q.start] = element;
    }
//    PRE: n >= 1
//    POST: R = a[n] & n' = n & immutable(1, n)
    public static Object peek(final ArrayQueueADT q) {
        assert q.size >= 1;
        return q.elements[(end(q) + 1) % q.elements.length];
    }
//    PRE: n >= 1
//    POST: R = a[n] & n' = n - 1 & immutable(1, n')
    public static Object remove(final ArrayQueueADT q) {
        assert q.size >= 1;
        int last = (end(q) + 1) % q.elements.length;
        var result = q.elements[last];
        q.elements[last] = null;
        q.size--;
        return result;
    }

    private static void ensureCapacity(final ArrayQueueADT q, int size) {
        if (size > q.elements.length) {
            var temp = new Object[(q.size + 2) * 2];
            System.arraycopy(q.elements, 0, temp, 0, q.start + 1);
            if (q.start + 1 != q.elements.length) {
                System.arraycopy(q.elements, q.start + 1, temp, temp.length - q.elements.length + q.start + 1, q.elements.length - q.start - 1);
            }
            q.elements = temp;
        }
    }
    private static int end(final ArrayQueueADT q) {
        return (q.elements.length + q.start - q.size) % q.elements.length;
    }
}