package org.micro.kojanni.tree;

import java.util.Random;

/**
 * Декартово дерево(Treap)
 */
public class TreapTree extends BinarySearchTree {
    
    private Random random;

    public TreapTree() {
        super();
        this.random = new Random();
    }

    @Override
    public void insert(int x) {
        if (search(x)) {
            return;
        }

        SplitResult split = split(root, x);
        Node newNode = new TreapNode(x, random.nextInt(1000000));
        root = merge(merge(split.left, newNode), split.right);
        size++;
    }

    @Override
    public void remove(int x) {
        root = removeNode(root, x);
    }

    private Node removeNode(Node t, int x) {
        if (t == null) {
            return null;
        }

        TreapNode node = (TreapNode) t;

        if (node.value == x) {
            size--;
            return merge(node.left, node.right);
        } else if (x < node.value) {
            node.left = removeNode(node.left, x);
            return node;
        } else {
            node.right = removeNode(node.right, x);
            return node;
        }
    }

    private SplitResult split(Node t, int x) {
        if (t == null) {
            return new SplitResult(null, null);
        }

        TreapNode node = (TreapNode) t;

        if (node.value < x) {
            SplitResult result = split(node.right, x);
            node.right = result.left;
            return new SplitResult(node, result.right);
        } else {
            SplitResult result = split(node.left, x);
            node.left = result.right;
            return new SplitResult(result.left, node);
        }
    }

    private Node merge(Node l, Node r) {
        if (l == null) return r;
        if (r == null) return l;

        TreapNode left = (TreapNode) l;
        TreapNode right = (TreapNode) r;

        if (left.priority > right.priority) {
            left.right = merge(left.right, right);
            return left;
        } else {
            right.left = merge(left, right.left);
            return right;
        }
    }

    protected static class TreapNode extends Node {

        int priority;
        TreapNode(int value, int priority) {
            super(value);
            this.priority = priority;
        }

    }

    private static class SplitResult {

        Node left;
        Node right;
        SplitResult(Node left, Node right) {
            this.left = left;
            this.right = right;
        }

    }
}
