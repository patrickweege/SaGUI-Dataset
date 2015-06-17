package com.tuamotu.commons.dataset;

import java.util.Comparator;
import java.util.Iterator;

import com.tuamotu.commons.field.IField;

public interface IDataset<T> {

    public IDatasetField<T> addField(IField<T> field);

    public DatasetIndex<T> addIndex(Comparator<T> comparator);

    public IBookmark<T> add(T element);

    public T remove(IBookmark<T> bookmark);

    public <V> void setValue(IBookmark<T> bookmark, IField<T> field, V value);

    public <V> V getValue(IBookmark<T> bookmark, IField<T> field);
    
    public <V> V getValue(int row, IField<T> field, DatasetIndex<T> datasetIndex);

    public IBookmark<T> findFirst(T example, DatasetIndex<T> datasetIndex);

    public int getRecordCount();
    
    public int indexOf(IBookmark<T> bookmark, DatasetIndex<T> datasetIndex);
    
    public IBookmark<T> getBookmark(int rowIndex, DatasetIndex<T> datasetIndex);
    
    public IBookmark<T> getBookmark(String bookmarkUUID);
    
    public void clear();
    
    public Iterator<T> getIterator(DatasetIndex<T> overIndex);

    public T getRow(IBookmark<T> bookmark);
    
}
