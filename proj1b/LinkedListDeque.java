public class LinkedListDeque<T> implements Deque<T>{
    private class Node {
        private final T item;
        private Node next;
        private Node prev;

        Node(T item, Node prev, Node next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    private final Node sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    @Override
    public void addFirst(T item) {
        Node oldFirst = sentinel.next;
        Node newFirst = new Node(item, sentinel, oldFirst);
        oldFirst.prev = newFirst;
        sentinel.next = newFirst;
        size++;
    }

    @Override
    public void addLast(T item) {
        Node oldLast = sentinel.prev;
        Node newLast = new Node(item, oldLast, sentinel);
        oldLast.next = newLast;
        sentinel.prev = newLast;
        size++;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        Node p = sentinel.next;
        while (p != sentinel) {
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T oldFirstItem = sentinel.next.item;
        Node oldFirstSuccessor = sentinel.next.next;
        oldFirstSuccessor.prev = sentinel;
        sentinel.next = oldFirstSuccessor;
        size--;
        return oldFirstItem;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        T oldLastItem = sentinel.prev.item;
        Node oldLastPredecessor = sentinel.prev.prev;
        sentinel.prev = oldLastPredecessor;
        oldLastPredecessor.next = sentinel;
        size--;
        return oldLastItem;
    }

    @Override
    public T get(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        Node p = sentinel.next;
        for (int i = 0; i < index; i++) {
            p = p.next;
        }
        return p.item;
    }

    public T getRecursive(int index) {
        return getRecursiveHelper(sentinel.next, index);
    }

    private T getRecursiveHelper(Node current, int index) {
        if (current == sentinel) {
            return null;
        }
        if (index == 0) {
            return current.item;
        }
        return getRecursiveHelper(current.next, --index);
    }
}
