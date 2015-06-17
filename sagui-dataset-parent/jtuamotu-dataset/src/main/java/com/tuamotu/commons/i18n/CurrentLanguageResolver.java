//______________________________________________________________________________
//
//          Project: SI-OUX 
//______________________________________________________________________________
//
//       created by: User
//    creation date: 18.10.2007
//       changed by: $Author: $
//      change date: $LastChangedDate: $
//      description: 
//______________________________________________________________________________
//
//        Copyright: QAware GmbH 2007
//______________________________________________________________________________
package com.tuamotu.commons.i18n;

import java.util.Locale;

public class CurrentLanguageResolver {

    private static ThreadLocal<Locale> currentLanguage = new ThreadLocal<Locale>();
    
    private static CurrentLanguageResolver INSTANCE = new CurrentLanguageResolver();

    private CurrentLanguageResolver() {
		
	}
    
    public static CurrentLanguageResolver getInstance() {
    	return INSTANCE;
    }
    
    /**
     * @param locale the new current locale
     */
    public void setCurrentLocale(Locale locale) {
        if (currentLanguage == null) {
            currentLanguage = new ThreadLocal<Locale>();
        }
        currentLanguage.set(locale);
    }

    /**
     * @return the current langage
     */
    public Locale getCurrentLocale() {
        if (currentLanguage.get() == null) {
            resetCurrentLocale();
        }
        return currentLanguage.get();
    }

    /**
     * resets the current locale
     */
    public void resetCurrentLocale() {
        currentLanguage.set(getDefaultLocale());
    }

    /**
     * @return the default locale
     */
    private Locale getDefaultLocale() {
        return Locale.getDefault();
    }
}
