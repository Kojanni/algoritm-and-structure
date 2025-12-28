package org.micro.kojanni.visual;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Компонент для визуализации процесса сортировки
 */
public class SortVisualizer extends JPanel {

    private static final Color DEFAULT_COLOR = new Color(100, 149, 237); // CornflowerBlue
    private static final Color ACTIVE_COLOR = new Color(255, 99, 71);   // Tomato
    private static final Color SECONDARY_COLOR = new Color(60, 179, 113); // MediumSeaGreen
    private static final Color SORTED_COLOR = new Color(138, 43, 226);  // BlueViolet
    private static final Color BACKGROUND_COLOR = new Color(240, 240, 240);
    
    private int[] array;
    private int activeIndex = -1;
    private int secondaryIndex = -1;
    private final Set<Integer> sortedIndices = new HashSet<>();
    private String statusText = "Готов к сортировке";
    
    /**
     * Обновляет состояние визуализации
     * @param array текущее состояние массива
     * @param activeIndex индекс активного элемента
     * @param secondaryIndex индекс вторичного элемента
     */
    public void updateState(int[] array, int activeIndex, int secondaryIndex) {
        this.array = array;
        this.activeIndex = activeIndex;
        this.secondaryIndex = secondaryIndex;
        
        updateStatusText();
        updateSortedIndices();
        repaint();
    }
    
    private void updateStatusText() {
        if (activeIndex >= 0 || secondaryIndex >= 0) {
            statusText = String.format("Сравниваем элементы [%d] и [%d]", 
                Math.min(activeIndex, secondaryIndex), 
                Math.max(activeIndex, secondaryIndex));
        } else if (array != null) {
            statusText = "Сортировка завершена!";
        } else {
            statusText = "Готов к сортировке";
        }
    }
    
    private void updateSortedIndices() {
        if (activeIndex == -1 && secondaryIndex == -1 && array != null) {
            // Если оба индекса -1 и массив не пустой, значит, массив отсортирован
            for (int i = 0; i < array.length; i++) {
                sortedIndices.add(i);
            }
        } else if (activeIndex >= 0 && secondaryIndex >= 0 && array != null) {
            // Если оба индекса валидны, добавляем их в отсортированные, если нужно
            if (array[activeIndex] <= array[secondaryIndex]) {
                sortedIndices.add(activeIndex);
            } else {
                sortedIndices.add(secondaryIndex);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Очищаем фон
        g2d.setColor(BACKGROUND_COLOR);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        if (array == null || array.length == 0) return;

        int width = getWidth();
        int height = getHeight();
        int barWidth = Math.max(2, (width - 20) / array.length);
        
        // Находим максимальное значение для масштабирования
        int max = Arrays.stream(array).max().orElse(1);
        if (max == 0) max = 1;
        
        // Рисуем столбцы
        for (int k = 0; k < array.length; k++) {
            int barHeight = (int) ((double) array[k] / max * (height - 60));
            int x = 10 + k * barWidth;
            int y = height - barHeight - 40;
            
            // Выбираем цвет в зависимости от состояния элемента
            if (k == activeIndex) {
                g2d.setColor(ACTIVE_COLOR);
            } else if (k == secondaryIndex) {
                g2d.setColor(SECONDARY_COLOR);
            } else if (sortedIndices.contains(k)) {
                g2d.setColor(SORTED_COLOR);
            } else {
                g2d.setColor(DEFAULT_COLOR);
            }
            
            // Рисуем столбец с тенью
            g2d.fill3DRect(x, y, barWidth - 2, barHeight, true);
            
            // Подпись значения
            g2d.setColor(Color.BLACK);
            String value = String.valueOf(array[k]);
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(value);
            g2d.drawString(value, x + (barWidth - textWidth) / 2, height - 20);
        }
        
        // Выводим статус
        g2d.setColor(Color.DARK_GRAY);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        int statusWidth = g2d.getFontMetrics().stringWidth(statusText);
        g2d.drawString(statusText, (width - statusWidth) / 2, 30);
        
        // Легенда
        int legendY = height - 15;
        drawLegendItem(g2d, "Текущий элемент", ACTIVE_COLOR, 10, legendY);
        drawLegendItem(g2d, "Сравниваемый элемент", SECONDARY_COLOR, 180, legendY);
        drawLegendItem(g2d, "Отсортированные", SORTED_COLOR, 400, legendY);
        drawLegendItem(g2d, "Неотсортированные", DEFAULT_COLOR, 600, legendY);
    }
    
    private void drawLegendItem(Graphics2D g2d, String text, Color color, int x, int y) {
        g2d.setColor(color);
        g2d.fillRect(x, y - 10, 15, 15);
        g2d.setColor(Color.BLACK);
        g2d.drawString(text, x + 20, y + 3);
    }
}
