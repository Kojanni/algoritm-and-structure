package org.micro.kojanni.string_search.kmp_lesson;

public class KMPSlow extends KMPBase {

    public KMPSlow(String pattern) {
        super(pattern);
    }

    @Override
    int[] getPi(String pattern) {
        return PrefixFunction.slowPrefixFunction(pattern);
    }
}
