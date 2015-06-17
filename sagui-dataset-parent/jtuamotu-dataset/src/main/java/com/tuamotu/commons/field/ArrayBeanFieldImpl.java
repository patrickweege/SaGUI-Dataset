package com.tuamotu.commons.field;

public class ArrayBeanFieldImpl<T> extends AbstractField<T[]> {

    private final int index;

    public ArrayBeanFieldImpl(int index) {
        this(index, Integer.toString(index), false);
    }

    public ArrayBeanFieldImpl(int index, String title, boolean isReadOnly) {
        super(Integer.toString(index), title, false);
        this.index = index;
    }

    @SuppressWarnings("unchecked")
    public <V> V getValue(T[] bean) {
        return (V) bean[index];
    }

    @SuppressWarnings("unchecked")
    public <V> void setValue(T[] bean, V value) {
        bean[index] = (T) value;
    }
}
