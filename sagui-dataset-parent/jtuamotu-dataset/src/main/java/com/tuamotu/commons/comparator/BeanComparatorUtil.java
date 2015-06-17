package com.tuamotu.commons.comparator;

import java.util.Comparator;

import org.apache.commons.collections4.ComparatorUtils;
import org.apache.commons.collections4.comparators.ComparableComparator;
import org.apache.commons.collections4.comparators.ComparatorChain;

import com.tuamotu.commons.field.BeanFieldHelper;
import com.tuamotu.commons.field.IField;

public class BeanComparatorUtil {

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T> IBeanComparator<T> getBeanComparator(Class<T> beanClass, BeanComparatorMetadata<T>... comparatorMetadata) {
        ComparatorChain<T> chain = new ComparatorChain<T>();
        for (IBeanComparatorMetadata<T> metadata : comparatorMetadata) {
            Comparator<T> c = ComparableComparator.INSTANCE;
            c = metadata.isNullFirst() ? ComparatorUtils.nullLowComparator(c) : ComparatorUtils.nullHighComparator(c);
            IField<T> field = BeanFieldHelper.getField(metadata.getField(), beanClass);
            c = new BeanFieldComparator<T>(field, c);
            c = ComparatorUtils.nullHighComparator(c);
            chain.addComparator(c, metadata.getOrder() == IBeanComparator.Order.ASC ? false : true);
        }
        return new BeanComparatorWrapper<T>(chain, comparatorMetadata);
    }

    @SafeVarargs
    public static <T> IBeanComparator<T> getBeanComparator(IFieldComparatorMetadata<T>... comparatorMetadata) {
        ComparatorChain<T> chain = new ComparatorChain<T>();
        for (IFieldComparatorMetadata<T> meta : comparatorMetadata) {
            Comparator<T> c = meta.getValueComparator();
            c = meta.isNullFirst() ? ComparatorUtils.nullLowComparator(c) : ComparatorUtils.nullHighComparator(c);
            c = new BeanFieldComparator<T>(meta.getBeanField(), c);
            c = ComparatorUtils.nullHighComparator(c);
            chain.addComparator(c, meta.getOrder() == IBeanComparator.Order.ASC ? false : true);
        }
        return new BeanComparatorWrapper<T>(chain, comparatorMetadata);
    }

}
