package com.tuamotu.commons.comparator;

import java.util.Comparator;

import org.apache.commons.collections4.ComparatorUtils;

public class ReferenceComparator<T> implements Comparator<T> {

    public ReferenceComparator() {
    }

    @Override
    public int compare(T one, T other) {
        String oneStr = one.toString();
        String otherStr = other.toString(); 
        return ComparatorUtils.NATURAL_COMPARATOR.compare(oneStr, otherStr) ;
    }
    

}
