package com.sagui.dataset.commons.dataset;

import java.util.Comparator;

class DatasetRowFindFirstComparator<T> implements Comparator<T> {

    private final Comparator<T> beanComparator;

    public DatasetRowFindFirstComparator(Comparator<T> beanComparator) {
        this.beanComparator = beanComparator;
    }

    @Override
    public int compare(T one, T other) {
        int retVal = beanComparator.compare(one, other);

        if (retVal != 0) return retVal;

        return +1;
    }


}
