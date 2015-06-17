package com.tuamotu.commons.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class Resources {

	private final byte[] bytes;

	public Resources(String fileName) throws FileNotFoundException {
		this.bytes = getResource(fileName);
	}

	
	/**
	 * @param fileName
	 *            Text File Name
	 * @param fromPackage
	 *            {@link Package} or null when load from Root class loader.
	 * @throws FileNotFoundException
	 */
	public Resources(String fileName, Package fromPackage) throws FileNotFoundException {
		String name = resourceName(fileName, fromPackage);
		this.bytes = getResource(name);
	}

	/**
	 * @param fileName
	 *            Text File Name
	 * @throws FileNotFoundException
	 */
	public Resources(String filePath, String fileName) throws FileNotFoundException {
		String name = resourceName(filePath, fileName);
		this.bytes = getResource(name);
	}

	/**
	 * Returns the Resource as Text
	 * 
	 * @return Resource loaded as String
	 * @throws FileNotFoundException
	 */
	public String asText() {
		return new String(bytes);
	}

	public byte[] asBinary() throws FileNotFoundException {
		return bytes;
	}
	
	private String resourceName(String fileName, Package fromPackage) {
		String pkgName = fromPackage == null ? "" : fromPackage.getName();
		String filePath = pkgName.replaceAll("\\.", "/");
		return resourceName(filePath, fileName);
	}

	private String resourceName(String filePath, String fileName) {
		StringBuilder resName = new StringBuilder(filePath);
		resName.append(fileName);
		return resName.toString();
	}

	private byte[] getResource(String fileName) throws FileNotFoundException {
		InputStream in = null;
		try {
			// try to get file from ClassLoader
			in = Resources.class.getClassLoader().getResourceAsStream(fileName);
			if (in == null) {
				// try from System
				File file = new File(fileName);
				if (!file.exists()) {
					throw new FileNotFoundException(fileName);
				}
				in = new FileInputStream(file);
			}
			try {
				byte[] bytes = IOUtils.toByteArray(in);
				return bytes;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

}
