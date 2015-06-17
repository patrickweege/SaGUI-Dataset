package com.tuamotu.commons.field;

import java.beans.PropertyDescriptor;
import java.util.StringTokenizer;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * Helper for BeanField
 * 
 * @User: pweege
 * @Date: 06/07/2010
 * @Time: 09:20:10
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BeanFieldHelper {

    public static <T, V> IField<T> getField(String name, Class<T> beanClass) {
        StringTokenizer tkn = new StringTokenizer(name, ".", false);
        FieldChain<T> fieldChain = new FieldChain<T>();
        Class clazz = beanClass;
        while (tkn.hasMoreTokens()) {
            String prop = tkn.nextToken();
            PropertyDescriptor descriptor = getPropertyDescriptor(prop, clazz);
            fieldChain.addField(new InternalIFieldImpl(descriptor));
            clazz = descriptor.getReadMethod().getReturnType();
        }
        return fieldChain;
    }

    private static PropertyDescriptor getPropertyDescriptor(String property, Class clazz) {
        PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(clazz);
        for (PropertyDescriptor desc : descriptors) {
            if (property.equals(desc.getName())) {
                return desc;
            }
        }
        return null;
    }


    private static class InternalIFieldImpl<T> implements IField<T> {

        private final PropertyDescriptor descriptor;

        private InternalIFieldImpl(PropertyDescriptor descriptor) {
            this.descriptor = descriptor;
        }

        public String getName() {
            return descriptor.getName();
        }

        public <V> V getValue(T bean) {
            try {
                return (V) descriptor.getReadMethod().invoke(bean);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public <V> void setValue(T bean, V value) {
            if (isReadOnly()) {
                throw new UnsupportedOperationException("Property: " + descriptor.getPropertyType() + "." + descriptor.getName() + " has no Write method");
            }
            try {
                descriptor.getWriteMethod().invoke(bean, value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String getDescription() {
            return descriptor.getName();
        }

        @Override
        public Class<?> getFieldClass() {
            return descriptor.getPropertyType();
        }

        @Override
        public boolean isReadOnly() {
            return descriptor.getWriteMethod() == null;
        }

        @Override
        public String getLabel() {
            return descriptor.getName();
        }

    }
}
