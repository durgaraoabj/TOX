package com.springmvc.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.covide.crf.dto.Crf;
import com.covide.crf.dto.CrfDescrpencyAudit;
import com.covide.crf.dto.CrfGroupDataRows;
import com.covide.crf.dto.CrfSection;
import com.covide.crf.service.CrfService;
import com.springmvc.dao.UserWiseStudiesAsignDao;
import com.springmvc.model.PeriodCrfs;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyPeriodMaster;
import com.springmvc.model.StudySiteSubject;
import com.springmvc.model.SubjectStatus;
import com.springmvc.model.Volunteer;
import com.springmvc.model.VolunteerPeriodCrf;
import com.springmvc.model.VolunteerPeriodCrfStatus;
import com.springmvc.service.DashBordService;
import com.springmvc.service.StudyService;
import com.springmvc.service.UserService;
import com.springmvc.service.UserWiseStudiesAsignService;

@Controller
@RequestMapping("/globalStudyView")
@PropertySource(value = { "classpath:application.properties" })
public class GlobalStudyViewContoller {

	@Autowired
	DashBordService dashBordService;
	
	@Autowired
    private Environment environment;
	
	@Autowired
	StudyService studyService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserWiseStudiesAsignService userWiseStudiesAsignService;
	
	@Autowired
	UserWiseStudiesAsignDao userWiseStudiesAsignDao; 
	
	@Autowired
	CrfService crfService;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String globalStudyView(HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
		try {
			String currentStudy = request.getSession().getAttribute("activeStudyNo").toString();
			if(currentStudy!=null && !currentStudy.equals("")) {
				model.addAttribute("PageHedding", "Global Study View");
				model.addAttribute("activeUrl", "/globalStudyView/");
				StudyMaster sm =  studyService.findByStudyId(Long.parseLong(request.getSession().getAttribute("activeStudyId").toString()));
				model.addAttribute("study", sm);
				
				List<Volunteer> vlist = new ArrayList<>(); 
				List<Volunteer> volList = studyService.studyVolunteerList(sm);
				List<StudyPeriodMaster> plist= studyService.allStudyPeriods(sm);
				Map<Long, Volunteer> volMap = new HashMap<>();
				
				for(Volunteer v : volList) {
					volMap.put(v.getId(), v);
//					v.setPeriods(plist);
					boolean continueState = true;
					for(StudyPeriodMaster sp: plist) {
						StudyPeriodMaster sp1 = new StudyPeriodMaster();
						sp1.setId(sp.getId());
						sp1.setName(sp.getName());
						sp1.setStartDate(sp.getStartDate());
						sp1.setEndDate(sp.getEndDate());
						if(continueState) {
							SubjectStatus ss = studyService.subjectStatus(sp, v);
							if(ss != null && ss.getPhaseContinue().equals("No"))
								continueState = false;
						}else {
							sp1.setPhaseContinue("No");
						}
						VolunteerPeriodCrfStatus vpcs = studyService.volunteerPeriodCrfStatus(sp, v);
						if(vpcs != null)
							sp1.setVpcs(vpcs);
						v.getPeriods().add(sp1);					
//						List<VolunteerPeriodCrf> vpcl  = studyService.volunteerPeriodCrfList(sp, v);
						
					}
					
					vlist.add(v);
				}
				
//				if(site.getSubjects() == volList.size()) {
//					model.addAttribute("createSubject", "No");
//				}else
//					model.addAttribute("createSubject", "Yes");
				model.addAttribute("plist", plist);
				model.addAttribute("vlist", vlist);
				return "globalStudyView.tiles";
			}else {
				redirectAttributes.addFlashAttribute("pageWarning", "Please select study");
				return "redirect:/dashboard/userWiseActiveStudies";
			}
		}catch(NullPointerException npe) {
			if(request.getSession().getAttribute("userRole").toString().equalsIgnoreCase("ADMIN"))
				return "adminDashBoard.tiles";
			else {
				redirectAttributes.addFlashAttribute("pageWarning", "Please select study");
				return "redirect:/dashboard/userWiseActiveStudies";
			}
		}
	}
	
	@RequestMapping(value = "/viewSubjectPeriodCrfs/{volid}/{peiodId}", method = RequestMethod.GET)
	public String viewSubjectInfo(@PathVariable("volid") Long volId, @PathVariable("peiodId") Long peiodId,
			HttpServletRequest request, ModelMap model, RedirectAttributes redirectAttributes) {
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		model.addAttribute("study", sm);
		StudyPeriodMaster period = studyService.studyPeriodMaster(peiodId);
		Volunteer vol = studyService.volunteer(volId);
		List<VolunteerPeriodCrf> vpcl = studyService.volunteerPeriodCrfList(period, vol);
		model.addAttribute("sm", sm);
		model.addAttribute("period", period);
		model.addAttribute("vol", vol);
		model.addAttribute("vpcl", vpcl);
		model.addAttribute("PageHedding", "Global Study View");
		model.addAttribute("activeUrl", "/globalStudyView/");
		return "globalStudyViewSubjectPeriodCrfs.tiles";
	}
	
	@RequestMapping(value = "/subjectPeriodCrfsDataEntryView/{vpcId}", method = RequestMethod.GET)
	public String subjectPeriodCrfsDataEntryView(@PathVariable("vpcId") Long vpcId, HttpServletRequest request,
			ModelMap model, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		String result= "globalStudyCrfDataEntryReview.tiles";
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
		
		if(vpc.getCrfStatus().equals("COMPLETED")) {
			model.addAttribute("CrfStatus", "COMPLETED");
		}else {
			model.addAttribute("CrfStatus", "IN-REVIEW");
		}
		
		crfData = crfService.getStudyCrfData(crf, vpc);
		model.addAttribute("crfData", crfData);	
		
		Map<Long, CrfGroupDataRows> groupoInfo = crfService.studyCrfGroupDataRows(vpc);
		for(CrfSection sec : crf.getSections()) {
			if(sec.getRoles().equals("ALL")) {
//			sswamy	sec.setAllowDataEntry(true);
			}else {
				String[] roles = sec.getRoles().split("\\,");
				List<String> rolesList = new ArrayList<>();
				for(String role : roles) rolesList.add(role.trim());
//				sswamy
//				if(rolesList.contains(userRole))
//					sec.setAllowDataEntry(true);
			}
			if(sec.getGroup() != null) {
				CrfGroupDataRows ginfo = groupoInfo.get(sec.getGroup().getId());
				if(ginfo != null) {
					sec.getGroup().setDisplayedRows(ginfo.getNoOfRows());
				}else
					sec.getGroup().setDisplayedRows(sec.getGroup().getMinRows());
			}
		}
		
		model.addAttribute("crf", crf); // study crf object
		model.addAttribute("sm", sm); // study object
		model.addAttribute("period", period);  // period object
		model.addAttribute("vol", vol);  // voluteer object
		model.addAttribute("vpc", vpc);
		model.addAttribute("vpcId", vpcId); // study period volu
		
		
		List<String> descrepencyUser =  crfService.userdiscrepencyInfo(crf, vpc);
		List<String> descrepencyReviewer =  crfService.reviewerdiscrepencyInfo(crf, vpc);
		List<String> descrepencyClosed =  crfService.closeddiscrepencyInfo(crf, vpc);
		List<String> descrepencyOnHold =  crfService.onHolddiscrepencyInfo(crf, vpc);
		List<String> descrepencyAll =  crfService.alldiscrepencyInfo(crf, vpc);
		List<String> descrepencyOpen =  crfService.opendiscrepencyInfo(crf, vpc);
		if(descrepencyAll.size() == descrepencyClosed.size())
			model.addAttribute("descrepencyAllClosed", "Yes");
		else
			model.addAttribute("descrepencyAllClosed", "No");
		
		model.addAttribute("descrepencyUser", descrepencyUser);
		model.addAttribute("descrepencyReviewer", descrepencyReviewer);
		model.addAttribute("descrepencyClosed", descrepencyClosed);
		model.addAttribute("descrepencyOnHold", descrepencyOnHold);
		model.addAttribute("descrepencyAll", descrepencyAll);
		model.addAttribute("descrepencyOpen", descrepencyOpen);
		Map<String, String> allElementIdsTypesJspStd = new HashMap<String, String>() ; // key-id and value is type
		// requied field element id-key in list 
		Map<String, String> requiredElementIdInJsp = new HashMap<String, String>() ; // key-id and value is type
		Map<String, String> pattrenIdsAndPattren = new HashMap<String, String>(); // key-id and value is type@pattren
		
		if(vpc.getCrfStatus().equals("IN PROGRESS")) {
			allElementIdsTypesJspStd = crfService.allElementIdsTypesJspStdUpdate(crf, vpc);
			requiredElementIdInJsp = crfService.requiredElementIdInJspStdUpdate(crf, vpc);
			pattrenIdsAndPattren = crfService.pattrenIdsAndPattrenStdUpdate(crf, vpc);
			
			model.addAttribute("requiredElementIdInJsp", requiredElementIdInJsp);
			model.addAttribute("allElementIdsTypesJspStd", allElementIdsTypesJspStd);
			model.addAttribute("pattrenIdsAndPattren", pattrenIdsAndPattren);
		}
		
		
		//E-Form rules filed
		//sswamy List<StudyCrfRule> rules = crfService.studyCrfRuleWithCrfAndSubElements(crf);
		
		//sswamy List<RulesInfoTemp> rulesFieldSec = new ArrayList<>();
		//sswamy rulesFieldSec = crfService.rulesFieldsSecMap(crf, rules);
		//sswamy model.addAttribute("rulesFieldSec", rulesFieldSec);
		
		//sswamy Map<String, String> rulesFieldGroup = new HashMap<String, String>();
		//sswamy rulesFieldGroup = crfService.rulesFieldsGroupMap(crf, rules);
		//sswamy model.addAttribute("rulesFieldGroup", rulesFieldGroup);
		

		return result;
	}
	
	@RequestMapping(value="/studyAuditLog", method=RequestMethod.GET)
	public String studyAuditLog(HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		try {
			String currentStudy = request.getSession().getAttribute("activeStudyNo").toString();
			if(currentStudy!=null && !currentStudy.equals("")) {
				model.addAttribute("PageHedding", "Study Audit Log");
				model.addAttribute("activeUrl", "/globalStudyView/studyAuditLog");
				StudyMaster sm =  studyService.findByStudyId(Long.parseLong(request.getSession().getAttribute("activeStudyId").toString()));
				model.addAttribute("study", sm);
				
				List<StudySiteSubject> list = studyService.studySiteSubjectList(sm);
				model.addAttribute("list", list);
				return "studyAuditLog.tiles";
			}else {
				redirectAttributes.addFlashAttribute("pageWarning", "Please select study");
				return "redirect:/dashboard/userWiseActiveStudies";
			}
		}catch(NullPointerException npe) {
			if(request.getSession().getAttribute("userRole").toString().equalsIgnoreCase("ADMIN"))
				return "adminDashBoard.tiles";
			else {
				redirectAttributes.addFlashAttribute("pageWarning", "Please select study");
				return "redirect:/dashboard/userWiseActiveStudies";
			}
		}
	}
	
	@RequestMapping(value="/subAuditLogs/{id}", method=RequestMethod.GET)
	public String subAuditLogs(@PathVariable("id") Long id, HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
		model.addAttribute("PageHedding", "Study Audit Log");
		model.addAttribute("activeUrl", "/globalStudyView/studyAuditLog");
		try {
			String currentStudy = request.getSession().getAttribute("activeStudyNo").toString();
			StudyMaster sm =  studyService.findByStudyId(Long.parseLong(request.getSession().getAttribute("activeStudyId").toString()));
			
			StudySiteSubject sss = studyService.studySiteSubject(id);
			model.addAttribute("sss", sss);
				
			List<VolunteerPeriodCrfStatus> vpcsList = studyService.volunteerPeriodCrfStatus(sss.getVol());
			model.addAttribute("vpcsList", vpcsList);
			
			List<CrfDescrpencyAudit> descrpencyList = studyService.studyCrfDescrpencyAudit(sss.getVol());
			model.addAttribute("descrpencyList", descrpencyList);
			
			List<SubjectStatus> sslist = studyService.subjectStatusForVol(sss.getVol());
			model.addAttribute("sslist", sslist);
			
			return "subAuditLogs.tiles";
			
		}catch(NullPointerException npe) {
			
			redirectAttributes.addFlashAttribute("pageWarning", "Please Try after some Time");
			return "redirect:/dashboard/userWiseActiveStudies";
		}
	}
}
