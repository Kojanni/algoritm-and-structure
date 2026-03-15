package org.micro.kojanni.string_search.kmp_lesson;

import java.util.ArrayList;
import java.util.List;

/**
 * Конечный автомат
 * Строится алфавит из уникальных символов шаблона (упорядоченный).
 * Таблица переходов stateTransitions[состояние][индекс символа] содержит новое состояние после добавления символа.
 * Состояние q означает, что последние q символов прочитанной части текста совпадают с префиксом шаблона длины q.
 * При достижении состояния q == m фиксируется вхождение.
 */
public class FSM extends BaseSearch {
    private final List<Character> alphabet;
    private final int[][] stateTransitions;

    public FSM(String pattern) {
        super(pattern);
        this.alphabet = buildAlphabet();
        this.stateTransitions = buildStateTransitions();
    }

    /**
     * Возвращает список всех позиций вхождений pattern в text
     */
    @Override
    public List<Integer> search(String text) {
        if (pattern.isEmpty()) {
            List<Integer> result = new ArrayList<>();
            for (int i = 0; i <= text.length(); i++) {
                result.add(i);
            }
            return result;
        }
        int length = pattern.length();
        List<Integer> occurrences = new ArrayList<>();
        int state = 0;
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            int idx = alphabet.indexOf(ch);
            state = (idx == -1) ? 0 : stateTransitions[state][idx];
            if (state == length) {
                occurrences.add(i - length + 1);
            }
        }
        return occurrences;
    }

    private List<Character> buildAlphabet() {
        return pattern.chars()
                .distinct()
                .sorted()
                .mapToObj(c -> (char) c)
                .toList();
    }

    private int[][] buildStateTransitions() {
        int length = pattern.length();
        int[][] stateTransitions = new int[length + 1][alphabet.size()];
        for (int q = 0; q <= length; q++) {
            for (int idx = 0; idx < alphabet.size(); idx++) {
                char c = alphabet.get(idx);
                // Строка, получающаяся добавлением c к текущему префиксу
                String line = pattern.substring(0, q) + c;
                int k = Math.min(length, q + 1);
                while (k > 0 && !pattern.substring(0, k).equals(line.substring(line.length() - k))) {
                    k--;
                }
                stateTransitions[q][idx] = k;
            }
        }
        return stateTransitions;
    }
}
