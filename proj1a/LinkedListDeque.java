public class LinkedListDeque<T> {
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

    public void addFirst(T item) {
        Node oldFirst = sentinel.next;
        Node newFirst = new Node(item, sentinel, oldFirst);
        oldFirst.prev = newFirst;
        sentinel.next = newFirst;
        size++;
    }

    public void addLast(T item) {
        Node oldLast = sentinel.prev;
        Node newLast = new Node(item, oldLast, sentinel);
        oldLast.next = newLast;
        sentinel.prev = newLast;
        size++;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        Node p = sentinel.next;
        while (p != sentinel) {
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.println();
    }

    public T removeFirst() {
        T oldFirstItem = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        size--;
        return oldFirstItem;
    }

    public T removeLast() {
        T oldLastItem = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        size--;
        return oldLastItem;
    }

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
