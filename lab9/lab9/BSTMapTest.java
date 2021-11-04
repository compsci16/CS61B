package lab9;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BSTMapTest {
    @Test
    public void testGet(){
        BSTMap<String, Integer> bstMap = new BSTMap<>();
        bstMap.put("c", 20);
        bstMap.put("a", 2);
        bstMap.put("b", 10);
        bstMap.put("z", 30);

        assertEquals(Integer.valueOf(30), bstMap.get("z"));
        assertEquals(Integer.valueOf(20), bstMap.get("c"));
        assertEquals(Integer.valueOf(10), bstMap.get("b"));
        assertNull(bstMap.get("d"));
    }
}
