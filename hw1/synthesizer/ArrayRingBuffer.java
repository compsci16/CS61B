package synthesizer;

import java.util.Iterator;

public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    private int first, last;
    private final T[] arr;

    public ArrayRingBuffer(int capacity) {
        first = -1;
        last = 0;
        this.capacity = capacity;
        arr = (T[]) new Object[capacity];
    }



    @Override
    public Iterator<T> iterator() {
        return new BufferIterator();
    }

    private class BufferIterator implements Iterator<T> {
        private int ptr;
        private int iterateThrough;

        BufferIterator() {
            ptr = first;
            iterateThrough = 0;
        }

        @Override
        public boolean hasNext() {
            return iterateThrough != fillCount;
        }

        @Override
        public T next() {
            T returnItem = arr[ptr];
            ptr = limitTheIndex(++ptr);
            ++iterateThrough;
            return returnItem;
        }
    }

    @Override
    public void enqueue(T x) {
        if (isFull()) {
            throw new RuntimeException("Ring  Buffer Overflow");
        }
        if (isEmpty()) {
            first = last;
        }
        fillCount++;
        arr[last] = x;
        last = limitTheIndex(++last);
    }

    @Override
    public T dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Ring Buffer Underflow");
        }
        fillCount--;
        T toRemove = arr[first];
        first = limitTheIndex(++first);
        return toRemove;
    }

    private int limitTheIndex(int index) {
        return index % capacity;
    }

    @Override
    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Ring Buffer Underflow");
        }
        return arr[first];
    }

}
