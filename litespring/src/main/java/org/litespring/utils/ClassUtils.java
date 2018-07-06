package org.litespring.utils;

public class ClassUtils {

    private ClassUtils(){
        throw new IllegalStateException("Utility Class");
    }

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;

        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable var3) {
            ;
        }

        if (cl == null) {
            cl = ClassUtils.class.getClassLoader();
            if (cl == null) {
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable var2) {
                    ;
                }
            }
        }

        return cl;
    }

    public static <T> boolean isAssignableValue(Class<T> requiredType, Object value) {
        Assert.notNull(requiredType,"Type must not be null.");
        return false;
    }
}
