package lab9;


import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class HashMapTest {
    private MyHashMap<String, Integer> hashMap;

    @Before
    public void init() {
        hashMap = new MyHashMap<>();
        hashMap.put("c", 20);
        hashMap.put("a", 2);
        hashMap.put("b", 10);
        hashMap.put("z", 30);
    }

    @Test
    public void testGet() {
        assertEquals(Integer.valueOf(30), hashMap.get("z"));
        assertEquals(Integer.valueOf(20), hashMap.get("c"));
        assertEquals(Integer.valueOf(10), hashMap.get("b"));
        assertNull(hashMap.get("d"));
    }

    @Test
    public void testSize() {
        assertEquals(4, hashMap.size());
        hashMap.put("a", 5);
        assertEquals(4, hashMap.size());
        hashMap.put("j", 10);
        hashMap.put("k", 10);
        hashMap.put("l", 10);
        hashMap.put("m", 10);
        hashMap.put("n", 10);
        assertEquals(9, hashMap.size());
        hashMap.clear();
        assertEquals(0, hashMap.size());
    }

    @Test
    public void testKeySet() {
        hashMap.put("z", 100);
        System.out.println(hashMap.keySet());
    }

    @Test
    public void testRemove() {
        assertEquals(Integer.valueOf(20), hashMap.remove("c"));
        assertEquals(Integer.valueOf(2), hashMap.remove("a"));
        assertEquals(Integer.valueOf(10), hashMap.remove("b"));

    }

    //@Test
//    public void testIterator(){
//        hashMap.remove("z");
//        for(String s : hashMap){
//            System.out.println(s);
//        }
//    }
}
