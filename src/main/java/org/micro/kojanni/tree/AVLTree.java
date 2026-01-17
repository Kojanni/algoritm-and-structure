package org.micro.kojanni.tree;

/**
 * AVL-дерево(сбалансированное двоичное дерево поиска)
 */
public class AVLTree extends BinarySearchTree {

    public AVLTree() {
        super();
    }

    @Override
    public void insert(int x) {
        root = insertAVL(root, x);
    }

    @Override
    public void remove(int x) {
        root = removeAVL(root, x);
    }

    private Node insertAVL(Node node, int x) {
        if (node == null) {
            size++;
            return new AVLNode(x);
        }

        if (x < node.value) {
            node.left = insertAVL(node.left, x);
        } else if (x > node.value) {
            node.right = insertAVL(node.right, x);
        } else {
            return node;
        }

        return rebalance(node);
    }

    private Node removeAVL(Node node, int x) {
        if (node == null) {
            return null;
        }

        if (x < node.value) {
            node.left = removeAVL(node.left, x);
        } else if (x > node.value) {
            node.right = removeAVL(node.right, x);
        } else {
            // Найден узел для удаления
            size--;

            if (node.left == null || node.right == null) {
                node = (node.left != null) ? node.left : node.right;
            } else {
                Node minNode = findMin(node.right);
                node.value = minNode.value;
                node.right = removeAVL(node.right, minNode.value);
                size++;
            }
        }

        if (node == null) {
            return null;
        }

        return rebalance(node);
    }

    private Node smallRightRotation(Node y) {
        Node x = y.left;
        Node t2 = x.right;

        x.right = y;
        y.left = t2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    private Node smallLeftRotation(Node x) {
        Node y = x.right;
        Node t2 = y.left;

        y.left = x;
        x.right = t2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    private Node bigLeftRotation(Node node) {
        node.right = smallRightRotation(node.right);
        return smallLeftRotation(node);
    }

    private Node bigRightRotation(Node node) {
        node.left = smallLeftRotation(node.left);
        return smallRightRotation(node);
    }

    private Node rebalance(Node node) {
        if (node == null) {
            return null;
        }

        updateHeight(node);
        int balance = getBalance(node);

        if (balance > 1 && getBalance(node.left) >= 0) {
            return smallRightRotation(node);
        }

        if (balance > 1 && getBalance(node.left) < 0) {
            return bigRightRotation(node);
        }

        if (balance < -1 && getBalance(node.right) <= 0) {
            return smallLeftRotation(node);
        }

        if (balance < -1 && getBalance(node.right) > 0) {
            return bigLeftRotation(node);
        }

        return node;
    }

    private int height(Node node) {
        if (node == null) {
            return 0;
        }
        return ((AVLNode) node).height;
    }

    private void updateHeight(Node node) {
        if (node != null) {
            ((AVLNode) node).height = 1 + Math.max(height(node.left), height(node.right));
        }
    }

    private int getBalance(Node node) {
        if (node == null) {
            return 0;
        }
        return height(node.left) - height(node.right);
    }

    protected static class AVLNode extends Node {
        int height;

        AVLNode(int value) {
            super(value);
            this.height = 1;
        }
    }
}
