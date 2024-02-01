package com.springmvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.springmvc.controllers.TestWebSocket;
import com.springmvc.util.StagoThread;
import com.springmvc.util.SysmexThread;
import com.springmvc.util.VistrosThread;

public class MainInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception {
		// TODO Auto-generated method stub
		
		System.out.println("=================================> preHandler(-,-,-) <========================================");
		
		String reqUrl = request.getRequestURL().toString();
	    String queryString = request.getQueryString();   // d=789
	    if (queryString != null) {
	        reqUrl += "?"+queryString;
	    }
	    System.out.println(reqUrl);
		String userName = (String) request.getSession().getAttribute("userName");
		TestWebSocket.emittersMap.remove(userName);
		TestWebSocket.sampleSeparationMap.remove(userName);
		
		VistrosThread vitrosThread = VistrosThread.runningThreads.get(userName);
		if (vitrosThread != null) {
			if (vitrosThread.isAlive())
				vitrosThread.interrupt();
			vitrosThread = null;
			VistrosThread.runningThreads.remove(userName);
		}
		
		SysmexThread sysmexThread = SysmexThread.runningThreads.get(userName);
		if (sysmexThread != null) {
			if (sysmexThread.isAlive())
				sysmexThread.interrupt();
			sysmexThread = null;
			SysmexThread.runningThreads.remove(userName);
		}
		StagoThread stagoThread = StagoThread.runningThreads.get(userName);
		if (stagoThread != null) {
			if (stagoThread.isAlive())
				stagoThread.interrupt();
			stagoThread = null;
			StagoThread.runningThreads.remove(userName);
		}
		
		
		request.getSession().setMaxInactiveInterval(30*60);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(
				"=================================> postHandler(-,-,-) <========================================");
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println(
				"=================================> afterCompletion(-,-,-) <========================================");
	}

}
