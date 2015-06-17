package com.tuamotu.commons.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class ResourceBundleManager {

	public static ResourceBundleManager INSTANCE = new ResourceBundleManager();

	private Map<Locale, ResourceBundle> bundles;

	private ResourceBundleManager() {
		this.bundles = new HashMap<Locale, ResourceBundle>();
	}

	public static ResourceBundleManager getInstance() {
		return ResourceBundleManager.INSTANCE;
	}

	public static ResourceBundle getResourceBundle(Locale locale) {
		ResourceBundleManager instance = ResourceBundleManager.getInstance();
		ResourceBundle bundle = instance.bundles.get(locale);
		if (bundle == null) {
			bundle = ResourceBundle.getBundle("MessageResources", locale);
			if (bundle != null) {
				instance.bundles.put(locale, bundle);
			}
		}
		return bundle;

	}

}
