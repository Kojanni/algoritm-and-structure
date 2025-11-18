package org.micro.kojanni.bitwise_arithmetic;

public class Knight extends Bitboard {

    public String process(int position) {
        this.position = position;

        this.bb = 1L << position;

        final long NOT_A = 0xFEFEFEFEFEFEFEFEL;
        final long NOT_AB = 0xFCFCFCFCFCFCFCFCL;
        final long NOT_H = 0x7F7F7F7F7F7F7F7FL;
        final long NOT_GH = 0x3F3F3F3F3F3F3F3FL;

        long mask = 0L;

        // Все 8 возможных ходов коня
        mask |= (bb & NOT_AB) << 6;    // 1 вверх, 2 влево
        mask |= (bb & NOT_A)  << 15;   // 2 вверх, 1 влево
        mask |= (bb & NOT_A)  >>> 17;  // 1 вниз, 2 влево
        mask |= (bb & NOT_AB) >>> 10;  // 2 вниз, 1 влево
        mask |= (bb & NOT_GH) >>> 6;   // 1 вниз, 2 вправо
        mask |= (bb & NOT_H)  >>> 15;  // 2 вниз, 1 вправо
        mask |= (bb & NOT_H)  << 17;   // 2 вверх, 1 вправо
        mask |= (bb & NOT_GH) << 10;   // 1 вверх, 2 вправо

        bb = mask;

        return new StringBuilder()
                .append(Long.bitCount(bb))
                .append("\r\n")
                .append(bb)
                .toString();
    }
}
