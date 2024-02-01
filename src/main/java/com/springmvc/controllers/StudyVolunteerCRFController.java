package com.springmvc.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.covide.crf.dto.Crf;
import com.covide.crf.dto.CrfGroupDataRows;
import com.covide.crf.dto.CrfSection;
import com.covide.crf.service.CrfService;
import com.springmvc.dao.EmployeeDao;
import com.springmvc.model.PeriodCrfs;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyPeriodMaster;
import com.springmvc.model.StudySite;
import com.springmvc.model.SubjectStatus;
import com.springmvc.model.Volunteer;
import com.springmvc.model.VolunteerPeriodCrf;
import com.springmvc.model.VolunteerPeriodCrfStatus;
import com.springmvc.service.StudyService;
import com.springmvc.service.UserService;
import com.springmvc.service.UserWiseStudiesAsignService;

@Controller
@RequestMapping("/studyexe")
@PropertySource(value = { "classpath:application.properties" })
public class StudyVolunteerCRFController {
	@Autowired
	StudyService studyService;

	@Autowired
	UserService userService;

	@Autowired
	UserWiseStudiesAsignService userWiseStudiesAsignService;

	@Autowired
	EmployeeDao employeeDao;

	@Autowired
	CrfService crfService;
	
	

	@Autowired
    private Environment environment;
	@RequestMapping(value = "/viewSubjectPeriodCrfs/{volid}/{peiodId}", method = RequestMethod.GET)
	public String viewSubjectInfo(@PathVariable("volid") Long volId, @PathVariable("peiodId") Long peiodId,
			HttpServletRequest request, ModelMap model, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		model.addAttribute("study", sm);
		StudyPeriodMaster period = studyService.studyPeriodMaster(peiodId);
		Volunteer vol = studyService.volunteer(volId);
		List<VolunteerPeriodCrf> vpcl = studyService.volunteerPeriodCrfList(period, vol);
		boolean withDraw = false;
		SubjectStatus ss  = studyService.subjectStatus(period, vol);
		if(ss == null)
		for(VolunteerPeriodCrf vpc : vpcl) {
			if(vpc.getStdCrf().getExitCrf(). equals("Yes")) {
				withDraw = true;
			}
		}else
			model.addAttribute("ss", ss);
		model.addAttribute("sm", sm);
		model.addAttribute("period", period);
		model.addAttribute("vol", vol);
		model.addAttribute("vpcl", vpcl);
		model.addAttribute("PageHedding", "Subject Matrix");
		model.addAttribute("activeUrl", "site/subjectMatrix");
		model.addAttribute("withDraw", withDraw);
		return "viewSubjectPeriodCrfs.tiles";
	}
	
	@RequestMapping(value = "/subjectStatus", method = RequestMethod.POST)
	public String subjectStatus(HttpServletRequest request, ModelMap model, RedirectAttributes redirectAttributes) {
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		model.addAttribute("study", sm);
		Long siteId = (Long) request.getSession().getAttribute("siteId");
		StudySite site = studyService.studySiteId(siteId);
		StudyPeriodMaster period = studyService.studyPeriodMaster(Long.parseLong(request.getParameter("periodId")));
		Volunteer vol = studyService.volunteer(Long.parseLong(request.getParameter("volId")));
		String subjectStatus = request.getParameter("subjectStatus"); 
		if(subjectStatus != null && subjectStatus.equals("withDraw")) {
			try {
				SubjectStatus ss  = new SubjectStatus();
				ss.setStudy(sm);
				ss.setVol(vol);
				ss.setPeriod(period);
				ss.setWithDraw("Yes");
				ss.setSite(site);
				String phaseStatus = request.getParameter("phaseSatus");
				if(phaseStatus != null && phaseStatus.equals("Continue")) {
					ss.setPhaseContinue("Continue");
				}
				ss.setCreatedOn(new Date());
				ss.setCreatedBy(request.getSession().getAttribute("userName").toString());
				studyService.saveSubjectStatus(ss);
				redirectAttributes.addFlashAttribute("pageMessage", "Subject Status Changed Successfully");
			}catch (Exception e) {
				redirectAttributes.addFlashAttribute("pageMessage", "Faild to Change Subject Status.");
			}
		}
		
		return "redirect:/studyexe/viewSubjectPeriodCrfs/"+Long.parseLong(request.getParameter("volId"))+"/"+Long.parseLong(request.getParameter("periodId"));
	}
	
	@RequestMapping(value = "/subjectPeriodCrfsDataEntryView/{vpcId}", method = RequestMethod.GET)
	public String subjectPeriodCrfsDataEntryView(@PathVariable("vpcId") Long vpcId, HttpServletRequest request,
			ModelMap model, RedirectAttributes redirectAttributes) {
		model.addAttribute("PageHedding", "Subject Matrix");
		model.addAttribute("activeUrl", "site/subjectMatrix");
		String result= "stdCrfDataEntryView.tiles";
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		String userRole = (String) request.getSession().getAttribute("userRole");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		model.addAttribute("study", sm);
		VolunteerPeriodCrf vpc = studyService.volunteerPeriodCrf(vpcId);
		StudyPeriodMaster period = vpc.getPeriod();
		Volunteer vol = vpc.getVol();

		PeriodCrfs pcrf = vpc.getStdCrf();
		Crf crf = crfService.getCrfForView(pcrf.getCrfId());
		
		
		Map<String, String> crfData = null;
		for(CrfSection sec : crf.getSections()) {
			if(sec.getRoles().equals("ALL")) {
				sec.setAllowDataEntry(true);
			}else {
				String[] roles = sec.getRoles().split("\\,");
				List<String> rolesList = new ArrayList<>();
				for(String role : roles) rolesList.add(role.trim());
				if(rolesList.contains(userRole))
					sec.setAllowDataEntry(true);
			}
		}
		if(!vpc.getCrfStatus().equals("NOT STARTED")) {
			crfData = crfService.getStudyCrfData(crf, vpc);
			model.addAttribute("crfData", crfData);	
			Map<String, String> tempSecdata = new HashMap<>();
			Map<String, String> tempGropdata = new HashMap<>();
			for(Map.Entry<String, String> e : crfData.entrySet()) {
				String sr = e.getKey();
				if(sr.substring(0, sr.indexOf("_")+1).equals("g_")) {
					sr = sr.replace("g_", "");
					sr = sr.substring(sr.indexOf("_")+1);
					sr = "g_"+sr;
					tempGropdata.put(sr, e.getValue());
				}else {
					sr = sr.substring(sr.indexOf("_")+1);
					tempSecdata.put(sr, e.getValue());
				}
				System.out.println(e.getKey() + "  ------------------- >   " + sr);
				
			}
			model.addAttribute("tempSecdata", tempSecdata);
			model.addAttribute("tempGropdata", tempGropdata);
			result= "stdCrfDataEntryUpdate.tiles";
			Map<Long, CrfGroupDataRows> groupoInfo = crfService.studyCrfGroupDataRows(vpc);
			for(CrfSection sec : crf.getSections()) {
				if(sec.getGroup() != null) {
					CrfGroupDataRows ginfo = groupoInfo.get(sec.getGroup().getId());
					if(ginfo != null) {
						sec.getGroup().setDisplayedRows(ginfo.getNoOfRows());
					}else
						sec.getGroup().setDisplayedRows(sec.getGroup().getMinRows());
				}
			}
		}
		Map<String, String> caliculationFieldSec = new HashMap<String, String>();
		caliculationFieldSec = crfService.caliculationFieldSec(crf);
		model.addAttribute("caliculationFieldSec", caliculationFieldSec);
		/*for(StudyCrfSection sec : crf.getSections()) {
			if(sec.getElement() == null || sec.getElement().size() == 0)Crfelse {
				for(StudyCrfSectionElement ele : sec.getElement()) {
					if(caliculationFieldSec.containsKey(ele.getName())) {
						ele.setCalculation(true);
					}
				}
			}
			StudyCrfGroup group = sec.getGroup();
			if(group != null) {
				if(group.getElement() == null || group.getElement().size()==0) {
				}else {
					for(StudyCrfGroupElement ele : group.getElement()) {
						if(caliculationFieldSec.containsKey(ele.getName())) {
							ele.setCalculation(true);
						}
					}
				}
			}
		}*/
		
		model.addAttribute("crf", crf); // study crf object
		model.addAttribute("sm", sm); // study object
		model.addAttribute("period", period);  // period object
		model.addAttribute("vol", vol);  // voluteer object
		model.addAttribute("vpc", vpc);
		model.addAttribute("vpcId", vpcId); // study period volu
		
		Map<String, String> allElementIdsTypesJspStd = new HashMap<String, String>() ; // key-id and value is type
		// requied field element id-key in list 
		Map<String, String> requiredElementIdInJsp = new HashMap<String, String>() ; // key-id and value is type
		Map<String, String> pattrenIdsAndPattren = new HashMap<String, String>(); // key-id and value is type@pattren
		
		if(vpc.getCrfStatus().equals("NOT STARTED") ) {
			allElementIdsTypesJspStd = crfService.allElementIdsTypesJspStd(crf, vpc);
			requiredElementIdInJsp = crfService.requiredElementIdInJspStd(crf, vpc, userRole);
			pattrenIdsAndPattren = crfService.pattrenIdsAndPattrenStd(crf, vpc);
		}else if(vpc.getCrfStatus().equals("IN PROGRESS")) {
			allElementIdsTypesJspStd = crfService.allElementIdsTypesJspStdUpdate(crf, vpc);
			requiredElementIdInJsp = crfService.requiredElementIdInJspStdUpdate(crf, vpc, userRole);
			pattrenIdsAndPattren = crfService.pattrenIdsAndPattrenStdUpdate(crf, vpc);
		}
		
		model.addAttribute("requiredElementIdInJsp", requiredElementIdInJsp);
		model.addAttribute("allElementIdsTypesJspStd", allElementIdsTypesJspStd);
		model.addAttribute("pattrenIdsAndPattren", pattrenIdsAndPattren);

		
		//sswamy 
		/*
		//E-Form rules filed
		List<StudyCrfRule> rules = crfService.studyCrfRuleWithCrfAndSubElements(crf);
		
		List<RulesInfoTemp> rulesFieldSec = new ArrayList<>();
		rulesFieldSec = crfService.rulesFieldsSecMap(crf, rules);
		model.addAttribute("rulesFieldSec", rulesFieldSec);
		
		Map<String, String> rulesFieldGroup = new HashMap<String, String>();
		rulesFieldGroup = crfService.rulesFieldsGroupMap(crf, rules);
		model.addAttribute("rulesFieldGroup", rulesFieldGroup);
	
		//E-Form rules date filed
		
		Long id = crf.getId();
		List<StudyCrfDateComparison> rulesdate = crfService.studyCrfDateComparisonAndSubElements(crf);
		List<String> rulesFieldSecdate = new ArrayList<>();//1217_screeDate
		List<String> rulesFieldGroupdate = new ArrayList<>();//g_218_sno3_1
		
		Map<Long, List<String>> map = new HashMap<>();
		
		Map<String, Long> map2 = new HashMap<>();
		for(StudyCrfDateComparison  c : rulesdate) {
			List<String> rulesFieldSecdate2 = new ArrayList<>();//1217_screeDate
			List<String> rulesFieldGroupdate2 = new ArrayList<>();//g_218_sno3_1
			
			if(c.getCrf1().getId() == id) {
				if(c.getSecEle1() != null) rulesFieldSecdate2.add(c.getSecEle1().getId()+"_"+c.getSecEle1().getName());
				if(c.getGroupEle1() != null) rulesFieldGroupdate2.add("g_"+c.getGroupEle1().getId()+"_"+c.getRowNo1());
			}
			if(c.getCrf2().getId() == id) {
				if(c.getSecEle2() != null)	rulesFieldSecdate2.add(c.getSecEle2().getId()+"_"+c.getSecEle2().getName());
				if(c.getGroupEle2() != null)	rulesFieldGroupdate2.add("g_"+c.getGroupEle2().getId()+"_"+c.getRowNo2());
			}
			if(c.getCrf3().getId() == id) {
				if(c.getSecEle3() != null) rulesFieldSecdate2.add(c.getSecEle3().getId()+"_"+c.getSecEle3().getName());
				if(c.getGroupEle3() != null)	rulesFieldGroupdate2.add("g_"+c.getGroupEle3().getId()+"_"+c.getRowNo3());
			}
			if(c.getCrf4().getId() == id) {
				if(c.getSecEle4() != null)	rulesFieldSecdate2.add(c.getSecEle4().getId()+"_"+c.getSecEle4().getName());
				if(c.getGroupEle4() != null)	rulesFieldGroupdate2.add("g_"+c.getGroupEle4().getId()+"_"+c.getRowNo4());
			}
			
			rulesFieldSecdate.addAll(rulesFieldSecdate2);
			rulesFieldSecdate.addAll(rulesFieldGroupdate2);
			
			
			List<String> sl = new ArrayList<>();
			for(String s : rulesFieldSecdate2) {
				sl.add(s);
				map2.put(s, c.getId());
			}
			for(String s : rulesFieldGroupdate2) {
				sl.add(s);
				map2.put(s, c.getId());
			}
			map.put(c.getId(), sl);
		}
		
		// individuval elemnts
		model.addAttribute("rulesFieldSecdate", rulesFieldSecdate);
		model.addAttribute("rulesFieldGroupdate", rulesFieldGroupdate);
		
		//key element , value id
		model.addAttribute("map2", map2);
		//key id, value list of elements
		model.addAttribute("map", map);
		*/
		return result;
	}

	@RequestMapping(value = "/studyCrfGroupElement/{groupId}/{rowNo}", method = RequestMethod.GET)
	public @ResponseBody String studyCrfGroupElement(@PathVariable("groupId") Long groupId,
			@PathVariable("rowNo") int rowNo) {
		try {
			return crfService.studyGroupElement(groupId, rowNo);
		}catch (Exception e) {
			e.printStackTrace();
			return "";
		} 
	}
	
	@RequestMapping(value = "/studyCrfGroupElementReuiedEleIds/{groupId}/{rowNo}", method = RequestMethod.GET)
	public @ResponseBody String studyCrfGroupElementReuiedEleIds(@PathVariable("groupId") Long groupId,
			@PathVariable("rowNo") int rowNo) {
		try {
			return crfService.requiredGoupElementIdInJsp(groupId, rowNo);
		}catch (Exception e) {
			e.printStackTrace();
			return "";
		} 
	}
	
	public static Set<Long> dataEntry = new HashSet<>();
	@RequestMapping(value = "/studyCrfSave", method = RequestMethod.POST)
	public String studyCrfSave(HttpServletRequest request,
			ModelMap model, RedirectAttributes redirectAttributes) {
	
		Long vpcId = Long.parseLong(request.getParameter("vpcId")); 
		if(dataEntry.contains(vpcId)) {
			redirectAttributes.addFlashAttribute("pageMessage", "Crf Data Entry Inprogress");
			return "redirect:/studyexe/subjectPeriodCrfsDataEntryView/"+vpcId;
		}
		String status = "";
		try {
			dataEntry.add(vpcId);
			Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
			StudyMaster sm = studyService.findByStudyId(activeStudyId);
			model.addAttribute("study", sm);
			VolunteerPeriodCrf vpc = studyService.volunteerPeriodCrf(vpcId);
			PeriodCrfs pcrf = vpc.getStdCrf();
			Crf crf = crfService.getCrfForView(pcrf.getCrfId());
			boolean flag = false;
			if(vpc.getCrfStatus().equals("NOT STARTED")) {
				//crf data entry logic
				status =  crfService.studyCrfDataEntry(crf, vpc, request);
				if(status.equals("success") || status.equals("IN PROGRESS") ) {
					redirectAttributes.addFlashAttribute("pageMessage", "Crf Saved Successfully.");
					flag = true;
				}else if(status.equals("IN REVIEW")) {
					redirectAttributes.addFlashAttribute("pageMessage", "Crf Saved Successfully.");
					flag = true;
				}else {
					redirectAttributes.addFlashAttribute("pageMessage", "Faild to save Crf Data.");
				}
			}else {
				//crf data entry logic
				status =  crfService.studyCrfDataUpdate(crf, vpc, request);
				if(status.equals("success") || status.equals("IN PROGRESS")) {
					redirectAttributes.addFlashAttribute("pageMessage", "Crf Saved Successfully.");
					flag = true;
				}else if(status.equals("IN REVIEW")) {
					redirectAttributes.addFlashAttribute("pageMessage", "Crf Saved Successfully.");
					flag = true;
				}else {
					redirectAttributes.addFlashAttribute("pageMessage", "Faild to save Crf Data.");
				}
			}
			VolunteerPeriodCrfStatus vpcs = studyService.volunteerPeriodCrfStatus(vpc.getPeriod(), vpc.getVol());
			if(flag) {
				if(vpcs.getStatus().equals("NOT STARTED")) {
					vpcs.setStatus("IN PROGRESS");
					studyService.updateVolunteerPeriodCrfStatus(vpcs);
				}
			}
			dataEntry.remove(vpcId);
			List<VolunteerPeriodCrf> list = studyService.volunteerPeriodCrfList(vpc.getPeriod(), vpc.getVol());
			boolean reviewflag = true;
			for(VolunteerPeriodCrf vpc1 : list ) {
				if(!vpc1.getCrfStatus().equals("IN REVIEW"))
					reviewflag = false;
			}
			if(reviewflag) {
				vpcs.setStatus("IN REVIEW");
				studyService.updateVolunteerPeriodCrfStatus(vpcs);
			}
//			if(status.equals("IN REVIEW")) {
//				return "redirect:/studyexe/viewSubjectPeriodCrfs/"+vpc.getVol().getId()+"/"+vpc.getPeriod().getId();
//			}else
//				return "redirect:/studyexe/subjectPeriodCrfsDataEntryView/"+vpcId;
			return "redirect:/studyexe/viewSubjectPeriodCrfs/"+vpc.getVol().getId()+"/"+vpc.getPeriod().getId();
		}catch (Exception e) {
			e.printStackTrace();
			dataEntry.remove(vpcId);
			return "redirect:/studyexe/subjectPeriodCrfsDataEntryView/"+vpcId;
		}
	}
	
//	$("#crfId").val()+"/"+$("#vpcId").val()+"/"+dateRuleDbId+"/"+values
	
	@RequestMapping(value = "/studyCrfSecEleDateRuelCheck/{crfId}/{dateRuleDbId}/{values}", method = RequestMethod.GET)
	public @ResponseBody String studyCrfSecEleDateRuelCheck(
			@PathVariable("crfId") Long crfId,
//			@PathVariable("vpcId") Long vpcId,
			@PathVariable("dateRuleDbId") Long dateRuleDbId,
			@PathVariable("values") String values) {
		try {
			return crfService.studyCrfSecDateRuelCheck(crfId,  dateRuleDbId, values);
		}catch (Exception e) {
			e.printStackTrace();
			return "";
		} 
	}
	@RequestMapping(value = "/crfEleRuelCheck/{crfId}/{dbid}/{id}/{value}/{values}", method = RequestMethod.GET)
	
	public @ResponseBody String crfEleRuelCheck(
			@PathVariable("crfId") Long crfId,
//			@PathVariable("testId") Long testId,
			@PathVariable("dbid") Long dbid,
			@PathVariable("id") String id,
			@PathVariable("value") String value,
			@PathVariable("values") String values,  HttpServletRequest request) {
		try {
			if(values.equals("nill")) values = null;
			return crfService.crfRuelCheck(crfId, dbid, id, value, values);
		}catch (Exception e) {
			e.printStackTrace();
			return "";
		} 
	}
	@RequestMapping(value = "/studyCrfSecEleRuelCheck/{crfId}/{dbid}/{id}/{value}/{values}/{oeles}", method = RequestMethod.GET)
	public @ResponseBody String studyCrfSecEleRuelCheck(
			@PathVariable("crfId") Long crfId,
//			@PathVariable("vpcId") Long vpcId,
			@PathVariable("dbid") Long dbid,
			@PathVariable("id") String id,
			@PathVariable("value") String value,
			@PathVariable("values") String values,
			@PathVariable("oeles") String oeles, HttpServletRequest request) {
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		try {
			if(oeles.equals("nill")) oeles = null;
			return crfService.studyCrfSecRuelCheck(crfId,  dbid, id, value, values, sm, oeles);
		}catch (Exception e) {
			e.printStackTrace();
			return "";
		} 
	}
	
	@RequestMapping(value = "/studyCrfGroupEleRuelCheck/{crfId}/{dbid}/{id}/{value}/{values}", method = RequestMethod.GET)
	public @ResponseBody String studyCrfGroupEleRuelCheck(
			@PathVariable("crfId") Long crfId,
//			@PathVariable("vpcId") Long vpcId,
			@PathVariable("dbid") Long dbid,
			@PathVariable("id") String id,
			@PathVariable("value") String value,
			@PathVariable("values") String values, HttpServletRequest request) {
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		try {
			return crfService.studyCrfGroupRuelCheck(crfId, dbid, id, value, values, sm);
		}catch (Exception e) {
			e.printStackTrace();
			return "";
		} 
	}
	
	@RequestMapping(value = "/studyCrfElementCalculationValue/{crfId}/{resultFiled}/{fieldAndVales}", 
			method = RequestMethod.GET)
	public @ResponseBody String studyCrfElementCalculationValue(
			@PathVariable("crfId") Long crfId,
			@PathVariable("resultFiled") String resultFiled,
			@PathVariable("fieldAndVales") String fieldAndVales,
			HttpServletRequest request) {
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		try {
			return crfService.studyCrfElementCalculationValue(sm, crfId, resultFiled, fieldAndVales);
		}catch (Exception e) {
			e.printStackTrace();
			return "";
		} 
	}
	
}
