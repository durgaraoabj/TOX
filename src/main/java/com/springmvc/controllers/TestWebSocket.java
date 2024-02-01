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
@RequestMapping("/testWebSocket")
public class TestWebSocket {
	public static Map<String, SseEmitter> emittersMap = new HashMap<String, SseEmitter>();
	public static Map<String, SseEmitter> sampleSeparationMap = new HashMap<String, SseEmitter>();
//	public static List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String testWebSocket0(HttpServletRequest request, 
			RedirectAttributes redirectAttributes) {
		return "testWebSocket.tiles";
	}
	@CrossOrigin
	@RequestMapping(value="/connect", method=RequestMethod.GET)
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
		if(emittersMap.containsKey(userName))
			emittersMap.remove(sseEmitter);
		emittersMap.put(userName, sseEmitter);
//		sseEmitter.onCompletion(() -> emitters.remove(sseEmitter));
//		emitters.add(sseEmitter);
//		request.getSession().setAttribute("sampleDashBoard", emitters);
		return sseEmitter;
	}
	
	
	@RequestMapping(value="/ss", method=RequestMethod.GET)
	public void dispathEventToClients() {
		Map<String, String> s = new HashedMap();
		s.put("title", "1000");
		s.put("text", "1002");
		s.put("text1", "1003");
		String json = "";
		try {
			json = new ObjectMapper().writeValueAsString(s);
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for(Map.Entry<String, SseEmitter> emitters : emittersMap.entrySet()){
			SseEmitter emitter = emitters.getValue();
			try {
				emitter.send(SseEmitter.event().name("latestNews").data(json));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				emittersMap.remove(emitters.getKey());
			}
		}
//		return "testWebSocket.tiles";
	}
}
