package com.springmvc.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.covide.dto.ObservationScheduleDto;
import com.springmvc.service.ObserVationDesinService;

@Controller
@RequestMapping("/obvd")
public class ObservationDesignControll {
	
	@Autowired
	ObserVationDesinService obvdservice;
	
	@RequestMapping(value="/obserVasionDesignSchedule", method=RequestMethod.GET)
	public String obserVasionDesignSchedule(ModelMap model, HttpServletRequest request) {
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
		ObservationScheduleDto osdto = obvdservice.getObservationScheduleDetails(studyId);
		model.addAttribute("osdto", osdto);
		return "obserVasionDesignScheduleDetails";
	}
	
	
	@RequestMapping(value="/datwiseobserVasionDesignSchedule", method=RequestMethod.GET)
	public String datwiseobserVasionDesignSchedule(ModelMap model, HttpServletRequest request) {
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
		ObservationScheduleDto osdto = obvdservice.todayObservationScheduleDetails(studyId);
		model.addAttribute("osdto", osdto);
		return "dayWiseobserVasionDesignScheduleDetails";
	}

}
