package org.litespring.transaction.handler;

/**
 * Created by zhengtengfei on 2019/2/20.
 */
public interface TransactionHandler {

    void transactionBefore();

    void transactionAfter();

    void transactionThrowing();
}
