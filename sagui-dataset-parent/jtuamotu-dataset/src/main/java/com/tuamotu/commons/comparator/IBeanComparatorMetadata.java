package com.tuamotu.commons.comparator;

public interface IBeanComparatorMetadata<T> {

    public String getField();

    public IBeanComparator.Order getOrder();

    public boolean isNullFirst();

}
