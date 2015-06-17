package com.tuamotu.commons.field;

import org.apache.commons.collections4.Transformer;

/**
 * This is a Implementation for IField that transforms the value of the
 * delegatef IField instance and return the transformed value with Transformer
 * instance<br>
 * Same as:
 * 
 * <pre>
 * Integer color = colorField.get(bean);
 * String strColor = transformer.transform(color);
 * return strColor;
 * 
 * public class ColorAsLabelTranformer implements Transformer {
 * 
 * 	transform(Object in) {
 * 		if (Color.WHITE)
 * 			return &quot;White&quot;;
 * 	}
 * }
 * </pre>
 * 
 * 
 * @author patrick.weege
 * 
 * @param <T>
 */
@SuppressWarnings("unchecked")
public class TransformedFieldImpl<T> extends DelegateField<T> {

	private final Transformer transformer;
	private final IField<T> delegate;

	public TransformedFieldImpl(IField<T> delegate, Transformer transformer) {
	    super(delegate);
        this.delegate = delegate;
        this.transformer = transformer;
	}

    public <V> V getValue(T bean) {
		Object tempValue = delegate.getValue(bean);
		return (V) transformer.transform(tempValue);
	}

	public <V> void setValue(T bean, V value) {
		throw new UnsupportedOperationException("Calculated Field are ReadOnly fields: " + getName());
	}
}
