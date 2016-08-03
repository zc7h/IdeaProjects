package com.zc7h.model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据访问对象 DAO
 * Created by BinLaden on 2016.07.10.
 */
public class DBUtil {
    private static Connection conn = null;
    private static PreparedStatement pstmt = null;
    private static CallableStatement cstmt = null;
    private static ResultSet rs = null;

    /*
    *   使用静态代码开加载数据库驱动，只加载一次
    *
    * */
    static {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*
    *   获取数据库连接
    *
    * */
    public static Connection getConnection() {
        try {
            conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:ZC7H", "scott", "tiger");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    /*
    *   封装查询功能,对应select语句
    *
    * */
    public static List<Map<String, String>> excuteQuery(String sql, Object[] params) {
        try {
            //  预编译，获取结果集
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }
            rs = pstmt.executeQuery();
            //  把结果集封装入ArrayList
            ArrayList<Map<String, String>> al = new ArrayList<Map<String, String>>();
            while (rs.next()) {
                ResultSetMetaData resultSetMetaData = rs.getMetaData();
                //  获取结果集的列数用来计数
                int columnCount = resultSetMetaData.getColumnCount();
                Map<String, String> map = new HashMap<String, String>();
                for (int i = 0; i < columnCount; i++) {
                    //  把结果集数据放入map
                    // 此处使用setString()把数据从原始对象直接转成String对象，如果用setObject将保留数据原始对象
                    map.put(resultSetMetaData.getColumnName(i + 1).toLowerCase(), rs.getString(i + 1));
                }
                al.add(map);
            }
            return al;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            close(rs, pstmt, conn);
        }
    }

    /*
    *   封装一条DML语句：insert,update,delete，不考虑事务。
    *
    * */
    public static void excuteUpdateSingle(String sql, Object[] params) {
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            close(rs, pstmt, conn);
        }
    }

    /*
    *   封装多个DML语句：insert,update,delete，需要考虑事务。
    *
    * */
    public static void excuteUpdateMulti(String[] sqls, Object[][] params) {
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            for (int i = 0; i < sqls.length; i++) {
                pstmt = conn.prepareStatement(sqls[i]);
                if (params[i] != null) {
                    for (int j = 0; j < params[i].length; j++) {
                        pstmt.setObject(j + 1, params[i][j]);
                    }
                }
                pstmt.executeUpdate();
            }
            conn.commit();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            close(rs, pstmt, conn);
        }
    }

    /*
    *   封装调用存储过程
    *
    * */
    public static void callProcedure(String sql, Object[] params) {
        try {
            conn = getConnection();
            cstmt = conn.prepareCall(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    cstmt.setObject(i + 1, params[i]);
                }
            }
            cstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /*
    *   封装关闭数据库连接
    *
    * */
    public static void close(ResultSet rs, Statement stmt, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            rs = null;
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            stmt = null;
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            conn = null;
        }
    }
}