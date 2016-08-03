package com.zc7h.view;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by BinLaden on 2016.06.30.
 */
public class MainFrame extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println("<span align='center'><h1>主界面</h1></span><br/>");
        out.println("<font color='red'>当前用户：</font>&nbsp;&nbsp;&nbsp;" +
                "安全退出<br/>");
        out.println("<ul>");
        out.println("<li><a href='/UserManage'>管理用户</a></li>");
        out.println("<li>添加用户</li>");
        out.println("<li>查找用户</li>");
        out.println("</ul>");
    }
}
