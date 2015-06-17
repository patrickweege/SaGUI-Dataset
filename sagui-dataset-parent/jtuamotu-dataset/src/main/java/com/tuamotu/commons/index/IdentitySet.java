package com.tuamotu.commons.index;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import com.tuamotu.commons.dataset.IBookmark;

@SuppressWarnings("unchecked")
public class IdentitySet<E> implements Set<E> {

    private final IdentityIndex<E> index;

    public IdentitySet() {
        this.index = new IdentityIndex<E>();
    }

    @Override
    public int size() {
        return index.size();
    }

    @Override
    public boolean isEmpty() {
        return index.size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        IBookmark<E> theBkm = index.getBookmark((E) o);
        return theBkm != null;
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean add(E e) {
        index.add(e);
        return true;
    }

    @Override
    public boolean remove(Object toRemove) {
        Object removed = index.remove((E) toRemove);
        return removed == toRemove;
    }

    @Override
    public boolean containsAll(Collection<?> all) {
        for (Object toFind : all) {
            if (!this.contains(toFind)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> all) {
        for (E toAdd : all) {
            this.index.add(toAdd);
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean removeAll(Collection<?> all) {
        for (Object element : all) {
            index.remove((E)element);
        }
        return true;
    }

    @Override
    public void clear() {
        index.clear();
    }

}
