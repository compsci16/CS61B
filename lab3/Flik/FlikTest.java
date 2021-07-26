import org.junit.Test;

import static org.junit.Assert.*;

public class FlikTest {

    @Test
    public void testIsSameNumber() {
        assertFalse(Flik.isSameNumber(2, 3));
        assertTrue(Flik.isSameNumber(0, 0));
        assertTrue(Flik.isSameNumber(-1, -1));
        assertTrue(Flik.isSameNumber(2, 2));
        assertFalse(Flik.isSameNumber(-2, 2));
        Integer a = 2;
        Integer b = 3;
        Integer c = 2;
        assertFalse(Flik.isSameNumber(a, b));
        assertTrue(Flik.isSameNumber(a, c));
    }
}
