/**
 */
package com.linyang.energy.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.esotericsoftware.minlog.Log;

/**
 * 
 * @author Angelo
 * @date 2016-4-1 上午10:29:49
 * @version 1.0
 */
public class NeuterRedirectServlet extends HttpServlet {

	private static final long serialVersionUID = 5076539267147965601L;

	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.getRequestDispatcher("/frameController/showLoginPage.htm?loginPath=neuter_login").forward(request, response);
        } catch (ServletException e) {
            Log.error(this.getClass()+"doGet error!");
        } catch (IOException e) {
            Log.error(this.getClass()+"doGet IO error!");
        }
	}


	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.getRequestDispatcher("/frameController/showLoginPage.htm?loginPath=neuter_login").forward(request, response);
        } catch (ServletException e) {
            Log.error(this.getClass()+"doPost error!");
        } catch (IOException e) {
        	 Log.error(this.getClass()+"doPost IO error!");
        }
    }
	
}
