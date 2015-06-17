package com.tuamotu.commons.dataset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.tuamotu.commons.collections.SortedListUtil;

@SuppressWarnings("unchecked")
public class DatasetIndex<T> {

    private final List<T> data;
    private final Comparator<T> beanComparator;
    private final DatasetRowComparator<T> rowComparator;
    private final DatasetRowFindFirstComparator<T> findFirstComparator;
    private final DatasetRowFindLastComparator<T> findLastComparator;
    private final boolean unique;

    public DatasetIndex(Comparator<T> beanComparator, Comparator<T> identiyComparator) {
        this.data = new ArrayList<T>();
        this.beanComparator = beanComparator;
        this.rowComparator = new DatasetRowComparator<T>(this.beanComparator, identiyComparator);
        this.findFirstComparator = new DatasetRowFindFirstComparator<T>(this.beanComparator);
        this.findLastComparator = new DatasetRowFindLastComparator<T>(this.beanComparator);
        this.unique = false;
    }

    protected void add(T row) {
        SortedListUtil.add(row, data, rowComparator, unique);
    }

    protected void remove(T row) {
        SortedListUtil.remove(row, data, rowComparator);
    }

    protected T findFirst(T example) {
        int pos = SortedListUtil.search(example, data, findFirstComparator);
        if (pos < 0) {
            pos = Math.abs(pos + 1);
        }
        T found = getRow(pos);

        if (found == null) {
            return null;
        }

        int matches = beanComparator.compare(example, found);

        return matches == 0 ? found : null;
    }

    protected T findLast(T example) {
        int pos = SortedListUtil.search(example, data, findLastComparator);
        if (pos < 0) {
            pos = Math.abs(pos + 1) - 1; // (-1) to Return to The Previous
        }
        T found = getRow(pos);

        if (found == null) {
            return null;
        }

        int matches = beanComparator.compare(example, found);

        return matches == 0 ? found : null;
    }

    protected T getRow(int index) {
        if (index >= 0 && index < data.size()) {
            return data.get(index);
        }
        return null;
    }

    protected int indexOf(T row) {
        int pos = SortedListUtil.search(row, data, rowComparator);
        return pos;
    }

    protected void setData(Collection<T> newData) {
        this.data.clear();
        this.data.addAll(newData);
        Collections.sort(data, rowComparator);
    }
    
    protected int size() {
        return data.size();
    }
    
}
