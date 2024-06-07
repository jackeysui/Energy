package com.linyang.energy.controller;

import com.esotericsoftware.minlog.Log;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Administrator on 19-6-3.
 */
public class ZdnyRedirectServlet extends HttpServlet {
    private static final long serialVersionUID = 5076539267147965601L;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.getRequestDispatcher("/frameController/showLoginPage.htm?loginPath=zdny_login").forward(request, response);
        } catch (ServletException e) {
            Log.error(this.getClass() + "doGet error!");
        } catch (IOException e) {
            Log.error(this.getClass()+"doGet IO error!");
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.getRequestDispatcher("/frameController/showLoginPage.htm?loginPath=zdny_login").forward(request, response);
        } catch (ServletException e) {
            Log.error(this.getClass()+"doPost error!");
        } catch (IOException e) {
            Log.error(this.getClass()+"doPost IO error!");
        }
    }
}
