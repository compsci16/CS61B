package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {


    private class Node {
        /* (K, V) pair stored in this Node. */
        private final K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */
    private Set<K> keySet;

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
        keySet = new HashSet<>();
    }

    /**
     * Returns the value mapped to by KEY in the subtree rooted in P.
     * or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null) return null;
        if (p.key.equals(key)) return p.value;
        if (key.compareTo(p.key) > 0)
            return getHelper(key, p.right);
        else
            return getHelper(key, p.left);
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, root);
    }

    /**
     * Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
     * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (p == null) {
            size++;
            keySet.add(key);
            return new Node(key, value);
        }
        if (key.compareTo(p.key) > 0)
            p.right = putHelper(key, value, p.right);
        else if (key.compareTo(p.key) < 0)
            p.left = putHelper(key, value, p.left);
        else
            p.value = value;
        return p;
    }

    /**
     * Inserts the key KEY
     * If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        root = putHelper(key, value, root);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        return keySet;
    }

    /**
     * Removes KEY from the tree if present
     * returns VALUE removed,
     * null on failed removal.
     */
    @Override
    public V remove(K key) {
        if (key == null) throw new IllegalArgumentException();
        V v = get(key);
        if (v != null) {
            root = removeHelper(key, root);
            keySet.remove(key);
            size--;
        }
        return v;
    }

    private Node removeHelper(K key, Node node) {
        int cmp = key.compareTo(node.key);
        if (cmp > 0)
            node.right = removeHelper(key, node.right);
        else if (cmp < 0)
            node.left = removeHelper(key, node.left);
        else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            Node predecessor = getPredecessor(node);
            Node newNode = new Node(predecessor.key, predecessor.value);
            remove(predecessor.key);
            newNode.right = node.right;
            newNode.left = node.left;
            return newNode;
        }
        return node;
    }

    private Node getPredecessor(Node n) {
        Node current = n.right;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    /**
     * Removes the key-value entry for the specified key only if it is
     * currently mapped to the specified value.  Returns the VALUE removed,
     * null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        if (get(key).equals(value))
            return remove(key);
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }

}