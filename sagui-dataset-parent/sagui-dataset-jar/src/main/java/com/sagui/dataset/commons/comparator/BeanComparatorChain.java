/**
 * 
 */
package com.sagui.dataset.commons.comparator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections4.comparators.ComparatorChain;

@SuppressWarnings({ "rawtypes", "unchecked" })
class BeanComparatorChain<T> implements IBeanComparator<T> {

    private final IBeanComparatorMetadata[] metadata;
    private final Comparator<T> comparator;

    public BeanComparatorChain(IBeanComparator<T>... comparators) {
        ComparatorChain chain = new ComparatorChain();
        List<IBeanComparatorMetadata> l = new ArrayList<IBeanComparatorMetadata>();
        for (IBeanComparator<T> c : comparators) {
            l.addAll(Arrays.asList(c.getMetadata()));
            chain.addComparator(c);
        }
        this.metadata = l.toArray(new IBeanComparatorMetadata[0]);
        this.comparator = chain;
    }

    public IBeanComparatorMetadata[] getMetadata() {
        return metadata;
    }

    public int compare(T one, T other) {
        return comparator.compare(one, other);
    }

}
