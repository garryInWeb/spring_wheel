package org.litespring.transaction.handler;

/**
 * Created by zhengtengfei on 2019/2/20.
 */
public class JDBCHandler implements TransactionHandler{
    // 通过 factory 传入 jdbc 的连接处理bean

    @Override
    public void transactionBefore() {
        System.out.println("before");
    }

    @Override
    public void transactionAfter() {
        System.out.println("after");
    }

    @Override
    public void transactionThrowing() {
        System.out.println("throwing");
    }
}
