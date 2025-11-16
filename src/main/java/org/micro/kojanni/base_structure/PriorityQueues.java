package org.micro.kojanni.base_structure;

public class PriorityQueues {

    /**
     * Приоритетная очередь на основе двух массивов
     * Динамическое расширение: Массивы автоматически увеличиваются при заполнении
     * Поиск максимального приоритета: При каждом извлечении ищется элемент с наивысшим приоритетом
     * Сдвиг элементов: После удаления элементы сдвигаются для заполнения пустого места
     */
    public static class PriorityQueue<T> {
        private Object[] queue;
        private int[] priority;
        private int count;
        private int capacity;

        public PriorityQueue() {
            this(100);
        }

        public PriorityQueue(int initialCapacity) {
            capacity = initialCapacity;
            queue = new Object[capacity];
            priority = new int[capacity];
            count = 0;
        }

        public void enqueue(T item, int prio) {
            // Если массив заполнен, увеличиваем его размер
            if (count >= capacity) {
                resize();
            }

            queue[count] = item;
            priority[count] = prio;
            count++;
        }

        @SuppressWarnings("unchecked")
        public T dequeue() {
            if (count == 0) {
                throw new IllegalStateException("Queue is empty");
            }

            // Находим элемент с наивысшим приоритетом
            int maxPriorityIndex = 0;
            for (int i = 1; i < count; i++) {
                if (priority[i] > priority[maxPriorityIndex]) {
                    maxPriorityIndex = i;
                }
            }

            T item = (T) queue[maxPriorityIndex];

            // Сдвигаем элементы после удаляемого
            for (int i = maxPriorityIndex; i < count - 1; i++) {
                queue[i] = queue[i + 1];
                priority[i] = priority[i + 1];
            }

            count--;
            queue[count] = null; // Помогаем garbage collector
            return item;
        }

        public int size() {
            return count;
        }

        public boolean isEmpty() {
            return count == 0;
        }

        private void resize() {
            int newCapacity = capacity * 2;
            Object[] newQueue = new Object[newCapacity];
            int[] newPriority = new int[newCapacity];

            System.arraycopy(queue, 0, newQueue, 0, count);
            System.arraycopy(priority, 0, newPriority, 0, count);

            queue = newQueue;
            priority = newPriority;
            capacity = newCapacity;
        }

        // Дополнительный метод для просмотра элемента с наивысшим приоритетом без удаления
        public T peek() {
            if (count == 0) {
                throw new IllegalStateException("Queue is empty");
            }

            int maxPriorityIndex = 0;
            for (int i = 1; i < count; i++) {
                if (priority[i] > priority[maxPriorityIndex]) {
                    maxPriorityIndex = i;
                }
            }

            return (T) queue[maxPriorityIndex];
        }

    }

    public static void main(String[] args) {
        PriorityQueue<String> pq = new PriorityQueue<>();

        pq.enqueue("Item 1", 1);
        pq.enqueue("Item 3", 3);
        pq.enqueue("Item 2", 2);
        pq.enqueue("Item 4", 3);

        System.out.println("Size: " + pq.size()); // Size: 4

        System.out.println(pq.dequeue()); // Item 3 (или Item 4)
        System.out.println(pq.dequeue()); // Item 4 (или Item 3)
        System.out.println(pq.dequeue()); // Item 2
        System.out.println(pq.dequeue()); // Item 1

        System.out.println("Is empty: " + pq.isEmpty()); // Is empty: true
    }
}
