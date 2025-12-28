package org.micro.kojanni.visual;

/**
 * Интерфейс для визуализации шагов
 */
public interface SortListener {
    /**
     * Шаг изменния
     * @param array текущее состояние массива
     * @param activeIndex текущий элемент
     * @param secondaryIndex второй индекс (или -1)
     */
    void onStep(int[] array, int activeIndex, int secondaryIndex);
}
