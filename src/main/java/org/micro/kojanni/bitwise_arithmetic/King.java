package org.micro.kojanni.bitwise_arithmetic;
/**
 * 1.Прогулка Короля - BITS
 * <p>
 * 00000000 00000000 00000011 00000010 = 2^9 + 2^8 + 2^1 = 512 + 256 + 2 = 770
 * <p>
 * Король решил прогуляться по пустынной шахматной доске.
 * Сейчас он находится в указанной клетке.
 * Куда он может походить?
 * Вывести количество возможных ходов короля
 * и ulong число с установленными битами тех полей, куда он может походить.
 * <p>
 * Дано>: число от 0 до 63 - индекс позиции короля
 * Поля нумеруются от а1 = 0, b1 = 1  до  h8 = 63.
 * <p>
 * Надо: два числа на двух строчках:
 * количество возможных ходов
 * битовая маска всех возможных ходов короля.
 * <p>
 * На доске кроме короля никого нет,
 * король ходит на одну клетку в любую сторону
 * по горизонтали/вертикали/диагонали.
 */
public class King extends Bitboard {

    public String process(int position) {
        this.position = position;

        this.bb = 1L << position;

        final long NOT_A = 0xFEFEFEFEFEFEFEFEL;
        final long NOT_H = 0x7F7F7F7F7F7F7F7FL;

        long mask = 0L;

        mask |= (bb & NOT_H) << 1;     // вправо
        mask |= (bb & NOT_A) >>> 1;    // влево

        mask |= bb << 8;               // вверх
        mask |= bb >>> 8;              // вниз

        mask |= (bb & NOT_A) >>> 9;    // вниз-влево
        mask |= (bb & NOT_A) << 7;     // вверх-влево
        mask |= (bb & NOT_H) << 9;     // вверх-вправо
        mask |= (bb & NOT_H) >>> 7;    // вниз-вправо

        bb = mask;

        return new StringBuilder()
                .append(Long.bitCount(bb))
                .append("\r\n")
                .append(Long.toUnsignedString(bb))
                .toString();
    }
}
