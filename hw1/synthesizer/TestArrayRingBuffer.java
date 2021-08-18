package synthesizer;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the ArrayRingBuffer class.
 *
 * @author Josh Hug
 */

public class TestArrayRingBuffer {
    private static ArrayRingBuffer<Integer> arb;

    @Before
    public void init() {
        arb = new ArrayRingBuffer<>(4);
    }

    @Test
    public void testEnqueue() {
//        arb.enqueue(0);
//        assertEquals("0", arb.getString());
//        arb.enqueue(1);
//        assertEquals("0 1", arb.getString());
//        arb.enqueue(2);
//        arb.enqueue(3);
//        assertEquals("0 1 2 3", arb.getString());
//
//        // no enqueue after full:
//        arb.enqueue(3);
//        arb.enqueue(3);
//        assertEquals("0 1 2 3", arb.getString());
    }

    private static Integer toInt(int x) {
        return x;
    }

    @Test
    public void testDequeue() {
        arb.enqueue(0);
        assertEquals(Integer.valueOf(0), arb.dequeue());
        arb.enqueue(0);
        arb.enqueue(1);
        assertEquals(toInt(0), arb.dequeue());
        assertEquals(toInt(1), arb.peek());

        arb.enqueue(2);
        arb.enqueue(3);
        arb.enqueue(4);

        assertEquals(toInt(1), arb.peek());
        assertEquals(4, arb.fillCount());
        assertEquals(toInt(1), arb.dequeue());
    }

    @Test
    public void testForEach() {
        arb.enqueue(1);
        arb.enqueue(2);
        arb.enqueue(3);
        arb.enqueue(4);
        for (Integer i : arb) {
            System.out.print(i); // expect 1 to 4
        }
    }

    /**
     * Calls tests for ArrayRingBuffer.
     */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
