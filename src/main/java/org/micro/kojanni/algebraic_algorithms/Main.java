package org.micro.kojanni.algebraic_algorithms;

public class Main {
    public static void main(String[] args) {

        GcdSolver gcdSolver = new GcdSolver();
        Test test1 = new Test(gcdSolver::subtraction);
        System.out.println("Алгоритм Евклида через вычитание:");
        test1.run("src/main/resources/algebraic_algorithms/2.GCD/");

//        Test test2 = new Test(gcdSolver::remainder);
//        System.out.println("Алгоритм Евклида через остаток:");
//        test2.run("src/main/resources/algebraic_algorithms/2.GCD/");

//        Test test3 = new Test(gcdSolver::bitOperation);
//        System.out.println("Алгоритм Стейнца через битовые операции:");
//        test3.run("src/main/resources/algebraic_algorithms/2.GCD/");
    }
}