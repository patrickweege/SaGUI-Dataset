package com.tuamotu.commons.comparator;

import java.util.Comparator;

import com.tuamotu.commons.field.IField;

@SuppressWarnings({"rawtypes","unchecked"})
class BeanFieldComparator<T> implements Comparator<T> {

    private final IField<T> field;
    private final Comparator comparator;

    public BeanFieldComparator(IField<T> field, Comparator<?> comparator) {
        this.field = field;
        this.comparator = comparator;
    }

    public int compare(T one, T other) {
        Object v1 = field.getValue(one);
        Object v2 = field.getValue(other);
        return comparator.compare(v1, v2);
    }
}
