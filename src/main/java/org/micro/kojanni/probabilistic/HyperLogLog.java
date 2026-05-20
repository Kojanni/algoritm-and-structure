package org.micro.kojanni.probabilistic;

/**
 * Реализация HyperLogLog - вероятностного алгоритма для подсчета количества уникальных элементов.
 * 
 * Особенности:
 * - Использует очень мало памяти (обычно несколько килобайт)
 * - Типичная погрешность около 2% при использовании 2^14 регистров
 * - Операция add выполняется за O(1)
 * - Подсчет уникальных элементов выполняется за O(m), где m - количество регистров
 * 
 * Алгоритм основан на подсчете максимального количества ведущих нулей в хешах элементов.
 * 
 * @param <T> тип элементов
 */
public class HyperLogLog<T> {
    
    private final int precision; // точность (обычно от 4 до 16)
    private final int m; // количество регистров (2^precision)
    private final byte[] registers; // массив регистров
    private final double alphaMM; // константа для коррекции
    
    /**
     * Создает HyperLogLog с заданной точностью.
     * 
     * @param precision точность (от 4 до 16). Чем больше, тем точнее, но больше памяти.
     *                  Рекомендуемое значение: 14 (использует ~16KB памяти, погрешность ~1.6%)
     */
    public HyperLogLog(int precision) {
        if (precision < 4 || precision > 16) {
            throw new IllegalArgumentException("Precision must be between 4 and 16");
        }
        
        this.precision = precision;
        this.m = 1 << precision; // 2^precision
        this.registers = new byte[m];
        this.alphaMM = getAlphaMM(m);
    }
    
    /**
     * Создает HyperLogLog с точностью по умолчанию (14).
     */
    public HyperLogLog() {
        this(14);
    }
    
    /**
     * Вычисляет константу alpha для коррекции оценки.
     */
    private double getAlphaMM(int m) {
        switch (m) {
            case 16:
                return 0.673 * m * m;
            case 32:
                return 0.697 * m * m;
            case 64:
                return 0.709 * m * m;
            default:
                // Для m >= 128
                return (0.7213 / (1 + 1.079 / m)) * m * m;
        }
    }
    
    /**
     * Добавляет элемент в HyperLogLog.
     */
    public void add(T element) {
        // Получаем качественный 64-битный хеш элемента
        long hash = hash64(element);
        
        // Первые precision бит используются для выбора регистра
        int registerIndex = (int) (hash & ((1 << precision) - 1));
        
        // Оставшиеся биты используются для подсчета ведущих нулей
        long w = hash >>> precision;
        
        // Подсчитываем количество ведущих нулей + 1
        // Защита от отрицательных значений и переполнения
        int leadingZerosCount = Long.numberOfLeadingZeros(w) - precision + 1;
        byte leadingZeros = (byte) Math.max(1, Math.min(leadingZerosCount, 64));
        
        // Обновляем регистр, если нашли больше ведущих нулей
        if (leadingZeros > registers[registerIndex]) {
            registers[registerIndex] = leadingZeros;
        }
    }
    
    /**
     * Оценивает количество уникальных элементов.
     */
    public long estimate() {
        double sum = 0;
        int zeros = 0;
        
        // Вычисляем гармоническое среднее
        for (byte register : registers) {
            sum += Math.pow(2, -register);
            if (register == 0) {
                zeros++;
            }
        }
        
        double estimate = alphaMM / sum;
        
        // Применяем коррекцию для малых значений
        if (estimate <= 2.5 * m) {
            if (zeros != 0) {
                // Linear counting для малых значений
                estimate = m * Math.log((double) m / zeros);
            }
        }
        // Коррекция для больших значений (для 32-битных хешей)
        else if (estimate > (1.0 / 30.0) * (1L << 32)) {
            estimate = -1L * (1L << 32) * Math.log(1 - estimate / (1L << 32));
        }
        
        return Math.round(estimate);
    }
    
    /**
     * Объединяет текущий HyperLogLog с другим.
     * Позволяет подсчитывать уникальные элементы в распределенных системах.
     */
    public void merge(HyperLogLog<T> other) {
        if (this.m != other.m) {
            throw new IllegalArgumentException("Cannot merge HyperLogLog with different precision");
        }
        
        for (int i = 0; i < m; i++) {
            if (other.registers[i] > this.registers[i]) {
                this.registers[i] = other.registers[i];
            }
        }
    }
    
    /**
     * Очищает все регистры.
     */
    public void clear() {
        for (int i = 0; i < m; i++) {
            registers[i] = 0;
        }
    }
    
    /**
     * Вычисляет 64-битный хеш элемента.
     * Использует полноценный MurmurHash3 для качественного распределения.
     */
    private long hash64(T element) {
        // Используем настоящий MurmurHash3 для получения качественного 64-битного хеша
        return MurmurHash3.hash64(element, 0);
    }
    
    /**
     * Возвращает теоретическую стандартную ошибку.
     */
    public double getStandardError() {
        return 1.04 / Math.sqrt(m);
    }
    
    /**
     * Возвращает количество используемой памяти в байтах.
     */
    public int getMemoryUsage() {
        return m; // каждый регистр занимает 1 байт
    }
    
    /**
     * Возвращает точность (precision).
     */
    public int getPrecision() {
        return precision;
    }
    
    /**
     * Возвращает количество регистров.
     */
    public int getRegisterCount() {
        return m;
    }
    
    @Override
    public String toString() {
        return String.format("HyperLogLog[precision=%d, registers=%d, memory=%dB, stdError=%.2f%%, estimate=%d]",
                precision, m, getMemoryUsage(), getStandardError() * 100, estimate());
    }
}
