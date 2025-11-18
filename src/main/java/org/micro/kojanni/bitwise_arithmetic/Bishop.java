package org.micro.kojanni.bitwise_arithmetic;

/**
 * Шахматный слон решил пробежаться по шахматной доске.
 * Сейчас он находится в указанной клетке.
 * Куда он может походить?
 * Вывести количество возможных ходов слона
 * и ulong число с установленными битами тех полей, куда он может походить.
 *
 * Дано: число от 0 до 63 - индекс позиции слона
 * Поля нумеруются от а1 = 0, b1 = 1  до  h8 = 63.
 *
 * Надо: два числа на двух строчках:
 * количество возможных ходов
 * битовая маска всех возможных ходов.
 *
 * На доске кроме слона никого нет,
 * слон ходит на любое количество полей по диагонали.
 *
 * http://www.talkchess.com/forum3/viewtopic.php?t=39053
 */
public class Bishop extends Bitboard {

    public String process(int position) {
        this.position = position;

        this.bb = 1L << position;

        final long NOT_A = 0xFEFEFEFEFEFEFEFEL;
        final long NOT_H = 0x7F7F7F7F7F7F7F7FL;

        long mask = 0L;

        // Вверх-вправо
        long temp = bb;
        while ((temp & NOT_H) != 0) {
            temp = (temp & NOT_H) << 9;
            mask |= temp;
        }

        // Вверх-влево
        temp = bb;
        while ((temp & NOT_A) != 0) {
            temp = (temp & NOT_A) << 7;
            mask |= temp;
        }

        // Вниз-вправо
        temp = bb;
        while ((temp & NOT_H) != 0) {
            temp = (temp & NOT_H) >>> 7;
            mask |= temp;
        }

        // Вниз-влево
        temp = bb;
        while ((temp & NOT_A) != 0) {
            temp = (temp & NOT_A) >>> 9;
            mask |= temp;
        }

        bb = mask;

        return new StringBuilder()
                .append(Long.bitCount(bb))
                .append("\r\n")
                .append(Long.toUnsignedString(bb))
                .toString();
    }
}
