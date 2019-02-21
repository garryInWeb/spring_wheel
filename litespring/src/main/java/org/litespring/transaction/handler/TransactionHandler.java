package org.litespring.transaction.handler;

import java.sql.SQLException;

/**
 * Created by zhengtengfei on 2019/2/20.
 */
public interface TransactionHandler {

    void transactionBefore() throws SQLException;

    void transactionAfter() throws SQLException;

    void transactionThrowing() throws SQLException;
}
