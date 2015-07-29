package com.sagui.dataset.commons.field;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class FieldChain<T> implements IField<T> {

    private List<IField> chain;
    private IField leafField;
    private boolean isReadOnly;
    private String name;

    public FieldChain() {
        chain = new ArrayList<IField>();
    }

    public void addField(IField<?> field) {
        this.chain.add(field);
        this.leafField = field;

        this.isReadOnly = true;
        StringBuilder sbName = new StringBuilder();
        for (Iterator iterator = chain.iterator(); iterator.hasNext();) {
            IField elem = (IField) iterator.next();
            isReadOnly = isReadOnly && elem.isReadOnly();
            sbName.append(elem.getName());
            if (iterator.hasNext()) {
                sbName.append(".");
            }
        }
        this.name = sbName.toString();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public <V> V getValue(T bean) {
        Object value = bean;
        for (IField elem : chain) {
            if (value == null) return null;
            value = elem.getValue(value);
        }
        return (V) value;
    }

    @Override
    public <V> void setValue(T bean, V value) {
        Object leafBean = getLeafBean(bean);
        this.leafField.setValue(leafBean, value);
    }

    @Override
    public String getDescription() {
        return this.name;
    }

    @Override
    public Class<?> getFieldClass() {
        return leafField.getFieldClass();
    }

    @Override
    public boolean isReadOnly() {
        return this.isReadOnly;
    }

    @Override
    public String getLabel() {
        return this.name;
    }

    private Object getLeafBean(Object rootBean) {
        Iterator<IField> iterator = chain.iterator();
        Object leafBean = rootBean;
        while (iterator.hasNext()) {
            IField next = iterator.next();
            if (next != leafField) {
                leafBean = next.getValue(leafBean);
            }
        }
        return leafBean;
    }

}
