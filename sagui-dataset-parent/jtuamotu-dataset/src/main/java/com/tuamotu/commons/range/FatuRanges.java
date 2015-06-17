package com.tuamotu.commons.range;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class FatuRanges<T> {

    private final Comparator<T> comparator;
    private final ArrayList<FatuRange<T>> ranges;

    public FatuRanges(Comparator<T> comparator) {
        this.comparator = comparator;
        this.ranges = new ArrayList<FatuRange<T>>();
    }

    public boolean contains(T element) {
        for (FatuRange<T> range : ranges) {
            if (range.contains(element)) return true;
        }
        return false;
    }

    public void add(T element) {
        if (!contains(element)) {
            ranges.add(new FatuRange<T>(element, element, comparator));
        }
    }

    public Collection<FatuRange<T>> getRanges() {
        return Collections.unmodifiableCollection(ranges);
    }

}
