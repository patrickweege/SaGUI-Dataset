package com.tuamotu.commons.comparator;

public class BeanComparatorMetadata<T> implements IBeanComparatorMetadata<T> {

    private final String field;
    private final IBeanComparator.Order order;
    private final boolean isNullFirst;
    
    public BeanComparatorMetadata(String field, IBeanComparator.Order order, boolean isNullFirst) {
        super();
        this.field = field;
        this.order = order;
        this.isNullFirst = isNullFirst;
    }
    
    public String getField() {
        return this.field;
    }

    public boolean isNullFirst() {
        return this.isNullFirst;
    }

    public IBeanComparator.Order getOrder() {
        return this.order;
    }


}
