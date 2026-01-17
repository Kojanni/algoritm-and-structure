package org.micro.kojanni.tree;

/**
 * Расширяющееся дерево поиска (Splay Tree).
 * Особенности:
 * - Самобалансирующееся дерево с амортизированной сложностью O(log n)
 * - При каждой операции (поиск, вставка, удаление) элемент поднимается в корень
 * - Часто используемые элементы оказываются ближе к корню
 * - Не требует хранения дополнительной информации в узлах
 */
public class SplayTree extends BinarySearchTree {

    public SplayTree() {
        super();
    }

    @Override
    public void insert(int x) {
        if (root == null) {
            root = new Node(x);
            size++;
            return;
        }

        root = splay(root, x);

        if (root.value == x) {
            return;
        }

        Node newNode = new Node(x);
        if (x < root.value) {
            newNode.right = root;
            newNode.left = root.left;
            root.left = null;
        } else {
            newNode.left = root;
            newNode.right = root.right;
            root.right = null;
        }
        root = newNode;
        size++;
    }

    @Override
    public boolean search(int x) {
        if (root == null) {
            return false;
        }
        root = splay(root, x);
        return root.value == x;
    }

    @Override
    public void remove(int x) {
        if (root == null) {
            return;
        }

        root = splay(root, x);

        if (root.value != x) {
            return;
        }

        size--;

        if (root.left == null) {
            root = root.right;
        } else {
            Node rightSubtree = root.right;
            root = root.left;
            root = splay(root, x);
            root.right = rightSubtree;
        }
    }

    /**
     * Splay операция - поднимает узел с ключом x к корню дерева
     */
    private Node splay(Node node, int x) {
        if (node == null) {
            return null;
        }

        if (x < node.value) {
            if (node.left == null) {
                return node;
            }

            if (x < node.left.value) {
                // Zig-Zig
                node.left.left = splay(node.left.left, x);
                node = rotateRight(node);
            } else if (x > node.left.value) {
                // Zig-Zag
                node.left.right = splay(node.left.right, x);
                if (node.left.right != null) {
                    node.left = rotateLeft(node.left);
                }
            }

            return (node.left == null) ? node : rotateRight(node);

        } else if (x > node.value) {
            if (node.right == null) {
                return node;
            }

            if (x > node.right.value) {
                // Zig-Zig
                node.right.right = splay(node.right.right, x);
                node = rotateLeft(node);
            } else if (x < node.right.value) {
                // Zig-Zag
                node.right.left = splay(node.right.left, x);
                if (node.right.left != null) {
                    node.right = rotateRight(node.right);
                }
            }

            return (node.right == null) ? node : rotateLeft(node);

        } else {
            return node;
        }
    }

    private Node rotateRight(Node node) {
        Node leftChild = node.left;
        node.left = leftChild.right;
        leftChild.right = node;
        return leftChild;
    }

    private Node rotateLeft(Node node) {
        Node rightChild = node.right;
        node.right = rightChild.left;
        rightChild.left = node;
        return rightChild;
    }
}
