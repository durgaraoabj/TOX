package com.springmvc.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springmvc.service.StudyDesignReviewService;

@Controller
@RequestMapping("/designReview")
public class StudyDesignReviewController {
	
	@Autowired
	StudyDesignReviewService stdrService;
	
	@RequestMapping(value="/checkReviewProcess/{studyId}", method=RequestMethod.GET)
	public String checkReviewProcess(ModelMap model, @PathVariable("studyId")Long studyId) {
		String result = stdrService.checReviewProcessDetails(studyId);
		model.addAttribute("resultVal", result);
		return "/pages/study/studyDesign/reviewResult";
	}
	
	@RequestMapping(value="/observationDesingSendToReview", method=RequestMethod.POST)
	public String observationDesingSendToReview(ModelMap model, HttpServletRequest request, @RequestParam("studyIdVal")Long studyId, RedirectAttributes redirectAttributes) {
		String userName = request.getSession().getAttribute("userName").toString();
		String result = stdrService.observationDesingSendToReviewProcess(studyId, userName);
		if(result.equals("success"))
			redirectAttributes.addFlashAttribute("pageMessage", "Observation Design Forwarded to Review.");
		else redirectAttributes.addFlashAttribute("pageError", "Observation Desing Forwarding to Review Process Failed. Please Try Again.");
		return "redirect:/expermentalDesign/observationConfig";
	}
	
	

}
