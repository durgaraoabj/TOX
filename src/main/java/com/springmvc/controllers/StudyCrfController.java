package com.springmvc.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.covide.crf.dto.Crf;
import com.covide.crf.dto.CrfDateComparison;
import com.covide.crf.dto.CrfEleCaliculation;
import com.covide.crf.dto.CrfGroup;
import com.covide.crf.dto.CrfSection;
import com.covide.crf.service.CrfService;
import com.covide.validator.CrfValidator;
import com.springmvc.model.PeriodCrfs;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyPeriodMaster;
import com.springmvc.service.StudyService;

@Controller
@RequestMapping("/buildStdyCrf")
@PropertySource(value = { "classpath:application.properties" })
public class StudyCrfController {
	
	@Autowired
	StudyService studyService;
	
	@Autowired
	CrfService crfService;
	@Autowired
    private Environment environment;
	
	
	@RequestMapping(value="/crfChangeStatus/{crfId}", method=RequestMethod.GET)
	public String changeStudy(@PathVariable("crfId") Long crfId, 
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		System.out.println("CRF ID : "+crfId);
		String username = request.getSession().getAttribute("userName").toString();
		Crf crf = crfService.changeStudyCrfStatus(crfId, username);
		if(crf != null)
			redirectAttributes.addFlashAttribute("pageMessage", "Study Crf '"+crf.getName()+"' Status changed.");
		else
			redirectAttributes.addFlashAttribute("pageMessage", "Faild to Change Crf Status.");
		return "redirect:/buildStdyCrf/uploadStudyCrf";
	}
	
	
	@RequestMapping(value = "/configureCrfsToPeriod", method = RequestMethod.GET)
	public String configureCrfsToPeriod(ModelMap model, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		model.addAttribute("study", sm);
		
		List<StudyPeriodMaster> periods = studyService.allStudyPeriods(sm);
		model.addAttribute("periods", periods);

		return "configureCrfsToPeriod.tiles";
	}
	
	public static Long pid;
	@RequestMapping(value = "/configureCrfsPeriod", method = RequestMethod.POST)
	public String configureCrfsPeriod(ModelMap model, @RequestParam Long periodId, HttpServletRequest request) {
		if (periodId == null)
			periodId = pid;
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		model.addAttribute("stdcrfConfiguation", sm.isCrfConfiguation());

		StudyPeriodMaster sp = studyService.studyPeriodMaster(periodId);
		model.addAttribute("crfConfiguation", sp.isCrfConfiguation());
		model.addAttribute("periodName", sp.getName());

		List<Crf> stdcrfs = crfService.findAllStudyCrfsForRules(sm);
		List<Long> temp = new ArrayList<>();
		Map<Long, PeriodCrfs> tempmap = new HashMap<Long, PeriodCrfs>();
		List<PeriodCrfs> pcrfs = studyService.periodCrfs(periodId);
		for (PeriodCrfs pc : pcrfs) {
			if (pc.isActive())
				temp.add(pc.getCrfId());
			tempmap.put(pc.getCrfId(), pc);
		}

		List<Crf> stdcrfsp = new ArrayList<>();
		for (Crf stdcrf : stdcrfs) {
//			if (temp.contains(stdcrf.getId())) {
//				stdcrf.setPeriodCrfstatus(true);
//				PeriodCrfs pc = tempmap.get(stdcrf.getId());
//				if(pc != null && pc.getExitCrf().equals("Yes")) {
//					stdcrf.setExitCrf(true);
//				}else
//					stdcrf.setExitCrf(false);
//			}else
//				stdcrf.setPeriodCrfstatus(false);
			stdcrfsp.add(stdcrf);
		}

		model.addAttribute("periodId", periodId);
		model.addAttribute("stdcrfsp", stdcrfsp);
		model.addAttribute("sp", sp);
		return "configureCrfsPeriod.tiles";
	}
	
	@RequestMapping(value = "/configureCrfsPeriodSave", method = RequestMethod.POST)
	public String configureCrfsPeriodSave(ModelMap model, @RequestParam Long periodId, @RequestParam String crfIds, @RequestParam String exitcrfIds,
			HttpServletRequest request) {
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		if (!sm.isCrfConfiguation()) {
			sm.setCrfConfiguation(true);
			studyService.updateStudy(sm);
		}
		StudyPeriodMaster sp = studyService.studyPeriodMaster(periodId);

		List<Crf> stdcrfs = crfService.findAllStudyCrfsForRules(sm);
		String[] ids = crfIds.split(",");
		List<Long> cids = new ArrayList<>();
		for (String s : ids)
			if (s.trim() != "")
				cids.add(Long.parseLong(s.trim()));

		ids = exitcrfIds.split(",");
		List<Long> exitcids = new ArrayList<>();
		for (String s : ids)
			if (s.trim() != "")
				exitcids.add(Long.parseLong(s.trim()));
		
		List<Long> temp = new ArrayList<>();
		List<PeriodCrfs> pcrfs = studyService.periodCrfs(periodId);
		List<PeriodCrfs> pcrfsupdate = new ArrayList<>();
		for (PeriodCrfs pc : pcrfs) {
			if (cids.contains(pc.getCrfId())) {
				if (!pc.isActive()) {
					pc.setActive(true);
				}
			} else {
				pc.setActive(false);
			}
			if(exitcids.contains(pc.getCrfId())) {
				pc.setExitCrf("Yes");
			}else {
				pc.setExitCrf("No");
			}
			pcrfsupdate.add(pc);
			temp.add(pc.getCrfId());
		}

		List<PeriodCrfs> pcrfsSave = new ArrayList<>();
		
		System.out.println(cids);
		System.out.println(temp);
		for (Crf stdcrf : stdcrfs) {
			System.out.println(stdcrf.getId());
			if (cids.contains(stdcrf.getId()) && !temp.contains(stdcrf.getId())) {
				// create PeriodCrfs
				PeriodCrfs pcrf = new PeriodCrfs();
				pcrf.setPeriod(sp);
				pcrf.setPeriodId(sp.getId());
				pcrf.setCrfName(stdcrf.getName());
				pcrf.setCrfId(stdcrf.getId());
				if(exitcids.contains(stdcrf.getId())) {
					pcrf.setExitCrf("Yes");
				}
				pcrfsSave.add(pcrf);
			}
		}

		studyService.upatePeriodCrfsList(pcrfsupdate);
		studyService.savePeriodCrfsList(pcrfsSave);
		String markComplete = request.getParameter("markComplete");
		if(markComplete.equals("Yes")) {
			sp.setCrfConfiguation(true);
			studyService.upatePeriodCrfs(sp);
		}
		model.addAttribute("pageMessage", "Crfs Configuration Done Successfully.");
		
		temp = new ArrayList<>();
		pcrfs = studyService.periodCrfs(periodId);
		for (PeriodCrfs pc : pcrfs) {
			if (pc.isActive())
				temp.add(pc.getCrfId());
		}

		Map<Long, PeriodCrfs> tempmap = new HashMap<Long, PeriodCrfs>();
		for(PeriodCrfs pc : pcrfsupdate)
			tempmap.put(pc.getCrfId(), pc);
		for(PeriodCrfs pc : pcrfsSave)
			tempmap.put(pc.getCrfId(), pc);
		
		List<Crf> stdcrfsp = new ArrayList<>();
//		for (Crf stdcrf : stdcrfs) {
//			if (temp.contains(stdcrf.getId()))
//				stdcrf.setPeriodCrfstatus(true);
//			else
//				stdcrf.setPeriodCrfstatus(false);
//			PeriodCrfs pc = tempmap.get(stdcrf.getId());
//			if(pc != null && pc.getExitCrf().equals("Yes")) {
//				stdcrf.setExitCrf(true);
//			}else
//				stdcrf.setExitCrf(false);
//			stdcrfsp.add(stdcrf);
//		}
		
		model.addAttribute("pageMessage", "Crfs Configuration Done Successfully.");
		model.addAttribute("stdcrfConfiguation", sm.isCrfConfiguation());
		model.addAttribute("crfConfiguation", sp.isCrfConfiguation());
		model.addAttribute("periodName", sp.getName());
		model.addAttribute("periodId", periodId);
		model.addAttribute("stdcrfsp", stdcrfsp);
		model.addAttribute("sp", sp);
		return "configureCrfsPeriod.tiles";
	}
	
	@RequestMapping(value = "/configureCrfsMarkCompletePeriod", method = RequestMethod.POST)
	public String configureCrfsMarkCompletePeriod(ModelMap model, @RequestParam Long periodId,
			HttpServletRequest request) {
		if (periodId == null)
			periodId = pid;
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		StudyPeriodMaster sp = studyService.studyPeriodMaster(periodId);
		sp.setCrfConfiguation(true);
		studyService.upatePeriodCrfs(sp);
		
		List<Crf> stdcrfs = crfService.findAllStudyCrfsForRules(sm);
		List<Long> temp = new ArrayList<>();
		List<PeriodCrfs> pcrfs = studyService.periodCrfs(periodId);
		for (PeriodCrfs pc : pcrfs) {
			if (pc.isActive())
				temp.add(pc.getCrfId());
		}

		List<Crf> stdcrfsp = new ArrayList<>();
//		for (Crf stdcrf : stdcrfs) {
//			if (temp.contains(stdcrf.getId()))
//				stdcrf.setPeriodCrfstatus(true);
//			else
//				stdcrf.setPeriodCrfstatus(false);
//			stdcrfsp.add(stdcrf);
//		}

		model.addAttribute("stdcrfConfiguation", sm.isCrfConfiguation());
		model.addAttribute("crfConfiguation", sp.isCrfConfiguation());
		model.addAttribute("periodName", sp.getName());
		model.addAttribute("periodId", periodId);
		model.addAttribute("stdcrfsp", stdcrfsp);
		model.addAttribute("pageMessage", "Crfs Configuration Done Successfully.");
		return "configureCrfsPeriod.tiles";
	}
	

	
	@RequestMapping(value = "/configureAllPeriodsCrfsMarkComplete", method = RequestMethod.POST)
	public String configureAllPeriodsCrfsMarkComplete(ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		sm.setCrfPeriodConfiguation(true);
		
//		if(environment.getRequiredProperty("pkSampelManagement").equals("true")) {
//			if(crfService.checkPkSamples(sm)) {
//				crfService.copyCrfCaliculationFromLib(sm);
//				studyService.updateStudy(sm);
//			}else {
//				redirectAttributes.addFlashAttribute("pageMessage", "Clinical information not avilable plese completed Clinical inforamtion" );
//			}
//		}else {
//			crfService.copyCrfCaliculationFromLib(sm);
//		}
		return "redirect:/buildStdyCrf/configureCrfsToPeriod";
	}
	
	@RequestMapping(value="/crfFieldCaliculation", method=RequestMethod.GET)
	public String crfFieldCaliculationPage(HttpServletRequest request, 
			ModelMap model,
			RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		List<CrfEleCaliculation> list = crfService.studyCrfEleCaliculation(activeStudyId);
		
		model.addAttribute("PageHedding", "Upload Study E-Form Calculation");
		model.addAttribute("activeUrl", "buildStdyCrf/crfFieldCaliculation");
		model.addAttribute("list", list);
		return "studycrfFieldCaliculation.tiles";

	}
	
	@RequestMapping(value = "/crfuploadxml", method = RequestMethod.POST)
	public String crfuploadxml(@RequestParam CommonsMultipartFile file,
			@Valid @ModelAttribute("crfpojo") Crf crf, HttpSession session,
			RedirectAttributes redirectAttributes, BindingResult results,Model model,
			HttpServletRequest request) {
		String username = request.getSession().getAttribute("userName").toString();
		String path = session.getServletContext().getRealPath("/");
		String filename = file.getOriginalFilename();
		System.out.println(path + " " + filename);
		if (!crf.getFile().getOriginalFilename().equals("")) {
//			new CrfValidator().validate(crf, results);
			System.out.println(results.getErrorCount());
			if (crf.getId() == null || crf.getId() == 0 ) {
				try {
					byte barr[] = file.getBytes();
					path = path + "caliculation.xml";
					BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(path ));
					bout.write(barr);
					bout.flush();
					bout.close();
					
				} catch (Exception e) {
					System.out.println(e);
					model.addAttribute("message", "Failde uploade");
				}
				
				FileInputStream fis = null;
				try {
					Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
					StudyMaster sm = studyService.findByStudyId(activeStudyId);
					File   fileforfis = new File(path);
					fis = new FileInputStream(fileforfis);
					crfService.uploadStudyCrfCaliculationFile(path, username, sm);
					
					redirectAttributes.addFlashAttribute("pageMessage", "Xml File uploaded Successfully" );
				} catch (Exception e) {
					e.printStackTrace();
					model.addAttribute("message", "Failde uploade");
				}
			}
		}
		
//		List<Crf> crfList = crfSer.findAllCrfs();
//		model.addAttribute("crfList", crfList);
		
		return "redirect:/buildStdyCrf/crfFieldCaliculation";
	}
	
	@RequestMapping(value="/crfChangeCaliculationStatus/{crfId}", method=RequestMethod.GET)
	public String crfChangeCaliculationStatus(@PathVariable("crfId") Long crfId, 
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		System.out.println("CRF ID : "+crfId);
		String username = request.getSession().getAttribute("userName").toString();
		
		CrfEleCaliculation crf = crfService.changeStudyCrfEleCaliculationStatus(crfId, username);
		if(crf != null)
			redirectAttributes.addFlashAttribute("pageMessage",  "Status changed.");
		else
			redirectAttributes.addFlashAttribute("pageMessage", "Faild to Change Status.");
		return "redirect:/buildStdyCrf/crfFieldCaliculation";
	}
	
	@RequestMapping(value="/crfRule", method=RequestMethod.GET)
	public String crfRule(ModelMap model,HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		model.addAttribute("PageHedding", "E-From Rule");
		model.addAttribute("activeUrl", "buildStdyCrf/crfRule/");
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		model.addAttribute("crfRules", crfService.studyCrfRuleWithCrfAndSubElements(sm));
		return "studycrfRule.tiles";
	}
	
	@RequestMapping(value="/crfRule/ruleCreation", method=RequestMethod.GET)
	public String crfRuleCreationPage(ModelMap model,HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		model.addAttribute("PageHedding", "E-From Rule");
		model.addAttribute("activeUrl", "buildStdyCrf/crfRule/");
		model.addAttribute("crfs", crfService.findAllStudyCrfsForRules(sm));
		return "studycrfRuleCreationPage.tiles";
	}
	
	@RequestMapping(value="/crfSectionElements/{id}", method=RequestMethod.GET)
	public @ResponseBody String crfSectionElements(@PathVariable("id") Long id) {
		return crfService.studysectionEleSelect(id);
	}
	
	@RequestMapping(value="/crfGroupElements/{id}", method=RequestMethod.GET)
	public @ResponseBody String crfGroupElements(@PathVariable("id") Long id) {
		return crfService.studygroupEleSelect(id);
	}
	
	@RequestMapping(value="/crfRuleElements/{rowId}", method=RequestMethod.GET)
	public @ResponseBody String crfRuleElements(@PathVariable("rowId") int rowId, HttpServletRequest request) {
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		return crfService.studycrfRuleElements(sm, rowId);
	}
	
	@RequestMapping(value="/otherCrfSectionElements/{id}/{count}", method=RequestMethod.GET)
	public @ResponseBody String otherCrfSectionElements(@PathVariable("id") Long id, @PathVariable("count") int count) {
		return crfService.otherStudyCrfSectionElements(id, count);
	}
	
	@RequestMapping(value="/otherCrfGroupElements/{id}/{count}", method=RequestMethod.GET)
	public @ResponseBody String otherCrfGroupElements(@PathVariable("id") Long id, @PathVariable("count") int count) {
		return crfService.otherStudyCrfGroupElements(id, count);
	}
	
	@RequestMapping(value = "/crfRuleSave", method = RequestMethod.POST)
	public String crfRuleSave(ModelMap model,
			HttpServletRequest request,
			HttpSession session,
			RedirectAttributes redirectAttributes) {
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		String username = request.getSession().getAttribute("userName").toString();
//		String ruleName = crfService.saveStudyCrfRule(username, request, sm);
//		if(ruleName != null)			
//			redirectAttributes.addFlashAttribute("pageMessage", "Rule Saved Succfully");
//		else
//			redirectAttributes.addFlashAttribute("pageMessage", "Faild to save Rule");
		return "redirect:/buildStdyCrf/crfRule";
	}
	
	@RequestMapping(value="/crfRuleChangeStatus/{id}", method=RequestMethod.GET)
	public String crfRuleChangeStatus(@PathVariable("id") Long id,
			ModelMap model, RedirectAttributes redirectAttributes) {
		String rule = crfService.studycrfRuleChangeStatus(id);
		if(rule != null)
			redirectAttributes.addFlashAttribute("pageMessage", "Rule '"+rule+"' Status Changed.");
		else
			redirectAttributes.addFlashAttribute("pageMessage", "Change to faild Rule Status");
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		return "redirect:/buildStdyCrf/crfRule";
	}
	
	@RequestMapping(value="/crfDateComparison", method=RequestMethod.GET)
	public String crfDateComparison(ModelMap model, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		model.addAttribute("PageHedding", "E-Form Date Comparison");
		model.addAttribute("activeUrl", "buildStdyCrf/crfDateComparison");
		List<CrfDateComparison> list = crfService.studycrfDateComparisonlistall(sm);
		model.addAttribute("list", list);
		return "studycrfDateComparison.tiles";
	}
	@RequestMapping(value="/crfDateComparisonPage", method=RequestMethod.GET)
	public String crfDateComparisonPage(ModelMap model, HttpServletRequest request, 
			RedirectAttributes redirectAttributes) {
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		model.addAttribute("PageHedding", "E-Form Date Comparison");
		model.addAttribute("activeUrl", "buildStdyCrf/crfDateComparison");
		List<Crf> list = crfService.findAllStudyCrfsForRules(sm);
		model.addAttribute("crfList", list);
		return "studycrfDateComparisonPage.tiles";
	}
	@RequestMapping(value="/crfSectionElements1/{id}", method=RequestMethod.GET)
	public @ResponseBody String crfSectionElements1(@PathVariable("id") Long id) {
		return crfService.studysectionEleSelect1(id);
	}
	
	@RequestMapping(value="/crfGroupElements1/{id}", method=RequestMethod.GET)
	public @ResponseBody String crfGroupElements1(@PathVariable("id") Long id) {
		return crfService.studygroupEleSelect1(id);
	}
	
	@RequestMapping(value="/crfSectionElements2/{id}", method=RequestMethod.GET)
	public @ResponseBody String crfSectionElements2(@PathVariable("id") Long id) {
		return crfService.studysectionEleSelect2(id);
	}
	
	@RequestMapping(value="/crfGroupElements2/{id}", method=RequestMethod.GET)
	public @ResponseBody String crfGroupElements2(@PathVariable("id") Long id) {
		return crfService.studygroupEleSelect2(id);
	}
	
	@RequestMapping(value="/crfSectionElements3/{id}", method=RequestMethod.GET)
	public @ResponseBody String crfSectionElements3(@PathVariable("id") Long id) {
		return crfService.studysectionEleSelect3(id);
	}
	
	@RequestMapping(value="/crfGroupElements3/{id}", method=RequestMethod.GET)
	public @ResponseBody String crfGroupElements3(@PathVariable("id") Long id) {
		return crfService.studygroupEleSelect3(id);
	}
	@RequestMapping(value="/crfSectionElements4/{id}", method=RequestMethod.GET)
	public @ResponseBody String crfSectionElements4(@PathVariable("id") Long id) {
		return crfService.studysectionEleSelect4(id);
	}
	
	@RequestMapping(value="/crfGroupElements4/{id}", method=RequestMethod.GET)
	public @ResponseBody String crfGroupElements4(@PathVariable("id") Long id) {
		return crfService.studygroupEleSelect4(id);
	}
	
	@RequestMapping(value = "/crfDateComparisonSave", method = RequestMethod.POST)
	public String crfDateComparisonSave(ModelMap model,
			HttpServletRequest request,
			HttpSession session,
			RedirectAttributes redirectAttributes) {
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		String username = request.getSession().getAttribute("userName").toString();
		String ruleName = crfService.saveStudyCrfDateComparison(sm,username, request);
		if(ruleName != null)			
			redirectAttributes.addFlashAttribute("pageMessage", "E-Form Date Comparison Saved Succfully");
		else
			redirectAttributes.addFlashAttribute("pageMessage", "Faild to save E-Form Date Comparison");
		return "redirect:/buildStdyCrf/crfDateComparison";
	}
	
	@RequestMapping(value="/crfDateComparisonStatus/{id}", method=RequestMethod.GET)
	public String crfDateComparisonStatus(@PathVariable("id") Long id,
			ModelMap model, RedirectAttributes redirectAttributes) {
		String rule = crfService.studycrfDateComparisonChangeStatus(id);
		if(rule != null)
			redirectAttributes.addFlashAttribute("pageMessage", "Date Comparison '"+rule+"' Status Changed.");
		else
			redirectAttributes.addFlashAttribute("pageMessage", "Change to faild Date Comparison Status");
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		return "redirect:/buildStdyCrf/crfDateComparison";
	}
}
