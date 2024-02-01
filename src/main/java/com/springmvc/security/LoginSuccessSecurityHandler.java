package com.springmvc.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

public class LoginSuccessSecurityHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = "";
		try {
			username = request.getSession().getAttribute("userName").toString();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			Object principal = auth.getPrincipal();
			if (principal instanceof UserDetails) {
				username = ((UserDetails) principal).getUsername();
			} else {
				username = principal.toString();
			}
		}
		if (auth != null && !username.equals("anonymous") && username != null && !username.equals("")) {
			request.getSession().setAttribute("userName", username);
			if(!request.getParameter("barcode").equals("")) {
				request.getSession().setAttribute("LabTechBacode", request.getParameter("barcode"));
				response.sendRedirect(request.getContextPath() + "/study/clinical/stdClinicalExecution");
			}else
				response.sendRedirect(request.getContextPath() + "/dashboard/");
		} else {
			new SecurityContextLogoutHandler().logout(request, response, auth);
			SecurityContextHolder.getContext().setAuthentication(null);
			response.sendRedirect(request.getContextPath() + "/login");
		}
	}

}
