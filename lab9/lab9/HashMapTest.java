package lab9;


import edu.princeton.cs.algs4.In;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class HashMapTest {
    private HashMap<String, Integer> bstMap;

    @Before
    public void init() {
        bstMap = new HashMap<>();
        bstMap.put("c", 20);
        bstMap.put("a", 2);
        bstMap.put("b", 10);
        bstMap.put("z", 30);
    }

    @Test
    public void testGet() {
        assertEquals(Integer.valueOf(30), bstMap.get("z"));
        assertEquals(Integer.valueOf(20), bstMap.get("c"));
        assertEquals(Integer.valueOf(10), bstMap.get("b"));
        assertNull(bstMap.get("d"));
    }

    @Test
    public void testKeySet() {
        bstMap.put("z", 100);
        System.out.println(bstMap.keySet());
    }

    @Test
    public void testRemove() {
        assertEquals(Integer.valueOf(20), bstMap.remove("c"));
        assertEquals(Integer.valueOf(2), bstMap.remove("a"));
        assertEquals(Integer.valueOf(10), bstMap.remove("b"));

    }

    //@Test
//    public void testIterator(){
//        bstMap.remove("z");
//        for(String s : bstMap){
//            System.out.println(s);
//        }
//    }
}
