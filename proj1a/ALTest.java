import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ALTest {
    private ArrayDeque<Integer> d;

    @Before
    public void init() {
        d = new ArrayDeque<>();
    }

    @Test
    public void testIsEmpty() {
        assertTrue(d.isEmpty());
    }

    @Test
    public void testAddFirst() {
        for (int i = 0; i < 7; i++) {
            d.addFirst(i);
        }
        d.printDeque();
    }

    @Test
    public void testAddLast() {
        for (int i = 0; i < 8; i++) {
            d.addLast(i);
        }
        d.printDeque();
    }

    @Test
    public void testPrintDeque() {
        for (int i = 0; i < 4; i++) {
            d.addLast(i);
        }
        for (int i = 4; i < 8; i++) {
            d.addFirst(i);
        }
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        d.printDeque();
        String expectedOutput = "7 6 5 4 0 1 2 3 \n";

        assertEquals(expectedOutput, outContent.toString());
        d.printDeque();
    }

    @Test
    public void testRemoveFirst() {
        for (int i = 0; i < 8; i++) {
            d.addLast(i);
        }
        for (int i = 0; i < 8; i++) {
            Integer n = d.removeFirst();
            assertEquals(Integer.valueOf(i), n);
        }
    }

    @Test
    public void testRemoveLast() {
        for (int i = 0; i < 8; i++) {
            d.addFirst(i);
        }
        for (int i = 0; i < 8; i++) {
            Integer n = d.removeLast();
            assertEquals(Integer.valueOf(i), n);
        }
    }


    @Test
    public void testResize() {
        StringBuilder expectedOutput = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            d.addFirst(i);
            expectedOutput.append(999 - i).append(" ");
        }
        expectedOutput.append("\n");
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        d.printDeque();
        assertEquals(expectedOutput.toString(), outContent.toString());
    }

    @Test
    public void testGet() {
        for (int i = 0; i < 1000; i++) {
            d.addLast(i);
        }
        for (int i = 0; i < 1000; i++) {
            assertEquals(Integer.valueOf(i), d.get(i));
        }
    }

}

