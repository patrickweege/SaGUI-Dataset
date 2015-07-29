package com.sagui.dataset.commons.comparator;

import java.util.Comparator;

import org.apache.commons.collections4.comparators.ComparableComparator;

import com.sagui.dataset.commons.comparator.IBeanComparator.Order;
import com.sagui.dataset.commons.field.IField;

public class IFieldComparatorMetadata<T> implements IBeanComparatorMetadata<T> {

    private final IField<T> field;
    private final Order order;
    private final boolean isNullFirst;
    private final Comparator<?> valueComparator;

    public IFieldComparatorMetadata(IField<T> field, IBeanComparator.Order order, boolean isNullFirst, Comparator<?> valueComparator) {
        this.field = field;
        this.order = order;
        this.isNullFirst = isNullFirst;
        this.valueComparator = valueComparator;
    }

    public IFieldComparatorMetadata(IField<T> field, IBeanComparator.Order order, boolean isNullFirst) {
        this(field, order, isNullFirst, ComparableComparator.INSTANCE);
    }

    public IFieldComparatorMetadata(IField<T> field, boolean isNullFirst) {
        this(field, Order.ASC, isNullFirst, ComparableComparator.INSTANCE);
    }

    @Override
    public String getField() {
        return field.getName();
    }

    @Override
    public Order getOrder() {
        return order;
    }

    @Override
    public boolean isNullFirst() {
        return isNullFirst;
    }

    @SuppressWarnings("unchecked")
    public <V> Comparator<V> getValueComparator() {
        return (Comparator<V>) valueComparator;
    }
    
    protected IField<T> getBeanField() {
        return this.field;
    }

}
