package com.springmvc.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.covide.crf.dto.CrfMapplingTableColumnMap;
import com.covide.crf.service.CrfService;
import com.springmvc.model.StdSubGroupObservationCrfs;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.SubGroupAnimalsInfoAll;
import com.springmvc.service.ExpermentalDesignService;
import com.springmvc.service.StudyService;
import com.springmvc.util.WeightPrintRS232Thread;
import com.springmvc.util.WeightPrintThread;

@Controller
@RequestMapping("/satroius")
public class SartoriusController {
	@Autowired
	CrfService crfSer;
	@Autowired
	StudyService studyService;
	@Autowired
	private ExpermentalDesignService expermentalDesignService;
	
	@Autowired
    private Environment environment;
	@RequestMapping(value="/readData", method=RequestMethod.GET)
	public String readData(HttpServletRequest request, 
			ModelMap model,
			RedirectAttributes redirectAttributes) {
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		List<SubGroupAnimalsInfoAll> animals = expermentalDesignService.subGroupAnimalsInfoInStudy(sm.getId());
		model.addAttribute("study", sm);
		model.addAttribute("animals", animals);
		model.addAttribute("PageHedding", "Satroius Data");
		model.addAttribute("activeUrl", "/satroius/readData");
		
		try {
			String portName = environment.getRequiredProperty("RS232PortName");
			WeightPrintRS232Thread thread = new WeightPrintRS232Thread();
			WeightPrintRS232Thread.studyService = studyService;
			WeightPrintRS232Thread.portName = portName;
			thread.start();
//					 getinstrumentData("192.168.0.159", 4001);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "satroiusData.tiles";

	}
	@RequestMapping(value="/getConfgiuredCrfs/{animalId}", method=RequestMethod.GET)
	public @ResponseBody String getConfgiuredCrfs(HttpServletRequest request, 
			ModelMap model, @PathVariable("animalId") Long animalId,
			RedirectAttributes redirectAttributes) {
		return expermentalDesignService.stdSubGroupObservationCrfsWithAnimal(animalId);
	}
	
	@RequestMapping(value="/getCrfElements/{crfId}", method=RequestMethod.GET)
	public @ResponseBody String getCrfElements(HttpServletRequest request, 
			ModelMap model, @PathVariable("crfId") Long crfId,
			RedirectAttributes redirectAttributes) {
		return expermentalDesignService.crfElements(crfId);
	}
	
	@RequestMapping(value="/saveInstrumentWeight", method=RequestMethod.POST)
	public String saveInstrumentWeight(ModelMap model, RedirectAttributes redirectAttributes, HttpSession session,
			@RequestParam("animal") Long animalId, @RequestParam("crf") Long crf, @RequestParam("crfSecEle") Long crfSecEle, @RequestParam("weight") String weight) {
		String userName = session.getAttribute("userName").toString();
		String save = expermentalDesignService.saveInstrumentWeight(animalId, crf, crfSecEle, weight, userName);
		if(save != null)
			redirectAttributes.addFlashAttribute("pageMessage", "Weight Saved Successfully.");
		else
			redirectAttributes.addFlashAttribute("pageMessage", "Faild to save TableToCrf. " + save);
		return "redirect:/satroius/readData";
	}
}
