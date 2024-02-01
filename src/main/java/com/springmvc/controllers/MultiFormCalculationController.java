package com.springmvc.controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.covide.crf.dto.Crf;
import com.springmvc.service.MultiFormCalculationService;

@Controller
@RequestMapping("/formCalculation")
public class MultiFormCalculationController {
	
	@Autowired
	MultiFormCalculationService mfromCalcService;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String multiFormCalculation(ModelMap model) {
		List<Crf> crfsList = mfromCalcService.getCrfsRecordsList();
		model.addAttribute("crfList", crfsList);
		return "multiFormCalculationPage";
	}
	
	@RequestMapping(value="/getCrfSectionAndGroupElements/{crfId}", method=RequestMethod.GET)
	public String getCrfSectionAndGroupElements(ModelMap model, @PathVariable("crfId")Long crfId) {
		Crf crf = mfromCalcService.getCrfRecord(crfId);
		model.addAttribute("crf", crf);
		return "pages/multiFormCalc/crfFromElementsPage";
	}
	
	
	@RequestMapping(value="/getFormulaCalculationData/{crfId}/{animalId}", method=RequestMethod.GET)
	public String getFormulaCalculationData(ModelMap model, @PathVariable("crfId")Long crfId, @PathVariable("animalId")Long animalId,HttpServletRequest request) {
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
		Map<String, String> calcMap = mfromCalcService.getgetFormulaCalculationData(crfId, animalId, studyId);
		model.addAttribute("calcMap", calcMap);
		return "pages/multiFormCalc/crfCalulatedPage";
	}
	
	
	@RequestMapping(value="/getFormulaCalculationForCurrentForm/{crfId}/{dataString}", method=RequestMethod.GET)
	public String getFormulaCalculationForCurrentForm(ModelMap model, @PathVariable("crfId")Long crfId, @PathVariable("dataString")List<String>dataString,HttpServletRequest request) {
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
		Map<String, String> calcMap = mfromCalcService.getFormulaCalculationDataForCurrentForm(crfId, dataString, studyId);
		model.addAttribute("calcMap", calcMap);
		return "pages/multiFormCalc/crfCalulatedPage";
	}

}
