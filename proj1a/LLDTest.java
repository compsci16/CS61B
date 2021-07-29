import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LLDTest {
    private LinkedListDeque<Integer> d;

    @Before
    public void init() {
        d = new LinkedListDeque<>();
        d.addLast(5);
        d.addFirst(2);
        d.addFirst(1);
        d.addLast(10);
    }

    @Test
    public void testAddFirst() {
        d.addFirst(2);
    }

    @Test
    public void testAddLast() {
        d.addLast(100);
    }

    @Test
    public void testPrintDeque() {
        d.printDeque();
    }

    @Test
    public void testRemoveFirst() {
        Integer i = d.removeFirst();
        for (int j : new int[]{1, 2, 5, 10}) {
            assertEquals(Integer.valueOf(j), i);
            i = d.removeFirst();
        }
        assertNull(i);
    }

    @Test
    public void testGet() {
        assertEquals(Integer.valueOf(1), d.get(0));
        assertEquals(Integer.valueOf(10), d.get(3));
        assertEquals(Integer.valueOf(2), d.get(1));
        assertNull(d.get(4));
    }

    @Test
    public void testGetRecursive() {
        assertEquals(Integer.valueOf(1), d.getRecursive(0));
        assertEquals(Integer.valueOf(10), d.getRecursive(3));
        assertEquals(Integer.valueOf(2), d.getRecursive(1));
        assertNull(d.getRecursive(4));
    }

    @Test
    public void testRemoveLast() {
        Integer i = d.removeLast();
        for (int j : new int[]{10, 5, 2, 1}) {
            assertEquals(Integer.valueOf(j), i);
            i = d.removeLast();
        }
        assertNull(i);
    }
}
