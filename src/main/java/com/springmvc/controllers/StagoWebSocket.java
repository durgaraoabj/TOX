package com.springmvc.controllers;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/stagoWebSocket")
public class StagoWebSocket {
	public static Map<String, SseEmitter> stagoEmittersMap = new HashMap<String, SseEmitter>();
	public static Map<String, SseEmitter> stagoSeparationMap = new HashMap<String, SseEmitter>();
//	public static List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
	
	@CrossOrigin
	@RequestMapping(value="/fatchStagoData", method=RequestMethod.GET)
	public SseEmitter testWebSocket(HttpServletRequest request, 
			RedirectAttributes redirectAttributes) {
		String userName = (String) request.getSession().getAttribute("userName");
		SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
		try {
			sseEmitter.send(SseEmitter.event().name("INIT"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(stagoEmittersMap.containsKey(userName))
			stagoEmittersMap.remove(sseEmitter);
		stagoEmittersMap.put(userName, sseEmitter);
//		sseEmitter.onCompletion(() -> emitters.remove(sseEmitter));
//		emitters.add(sseEmitter);
//		request.getSession().setAttribute("sampleDashBoard", emitters);
		return sseEmitter;
	}
	
}
