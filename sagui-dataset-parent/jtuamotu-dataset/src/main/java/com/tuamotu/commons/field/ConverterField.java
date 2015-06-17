package com.tuamotu.commons.field;

import org.apache.commons.beanutils.Converter;

@SuppressWarnings("unchecked")
public class ConverterField<T> extends DelegateField<T> {

    private final Class<?> inClass;
	private final Class<?> outClass;
	private final  Converter converter;
	private final IField<T> delegate;

	public ConverterField(Class<?> inClass, Class<?> outClass, Converter converter, IField<T> delegate) {
	    super(delegate);
		this.inClass = inClass;
		this.outClass = outClass;
		this.converter = converter;
		this.delegate = delegate;
	}

	@Override
    public <V> V getValue(T bean) {
		V value = (V) converter.convert(outClass, delegate.getValue(bean));
		return value;
	}

    @Override
	public <V> void setValue(T bean, V value) {
		delegate.setValue(bean, converter.convert(inClass, value));
	}

    @Override
	public Class<?> getFieldClass() {
		return outClass;
	}


}
