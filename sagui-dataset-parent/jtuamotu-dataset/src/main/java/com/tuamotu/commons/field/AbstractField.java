package com.tuamotu.commons.field;

public abstract class AbstractField<T> implements IField<T> {

    private final String name;
    private final String description;
    private final String label;
    private final boolean readOnly;
    private final Class<?> fieldClass;

    public AbstractField(String name) {
        this(name, name, name, Object.class, false);
    }

    public AbstractField(String name, String label, boolean isReadOnly) {
        this(name, label, label, Object.class, isReadOnly);
    }

    public AbstractField(String name, String label, String description, Class<?> fieldClass, boolean isReadOnly) {
        this.name = name;
        this.label = label;
        this.description = description;
        this.readOnly = isReadOnly;
        this.fieldClass = fieldClass;
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public String getLabel() {
        return this.label;
    }

    @Override
    public Class<?> getFieldClass() {
        return fieldClass;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }
}
