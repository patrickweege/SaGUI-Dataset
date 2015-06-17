package com.tuamotu.commons.dataset;

import java.util.Comparator;

class DatasetRowFindLastComparator<T> implements Comparator<T> {

    private final Comparator<T> beanComparator;

    public DatasetRowFindLastComparator(Comparator<T> beanComparator) {
        this.beanComparator = beanComparator;
    }

    @Override
    public int compare(T one, T other) {
        int retVal = beanComparator.compare(one, other);

        if (retVal != 0) return retVal;

        return -1;
    }


}
