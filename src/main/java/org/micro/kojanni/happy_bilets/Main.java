package org.micro.kojanni.happy_bilets;

public class Main {
    public static void main(String[] args) {

        Solver solver = new Solver();
        Test test = new Test(solver::run);
        test.run();
    }
}