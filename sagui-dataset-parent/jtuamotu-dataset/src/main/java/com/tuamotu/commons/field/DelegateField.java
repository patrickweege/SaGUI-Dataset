package com.tuamotu.commons.field;

public class DelegateField<T> extends AbstractDelegateField<T, T> implements IField<T> {

    public DelegateField(IField<T> delegate) {
        super(delegate);
    }

    public DelegateField(String description, boolean isReadOnly, IField<T> delegate) {
        super(description, isReadOnly, delegate);
    }

    public DelegateField(String description, IField<T> delegate) {
        super(description, delegate);
    }

    public DelegateField(String name, String description, boolean isReadOnly, IField<T> delegate) {
        super(name, description, isReadOnly, delegate);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V> V getValue(T bean) {
        return (V) super.getDelegateValue(bean);
    }

    @Override
    public <V> void setValue(T bean, V value) {
        super.setDelegateValue(bean, value);
    }

}
