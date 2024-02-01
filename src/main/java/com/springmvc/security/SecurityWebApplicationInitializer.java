package com.springmvc.security;

import javax.servlet.ServletContext;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.multipart.support.MultipartFilter;

public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

	@Override
	public boolean enableHttpSessionEventPublisher() {
		return true;
	}
	
	@Override
    protected void beforeSpringSecurityFilterChain(ServletContext servletContext) {
	  insertFilters(servletContext, new MultipartFilter()); 
    }

}
