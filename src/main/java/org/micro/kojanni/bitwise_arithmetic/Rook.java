package org.micro.kojanni.bitwise_arithmetic;

/**
 *
 * Шахматная ладья решила пробежаться по шахматной доске.
 * Сейчас она находится в указанной клетке.
 * Куда она может сейчас походить?
 * Вывести количество возможных ходов ладьи
 * и ulong число с установленными битами тех полей, куда она может походить.
 *
 * Дано: число от 0 до 63 - индекс позиции ладьи
 * Поля нумеруются от а1 = 0, b1 = 1  до  h8 = 63.
 *
 * Надо: два числа на двух строчках:
 * количество возможных ходов
 * битовая маска всех возможных ходов коня.
 *
 * На доске кроме ладьи никого нет,
 * ладья ходит на любое количество полей по горизонтали или по вертикали.
 *
 * http://www.talkchess.com/forum3/viewtopic.php?t=39053
 */
public class Rook extends Bitboard {

    public String process(int position) {
        this.position = position;

        this.bb = 1L << position;

        final long NOT_A = 0xFEFEFEFEFEFEFEFEL;
        final long NOT_H = 0x7F7F7F7F7F7F7F7FL;

        long mask = 0L;

        // Вверх
        long temp = bb;
        while (temp != 0) {
            temp = temp << 8;
            mask |= temp;
        }

        // Вниз
        temp = bb;
        while (temp != 0) {
            temp = temp >>> 8;
            mask |= temp;
        }

        // Вправо
        temp = bb;
        while ((temp & NOT_H) != 0) {
            temp = (temp & NOT_H) << 1;
            mask |= temp;
        }

        // Влево
        temp = bb;
        while ((temp & NOT_A) != 0) {
            temp = (temp & NOT_A) >>> 1;
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
