public class AList<T> {
    private final T[] list;
    private int size;

    /**
     * Creates an empty list.
     */
    public AList() {
        list = (T[]) new Object[8];
        size = 0;
    }

    /**
     * Inserts X into the back of the list.
     */
    public void addLast(T x) {
        list[size] = x;
        size++;
    }

    /**
     * Returns the item from the back of the list.
     */
    public T getLast() {
        return list[size - 1];
    }

    /**
     * Gets the ith item in the list (0 is the front).
     */
    public T get(int i) {
        return list[i];
    }

    /**
     * Returns the number of items in the list.
     */
    public int size() {
        return size;
    }

    /**
     * Deletes item from back of the list and
     * returns deleted item.
     */
    public T removeLast() {
        T last = list[size - 1];
        list[size - 1] = null;
        size--;
        return last;
    }

}


