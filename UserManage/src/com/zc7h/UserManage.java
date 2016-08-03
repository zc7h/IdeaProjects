package com.zc7h;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

/**
 * Created by BinLaden on 2016.06.30.
 * BinLaden
 */
public class UserManage extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = null;
        /*
        **  分页
        **  ZC7h
        **
        **/

        //  总页数
        int pageCount = 1;
        //  总记录数
        int count = 1;

        //  当前页
        String getPageNowParm = request.getParameter("pageCurrent");
        int pageCurrent = (getPageNowParm != null) ? Integer.parseInt(getPageNowParm) : 1;

        //  页记录数
        String getPageSize = request.getParameter("pageSize");
        int pageSize = (getPageSize != null) ? Integer.parseInt(getPageSize) : 3;


        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:zc7h", "scott", "tiger");
            //构造查询总记录数SQL语句
            sql = "select count(*) from users";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            rs.next();
            //获取总记录数
            count = rs.getInt(1);
            //计算总页数
            pageCount = (count-1)/pageSize + 1 ;
            //构造分页SQL语句
            sql = "select * from (select t.*, rownum rn from (select * from users order by id asc) t where rownum <= ?) where rn >= ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, Integer.toString(pageSize*pageCurrent));
            ps.setString(2, Integer.toString((pageCurrent-1)*pageSize+1));
            rs = ps.executeQuery();

            //  页码数
            int pageCodeSize = 10;

            //  页码分组数
            int pageCodeCount = (pageCount-1)/pageCodeSize + 1;

            //  当前页码分组
            String getPageCodeCurrent = request.getParameter("pageCodeCurrent");
            int pageCodeCurrent = (getPageCodeCurrent !=null) ? Integer.parseInt(getPageCodeCurrent) : 1;

            out.print("<script type='text/javascript' language='javascript'>");
            out.print("function gotoPage() { " +
                    "var pageCurrent = document.getElementById('page').value;" +
                    "if((/[0-9]+/gi).test(pageCurrent)) {" +
                   "if (pageCurrent <= " + pageCount + ") {" +
                    "window.open('/UserManage?pageCurrent=' + pageCurrent + '&pageSize=" + pageSize + "', '_self');" +
                    "} else {window.open('/UserManage?pageCurrent=" + pageCount + "&pageSize=" + pageSize + "', '_self');}" +
                    "} else {" +
                    "window.open('/UserManage?pageCurrent=" + pageCurrent + "&pageSize=" + pageSize + "', '_self');}}");

            out.print("function setPageSize() {" +
                    "var pageSize = document.getElementById('pageSize').value;" +
                    "if((/[0-9]+/gi).test(pageSize)) {"  +
                    "window.open('/UserManage?pageSize=' + pageSize, '_self');" +
                    "} else {" +
                    "window.open('/UserManage?pageCurrent=" + pageCurrent + "&pageSize=" + pageSize + "','_self');}}");
            out.print("</script>");

            out.print("<table border='1' bordercolor='green' width='800px' align='center'>");
            out.print("<tr>" +
                    "<th>ID</th>" +
                    "<th>名字</th>" +
                    "<th>邮箱</th>" +
                    "<th>权限</th>" +
                    "<th>编辑</th>" +
                    "<th>删除</th>" +
                    "</tr>");
            while (rs.next()) {
                out.print("<tr align='center'>" +
                        "<td>" + rs.getInt(1) + "</td>" +
                        "<td>" + rs.getString(2) + "</td>" +
                        "<td>" + rs.getString(4) + "</td>" +
                        "<td>" + rs.getInt(5) + "</td>" +
                        "<td><a href='#'>编辑</a></td>" +
                        "<td><a href='#'>删除</a></td>" +
                        "</tr>");
            }
            out.print("</table>");

            /*显示多少条*/
            out.print("<table align='center'><tr><td>");
            out.print("以多少条显示：<input type='text' name='pageSize' size='1' maxlength='3' id='pageSize' value='" + pageSize + "'/>" +
                    "<input type='button' value='go' onClick='setPageSize()'/>");
            out.print("&nbsp;");

            /*首页*/
            if(pageCurrent != 1) {
                out.print("<a href='/UserManage?pageCurrent=1&pageSize=" + pageSize + "'>首页</a>");
            } else {
                out.print("首页");
            }
            out.print("&nbsp;");

            /*上一页*/
            if(pageCurrent != 1) {
                out.print("<a href='/UserManage?pageCurrent=" + (pageCurrent - 1) + "&pageSize=" + pageSize + "&pageCodeCurrent=" + pageCodeCurrent + "'>上页</a>");
            } else {
                out.print("上页");
            }
            out.print("&nbsp;");

            /*  显示“<<” */
            if(pageCodeCurrent != 1) {
                out.print("<a href='/UserManage?pageSize=" + pageSize + "&pageCodeCurrent=" + (pageCodeCurrent-1) + "&pageCurrent=" + ((pageCodeCurrent-1)*pageCodeSize+1)  + "'> << </a>");
            } else {
                out.print("<<");
            }

            /*循环显示页面*/
            if(pageCodeCurrent*pageCodeSize < pageCount) {
                for (int i = (pageCodeCurrent - 1) * pageCodeSize + 1; i <= pageCodeCurrent * pageCodeSize; i++) {
                    out.print("<a href='/UserManage?pageCurrent=" + i + "&pageSize=" + pageSize + "&pageCodeCurrent=" + pageCodeCurrent + "'>" + "[" + i + "]" + "</a>");
                }
            } else {
                for (int i = (pageCodeCurrent - 1) * pageCodeSize + 1; i <= pageCount; i++) {
                    out.print("<a href='/UserManage?pageCurrent=" + i + "&pageSize=" + pageSize + "&pageCodeCurrent=" + pageCodeCurrent + "'>" + "[" + i + "]" + "</a>");
                }
            }

            /* 显示“>>” */
            if(pageCodeCurrent != pageCodeCount) {
                out.print("<a href='/UserManage?pageSize=" + pageSize + "&pageCodeCurrent=" + (pageCodeCurrent+1) + "&pageCurrent=" + (pageCodeCurrent*pageCodeSize+1) + "'> >> </a>");
            } else {
                out.print(">>");
            }

            /*下页*/
            if(pageCurrent != pageCount) {
                out.print("<a href='/UserManage?pageCurrent=" + (pageCurrent + 1) + "&pageSize=" + pageSize + "&pageCodeCurrent=" + pageCodeCurrent +"'>下页</a>");
            } else {
                out.print("下页");
            }
            out.print("&nbsp;");

            /*尾页*/
            if(pageCurrent != pageCount) {
                out.print("<a href='/UserManage?pageCurrent=" + pageCount + "&pageSize=" + pageSize + "'>尾页</a>");
            } else {
                out.print("尾页");
            }

            out.print("&nbsp;");

            /*统计页数*/
            out.print("第" + pageCurrent + "/" + pageCount + "页");
            out.print("&nbsp;共" + count + "条");
            out.print("&nbsp;");


            /*跳转*/
            out.print("跳转到<input type='text' size='1' maxlength='3' id='page' name='page' value='" + pageCurrent + "'/>页" +
                    "<input type='button' value='go' onClick='gotoPage()'/>");
            out.print("</td></tr></table>");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            /*关闭资源*/
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
