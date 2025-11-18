package org.micro.kojanni.bitwise_arithmetic;

public abstract class Bitboard {

    protected long bb = 0;
    protected int position;

    public Bitboard() {
    }

    public String steps(String[] args) {
        int position = Integer.parseInt(args[0]);
        this.position = position;
        return process(position);
    }

    abstract String process(int position);


    public void print() {
        if (bb == 0) {
            System.out.println("Битовая доска не инициализирована (bb = 0)");
            return;
        }

        final String RESET = "\u001B[0m";
        final String BG_WHITE = "\u001B[47m";
        final String BG_GRAY  = "\u001B[100m";
        final String BG_RED   = "\u001B[41m";
        final String BG_GREEN = "\u001B[42m";
        final String BG_BLUE  = "\u001B[44m";

        final String FG_BLACK = "\u001B[30m";
        final String FG_WHITE = "\u001B[97m";

        boolean color = false;

        System.out.println("Битовая доска (bb = " + Long.toUnsignedString(bb) + "):");
        System.out.println("Шестнадцатеричный вид: 0x" + Long.toHexString(bb).toUpperCase());
        if (position >= 0 && position < 64) {
            System.out.println("Позиция фигуры: " + position + " (" +
                    getChessNotation(position) + ")");
        }
        System.out.println("Количество установленных битов: " + Long.bitCount(bb));
        System.out.println();

        for (int row = 7; row >= 0; row--) {
            // Печать номера строки
            System.out.print(BG_WHITE + FG_BLACK + (row + 1) + ":  " + RESET);

            for (int col = 0; col < 8; col++) {
                int cellIndex = row * 8 + col;
                boolean isSet = (bb & (1L << cellIndex)) != 0;
                boolean isFigure = (cellIndex == position);

                String bg, fg;
                String symbol = " • ";

                if (isFigure && isSet) {
                    // Фигура на возможном ходе (для отладки)
                    bg = BG_BLUE;
                    fg = FG_WHITE;
                    symbol = " F ";
                } else if (isFigure) {
                    // Позиция фигуры
                    bg = BG_GREEN;
                    fg = FG_WHITE;
                    symbol = " F ";
                } else if (isSet) {
                    // Возможные ходы
                    bg = BG_RED;
                    fg = FG_WHITE;
                    symbol = " X ";
                } else {
                    // Обычная клетка
                    bg = color ? BG_GRAY : BG_WHITE;
                    fg = FG_BLACK;
                    symbol = " • ";
                }

                System.out.print(bg + fg + symbol + RESET);
                color = !color;
            }

            color = !color;
            System.out.println();
        }

        // Подпись вертикалей
        System.out.print("    ");
        for (int i = 0; i < 8; i++) {
            System.out.print(BG_WHITE + FG_BLACK + " " + (char) ('A' + i) + " " + RESET);
        }
        System.out.println();
    }

    protected String getChessNotation(int position) {
        if (position < 0 || position > 63) return "invalid";
        char file = (char) ('a' + (position % 8));
        int rank = (position / 8) + 1;
        return "" + file + rank;
    }
}