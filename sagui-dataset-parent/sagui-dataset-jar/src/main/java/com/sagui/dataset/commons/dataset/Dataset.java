package com.sagui.dataset.commons.dataset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.LazyDynaClass;
import org.apache.commons.beanutils.MutableDynaClass;

import com.sagui.dataset.commons.field.AbstractDelegateField;
import com.sagui.dataset.commons.field.DynaBeanField;
import com.sagui.dataset.commons.field.IField;
import com.sagui.dataset.commons.index.ElementNotFoundException;
import com.sagui.dataset.commons.index.IdentityIndex;

public class Dataset<T> implements IDataset<T> {

    private final IdentityIndex<T> data;
    private final ArrayList<DatasetIndex<T>> indexes;
    private final List<IField<T>> fields;

    private final MutableDynaClass dynaClass;
    private final IdentityHashMap<T, DynaBean> dynaData;

    public Dataset() {
        this.data = new IdentityIndex<T>();
        this.indexes = new ArrayList<DatasetIndex<T>>();
        this.fields = new ArrayList<IField<T>>();
        this.dynaClass = new LazyDynaClass();
        this.dynaData = new IdentityHashMap<T, DynaBean>();
    }

    /* (non-Javadoc)
     * @see com.pw.common.dataset.IDataset#addField(com.pw.common.field.IField)
     */
    @Override
    public IDatasetField<T> addField(IField<T> field) {
        DatasetField datasetField = new DatasetField(field);
        this.fields.add(datasetField);
        return datasetField;
    }

    public IDatasetField<T> addField(String name, Class<?> clazz) {
        dynaClass.add(name, clazz);
        DynaBeanField dynaField = new DynaBeanField(dynaClass, name, name, false);
        DatasetDynaField dsField = new DatasetDynaField(dynaField);
        fields.add(dsField);
        return dsField;
    }

    /* (non-Javadoc)
     * @see com.pw.common.dataset.IDataset#addIndex(java.util.Comparator)
     */
    @Override
    public DatasetIndex<T> addIndex(Comparator<T> comparator) {
        DatasetIndex<T> theIndex = new DatasetIndex<T>(comparator, data.IDENTITY_COMPARATOR);
        indexes.add(theIndex);
        return theIndex;
    }

    /* (non-Javadoc)
     * @see com.pw.common.dataset.IDataset#add(T)
     */
    @Override
    public IBookmark<T> add(T element) {
        this.data.add(element);
        IBookmark<T> bookmark = this.data.getBookmark(element);
        try {
            this.addToIndexes(element);
        } catch (Exception e) {
            this.data.remove(bookmark);
            throw new RuntimeException(e);
        }
        return bookmark;
    }

    /* (non-Javadoc)
     * @see com.pw.common.dataset.IDataset#remove(com.pw.common.dataset.IBookmark)
     */
    @Override
    public T remove(IBookmark<T> bookmark) {
        T removed = internalRemove(bookmark);
        return removed;
    }

    /* (non-Javadoc)
     * @see com.pw.common.dataset.IDataset#setValue(com.pw.common.dataset.IBookmark, com.pw.common.field.IField, V)
     */
    @Override
    public <V> void setValue(IBookmark<T> bookmark, IField<T> field, V value) {
        if (fields.contains(field)) {
            T theBean = getRow(bookmark);
            if (theBean != null) {
                field.setValue(theBean, value);
            } else {
                throw new RuntimeException("Invalid Bookmark");
            }
        } else {
            throw new RuntimeException("Invalid Field");
        }
    }

    /* (non-Javadoc)
     * @see com.pw.common.dataset.IDataset#getValue(com.pw.common.dataset.IBookmark, com.pw.common.field.IField, V)
     */
    @Override
    public <V> V getValue(IBookmark<T> bookmark, IField<T> field) {
        if (fields.contains(field)) {
            T found = getRow(bookmark);
            if (found == null) {
                throw new RuntimeException("Invalid Bookmark");
            }
            return field.getValue(found);
        } else {
            throw new RuntimeException("Invalid Field");
        }
    }
    
    @Override
    public <V> V getValue(int row, IField<T> field, DatasetIndex<T> index) {
        T value = index.getRow(row);
        return field.getValue(value);
    }

    /* (non-Javadoc)
     * @see com.pw.common.dataset.IDataset#findFirst(T, com.pw.common.dataset.DatasetIndex)
     */
    @Override
    public IBookmark<T> findFirst(T example, DatasetIndex<T> datasetIndex) {
        T found = datasetIndex.findFirst(example);
        if (found == null) return null;
        IBookmark<T> bookmark = data.getBookmark(found);
        return bookmark;
    }

    /*
     * (non-Javadoc)
     * @see com.pw.common.dataset.IDataset#getBookmark(int, com.pw.common.dataset.DatasetIndex)
     */
    @Override
    public IBookmark<T> getBookmark(int rowIndex, DatasetIndex<T> datasetIndex) {
        T element = datasetIndex.getRow(rowIndex);
        IBookmark<T> bookmark = data.getBookmark(element);
        return bookmark;
    }

    /*
     * (non-Javadoc)
     * @see com.pw.common.dataset.IDataset#getBookmark(java.lang.String, com.pw.common.dataset.DatasetIndex)
     */
    @Override
    public IBookmark<T> getBookmark(String bookmarkUUID) {
        return data.getBookmark(bookmarkUUID);
    }

    /*
     * (non-Javadoc)
     * @see com.pw.common.dataset.IDataset#clear()
     */
    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        this.data.clear();
        for (DatasetIndex<T> idx : indexes) {
            idx.setData(Collections.EMPTY_LIST);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.pw.common.dataset.IDataset#getRecordCount()
     */
    @Override
    public int getRecordCount() {
        return data.size();
    }

    /*
     * (non-Javadoc)
     * @see com.pw.common.dataset.IDataset#indexOf(com.pw.common.dataset.IBookmark, com.pw.common.dataset.DatasetIndex)
     */
    @Override
    public int indexOf(IBookmark<T> bookmark, DatasetIndex<T> datasetIndex) {
        T found = getRow(bookmark);
        return datasetIndex.indexOf(found);
    }

    @Override
    public Iterator<T> getIterator(DatasetIndex<T> overIndex) {
        return new DatasetIterator<T>(this, overIndex);
    }

    public final T getRow(IBookmark<T> bookmark) {
        T found = data.getElement(bookmark);
        if (found == null) {
            throw new RuntimeException("Invalid Bookmark");
        }
        return found;
    }

    private T internalRemove(IBookmark<T> toRemove) {
        T theElement = data.getElement(toRemove);
        if (toRemove != null) {
            for (DatasetIndex<T> idx : indexes) {
                idx.remove(theElement);
            }
            theElement = data.remove(theElement);
            removeDynaDataForRow(theElement);
        } else {
            throw new RuntimeException("Invalid Bookmark");
        }
        return theElement;
    }

    private void removeFromIndexes(T toRemove) {
        for (DatasetIndex<T> idx : indexes) {
            idx.remove(toRemove);
        }
    }

    private void addToIndexes(T toAdd) {
        for (DatasetIndex<T> idx : indexes) {
            idx.add(toAdd);
        }
    }

    private boolean hasRow(T row) {
        return this.data.contains(row);
    }

    private DynaBean getDynaDataForRow(T row, boolean createIfMissing) {
        DynaBean dynaBean = this.dynaData.get(row);
        if (dynaBean == null && createIfMissing) {
            try {
                dynaBean = this.dynaClass.newInstance();
                this.dynaData.put(row, dynaBean);
            } catch (IllegalAccessException | InstantiationException e) {
                throw new RuntimeException(e);
            }
        }
        return dynaBean;
    }

    private void removeDynaDataForRow(T row) {
        this.dynaData.remove(row);
    }

    private class DatasetIterator<E> implements Iterator<E> {

        private final Dataset<E> dataset;
        private final DatasetIndex<E> index;

        private int cursor; // index of next element to return
        private int lastRet = -1; // index of last element returned; -1 if no such
        private int expectedModCount;

        public DatasetIterator(Dataset<E> dataset, DatasetIndex<E> index) {
            this.dataset = dataset;
            this.index = index;
            this.expectedModCount = dataset.getRecordCount();
        }

        public boolean hasNext() {
            return cursor != dataset.getRecordCount();
        }

        public E next() {
            checkForComodification();
            int i = cursor;
            if (i >= data.size()) throw new NoSuchElementException();
            if (i >= index.size()) throw new ConcurrentModificationException();
            cursor = i + 1;
            return (E) index.getRow(lastRet = i);
        }

        public void remove() {
            if (lastRet < 0) throw new IllegalStateException();
            checkForComodification();
            try {
                E toRemove = this.index.getRow(lastRet);
                this.dataset.removeFromIndexes(toRemove);
                this.dataset.data.remove(toRemove);
                cursor = lastRet;
                lastRet = -1;
                expectedModCount = data.size();
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        final void checkForComodification() {
            if (data.size() != expectedModCount) throw new ConcurrentModificationException();
        }
    }

    private class DatasetField extends AbstractDelegateField<T, T> implements IDatasetField<T> {

        public DatasetField(IField<T> delegate) {
            super(delegate);
        }

        @Override
        public <V> V getValue(T bean) {
            return super.getDelegateValue(bean);
            // TODO patrickweege - Verify if Only Dataset elements Check is necessary
            //            if (Dataset.this.hasRow(bean)) {
            //                return super.getDelegateValue(bean);
            //            }
            //            throw new ElementNotFoundException();
        }

        public <V> void setValue(T bean, V value) {
            if (Dataset.this.hasRow(bean)) {
                Dataset.this.removeFromIndexes(bean);
                try {
                    super.setDelegateValue(bean, value);
                } finally {
                    Dataset.this.addToIndexes(bean);
                }
            } else {
                super.setDelegateValue(bean, value);
                // TODO patrickweege - Verify if Only Dataset elements Check is necessary
                //throw new ElementNotFoundException();
            }

        };
    }

    private class DatasetDynaField extends AbstractDelegateField<T, DynaBean> implements IDatasetField<T> {

        public DatasetDynaField(IField<DynaBean> delegate) {
            super(delegate);
        }

        @Override
        public <V> V getValue(T bean) {
            if (Dataset.this.hasRow(bean)) {
            	V value = null;
                DynaBean dyBean = getDynaDataForRow(bean, false);
                if(dyBean != null) {
                	value = super.getDelegateValue(dyBean);	
                }
                return value;
            }
            throw new ElementNotFoundException();
        }

        public <V> void setValue(T bean, V value) {
            if (Dataset.this.hasRow(bean)) {
                DynaBean dyBean = getDynaDataForRow(bean, true);
                Dataset.this.removeFromIndexes(bean);
                try {
                    super.setDelegateValue(dyBean, value);
                } finally {
                    Dataset.this.addToIndexes(bean);
                }
            } else {
                throw new ElementNotFoundException();
            }
        };

    }

}
