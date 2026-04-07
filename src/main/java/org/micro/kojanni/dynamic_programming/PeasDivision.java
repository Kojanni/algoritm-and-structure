package org.micro.kojanni.dynamic_programming;

import org.micro.kojanni.utils.Test;

public class PeasDivision {

    public static String run(String[] input) {
        String line = input[0];
        String[] parts = line.split("\\+");
        
        String[] frac1 = parts[0].split("/");
        long a = Long.parseLong(frac1[0]);
        long b = Long.parseLong(frac1[1]);
        
        String[] frac2 = parts[1].split("/");
        long c = Long.parseLong(frac2[0]);
        long d = Long.parseLong(frac2[1]);
        
        // a/b + c/d = (a*d + c*b) / (b*d)
        long numerator = a * d + c * b;
        long denominator = b * d;

        long gcd = gcd(numerator, denominator);
        numerator /= gcd;
        denominator /= gcd;
        
        return numerator + "/" + denominator;
    }

    //алгоритма Евклида
    private static long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public static void main(String[] args) {
        Test test = new Test(PeasDivision::run);
        test.run("src/test/java/org/micro/kojanni/dynamic_programming/Раз_горох/");
    }
}
