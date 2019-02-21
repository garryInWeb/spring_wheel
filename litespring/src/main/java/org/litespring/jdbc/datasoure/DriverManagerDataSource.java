package org.litespring.jdbc.datasoure;

import org.litespring.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by zhengtengfei on 2019/2/21.
 */
public class DriverManagerDataSource {
    Connection conn = null;

    public DriverManagerDataSource(String driverClassName, String url, String username, String password) throws ClassNotFoundException, SQLException {
        Class.forName(driverClassName); //classLoader,加载对应驱动
        conn = DriverManager.getConnection(url, username, password);
        JDBCUtils.set(conn);
    }

}
