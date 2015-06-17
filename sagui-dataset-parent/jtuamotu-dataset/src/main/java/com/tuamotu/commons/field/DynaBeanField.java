package com.tuamotu.commons.field;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;

public class DynaBeanField extends AbstractField<DynaBean> {

    private final DynaClass dynaClass;

    public DynaBeanField(DynaClass dynaClass, String name, String label, boolean isReadOnly) {
        super(name, label, isReadOnly);
        this.dynaClass = dynaClass;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V> V getValue(DynaBean bean) {
        return (V) bean.get(super.getName());
    }

    @Override
    public <V> void setValue(DynaBean bean, V value) {
        bean.set(super.getName(), value);
    }

    @Override
    public Class<?> getFieldClass() {
        DynaProperty dynaProp = dynaClass.getDynaProperty(super.getName());
        return dynaProp.getType();
    }
    

}
