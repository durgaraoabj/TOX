package com.covide.aspect;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Aspect
@Component
@PropertySource(value = { "classpath:application.properties" })
public class ConrollerAscpenct {
	@Autowired
	private Environment environment;

	@Autowired
	private HttpSession session;
	
	@AfterReturning(pointcut = "execution(* com.springmvc.controllers.*Controller.*(..))", returning = "userObj")
	public void requiestDetials(Object userObj) throws Throwable {
//		System.out.println("****requiestDetials()  start--");
////		System.out.println(request.getURI());
//		System.out.println("****requiestDetials()  end--");
	}
}
