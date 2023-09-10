package queue;

public interface Queue {
//    Model a[1]...a[n], n >= 0
//    for i=1..n: a[i] != null
//    let Immutable(a, b): for i=a..b: a'[i]=a[i]

//
    boolean contains(Object element);
    boolean removeFirstOccurrence(Object element);

//    PRE: n >= 1
//    POST: R = a[1] & immutable(1, n) & n' = n
    Object element();

//    PRE: true
//    POST: R = n & immutable(1, n) & n' = n
    int size();

//    PRE: true
//    POST: immutable(1, n) & R = (n == 0) & n' = n
    boolean isEmpty();

//    PRE: true
//    POST: n' = 0
    void clear();

//    PRE: element != null
//    POST: n' = n + 1 & immutable(1, n) & a[n'] = element
    void enqueue(Object element);

//    PRE: n >= 1
//    POST: for i=1..n-1: a'[i] = a[i + 1] & n' = n-1 & R = a[1]
    Object dequeue();
}
