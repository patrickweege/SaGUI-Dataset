package com.sagui.dataset.commons.field;

import org.apache.commons.collections4.Transformer;

public final class CalculatedFieldImpl<T, V> implements IField<T> {

    private String name;
    private Transformer calculator;

    public CalculatedFieldImpl(String name, Transformer calculator) {
        if(name == null || calculator == null) {
            throw new IllegalArgumentException("name and calculator cannot be null");
        }
        this.name = name;
        this.calculator = calculator;
    }

    public String getName() {
        return this.name;
    }

    @SuppressWarnings({"unchecked","hiding"})
    public <V> V getValue(T bean) {
        return (V)calculator.transform(bean);
    }

    /**
     * throws new UnsupportedOperationException("Not implemented");
     */
    @SuppressWarnings("hiding")
    public <V> void setValue(T bean, V value) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Class<?> getFieldClass() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isReadOnly() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getLabel() {
        // TODO Auto-generated method stub
        return null;
    }

}
