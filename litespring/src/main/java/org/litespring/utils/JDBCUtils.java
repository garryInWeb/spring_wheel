package org.litespring.utils;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhengtengfei on 2019/2/21.
 */
public class JDBCUtils {

    static Connection connection;

    public static Connection getConnection(){
        return connection;
    }
    public static void set(Connection connection){
        JDBCUtils.connection = connection;
    }
}
