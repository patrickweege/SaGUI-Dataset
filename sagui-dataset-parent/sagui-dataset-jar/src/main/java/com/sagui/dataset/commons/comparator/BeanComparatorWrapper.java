package com.sagui.dataset.commons.comparator;

import java.util.Comparator;

@SuppressWarnings("rawtypes")
class BeanComparatorWrapper<T> implements IBeanComparator<T> {

	private final Comparator<T> comparator;
    private final IBeanComparatorMetadata[] metadata;

	public BeanComparatorWrapper(Comparator<T> comparator, IBeanComparatorMetadata... metadata) {
		this.comparator = comparator;
		this.metadata = metadata;
	}

	public IBeanComparatorMetadata[] getMetadata() {
		return metadata;
	}

	public int compare(T one, T other) {
		return comparator.compare(one, other);
	}
}
