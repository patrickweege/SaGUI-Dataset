package com.tuamotu.commons.field;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;


public abstract class AbstractDelegateField<T,DELEGATE> implements IField<T> {

    private final String name;
    private final String description;
    private final String label;
    private final Boolean readOnly;
    private final Class<?> fieldClass;

    private final IField<DELEGATE> delegate;

    public AbstractDelegateField(String name, String description, boolean isReadOnly, IField<DELEGATE> delegate) {
        this.delegate = delegate;
        this.name = name;
        this.description = description;
        this.label = null;
        this.readOnly = isReadOnly;
        this.fieldClass = null;
    }

    public AbstractDelegateField(String description, boolean isReadOnly, IField<DELEGATE> delegate) {
        this.delegate = delegate;
        this.name = null;
        this.description = description;
        this.label = null;
        this.readOnly = isReadOnly;
        this.fieldClass = null;
    }

    public AbstractDelegateField(String description, IField<DELEGATE> delegate) {
        this.delegate = delegate;
        this.name = null;
        this.description = description;
        this.label = null;
        this.readOnly = null;
        this.fieldClass = null;
    }

    public AbstractDelegateField(IField<DELEGATE> delegate) {
        this.delegate = delegate;
        this.name = null;
        this.description = null;
        this.label = null;
        this.readOnly = null;
        this.fieldClass = null;
    }

    @Override
    public String getName() {
        return (String) ObjectUtils.defaultIfNull(this.name, delegate.getName());
    }

    @Override
    public String getLabel() {
        return (String) ObjectUtils.defaultIfNull(this.label, delegate.getLabel());
    }

    @Override
    public String getDescription() {
        return (String) ObjectUtils.defaultIfNull(this.description, delegate.getDescription());
    }

    @Override
    public Class<?> getFieldClass() {
        return (Class<?>) ObjectUtils.defaultIfNull(this.fieldClass, delegate.getFieldClass());
    }

    @Override
    public boolean isReadOnly() {
        return (Boolean) ObjectUtils.defaultIfNull(this.readOnly, delegate.isReadOnly());
    }
    
    @Override
    public boolean equals(Object obj) {
        String thisName = this.getName();
        String otherName = ((IField<?>)obj).getName();
        return StringUtils.equals(thisName, otherName);
    }
    
    protected <V> V getDelegateValue(DELEGATE bean) {
        return delegate.getValue(bean);
    };
    
    protected <V> void setDelegateValue(DELEGATE bean, V value) {
        delegate.setValue(bean, value);
    };

}
