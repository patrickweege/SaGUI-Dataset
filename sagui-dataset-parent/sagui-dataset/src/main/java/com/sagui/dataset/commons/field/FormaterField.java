package com.sagui.dataset.commons.field;

import java.text.Format;

@SuppressWarnings("unchecked")
public class FormaterField<T> extends DelegateField<T> {

	private final Format formater;
	private final IField<T> delegate;

	public FormaterField(IField<T> delegate, Format formater) {
	    super(delegate);
		this.delegate = delegate;
		this.formater = formater;
	}

    public <V> V getValue(T bean) {
		Object value = delegate.getValue(bean);
		String fmtValue = null;
		if(value != null) {
			fmtValue = formater.format(value);
		}
		return (V) fmtValue;
	}

	public <V> void setValue(T bean, V value) {
		try {
			Object toSet = value;
			if (value != null) {
				String src = value.toString();
				toSet = formater.parseObject(src);
			}
			delegate.setValue(bean, toSet);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public Class<?> getFieldClass() {
	    return String.class;
	}

}
