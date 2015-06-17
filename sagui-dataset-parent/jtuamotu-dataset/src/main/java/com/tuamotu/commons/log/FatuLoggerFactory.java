package com.tuamotu.commons.log;

import org.slf4j.Logger;

/**
 * A factory for creating Log instances conveniently. By using this class it is no longer necessary to provide a class when creating a Log instance.
 * 
 * @author patrick.weege
 */
public class FatuLoggerFactory {

    /**
     * Private constructor since there should be no instances of this class.
     */
    private FatuLoggerFactory() {
    }

    /**
     * Creates a {@link org.slf4j.Logger} instance. <br>
     * The log instance is initialized with the class calling this method.
     * 
     * @return Instance of {@link org.slf4j.Logger}
     */
    public static Logger create() {
        Throwable t = new Throwable();
        StackTraceElement directCaller = t.getStackTrace()[1];
        return create(directCaller.getClassName());
    }

    public static Logger create(Class<?> clazz) {
        return create(clazz.getName());
    }

    public static Logger create(String name) {
        Logger logger = org.slf4j.LoggerFactory.getLogger(name);
        return logger;
    }

}
