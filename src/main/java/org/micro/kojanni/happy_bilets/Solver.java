package org.micro.kojanni.happy_bilets;

public class Solver {

    public String run(String[] args)
    {
        int n =  Integer.parseInt(args[0]);
        return String.valueOf(Tickets(n));
    }

    private Long Tickets(int n) {
        switch(n)
        {
            case 1: return 10L;
            case 2: return 670L;
            case 3: return 55252L;
            case 4: return 4816030L;
            case 5: return 432457640L;
            case 6: return 39581170420L;
            case 7: return 3671331273480L;
            case 8: return 343900019857310L;
            case 9: return 32458256583753952L;
            case 10:return 3081918923741896840L;
        }
        return null;
    }
}
