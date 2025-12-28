package org.micro.kojanni.visual;

import org.micro.kojanni.linear_sorting.Sorting;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class VisualSortingDemo extends JFrame {

    private static final int ARRAY_SIZE = 10;
    private static final int MAX_VALUE = 100;

    private final JComboBox<String> algorithmSelector;
    private final JButton startButton;
    private final JButton generateButton;
    private final JSlider speedSlider;
    private final JTextArea arrayInput;
    private final JPanel controlPanel;
    private final SortVisualizer visualizer;

    private int[] currentArray;
    private Thread sortingThread;


//    private SortProcessor currentProcessor;

    public VisualSortingDemo() {
        setTitle("Визуализация сортировок");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        // Выбор алгоритма
        algorithmSelector = new JComboBox<>(SortingAlgorithmFactory.getAvailableAlgorithms().toArray(new String[0]));

        startButton = new JButton("Запуск");
        startButton.addActionListener(e -> startSorting());

        generateButton = new JButton("Новый массив");
        generateButton.addActionListener(e -> generateNewArray());

        // Ползунок скорости
        speedSlider = new JSlider(100, 1000, 500);
        speedSlider.setMajorTickSpacing(200);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);

        arrayInput = new JTextArea(3, 30);
        arrayInput.setLineWrap(true);

        generateNewArray();

        visualizer = new SortVisualizer();
        add(visualizer, BorderLayout.CENTER);

        controlPanel.add(new JLabel("Алгоритм:"));
        controlPanel.add(algorithmSelector);
        controlPanel.add(startButton);
        controlPanel.add(generateButton);
        controlPanel.add(new JLabel("Скорость (мс):"));
        controlPanel.add(speedSlider);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(new JLabel("Массив:"), BorderLayout.NORTH);
        inputPanel.add(new JScrollPane(arrayInput), BorderLayout.CENTER);

        add(controlPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void generateNewArray() {
        Random random = new Random();
        currentArray = new int[ARRAY_SIZE];
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < ARRAY_SIZE; i++) {
            currentArray[i] = random.nextInt(MAX_VALUE) + 1;
            sb.append(currentArray[i]);
            if (i < ARRAY_SIZE - 1) sb.append(" ");
        }

        arrayInput.setText(sb.toString());
    }

    private void startSorting() {
        if (sortingThread != null && sortingThread.isAlive()) {
            sortingThread.interrupt();
        }

        try {
            String input = arrayInput.getText().trim();
            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Введите числа для сортировки через пробел",
                        "Ошибка ввода",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String[] parts = input.split("\\s+");
            int[] array = new int[parts.length];
            for (int i = 0; i < parts.length; i++) {
                try {
                    array[i] = Integer.parseInt(parts[i]);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this,
                            String.format("Некорректное число: %s", parts[i]),
                            "Ошибка ввода",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (array.length == 0) {
                JOptionPane.showMessageDialog(this,
                        "Введите хотя бы одно число",
                        "Ошибка ввода",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            currentArray = array;

            String selectedAlgorithm = (String) algorithmSelector.getSelectedItem();
            if (selectedAlgorithm == null || selectedAlgorithm.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Не выбран алгоритм сортировки",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Sorting sorter = SortingAlgorithmFactory.createSorter(selectedAlgorithm);
            if (sorter == null) {
                JOptionPane.showMessageDialog(this,
                        "Не удалось создать экземпляр сортировки: " + selectedAlgorithm,
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }


            //  задержку из слайдера
            sorter.setDelay(1000 - speedSlider.getValue() + 100);

            // слушатель для обновления визуализации
            sorter.setListener((array1, i, j) ->
                    SwingUtilities.invokeLater(() ->
                            visualizer.updateState(array1, i, j)
                    )
            );

            // сортировку
            sortingThread = new Thread(() -> {
                try {
                    int[] arrayToSort = array.clone();
                    SwingUtilities.invokeLater(() ->
                            visualizer.updateState(arrayToSort, -1, -1)
                    );

                    sorter.sort(arrayToSort);
                } catch (Exception e) {
                    if (!(e instanceof InterruptedException)) {
                        SwingUtilities.invokeLater(() ->
                                JOptionPane.showMessageDialog(
                                        VisualSortingDemo.this,
                                        "Ошибка при сортировке: " + e.getMessage(),
                                        "Ошибка",
                                        JOptionPane.ERROR_MESSAGE
                                )
                        );
                        e.printStackTrace();
                    }
                }
            });
            sortingThread.start();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Непредвиденная ошибка: " + ex.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VisualSortingDemo demo = new VisualSortingDemo();
            demo.setExtendedState(JFrame.MAXIMIZED_BOTH);
            demo.setVisible(true);
        });
    }
}
