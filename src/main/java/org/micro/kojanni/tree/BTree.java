package org.micro.kojanni.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * N-ричное B-дерево поиска.
 * Особенности B-дерева:
 * - Каждый узел может содержать от t-1 до 2t-1 ключей, где t - минимальная степень
 * - Все листья находятся на одном уровне
 * - Узел с k ключами имеет k+1 потомков
 * - Ключи в узле упорядочены по возрастанию
 * - Балансировка происходит автоматически при вставке и удалении
 */
public class BTree {

    private Node root;
    private int minDegree;
    private int size;

    public BTree(int minDegree) {
        if (minDegree < 2) {
            throw new IllegalArgumentException("Минимальная степень должна быть >= 2");
        }
        this.root = null;
        this.minDegree = minDegree;
        this.size = 0;
    }

    public boolean search(int key) {
        return search(root, key) != null;
    }

    public void insert(int key) {
        if (search(key)) {
            return;
        }

        if (root == null) {
            root = new Node(true);
            root.keys.add(key);
            root.keyCount = 1;
            size++;
            return;
        }

        // Если корень полон, разделяем его
        if (root.keyCount == 2 * minDegree - 1) {
            Node newRoot = new Node(false);
            newRoot.children.add(root);
            splitChild(newRoot, 0);
            root = newRoot;
        }

        insertNonFull(root, key);
        size++;
    }

    public void remove(int key) {
        if (root == null) {
            return;
        }

        remove(root, key);

        // Если корень стал пустым, делаем его потомка новым корнем
        if (root.keyCount == 0) {
            if (!root.isLeaf && !root.children.isEmpty()) {
                root = root.children.get(0);
            } else {
                root = null;
            }
        }
    }

    private Node search(Node node, int key) {
        if (node == null) {
            return null;
        }

        int i = 0;
        while (i < node.keyCount && key > node.keys.get(i)) {
            i++;
        }

        if (i < node.keyCount && key == node.keys.get(i)) {
            return node;
        }

        if (node.isLeaf) {
            return null;
        }

        return search(node.children.get(i), key);
    }

    private void insertNonFull(Node node, int key) {
        int i = node.keyCount - 1;

        if (node.isLeaf) {
            // Вставка в лист
            node.keys.add(0); // Добавляем место
            while (i >= 0 && key < node.keys.get(i)) {
                node.keys.set(i + 1, node.keys.get(i));
                i--;
            }
            node.keys.set(i + 1, key);
            node.keyCount++;
        } else {
            // Находим потомка для вставки
            while (i >= 0 && key < node.keys.get(i)) {
                i--;
            }
            i++;

            // Если потомок полон, разделяем его
            if (node.children.get(i).keyCount == 2 * minDegree - 1) {
                splitChild(node, i);
                if (key > node.keys.get(i)) {
                    i++;
                }
            }
            insertNonFull(node.children.get(i), key);
        }
    }

    private void splitChild(Node parent, int index) {
        Node fullChild = parent.children.get(index);
        Node newChild = new Node(fullChild.isLeaf);

        int midIndex = minDegree - 1;
        newChild.keyCount = minDegree - 1;

        // Копируем половину ключей в новый узел
        for (int j = 0; j < minDegree - 1; j++) {
            newChild.keys.add(fullChild.keys.get(j + minDegree));
        }

        // потомки
        if (!fullChild.isLeaf) {
            for (int j = 0; j < minDegree; j++) {
                newChild.children.add(fullChild.children.get(j + minDegree));
            }
        }

        // Средний ключ поднимается в родителя
        parent.keys.add(index, fullChild.keys.get(midIndex));
        parent.children.add(index + 1, newChild);
        parent.keyCount++;

        // Обрезаем старый узел
        fullChild.keys.subList(midIndex, fullChild.keys.size()).clear();
        if (!fullChild.isLeaf) {
            fullChild.children.subList(minDegree, fullChild.children.size()).clear();
        }
        fullChild.keyCount = minDegree - 1;
    }

    private void remove(Node node, int key) {
        int idx = findKey(node, key);

        if (idx < node.keyCount && node.keys.get(idx) == key) {
            if (node.isLeaf) {
                removeFromLeaf(node, idx);
            } else {
                removeFromNonLeaf(node, idx);
            }
            size--;
        } else {
            if (node.isLeaf) {
                return;
            }

            boolean isInLastChild = (idx == node.keyCount);

            if (node.children.get(idx).keyCount < minDegree) {
                fill(node, idx);
            }

            if (isInLastChild && idx > node.keyCount) {
                remove(node.children.get(idx - 1), key);
            } else {
                remove(node.children.get(idx), key);
            }
        }
    }

    private int findKey(Node node, int key) {
        int idx = 0;
        while (idx < node.keyCount && node.keys.get(idx) < key) {
            idx++;
        }
        return idx;
    }

    private void removeFromLeaf(Node node, int idx) {
        node.keys.remove(idx);
        node.keyCount--;
    }

    private void removeFromNonLeaf(Node node, int idx) {
        int key = node.keys.get(idx);

        if (node.children.get(idx).keyCount >= minDegree) {
            // из левого поддерева
            int pred = getPredecessor(node, idx);
            node.keys.set(idx, pred);
            remove(node.children.get(idx), pred);
        } else if (node.children.get(idx + 1).keyCount >= minDegree) {
            // из правого поддерева
            int succ = getSuccessor(node, idx);
            node.keys.set(idx, succ);
            remove(node.children.get(idx + 1), succ);
        } else {
            // Объединение с соседом
            merge(node, idx);
            remove(node.children.get(idx), key);
        }
    }

    private int getPredecessor(Node node, int idx) {
        Node current = node.children.get(idx);
        while (!current.isLeaf) {
            current = current.children.get(current.keyCount);
        }
        return current.keys.get(current.keyCount - 1);
    }

    private int getSuccessor(Node node, int idx) {
        Node current = node.children.get(idx + 1);
        while (!current.isLeaf) {
            current = current.children.get(0);
        }
        return current.keys.get(0);
    }

    private void fill(Node node, int idx) {
        // левый
        if (idx != 0 && node.children.get(idx - 1).keyCount >= minDegree) {
            borrowFromPrev(node, idx);
        }
        // правый
        else if (idx != node.keyCount && node.children.get(idx + 1).keyCount >= minDegree) {
            borrowFromNext(node, idx);
        }
        // Объединение с соседом
        else {
            if (idx != node.keyCount) {
                merge(node, idx);
            } else {
                merge(node, idx - 1);
            }
        }
    }

    private void borrowFromPrev(Node node, int idx) {
        Node child = node.children.get(idx);
        Node sibling = node.children.get(idx - 1);

        // Перемещаем ключ из родителя
        child.keys.add(0, node.keys.get(idx - 1));

        // Перемещаем ключ в родителя
        node.keys.set(idx - 1, sibling.keys.get(sibling.keyCount - 1));

        // Перемещаем потомка
        if (!child.isLeaf) {
            child.children.add(0, sibling.children.get(sibling.keyCount));
            sibling.children.remove(sibling.keyCount);
        }

        sibling.keys.remove(sibling.keyCount - 1);
        child.keyCount++;
        sibling.keyCount--;
    }

    private void borrowFromNext(Node node, int idx) {
        Node child = node.children.get(idx);
        Node sibling = node.children.get(idx + 1);

        // Перемещаем ключ из родителя
        child.keys.add(node.keys.get(idx));

        // Перемещаем ключ в родителя
        node.keys.set(idx, sibling.keys.get(0));

        // Перемещаем потомка
        if (!child.isLeaf) {
            child.children.add(sibling.children.get(0));
            sibling.children.remove(0);
        }

        sibling.keys.remove(0);
        child.keyCount++;
        sibling.keyCount--;
    }

    private void merge(Node node, int idx) {
        Node child = node.children.get(idx);
        Node sibling = node.children.get(idx + 1);

        child.keys.add(node.keys.get(idx));
        child.keyCount++;

        for (int i = 0; i < sibling.keyCount; i++) {
            child.keys.add(sibling.keys.get(i));
            child.keyCount++;
        }

        if (!child.isLeaf) {
            for (int i = 0; i <= sibling.keyCount; i++) {
                child.children.add(sibling.children.get(i));
            }
        }

        node.keys.remove(idx);
        node.children.remove(idx + 1);
        node.keyCount--;
    }

    public List<Integer> traverse() {
        List<Integer> result = new ArrayList<>();
        traverse(root, result);
        return result;
    }

    private void traverse(Node node, List<Integer> result) {
        if (node == null) {
            return;
        }

        int i;
        for (i = 0; i < node.keyCount; i++) {
            if (!node.isLeaf) {
                traverse(node.children.get(i), result);
            }
            result.add(node.keys.get(i));
        }

        if (!node.isLeaf) {
            traverse(node.children.get(i), result);
        }
    }

    public int height() {
        return height(root);
    }

    private int height(Node node) {
        if (node == null) {
            return 0;
        }
        if (node.isLeaf) {
            return 1;
        }
        return 1 + height(node.children.get(0));
    }

    public int size() {
        return size;
    }

    public int getMinDegree() {
        return minDegree;
    }


    public void printTree() {
        printTree(root, "", true);
    }

    private void printTree(Node node, String prefix, boolean isTail) {
        if (node == null) {
            return;
        }

        System.out.println(prefix + (isTail ? "└── " : "├── ") + keysToString(node));

        if (!node.isLeaf) {
            for (int i = 0; i < node.children.size() - 1; i++) {
                printTree(node.children.get(i), prefix + (isTail ? "    " : "│   "), false);
            }
            if (!node.children.isEmpty()) {
                printTree(node.children.get(node.children.size() - 1),
                        prefix + (isTail ? "    " : "│   "), true);
            }
        }
    }

    private String keysToString(Node node) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < node.keyCount; i++) {
            sb.append(node.keys.get(i));
            if (i < node.keyCount - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private class Node {
        List<Integer> keys;
        List<Node> children;
        boolean isLeaf;
        int keyCount;

        Node(boolean isLeaf) {
            this.keys = new ArrayList<>();
            this.children = new ArrayList<>();
            this.isLeaf = isLeaf;
            this.keyCount = 0;
        }
    }
}
