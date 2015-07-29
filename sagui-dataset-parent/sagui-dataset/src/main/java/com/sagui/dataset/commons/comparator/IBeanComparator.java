package com.sagui.dataset.commons.comparator;

import java.util.Comparator;


public interface IBeanComparator<T> extends Comparator<T> {

    public enum Order {
        ASC, DESC
    }

    public IBeanComparatorMetadata[] getMetadata();

}
