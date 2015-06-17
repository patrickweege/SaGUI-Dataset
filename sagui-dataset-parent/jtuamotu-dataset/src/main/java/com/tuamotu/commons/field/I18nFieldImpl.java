package com.tuamotu.commons.field;

import com.tuamotu.commons.i18n.I18n;

public class I18nFieldImpl<T> implements IField<T> {

    private final IField<T> delegate;
    private final I18n description;
    private final I18n label;
    private final String fieldName;

    public I18nFieldImpl(String fieldName, I18n label, I18n description, IField<T> delegate) {
        this.fieldName = fieldName;
        this.label = label;
        this.description = description;
        this.delegate = delegate;
    }

    public I18nFieldImpl(I18n label, I18n description, IField<T> delegate) {
        this(null, label, description, delegate);
    }

    public I18nFieldImpl(I18n label, IField<T> delegate) {
        this(label, label, delegate);
    }

    @Override
    public String getName() {
        return fieldName != null ? fieldName : delegate.getName();
    }

    @Override
    public String getLabel() {
        return label.getDefault();
    }

    @Override
    public String getDescription() {
        return description.getDefault();
    }

    @Override
    public <V> V getValue(T bean) {
        return delegate.getValue(bean);
    }

    @Override
    public <V> void setValue(T bean, V value) {
        this.delegate.setValue(bean, value);
    }

    @Override
    public Class<?> getFieldClass() {
        return this.delegate.getFieldClass();
    }

    @Override
    public boolean isReadOnly() {
        return this.delegate.isReadOnly();
    }

}
