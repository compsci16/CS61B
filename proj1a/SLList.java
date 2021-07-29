public class SLList<T> {
    public class Node {
        private T item;
        private Node next;

        public Node(T i, Node n) {
            item = i;
            next = n;
        }
    }

    private Node first;
    private int size;

    public SLList(T x) {
        first = new Node(x, null);
        size = 0;
    }

    /**
     * Adds an item to the front of the list.
     */
    public void addFirst(T x) {
        first = new Node(x, first);
        size++;
    }

    /**
     * Retrieves the front item from the list.
     */
    public T getFirst() {
        return first.item;
    }

    /**
     * Adds an item to the end of the list.
     */
    public void addLast(T x) {
        Node p = this.first;
        while (p.next != null) {
            p = p.next;
        }
        p.next = new Node(x, null);
        size++;
    }

    /**
     * Returns the number of items in the list using recursion.
     */
    public int size() {
        return size;
    }

}

