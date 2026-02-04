package org.micro.kojanni.trie;

public class TrieMap<V> {

    private final Node<V> root = new Node<>();

    public void put(String key, V value) {
        Node<V> curr = root;
        for (char c : key.toCharArray()) {
            int idx = c - 'a';
            if (curr.children[idx] == null) {
                curr.children[idx] = new Node<>();
            }
            curr = curr.children[idx];
        }
        curr.isEnd = true;
        curr.value = value;
    }

    public V get(String key) {
        Node<V> node = find(key);
        if (node == null || !node.isEnd) return null;
        return node.value;
    }

    public boolean containsKey(String key) {
        Node<V> node = find(key);
        return node != null && node.isEnd;
    }

    public boolean startsWith(String prefix) {
        return find(prefix) != null;
    }

    private Node<V> find(String key) {
        Node<V> curr = root;
        for (char c : key.toCharArray()) {
            int idx = c - 'a';
            if (curr.children[idx] == null) return null;
            curr = curr.children[idx];
        }
        return curr;
    }

    public boolean remove(String key) {
        return remove(root, key, 0);
    }

    private boolean remove(Node<V> node, String key, int depth) {
        if (node == null) return false;

        if (depth == key.length()) {
            if (!node.isEnd) return false;

            node.isEnd = false;
            node.value = null;

            return hasNoChildren(node);
        }

        int idx = key.charAt(depth) - 'a';
        Node<V> child = node.children[idx];
        if (child == null) return false;

        boolean shouldDeleteChild = remove(child, key, depth + 1);

        if (shouldDeleteChild) {
            node.children[idx] = null;
            return !node.isEnd && hasNoChildren(node);
        }

        return false;
    }

    private boolean hasNoChildren(Node<V> node) {
        for (Node<V> c : node.children) {
            if (c != null) return false;
        }
        return true;
    }

    private static class Node<V> {
        int size = 26;
        Node<V>[] children = new Node[size];
        boolean isEnd;
        V value;
    }
}
