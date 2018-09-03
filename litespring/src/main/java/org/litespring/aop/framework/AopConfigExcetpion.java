package org.litespring.aop.framework;

/**
 * Created by zhengtengfei on 2018/9/3.
 */
public class AopConfigExcetpion extends RuntimeException {
    public AopConfigExcetpion(String msg) {
        super(msg);
    }

    public AopConfigExcetpion(String message, Throwable cause) {
        super(message, cause);
    }
}
