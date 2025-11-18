package org.micro.kojanni.bitwise_arithmetic;


public class Main {
    public static void main(String[] args) {

        King solverKing = new King();
        Test testKing = new Test(solverKing::steps);
        System.out.println("Прогулка Короля:");
        testKing.run("src/main/resources/bitwise_arithmetic/1.Bitboard - Король/");

        King king = new King();
        king.process(7);
        king.print();

        Knight solverKnight = new Knight();
        Test testKnight = new Test(solverKnight::steps);
        System.out.println("Прогулка Коня:");
        testKnight.run("src/main/resources/bitwise_arithmetic/2.Bitboard - Конь/");

        Knight knight = new Knight();
        knight.process(0);
        knight.print();

        Bishop solverBishop = new Bishop();
        Test testBishop = new Test(solverBishop::steps);
        System.out.println("Прогулка Слона:");
        testBishop.run("src/main/resources/bitwise_arithmetic/4.Bitboard - Слон/");

        Bishop bishop = new Bishop();
        bishop.process(0);
        bishop.print();

        Rook solverRook = new Rook();
        Test testRook = new Test(solverRook::steps);
        System.out.println("Прогулка Ладья:");
        testRook.run("src/main/resources/bitwise_arithmetic/3.Bitboard - Ладья/");

        Rook rook = new Rook();
        rook.process(0);
        rook.print();


        Queen solverQueen = new Queen();
        Test testQueen = new Test(solverQueen::steps);
        System.out.println("Прогулка Ферзь:");
        testQueen.run("src/main/resources/bitwise_arithmetic/5.Bitboard - Ферзь/");

        Queen queen = new Queen();
        queen.process(0);
        queen.print();

    }
}