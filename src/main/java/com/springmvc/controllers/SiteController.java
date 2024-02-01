package com.springmvc.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.covide.crf.service.CrfService;
import com.springmvc.model.StudyGroup;
import com.springmvc.model.StudyGroupClass;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyPeriodMaster;
import com.springmvc.model.StudySite;
import com.springmvc.model.StudySiteSubject;
import com.springmvc.model.SubjectStatus;
import com.springmvc.model.Volunteer;
import com.springmvc.model.VolunteerPeriodCrfStatus;
import com.springmvc.service.StudyService;
import com.springmvc.service.UserService;
import com.springmvc.service.UserWiseStudiesAsignService;

@Controller
@RequestMapping("/site")
public class SiteController {
	@Autowired
	StudyService studyService;

	@Autowired
	UserService userService;

	@Autowired
	UserWiseStudiesAsignService userWiseStudiesAsignService;

	@Autowired
	CrfService crfService;
	
	@RequestMapping(value = "/groupClassList", method = RequestMethod.GET)
	public String groupClassList(ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		model.addAttribute("study", sm);
		try {
			model.addAttribute("configureStatus", sm.isCrfConfiguation());
		} catch (Exception e) {
			model.addAttribute("configureStatus", false);
		}
		List<StudyGroupClass> groups = studyService.studyGroupClass(sm);
		model.addAttribute("groups", groups);
		return "groupClassList.tiles";

	}
	
	@RequestMapping(value = "/createGroupClass", method = RequestMethod.GET)
	public String createGroupClass(ModelMap model,  HttpServletRequest request, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		model.addAttribute("study", sm);
		model.addAttribute("group", new StudyGroupClass());
		try {
			model.addAttribute("configureStatus", sm.isCrfConfiguation());
		} catch (Exception e) {
			model.addAttribute("configureStatus", false);
		}
		return "createGroupClass.tiles";
	}
	
	@RequestMapping(value="/checkGroupClassName/{groupclass}")
	public @ResponseBody String checkGroupClassName(@PathVariable("groupclass") String groupName, HttpServletRequest request) {
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		boolean flag = studyService.checkGroupClassNameExistOrNot(sm, groupName);
		if(flag)
			return "'"+groupName+"' already exist, please check.";
		else
			return "";
	} 
	
	@RequestMapping(value = "/createGroupClassSave", method = RequestMethod.POST)
	public String createGroupClassSave(StudyGroupClass group, ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		model.addAttribute("study", sm);
		String username = request.getSession().getAttribute("userName").toString();
		group.setCreatedBy(username);
		group.setStudyMaster(sm);
		String name = studyService.crateGroupClass(group);
		if(name != null) {
			redirectAttributes.addFlashAttribute("pageMessage",  "Group Class changed with name " + name);
			return "redirect:/site/groupClassList";
		}else
			redirectAttributes.addFlashAttribute("pageMessage", "Group Class createion Faild");
		return "redirect:/site/createGroupClass";
	}
	
	@RequestMapping(value = "/groupList", method = RequestMethod.GET)
	public String groupList(ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		model.addAttribute("study", sm);
		try {
			model.addAttribute("configureStatus", sm.isCrfConfiguation());
		} catch (Exception e) {
			model.addAttribute("configureStatus", false);
		}
		List<StudyGroup> groups = studyService.studyGroup(sm);
		model.addAttribute("groups", groups);
		return "groupList.tiles";

	}
	
	@RequestMapping(value = "/createGroup", method = RequestMethod.GET)
	public String createGroup(ModelMap model,  HttpServletRequest request, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		model.addAttribute("study", sm);
		model.addAttribute("group", new StudyGroup());
		model.addAttribute("groupClass", studyService.studyGroupClass(sm));
		try {
			model.addAttribute("configureStatus", sm.isCrfConfiguation());
		} catch (Exception e) {
			model.addAttribute("configureStatus", false);
		}
		return "createGroup.tiles";
	}
	@RequestMapping(value="/checkGroupName/{groupclass}/{groupName}")
	public @ResponseBody String checkGroupNameExistOrNot(@PathVariable("groupclass") Long groupclass, @PathVariable("groupName") String groupName) {
		boolean flag = studyService.checkGroupNameExistOrNot(groupclass, groupName);
		if(flag)
			return "'"+groupName+"' already exist, please check.";
		else
			return "";
	} 
	
	@RequestMapping(value = "/createGroupSave", method = RequestMethod.POST)
	public String createGroupSave(StudyGroup group, ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		model.addAttribute("study", sm);
		String username = request.getSession().getAttribute("userName").toString();
		group.setCreatedBy(username);
		group.setStudyMaster(sm);
		StudyGroupClass gc = studyService.studyGroupClassById(Long.parseLong(request.getParameter("gc")));
		group.setGroupClass(gc);
		String name = studyService.crateGrop(group);
		if(name != null) {
			redirectAttributes.addFlashAttribute("pageMessage",  "Group changed with name " + name);
			return "redirect:/site/groupList";
		}else
			redirectAttributes.addFlashAttribute("pageMessage", "Group createion Faild");
		return "redirect:/site/createGroup";
	}
	
	@RequestMapping(value = "/siteList", method = RequestMethod.GET)
	public String siteList(ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		model.addAttribute("study", sm);
		try {
			model.addAttribute("configureStatus", sm.isCrfConfiguation());
		} catch (Exception e) {
			model.addAttribute("configureStatus", false);
		}
		List<StudySite> sites = studyService.studySite(sm);
		model.addAttribute("sites", sites);
		return "siteList.tiles";
		
//		if (sm.isVolConfiguation()) {
//			List<Volunteer> vlist = studyService.studyVolunteerList(sm);
//			model.addAttribute("vlist", vlist);
//			return "volunteerCreationDone.tiles";
//		} else
//			return "volunteerCreation.tiles";
	}
	
	@RequestMapping(value = "/createSite", method = RequestMethod.GET)
	public String createSite(ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		model.addAttribute("study", sm);
		try {
			model.addAttribute("configureStatus", sm.isCrfConfiguation());
		} catch (Exception e) {
			model.addAttribute("configureStatus", false);
		}
		return "createSite.tiles";
	}
	
	@RequestMapping(value="/checkSiteName/{siteName}")
	public @ResponseBody String checkSiteName(@PathVariable("siteName") String siteName, HttpServletRequest request) {
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		boolean flag = studyService.checkSiteNameExistOrNot(sm, siteName);
		if(flag)
			return "'"+siteName+"' already exist, please check.";
		else
			return "";
	} 
	
	
	@RequestMapping(value = "/createSiteSave", method = RequestMethod.POST)
	public String createSiteSave(ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		model.addAttribute("study", sm);
		String username = request.getSession().getAttribute("userName").toString();
		String name = studyService.crateSite(sm, request, username);
		if(name != null) {
			redirectAttributes.addFlashAttribute("pageMessage",  "Site created with name " + name);
			return "redirect:/site/siteList";
		}else
			redirectAttributes.addFlashAttribute("pageMessage", "Site createion Faild");
		return "redirect:/site/createSite";
	}
	
	@RequestMapping(value = "/subjectMatrix", method = RequestMethod.GET)
	public String subjectMatrix(ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm =  studyService.findByStudyId(activeStudyId);
		model.addAttribute("study", sm);
		if(request.getSession().getAttribute("siteId") != null) {
			Long siteId = (Long) request.getSession().getAttribute("siteId");
			StudySite site = studyService.studySiteId(siteId);
			
				List<Volunteer> vlist = new ArrayList<>(); 
				List<Volunteer> volList = studyService.studyVolunteerListWithSite(sm, site);
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
				
				if(site.getSubjects() == volList.size()) {
					model.addAttribute("createSubject", "No");
				}else
					model.addAttribute("createSubject", "Yes");
				model.addAttribute("PageHedding", "Study");
				model.addAttribute("activeUrl", "study/");
				model.addAttribute("plist", plist);
				model.addAttribute("vlist", vlist);
		}else {
			model.addAttribute("pageMessage", "Site Not Active for You.");
			model.addAttribute("createSubject", "No");
		}
		return "subjectMatrixForSite.tiles";
	}
	
	@RequestMapping(value = "/addSubject", method = RequestMethod.GET)
	public String addSubject(ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		model.addAttribute("study", sm);
		try {
			model.addAttribute("configureStatus", sm.isCrfConfiguation());
			
			Map<String, List<StudyGroup>> groupMap = new HashMap<>();
			List<StudyGroup> groups = studyService.studyGroup(sm);
			for(StudyGroup g : groups) {
				List<StudyGroup> g1 = groupMap.get(g.getGroupClass().getGroupName());
				if(g1 == null) g1  =  new ArrayList<>();
				g1.add(g);
				groupMap.put(g.getGroupClass().getGroupName(), g1);
			}
			model.addAttribute("groupMap", groupMap);
		} catch (Exception e) {
			model.addAttribute("configureStatus", false);
		}
		return "createSubject.tiles";
	}
	
	
	@RequestMapping(value = "/saveSubject", method = RequestMethod.POST)
	public String saveSubject(StudySiteSubject subject, ModelMap model, HttpServletRequest request, 
			RedirectAttributes redirectAttributes) {
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		model.addAttribute("study", sm);
		subject.setStudyMaster(sm);
		subject.setCreatedBy(request.getSession().getAttribute("userName").toString());
		Long siteId = (Long) request.getSession().getAttribute("siteId");
		StudySite site = studyService.studySiteId(siteId);
		subject.setSite(site);
		List<Long> groups = new ArrayList<>();
		System.out.println(request.getParameter("group"));
		System.out.println(request.getParameterValues("group"));
		String[] ids = request.getParameterValues("group");
		for(String id : ids )
			groups.add(Long.parseLong(id));
		String name = studyService.createSubject(subject, groups);
		if(name != null) {
			redirectAttributes.addFlashAttribute("pageMessage",  "Subject Created with name " + name);
			return "redirect:/site/subjectMatrix";
		}else
			redirectAttributes.addFlashAttribute("pageMessage", "Subject createion Faild");
		return "redirect:/site/addSubject";
	}
	
	@RequestMapping(value = "/scheduleSubject/{volid}/{periodId}", method = RequestMethod.GET)
	public String scheduleSubject(@PathVariable("volid") Long volid,
			@PathVariable("periodId") Long periodId, 
			ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		Volunteer vol = studyService.volunteer(volid);
		StudyPeriodMaster sp = studyService.studyPeriodMaster(periodId);
		model.addAttribute("volid", volid);
		model.addAttribute("sub", vol.getVolId());
		model.addAttribute("periodId", periodId);
		model.addAttribute("phaseName", sp.getName());
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		model.addAttribute("study", sm);
		try {
			model.addAttribute("configureStatus", sm.isCrfConfiguation());
			
			Map<String, List<StudyGroup>> groupMap = new HashMap<>();
			List<StudyGroup> groups = studyService.studyGroup(sm);
			for(StudyGroup g : groups) {
				List<StudyGroup> g1 = groupMap.get(g.getGroupClass().getGroupName());
				if(g1 == null) g1  =  new ArrayList<>();
				g1.add(g);
				groupMap.put(g.getGroupClass().getGroupName(), g1);
			}
			model.addAttribute("groupMap", groupMap);
		} catch (Exception e) {
			model.addAttribute("configureStatus", false);
		}
		return "scheduleSubject.tiles";
	}
	
	@RequestMapping(value = "/saveScheduleSubject", method = RequestMethod.POST)
	public String saveSubject(ModelMap model, HttpServletRequest request, 
			RedirectAttributes redirectAttributes) {
		Long activeStudyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster sm = studyService.findByStudyId(activeStudyId);
		model.addAttribute("study", sm);
		
		Volunteer vol = studyService.volunteer(Long.parseLong(request.getParameter("volid")));
		StudyPeriodMaster sp = studyService.studyPeriodMaster(Long.parseLong(request.getParameter("periodId")));
		String userName = request.getSession().getAttribute("userName").toString();
		String startDate = request.getParameter("startDate");
		String name = studyService.scheduleSubject(vol, sp, userName, startDate);
		if(name != null) {
			redirectAttributes.addFlashAttribute("pageMessage",  "Schedule Subject Successfuly.");
			return "redirect:/site/subjectMatrix";
		}else
			redirectAttributes.addFlashAttribute("pageMessage", "Schedule Subject Faild");
		return "redirect:/site/scheduleSubject/"+request.getParameter("volid")+"/"+request.getParameter("periodId");
		
	}
}
