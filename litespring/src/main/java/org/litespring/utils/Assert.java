package org.litespring.utils;

/**
 * Created by zhengtengfei on 2018/6/28.
 */
public class Assert {
    public static void notNull(Object o,String message){
        if (o == null)
            throw new IllegalArgumentException(message);
    }

    private Assert(){
        throw new IllegalStateException("Utility Class");
    }
}
