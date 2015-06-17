package com.tuamotu.commons.index;

import com.tuamotu.commons.dataset.IBookmark;

public interface IIdentityIndex<T> {

    public abstract void add(T element);

    public abstract T remove(T toRemove);

    public abstract T remove(IBookmark<T> toRemove);

    public abstract IBookmark<T> getBookmark(T forElement);

    public abstract IBookmark<T> getBookmark(String bookmarkUUID);

    public abstract T getElement(IBookmark<T> bookmark);

    public abstract boolean contains(T element);

    public abstract void clear();

    public abstract int size();

}
