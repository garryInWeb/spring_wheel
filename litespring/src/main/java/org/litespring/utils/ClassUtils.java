package org.litespring.utils;

import java.util.HashMap;
import java.util.Map;

public class ClassUtils {


    /**
     * Map with primitive wrapper type as key and corresponding primitive
     * type as value, for example: Integer.class -> int.class.
     */
    private static final Map<Class<?>, Class<?>> wrapperToPrimitiveTypeMap = new HashMap<Class<?>, Class<?>>(8);

    /**
     * Map with primitive type as key and corresponding wrapper
     * type as value, for example: int.class -> Integer.class.
     */
    private static final Map<Class<?>, Class<?>> primitiveTypeToWrapperMap = new HashMap<Class<?>, Class<?>>(8);
    public static final String PACKAGE_SEPARATOR = ".";
    public static final String PATH_SEPARATOR = "/";

    static {
        wrapperToPrimitiveTypeMap.put(Boolean.class, boolean.class);
        wrapperToPrimitiveTypeMap.put(Byte.class, byte.class);
        wrapperToPrimitiveTypeMap.put(Character.class, char.class);
        wrapperToPrimitiveTypeMap.put(Double.class, double.class);
        wrapperToPrimitiveTypeMap.put(Float.class, float.class);
        wrapperToPrimitiveTypeMap.put(Integer.class, int.class);
        wrapperToPrimitiveTypeMap.put(Long.class, long.class);
        wrapperToPrimitiveTypeMap.put(Short.class, short.class);

        for (Map.Entry<Class<?>, Class<?>> entry : wrapperToPrimitiveTypeMap.entrySet()) {
            primitiveTypeToWrapperMap.put(entry.getValue(),entry.getKey());
        }
    }


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
        return (value != null ? isAssignable(requiredType,value.getClass()) : !requiredType.isPrimitive());
    }

    public static boolean isAssignable(Class<?> lhsType,Class<?> rhsType){
        Assert.notNull(lhsType,"Left-hand site type must not be null!");
        Assert.notNull(rhsType,"Right-hand site type must not be null!");
        if (lhsType.isAssignableFrom(rhsType)){
            return true;
        }
        if (lhsType.isPrimitive()){
            Class<?> resolvedPrimitive = primitiveTypeToWrapperMap.get(rhsType);
            if (resolvedPrimitive != null && lhsType.equals(resolvedPrimitive)){
                return true;
            }
        }else{
            Class<?> resolvedPrimitive = wrapperToPrimitiveTypeMap.get(rhsType);
            if (resolvedPrimitive != null && lhsType.equals(resolvedPrimitive)){
                return true;
            }
        }
        return false;
    }

    public static String convertClassNameToResourcePath(String className) {
        Assert.notNull(className,"Class Name must not be null!");
        return className.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR);
    }
    public static String convertResourcePathToClassName(String className) {
        Assert.notNull(className,"Class Name must not be null!");
        return className.replace(PATH_SEPARATOR, PACKAGE_SEPARATOR);
    }
}
