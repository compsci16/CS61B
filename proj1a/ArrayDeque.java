public class ArrayDeque<T> {
    private T[] items;
    private int nextFirst;
    private int nextLast;
    private int size;
    private static final double LOAD_FACTOR = 0.25;
    private static final int REFACTOR = 2;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 0;
        nextLast = 1;
    }


    public void addFirst(T item) {
        items[nextFirst] = item;
        size++;
        nextFirst = decrementIndex(nextFirst);
        resizeIfRequired();
    }

    public void addLast(T item) {
        items[nextLast] = item;
        size++;
        nextLast = incrementIndex(nextLast);
        resizeIfRequired();
    }


    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        int firstIndex = getFirstIndex();
        T firstItem = items[firstIndex];
        items[firstIndex] = null;
        nextFirst = incrementIndex(nextFirst);
        size--;
        resizeIfRequired();
        return firstItem;
    }

    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        int lastIndex = getLastIndex();
        T lastItem = items[lastIndex];
        items[lastIndex] = null;
        nextLast = decrementIndex(nextLast);
        size--;
        resizeIfRequired();
        return lastItem;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }


    private int getFirstIndex() {
        int first = nextFirst + 1;
        if (first == items.length) {
            first = 0;
        }
        return first;
    }

    private int getLastIndex() {
        int last = nextLast - 1;
        if (last == -1) {
            last = items.length - 1;
        }
        return last;
    }


    private int incrementIndex(int index) {
        index = (index + 1) % items.length;
        return index;
    }

    private int decrementIndex(int index) {
        index = (index - 1);
        if (index == -1) {
            index = items.length - 1;
        }
        return index;
    }

    private void resizeIfRequired() {
        int length = items.length;
        if (size == length) {
            resize(length * REFACTOR);
        } else if (length >= 16 && (1.0 * size / length) < LOAD_FACTOR) {
            resize(length / REFACTOR);
        }
    }

    private void resize(int capacity) {
        int firstIndex = getFirstIndex();
        T[] newList = (T[]) new Object[capacity];
        int newListIndex = 0;
        for (int i = firstIndex; i < items.length && items[i] != null; i++) {
            newList[newListIndex++] = items[i];
        }
        for (int i = 0; i < firstIndex && items[i] != null; i++) {
            newList[newListIndex++] = items[i];
        }
        items = newList;
        nextFirst = newList.length - 1;
        nextLast = newListIndex;
    }


    public T get(int index) {
        int firstIndex = getFirstIndex();
        int getIndex = firstIndex + index;
        if (getIndex > items.length - 1) {
            getIndex = ((items.length - 1) - (getIndex));
        }
        return items[getIndex];
    }


    public void printDeque() {
        for (int i = getFirstIndex(); i < items.length && items[i] != null; i++) {
            System.out.print(items[i] + " ");
        }
        for (int i = 0; i < getFirstIndex() && items[i] != null; i++) {
            System.out.print(items[i] + " ");
        }
        System.out.println();
    }

}
