package com.springmvc.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.covide.crf.service.CrfService;
import com.springmvc.service.StudyService;
import com.springmvc.service.UserService;

@Controller
@RequestMapping("/crfCreation")
public class CrfCreationController {

	@Autowired
	StudyService studyService;

	@Autowired
	UserService userService;
	
	@Autowired
	CrfService crfService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String adminHomePage(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		return "crfCreation.tiles";
	}
}
