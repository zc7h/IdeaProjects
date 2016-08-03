package com.zc7h.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

/**
 * Created by BinLaden on 2016.06.29.
 *
 */
public class LoginCtrlServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String sql = null;
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //1.加载驱动
            Class.forName("oracle.jdbc.driver.OracleDriver");

            //2.得到连接
            con = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:ZC7H", "scott", "tiger");

            //3.创建PrepareStatement
            sql = "select * from users where username=? and password=?";
            ps = con.prepareStatement(sql);

            //4.赋值
            ps.setString(1, username);
            ps.setString(2, password);

            //5.执行操作
            rs = ps.executeQuery();

            //6.根据结果做处理
            if(rs.next()) {
                request.getRequestDispatcher("/MainFrame").forward(request, response);
            } else {
                request.setAttribute("err", "用户名或者密码有误！");
                request.getRequestDispatcher("/LoginServlet").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("err", "您的用户名可能有误！");
            request.getRequestDispatcher("/LoginServlet").forward(request, response);

        } finally {
//            关闭资源
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                rs = null;
            }
            if(ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                ps = null;
            }
            if(con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                con = null;
            }
        }
    }
}
