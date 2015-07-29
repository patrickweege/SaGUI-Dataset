package com.sagui.dataset.commons.collections;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortedListUtil {

    public static <T> boolean contains(T element, List<T> list, Comparator<T> comparator) {
        int pos = Collections.binarySearch(list, element, comparator);
        return pos >= 0;
    }

    public static <T> int add(T element, List<T> list, Comparator<T> comparator, boolean checkUnique) {
        int pos = Collections.binarySearch(list, element, comparator);
        if (pos < 0) {
            pos = Math.abs(pos + 1);

        } else if (checkUnique) {
            throw new RuntimeException("Unique Key Viollation");
        }
        list.add(pos, element);
        return pos;
    }

    public static <T> int remove(T example, List<T> list, Comparator<T> comparator) {
        int pos = search(example, list, comparator);
        if (pos >= 0) {
            list.remove(pos);
        }
        return pos;
    }

    /**
     * @see Collections#binarySearch(List, Object, Comparator)
     */
    public static <T> int search(T example, List<T> list, Comparator<T> comparator) {
        int pos = Collections.binarySearch(list, example, comparator);
        return pos;
    }
    
    public static <T> void sort(List<T> list, Comparator<T> comparator) {
        Collections.sort(list, comparator);
    }

}
