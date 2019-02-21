package org.litespring.transaction.handler;

import org.litespring.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by zhengtengfei on 2019/2/20.
 */
public class JDBCHandler implements TransactionHandler{
    Connection connection;

    @Override
    public void transactionBefore() throws SQLException {
        System.out.println("before");
        JDBCUtils.getConnection().setAutoCommit(false);
    }

    @Override
    public void transactionAfter() throws SQLException {
        System.out.println("after");
        JDBCUtils.getConnection().commit();
    }

    @Override
    public void transactionThrowing() throws SQLException {
        System.out.println("throwing");
        JDBCUtils.getConnection().rollback();
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
