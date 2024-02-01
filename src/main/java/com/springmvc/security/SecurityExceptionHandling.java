package com.springmvc.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

public class SecurityExceptionHandling implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		// TODO Auto-generated method stub
		 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		 String username = "";
		 try {
			 username = request.getSession().getAttribute("userName").toString();
		 }catch(Exception e) {
			 System.out.println(e.getMessage());
		 }
       if (auth != null && username!=null && !username.equals("")) {
    	   response.sendRedirect(request.getContextPath() + "/main/home");
       }else {
    	   new SecurityContextLogoutHandler().logout(request, response, auth);
    	   SecurityContextHolder.getContext().setAuthentication(null);
	       response.sendRedirect(request.getContextPath() + "/login");
       }
	}
}
