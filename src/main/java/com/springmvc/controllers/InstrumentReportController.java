package com.springmvc.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springmvc.service.InstrumentService;
import com.springmvc.service.StudyService;

@RequestMapping("/instrumentReport")
@Controller
public class InstrumentReportController {
	@Autowired
	InstrumentService instrumentService;
	
	@Autowired
	StudyService studyService;
	
	@RequestMapping(value = "/{instumentName}", method = RequestMethod.GET)
	public String callStagoService(HttpServletRequest request, ModelMap model,
			@PathVariable("instumentName") String instumentName,
			RedirectAttributes redirectAttributes) {
		if(instumentName.equalsIgnoreCase("Stago")) {
			model.addAttribute("studyNumbers", studyService.stagoStudyNumbers());
			return "stagoDataExport";
		}else if(instumentName.equalsIgnoreCase("Sysmex")) {
			model.addAttribute("studyNumbers", studyService.sysmaxStudyNumbers());
			return "sysmexDataExport";
		}else if(instumentName.equalsIgnoreCase("Vitros")) {
			model.addAttribute("studys", studyService.allActiveStudys());
			return "vitrosDataExport";
		}
		return "sysmexDataExport";
	}
}
