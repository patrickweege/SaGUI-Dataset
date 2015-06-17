package com.tuamotu.commons.field;

/**
 * Describe a Bean Field
 * 
 * @User: pweege
 * @Date: 06/07/2010 Time: 09:16:50
 */
public interface IField<T> {

    public String getName();

    public String getLabel();
    
    public String getDescription();

    public <V> V getValue(T bean);

    public <V> void setValue(T bean, V value);

    public Class<?> getFieldClass();

    public boolean isReadOnly();

}
