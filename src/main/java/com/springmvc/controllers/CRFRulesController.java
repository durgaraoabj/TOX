package com.springmvc.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.covide.crf.dto.Crf;
import com.covide.crf.dto.CrfDateComparison;
import com.covide.crf.service.CrfService;

@Controller
@RequestMapping("/admini/crfRule")
public class CRFRulesController {

	@Autowired
	CrfService crfSer;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String crfRule(ModelMap model, RedirectAttributes redirectAttributes) {
		model.addAttribute("PageHedding", "Crf Rule");
		model.addAttribute("activeUrl", "admini/crfRule/");
		
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		model.addAttribute("crfRules", crfSer.findAllCrfRules());
		return "crfRule.tiles";
	}
	
	@RequestMapping(value="/crfRuleChangeStatus/{id}", method=RequestMethod.GET)
	public String crfRuleChangeStatus(@PathVariable("id") Long id,
			ModelMap model, RedirectAttributes redirectAttributes) {
		String rule = crfSer.crfRuleChangeStatus(id);
		if(rule != null)
			redirectAttributes.addFlashAttribute("pageMessage", "Rule '"+rule+"' Status Changed.");
		else
			redirectAttributes.addFlashAttribute("pageMessage", "Change to faild Rule Status");
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		return "redirect:/admini/crfRule/";
	}
	
	
	@RequestMapping(value="/ruleCreation", method=RequestMethod.GET)
	public String crfRuleCreationPage(ModelMap model, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		model.addAttribute("PageHedding", "CRF Rule");
		model.addAttribute("activeUrl", "admini/crfRule/");
		model.addAttribute("crfs", crfSer.findAllCrfs());
		return "crfRuleCreationPage.tiles";
	}
	
	
	
	@RequestMapping(value="/crfRuleElements/{rowId}/{crfId}", method=RequestMethod.GET)
	public @ResponseBody String crfRuleElements(@PathVariable("rowId") int rowId, @PathVariable("crfId") Long crfId) {
		return crfSer.crfRuleElements(rowId, crfId);
	}
	
	@RequestMapping(value="/otherCrfSectionElements/{id}/{count}", method=RequestMethod.GET)
	public @ResponseBody String otherCrfSectionElements(@PathVariable("id") Long id, @PathVariable("count") int count) {
		return crfSer.otherCrfSectionElements(id, count);
	}
	
	@RequestMapping(value="/otherCrfGroupElements/{id}/{count}", method=RequestMethod.GET)
	public @ResponseBody String otherCrfGroupElements(@PathVariable("id") Long id, @PathVariable("count") int count) {
		return crfSer.otherCrfGroupElements(id, count);
	}
	
	@RequestMapping(value = "/crfRuleSave", method = RequestMethod.POST)
	public String crfuploadsave(ModelMap model,
			HttpServletRequest request,
			HttpSession session,
			RedirectAttributes redirectAttributes) {
		String username = request.getSession().getAttribute("userName").toString();
		String ruleName = crfSer.saveCrfRule(username, request);
		if(ruleName != null)			
			redirectAttributes.addFlashAttribute("pageMessage", "Rule Saved Successfully");
		else
			redirectAttributes.addFlashAttribute("pageMessage", "Faild to save Rule");
		return "redirect:/admini/crfRule/";
	}
	
	@RequestMapping(value="/crfSectionElements/{id}", method=RequestMethod.GET)
	public @ResponseBody String crfSectionElements(@PathVariable("id") Long id) {
		return crfSer.sectionEleSelect(id);
	}
	
	@RequestMapping(value="/crfGroupElements/{id}", method=RequestMethod.GET)
	public @ResponseBody String crfGroupElements(@PathVariable("id") Long id) {
		return crfSer.groupEleSelect(id);
	}
	
	@RequestMapping(value="/crfDateComparison", method=RequestMethod.GET)
	public String crfDateComparison(ModelMap model, RedirectAttributes redirectAttributes) {
		model.addAttribute("PageHedding", "E-Form Date Comparison");
		model.addAttribute("activeUrl", "admini/crfRule/crfDateComparison");
		List<CrfDateComparison> list = crfSer.crfDateComparisonlist();
		model.addAttribute("list", list);
		return "crfDateComparison.tiles";
	}
	
	@RequestMapping(value="/crfDateComparisonPage", method=RequestMethod.GET)
	public String crfDateComparisonPage(ModelMap model, RedirectAttributes redirectAttributes) {
//		model.addAllAttributes(redirectAttributes.getFlashAttributes());
//		model.addAttribute("crfRules", crfSer.findAllCrfRules());
		model.addAttribute("PageHedding", "E-Form Date Comparison");
		model.addAttribute("activeUrl", "admini/crfRule/crfDat	eComparison");
		List<Crf> list = crfSer.findAllActiveCrfs();
		model.addAttribute("crfList", list);
		return "crfDateComparisonPage.tiles";
	}
	
	@RequestMapping(value="/crfSectionElements1/{id}", method=RequestMethod.GET)
	public @ResponseBody String crfSectionElements1(@PathVariable("id") Long id) {
		return crfSer.sectionEleSelect1(id);
	}
	
	@RequestMapping(value="/crfGroupElements1/{id}", method=RequestMethod.GET)
	public @ResponseBody String crfGroupElements1(@PathVariable("id") Long id) {
		return crfSer.groupEleSelect1(id);
	}
	
	@RequestMapping(value="/crfSectionElements2/{id}", method=RequestMethod.GET)
	public @ResponseBody String crfSectionElements2(@PathVariable("id") Long id) {
		return crfSer.sectionEleSelect2(id);
	}
	
	@RequestMapping(value="/crfGroupElements2/{id}", method=RequestMethod.GET)
	public @ResponseBody String crfGroupElements2(@PathVariable("id") Long id) {
		return crfSer.groupEleSelect2(id);
	}
	
	@RequestMapping(value="/crfSectionElements3/{id}", method=RequestMethod.GET)
	public @ResponseBody String crfSectionElements3(@PathVariable("id") Long id) {
		return crfSer.sectionEleSelect3(id);
	}
	
	@RequestMapping(value="/crfGroupElements3/{id}", method=RequestMethod.GET)
	public @ResponseBody String crfGroupElements3(@PathVariable("id") Long id) {
		return crfSer.groupEleSelect3(id);
	}
	@RequestMapping(value="/crfSectionElements4/{id}", method=RequestMethod.GET)
	public @ResponseBody String crfSectionElements4(@PathVariable("id") Long id) {
		return crfSer.sectionEleSelect4(id);
	}
	
	@RequestMapping(value="/crfGroupElements4/{id}", method=RequestMethod.GET)
	public @ResponseBody String crfGroupElements4(@PathVariable("id") Long id) {
		return crfSer.groupEleSelect4(id);
	}
	
	@RequestMapping(value = "/crfDateComparisonSave", method = RequestMethod.POST)
	public String crfDateComparisonSave(ModelMap model,
			HttpServletRequest request,
			HttpSession session,
			RedirectAttributes redirectAttributes) {
		String username = request.getSession().getAttribute("userName").toString();
		String ruleName = crfSer.saveCrfDateComparison(username, request);
		if(ruleName != null)			
			redirectAttributes.addFlashAttribute("pageMessage", "E-Form Date Comparison Saved Successfully");
		else
			redirectAttributes.addFlashAttribute("pageMessage", "Faild to save E-Form Date Comparison");
		return "redirect:/admini/crfRule/crfDateComparisonPage";
	}
	
	@RequestMapping(value="/crfDateComparisonStatus/{id}", method=RequestMethod.GET)
	public String crfDateComparisonStatus(@PathVariable("id") Long id,
			ModelMap model, RedirectAttributes redirectAttributes) {
		String rule = crfSer.crfDateComparisonChangeStatus(id);
		if(rule != null)
			redirectAttributes.addFlashAttribute("pageMessage", "Date Comparison '"+rule+"' Status Changed.");
		else
			redirectAttributes.addFlashAttribute("pageMessage", "Change to faild Date Comparison Status");
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		return "redirect:/admini/crfRule/crfDateComparison";
	}
	
	@RequestMapping(value="/crfSectionElementsSelectTable/{id}", method=RequestMethod.GET)
	public @ResponseBody String crfSectionElementsSelectTable(@PathVariable("id") Long id) {
		return crfSer.crfSectionElementsSelectTable(id);
	}
	
	@RequestMapping(value="/crfMappingTableColumns/{id}", method=RequestMethod.GET)
	public @ResponseBody String crfMappingTableColumns(@PathVariable("id") Long id) {
		return crfSer.crfMappingTableColumns(id);
	}
}
