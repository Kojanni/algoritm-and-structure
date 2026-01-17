package org.micro.kojanni.tree;

public class BTreeDemo {

    public static void main(String[] args) {
        System.out.println("ДЕМОНСТРАЦИЯ B-ДЕРЕВА");

        demonstrateBTree();
        comparePerformance();
    }

    private static void demonstrateBTree() {
        BTree btree = new BTree(3);

        System.out.println("\nВставка элементов: 10, 20, 5, 6, 12, 30, 7, 17");
        int[] values = {10, 20, 5, 6, 12, 30, 7, 17};
        for (int val : values) {
            btree.insert(val);
        }

        System.out.println("\nСтруктура B-дерева:");
        btree.printTree();

        System.out.println("\nХарактеристики:");
        System.out.println("  • Высота: " + btree.height());
        System.out.println("  • Размер: " + btree.size());
        System.out.println("  • Минимальная степень: " + btree.getMinDegree());

        System.out.println("\nПоиск элементов:");
        System.out.println("  • Поиск 12: " + (btree.search(12) ? "✓ Найден" : "✗ Не найден"));
        System.out.println("  • Поиск 15: " + (btree.search(15) ? "✓ Найден" : "✗ Не найден"));

        System.out.println("\nОбход в порядке возрастания:");
        System.out.println("  " + btree.traverse());

        System.out.println("\nУдаление элемента 6:");
        btree.remove(6);
        System.out.println("  Обход после удаления: " + btree.traverse());
    }

    private static void comparePerformance() {

        int[] sizes = {1000, 5000, 10000};

        System.out.println(String.format("\n%-10s | %-20s", "Размер", "B-Tree вставка (нс)"));
        System.out.println("─".repeat(80));

        for (int size : sizes) {
            BTree btree = new BTree(5);
            long startTime = System.nanoTime();
            for (int i = 0; i < size; i++) {
                btree.insert(i);
            }
            long btreeTime = (System.nanoTime() - startTime);

            System.out.println(String.format("%-10d | %-20d",
                    size, btreeTime));
        }
    }
}
