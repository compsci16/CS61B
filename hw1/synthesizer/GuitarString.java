
package synthesizer;

import java.util.HashMap;
import java.util.Map;

//Make sure this class is public
public class GuitarString {
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    private BoundedQueue<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        int capacity = (int) Math.round(SR / frequency);
        buffer = new ArrayRingBuffer<Double>(capacity);
        for (int i = 0; i < capacity; i++) {
            buffer.enqueue(0D);
        }
    }

    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        while (!buffer.isEmpty()) {
            buffer.dequeue();
        }
        Map<Integer, Double> randoms = new HashMap<>();
        int index = 0;
        while (!buffer.isFull()) {
            double r = Math.random() - 0.5;
            if (!randoms.containsValue(r)) {
                randoms.put(index++, r);
                buffer.enqueue(r);
            }
        }

    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    public void tic() {
        double front = buffer.dequeue();
        double last = buffer.peek();
        double insert = (front + last) / 2 * DECAY;
        buffer.enqueue(insert);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        return buffer.peek();
    }


}
