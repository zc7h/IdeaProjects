package com.zc7h.view;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by BinLaden on 2016.06.29.
 */
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println("<form method='post' action='/LoginCtrlServlet'>");
        out.println("用户名：<input type='text' name='username'/><br/>");
        out.println("密　码：<input type='password' name='password'/><br/>");
        out.println("<input type='submit' value='登陆'/>");
        out.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
        out.println("<input type='reset' value='重置'/>");
        out.println("</form>");
        Object err = request.getAttribute("err");
        if(err != null) {
            out.println("<font color='red'>" + err.toString() + "</font>");
        }
    }
}
