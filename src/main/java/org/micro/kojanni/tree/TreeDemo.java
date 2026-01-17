package org.micro.kojanni.tree;

/**
 * Демонстрация работы деревьев
 */
public class TreeDemo {
    
    public static void main(String[] args) {
        demonstrateBST();
        demonstrateAVL();
        demonstrateTreap();
    }
    
    private static void demonstrateBST() {
        System.out.println("BST\n");
        
        BinarySearchTree bst = new BinarySearchTree();
        int[] values = {50, 30, 70, 20, 40, 60, 80};
        
        System.out.println("Вставка элементов: 50, 30, 70, 20, 40, 60, 80");
        for (int value : values) {
            bst.insert(value);
        }
        
        System.out.println("Размер дерева: " + bst.size());
        System.out.println("Высота дерева: " + bst.height());
        
        System.out.println("\nПоиск элементов:");
        System.out.println("  Поиск 40: " + (bst.search(40) ? "Найден ✓" : "Не найден ✗"));
        System.out.println("  Поиск 100: " + (bst.search(100) ? "Найден ✓" : "Не найден ✗"));
        
        System.out.println("\nУдаление элемента 30");
        bst.remove(30);
        System.out.println("  Поиск 30 после удаления: " + (bst.search(30) ? "Найден ✓" : "Не найден ✗"));
        System.out.println("  Размер дерева: " + bst.size());
    }
    
    private static void demonstrateAVL() {
        System.out.println("AVL-ДЕРЕВО\n");
        
        AVLTree avl = new AVLTree();
        int[] values = {10, 20, 30, 40, 50, 25};
        
        System.out.println("Вставка элементов: 10, 20, 30, 40, 50, 25");
        for (int value : values) {
            avl.insert(value);
        }
        
        System.out.println("\nРазмер дерева: " + avl.size());
        System.out.println("Высота дерева: " + avl.height());
        
        System.out.println("\nПоиск элементов:");
        System.out.println("  Поиск 25: " + (avl.search(25) ? "Найден ✓" : "Не найден ✗"));
        System.out.println("  Поиск 35: " + (avl.search(35) ? "Найден ✓" : "Не найден ✗"));
        
        System.out.println("\nУдаление элемента 30");
        avl.remove(30);
        System.out.println("  Размер дерева: " + avl.size());
        System.out.println("  Высота дерева: " + avl.height());
    }
    
    private static void demonstrateTreap() {
        System.out.println("ДЕКАРТОВО ДЕРЕВО\n");
        
        TreapTree treap = new TreapTree();
        int[] values = {15, 10, 20, 8, 12, 16, 25};
        
        System.out.println("Вставка элементов: 15, 10, 20, 8, 12, 16, 25");
        for (int value : values) {
            treap.insert(value);
        }
        
        System.out.println("\nРазмер дерева: " + treap.size());
        System.out.println("Высота дерева: " + treap.height());
        
        System.out.println("\nПоиск элементов:");
        System.out.println("  Поиск 12: " + (treap.search(12) ? "Найден ✓" : "Не найден ✗"));
        System.out.println("  Поиск 18: " + (treap.search(18) ? "Найден ✓" : "Не найден ✗"));
        
        System.out.println("\nУдаление элемента 20");
        treap.remove(20);
        System.out.println("  Размер дерева: " + treap.size());
        System.out.println("  Поиск 20 после удаления: " + (treap.search(20) ? "Найден ✓" : "Не найден ✗"));
    }
}
