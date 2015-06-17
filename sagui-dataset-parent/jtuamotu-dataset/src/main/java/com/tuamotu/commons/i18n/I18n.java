package com.tuamotu.commons.i18n;

//______________________________________________________________________________
//
//        Project: SI-OUX
//______________________________________________________________________________
//
//     created by: Johannes Weigend
//  creation date: 28.08.2007
//     changed by: $Author: pweege $
//    change date: $LastChangedDate: 2011-06-27 11:23:46 -0300 (seg, 27 jun 2011) $
//    description: Simp
//______________________________________________________________________________
//
//      Copyright: T-Systems Enterprise Services GmbH 2007
//______________________________________________________________________________

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang.builder.CompareToBuilder;

/**
 * Simple class for international strings.
 */
public class I18n implements Comparable<I18n>, Serializable {

	private final Map<Locale, String> translations;
	private final String i18nKey;

	public I18n(String i18nKey) {
		this.i18nKey = i18nKey;
		translations = new HashMap<Locale, String>();
	}

	public I18n(I18n other) {
		this(other.i18nKey);
		this.translations.putAll(other.translations);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return i18nKey.hashCode();
	}

	public String getTranslation(Locale locale) {
		return this.translations.get(locale);
	}

	public void setTranslation(Locale locale, String translation) {
		this.translations.put(locale, translation);
	}

	public I18n setDefault(String translation) {
		Locale currLocale = CurrentLanguageResolver.getInstance().getCurrentLocale();
		setTranslation(currLocale, translation);
		return this;
	}
	
	public String getDefault() {
		Locale currLocale = CurrentLanguageResolver.getInstance().getCurrentLocale();
		return getLocalizedString(currLocale);
	}

	public String getDefault(Object... args) {
		String text = this.getDefault();
		return MessageFormat.format(text, args);
	}

    public String getLocalizedString(Locale locale) {
    	if(locale == null) throw new IllegalArgumentException("the locale must not be null");
    	String translation = translations.get(locale);
    	if(translation == null) {
            ResourceBundle resourceBoundle = ResourceBundleManager.getResourceBundle(locale);
            try {
                translation = resourceBoundle.getString(i18nKey);
            } catch (MissingResourceException e) {
            	translation = "_" + i18nKey + "_";
            }
    	}
    	return translation;
    }

	@Override
	public int compareTo(I18n other) {
		return new CompareToBuilder().append(this.i18nKey,other.i18nKey).toComparison();
	}
}
