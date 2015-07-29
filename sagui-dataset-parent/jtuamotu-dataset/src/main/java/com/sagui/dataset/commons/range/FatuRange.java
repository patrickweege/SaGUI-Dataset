package com.sagui.dataset.commons.range;

import java.util.Comparator;

public class FatuRange<T> {

    private final T from;
    private final T to;
    private final Comparator<T> comparator;

    public FatuRange(T from, T to, Comparator<T> comparator) {
        this.comparator = comparator;
        int result = comparator.compare(from, to);
        if (result <= 0) {
            this.from = from;
            this.to = to;
        } else {
            this.from = to;
            this.to = from;
        }
    }

    public T getFrom() {
        return from;
    }

    public T getTo() {
        return to;
    }

    public boolean contains(T element) {
        boolean geFrom = this.comparator.compare(from, element) <= 0;
        boolean ltTo = this.comparator.compare(to, element) >= 0;
        return geFrom && ltTo;
    }

}
