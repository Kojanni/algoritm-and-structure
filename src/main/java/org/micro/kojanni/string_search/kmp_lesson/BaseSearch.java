package org.micro.kojanni.string_search.kmp_lesson;

import java.util.List;

public abstract class BaseSearch {

    protected String pattern;

    public BaseSearch(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String toString() {
        return "pattern=" + pattern;
    }

    public abstract List<Integer> search(String text);
}
