package org.micro.kojanni.tree;

/**
 * Простое двоичное дерево поиска (Binary Search Tree)
 */
public class BinarySearchTree {

    protected Node root;

    protected int size;
    public BinarySearchTree() {
        this.root = null;
        this.size = 0;
    }

    public void insert(int x) {
        root = insertRec(root, x);
    }

    public boolean search(int x) {
        return searchRec(root, x);
    }

    public void remove(int x) {
        root = removeRec(root, x);
    }

    protected Node insertRec(Node node, int x) {
        if (node == null) {
            size++;
            return new Node(x);
        }

        if (x < node.value) {
            node.left = insertRec(node.left, x);
        } else if (x > node.value) {
            node.right = insertRec(node.right, x);
        }

        return node;
    }

    private boolean searchRec(Node node, int x) {
        if (node == null) {
            return false;
        }

        if (x == node.value) {
            return true;
        }

        if (x < node.value) {
            return searchRec(node.left, x);
        } else {
            return searchRec(node.right, x);
        }
    }

    protected Node removeRec(Node node, int x) {
        if (node == null) {
            return null;
        }

        if (x < node.value) {
            node.left = removeRec(node.left, x);
        } else if (x > node.value) {
            node.right = removeRec(node.right, x);
        } else {
            // Найден узел для удаления
            size--;

            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            }

            node.value = findMin(node.right).value;
            node.right = removeRec(node.right, node.value);
            size++;
        }

        return node;
    }

    protected Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public int size() {
        return size;
    }

    public int height() {
        return heightRec(root);
    }

    protected int heightRec(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(heightRec(node.left), heightRec(node.right));
    }

    protected static class Node {
        int value;
        Node left;
        Node right;

        Node(int value) {
            this.value = value;
            this.left = null;
            this.right = null;
        }
    }
}
