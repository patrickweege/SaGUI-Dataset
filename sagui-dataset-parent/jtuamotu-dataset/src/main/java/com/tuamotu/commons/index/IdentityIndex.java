package com.tuamotu.commons.index;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.slf4j.Logger;

import com.tuamotu.commons.dataset.IBookmark;
import com.tuamotu.commons.log.FatuLoggerFactory;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class IdentityIndex<T> implements IIdentityIndex<T> {

    private static final int SEQUENCE_INDEX = 0;
    private static final int ELEMENT_INDEX = 1;
    private static final Logger log = FatuLoggerFactory.create(IdentityIndex.class);

    public final Comparator<T> IDENTITY_COMPARATOR;

    private Map<Integer, Object> index;
    private int recordCount;

    private long sequence;

    public IdentityIndex() {
        this.index = new HashMap<Integer, Object>();
        this.IDENTITY_COMPARATOR = new IdentityIndexComparator();
        this.recordCount = 0;
    }

    /* (non-Javadoc)
     * @see com.tuamotu.commons.index.IIdentityIndex#add(T)
     */
    @Override
    public void add(T element) {
        if (element != null) {
            Integer hash = System.identityHashCode(element);
            Object indexElement = this.index.get(hash);
            if (indexElement == null) {
                Object[] record = new Object[] { sequence++, element };
                index.put(hash, record);
                recordCount++;
            } else if (indexElement instanceof Object[]) {
                Object[] record = (Object[]) indexElement;
                if (record[ELEMENT_INDEX] == element) {
                    throw new IllegalArgumentException("The element you try ADD already exists at Index");
                }
                Collection<Object[]> newValue = new HashSet<Object[]>();
                newValue.add(record);
                newValue.add(new Object[] { sequence++, element });
                index.put(hash, newValue);
                recordCount++;
            } else if (indexElement instanceof Collection) {
                Collection<Object[]> colisions = (Collection<Object[]>) indexElement;
                if (getByElement(element, colisions) != null) {
                    throw new IllegalArgumentException("The element you try ADD already exists at Index");
                }
                colisions.add(new Object[] { sequence++, element });
                recordCount++;
                log.debug("Collisions for hash " + hash + " count " + colisions.size());
            }
        } else {
            throw new IllegalArgumentException("Canot ADD null elements to the Index");
        }
    }

    /* (non-Javadoc)
     * @see com.tuamotu.commons.index.IIdentityIndex#remove(T)
     */
    @Override
    public T remove(T toRemove) {
        boolean isRemoved = false;
        Integer hash = System.identityHashCode(toRemove);
        Object indexElement = this.index.get(hash);
        if (indexElement == null) {
            throw new ElementNotFoundException();
        } else if (indexElement instanceof Object[]) {
            Object[] record = (Object[]) indexElement;
            if (record[ELEMENT_INDEX] == toRemove) {
                this.index.remove(hash);
                this.recordCount--;
                isRemoved = true;
            }
        } else if (indexElement instanceof Collection) {
            Collection<Object[]> colisions = (Collection<Object[]>) indexElement;
            Object[] record = getByElement(toRemove, colisions);
            if (record != null) {
                if (colisions.remove(record)) {
                    this.recordCount--;
                    if (colisions.size() == 1) {
                        this.index.put(hash, colisions.iterator().next());
                    } else if (colisions.isEmpty()) {
                        this.index.remove(hash);
                    }
                    return (T) record[ELEMENT_INDEX];
                }
            }
        }
        if (!isRemoved) {
            throw new ElementNotFoundException();
        }
        return toRemove;
    }

    /* (non-Javadoc)
     * @see com.tuamotu.commons.index.IIdentityIndex#remove(com.tuamotu.commons.dataset.IBookmark)
     */
    @Override
    public T remove(IBookmark<T> toRemove) {
        checkValidBookmark(toRemove, false);
        T removed = null;
        IdentityIndexBookmark bkm = (IdentityIndexBookmark) toRemove;
        Integer hash = bkm.elemHash;
        long sequenceToRemove = bkm.insertIndex;
        Object indexElement = this.index.get(hash);
        if (indexElement == null) {
            throw new ElementNotFoundException();
        } else if (indexElement instanceof Object[]) {
            Object[] record = (Object[]) indexElement;
            long currSequence = (long) record[SEQUENCE_INDEX];
            if (currSequence == sequenceToRemove) {
                if (this.index.remove(hash) != null) {
                    this.recordCount--;
                    removed = (T) record[ELEMENT_INDEX];
                }
            }
        } else if (indexElement instanceof Collection) {
            Collection<Object[]> colisions = (Collection<Object[]>) indexElement;
            Object[] recordFound = getBySequence(sequenceToRemove, colisions);
            if (recordFound != null) {
                if (colisions.remove(recordFound)) {
                    this.recordCount--;
                    if (colisions.size() == 1) {
                        this.index.put(hash, colisions.iterator().next());
                    } else if (colisions.isEmpty()) {
                        this.index.remove(hash);
                    }
                    return (T) recordFound[ELEMENT_INDEX];
                }
            }
        }
        if (removed == null) {
            throw new ElementNotFoundException();
        }
        return removed;
    }

    /* (non-Javadoc)
     * @see com.tuamotu.commons.index.IIdentityIndex#getBookmark(T)
     */
    @Override
    public IBookmark<T> getBookmark(T forElement) {
        int hash = System.identityHashCode(forElement);
        if (hash != 0) {
            Object indexElement = this.index.get(hash);
            if (indexElement == null) {
                throw new ElementNotFoundException();
            } else if (indexElement instanceof Object[]) {
                Object[] record = (Object[]) indexElement;
                return new IdentityIndexBookmark(indexElement.getClass(), hash, (long) record[SEQUENCE_INDEX]);
            } else if (indexElement instanceof Collection) {
                Collection<Object[]> colisions = (Collection<Object[]>) indexElement;
                Object[] found = getByElement(forElement, colisions);
                if (found != null) {
                    return new IdentityIndexBookmark(forElement.getClass(), hash, (long) found[SEQUENCE_INDEX]);
                }
            }
        }
        throw new ElementNotFoundException();
    }

    /* (non-Javadoc)
     * @see com.tuamotu.commons.index.IIdentityIndex#getBookmark(java.lang.String)
     */
    @Override
    public IBookmark<T> getBookmark(String bookmarkUUID) {
        return new IdentityIndexBookmark(bookmarkUUID);
    }

    /* (non-Javadoc)
     * @see com.tuamotu.commons.index.IIdentityIndex#getElement(com.tuamotu.commons.dataset.IBookmark)
     */
    @Override
    public T getElement(IBookmark<T> bookmark) {
        checkValidBookmark(bookmark, false);
        T found = null;
        IdentityIndexBookmark bkm = (IdentityIndexBookmark) bookmark;
        Integer hash = bkm.elemHash;
        Object indexElement = this.index.get(hash);
        if (indexElement == null) {
            throw new ElementNotFoundException();
        } else if (indexElement instanceof Object[]) {
            found = (T) ((Object[]) indexElement)[ELEMENT_INDEX];
        } else if (indexElement instanceof Collection) {
            found = (T) getBySequence(bkm.insertIndex, (Collection<Object[]>) indexElement);
        }

        if (found != null) {
            // TODO compare element class from BookMark and from the found Instance
            return found;
        }
        throw new ElementNotFoundException();
    }

    /* (non-Javadoc)
     * @see com.tuamotu.commons.index.IIdentityIndex#contains(T)
     */
    @Override
    public boolean contains(T toFind) {
        Integer hash = System.identityHashCode(toFind);
        Object indexElement = this.index.get(hash);
        if (indexElement == null) {
            return false;
        } else if (indexElement instanceof Object[]) {
            return ((Object[]) indexElement)[ELEMENT_INDEX] == toFind;
        } else if (indexElement instanceof Collection) {
            return getByElement(toFind, (Collection<Object[]>) indexElement) != null;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see com.tuamotu.commons.index.IIdentityIndex#clear()
     */
    @Override
    public void clear() {
        this.index.clear();
        this.recordCount = 0;
    }

    /* (non-Javadoc)
     * @see com.tuamotu.commons.index.IIdentityIndex#size()
     */
    @Override
    public int size() {
        return recordCount;
    }

    private boolean checkValidBookmark(IBookmark<T> bookmark, boolean silent) {
        boolean isValid;
        isValid = (bookmark instanceof IdentityIndex.IdentityIndexBookmark);
        if (isValid) {
            IdentityIndexBookmark identityIndexBookmark = (IdentityIndexBookmark) bookmark;
            isValid = isValid && this.getClass().equals(identityIndexBookmark.indexClass);
            isValid = isValid && (System.identityHashCode(this) == identityIndexBookmark.indexHash);
        }

        if (!isValid && !silent) {
            throw new IllegalArgumentException("Not valid Bookmark for This Index");
        }

        return isValid;
    }

    private Object[] getBySequence(long sequenceToFind, Collection<Object[]> atCollection) {
        for (Object[] oneRecord : atCollection) {
            if ((long) oneRecord[SEQUENCE_INDEX] == sequenceToFind) {
                return oneRecord;
            }
        }
        return null;
    }

    private Object[] getByElement(T elementToFind, Collection<Object[]> atCollection) {
        for (Object[] oneRecord : atCollection) {
            if ((T) oneRecord[ELEMENT_INDEX] == elementToFind) {
                return oneRecord;
            }
        }
        return null;
    }

    private Object[] findRecord(T toFind) {
        int hash = System.identityHashCode(toFind);
        Object indexElement = this.index.get(hash);
        if (indexElement == null) {
            return null;
        } else if (indexElement instanceof Object[]) {
            return (Object[]) indexElement;
        } else if (indexElement instanceof Collection) {
            return getByElement(toFind, (Collection<Object[]>) indexElement);
        }
        return null;
    }

    private class IdentityIndexComparator implements Comparator<T> {

        @Override
        public int compare(T one, T other) {

            Object[] oneRecord = IdentityIndex.this.findRecord(one);
            if (oneRecord == null) {
                return -1;
            }

            Object[] otherRecord = IdentityIndex.this.findRecord(other);
            if (otherRecord == null) {
                return 1;
            }

            long oneSequence = (long) oneRecord[SEQUENCE_INDEX];

            long otherSequence = (long) otherRecord[SEQUENCE_INDEX];

            return Long.compare(oneSequence, otherSequence);
        }

    }

    private class IdentityIndexBookmark implements IBookmark<T> {

        private static final String ARROBA = "@";

        private final int cachedHashCode;

        private final String UUID;

        private final Class indexClass;
        private final int indexHash;

        private final Class elemClass;
        private final int elemHash;
        private final long insertIndex;

        private IdentityIndexBookmark(String bookmarkUUID) {
            if (bookmarkUUID != null && bookmarkUUID.length() > 0) {
                String[] parts = bookmarkUUID.split("@");

                String className = null;
                try {
                    className = parts[0];
                    this.indexClass = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException("Index class not found: [" + className + "]");
                }
                this.indexHash = Integer.parseInt(parts[1]);

                try {
                    className = parts[2];
                    this.elemClass = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException("Element class not found: [" + className + "]");
                }
                this.elemHash = Integer.parseInt(parts[3]);
                this.insertIndex = Long.parseLong(parts[4]);

                IdentityIndex.this.checkValidBookmark(this, false);
                this.UUID = bookmarkUUID;
                this.cachedHashCode = this.UUID.hashCode();
            } else {
                throw new IllegalArgumentException("Not valid Bookmark for This Index");
            }
        }

        private IdentityIndexBookmark(Class elementClass, int elementHash, long colisionIndex) {
            this.indexClass = IdentityIndex.this.getClass();
            this.indexHash = System.identityHashCode(IdentityIndex.this);

            this.elemClass = elementClass;
            this.elemHash = elementHash;
            this.insertIndex = colisionIndex;
            this.UUID = generateUUID();
            this.cachedHashCode = this.UUID.hashCode();
        }

        @Override
        public int compareTo(IBookmark<T> other) {
            return this.UUID.compareTo(other.getUUID());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null) return false;
            if (!this.getClass().equals(o.getClass())) return false;
            IdentityIndexBookmark other = (IdentityIndexBookmark) o;
            if (this.UUID.equals(other.UUID)) return true;
            return false;
        }

        @Override
        public int hashCode() {
            return this.cachedHashCode;
        }

        @Override
        public String getUUID() {
            return this.UUID;
        }

        private String generateUUID() {
            String UUID = new StringBuilder(indexClass.getName()).append(ARROBA).append(indexHash).append(ARROBA).append(elemClass.getName()).append(ARROBA).append(elemHash).append(ARROBA).append(insertIndex).toString();
            return UUID;
        }

    }

}
