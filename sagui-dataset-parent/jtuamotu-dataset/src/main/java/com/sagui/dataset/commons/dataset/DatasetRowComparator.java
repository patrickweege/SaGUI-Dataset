package com.sagui.dataset.commons.dataset;

import java.util.Comparator;

class DatasetRowComparator<T> implements Comparator<T> {

    private final Comparator<T> beanComparator;
    private final Comparator<T> tieBreakComparator;
    
    public DatasetRowComparator(Comparator<T> beanComparator, Comparator<T> tieBreakComparator) {
        this.beanComparator = beanComparator;
        this.tieBreakComparator = tieBreakComparator;
    }

    @Override
    public int compare(T one, T other) {
        int retVal = beanComparator.compare(one, other);

        if (retVal != 0) return retVal;

        return tieBreakComparator.compare(one, other);
    }

}
