package com.springmvc.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.ManyToOne;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springmvc.model.IpRequest;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.Randomization;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyPeriodMaster;
import com.springmvc.model.StudySite;
import com.springmvc.model.StudySiteSubject;
import com.springmvc.service.RandomizationService;
import com.springmvc.service.StudyService;
import com.springmvc.service.UserService;

@Controller
@RequestMapping("/randomization")
public class RandomizationController {

	@Autowired
	StudyService studyService;
	
	@Autowired
	UserService userService;
	
	@RequestMapping(value="/iwrs", method=RequestMethod.GET)
	public String iwrs(HttpServletRequest request, 
			ModelMap model, RedirectAttributes redirectAttribute) {
		model.addAllAttributes(redirectAttribute.getFlashAttributes());
		return "iwrs.tiles";
	}
	
	@Autowired
	RandomizationService randomizationService;
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String randomization(HttpServletRequest request, 
			ModelMap model, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		String studyNo = request.getSession().getAttribute("activeStudyNo").toString();
		StudyMaster study = studyService.findByStudyNo(studyNo);
//		String username = request.getSession().getAttribute("userName").toString();
		model.addAttribute("study",  study);
		model.addAttribute("periods",  randomizationService.onlyStudyPeriodsWithSubEle(study));
		List<Randomization> list  =  randomizationService.randomizationList(study);
		
		if(list.size() > 0 ) {
			Map<String, String> mp = new HashedMap<>();
			for(Randomization r : list) 
				mp.put(r.getSubject()+"_"+r.getPeriod().getId(), r.getTestSeq());
			model.addAttribute("mp",  mp);
			return "randomizationUpdate.tiles";
		}else
			return "randomization.tiles";
	}
	
	@RequestMapping(value="/createRandomization", method=RequestMethod.POST)
	public String createRandomization(HttpServletRequest request, 
			ModelMap model, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		String studyNo = request.getSession().getAttribute("activeStudyNo").toString();
		StudyMaster study = studyService.findByStudyNo(studyNo);
		String username = request.getSession().getAttribute("userName").toString();
		model.addAttribute("study",  study);
		List<StudyPeriodMaster> splist  =  randomizationService.onlyStudyPeriodsWithSubEle(study);
		List<Randomization> list  =  new ArrayList<>();
		for(int i=1; i<= study.getSubjects(); i++) {
			for(StudyPeriodMaster sp : splist) {
				Randomization r = new Randomization();
				r.setCreatedBy(username);
				r.setCreatedOn(new Date());
				r.setSubject(i);
				r.setStd(study);
				r.setPeriod(sp);
				r.setTestSeq(request.getParameter(i+"_"+sp.getId()));
				list.add(r);
			}
		}
		if(list.size() > 0) {
			try {
				randomizationService.saveRandomizationList(list);
				model.addAttribute("pageMessage", "Randamization Data Saved successfully");
			}catch (Exception e) {
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("pageMessage", "Randamization Data Faild.");
			}
		}else
			redirectAttributes.addFlashAttribute("pageError", "Randamization Data Faild.");
		return "redirect:/randomization/";
	}
	
	@RequestMapping(value="/ipRequest", method=RequestMethod.GET)
	public String ipRequest(HttpServletRequest request, 
			ModelMap model, RedirectAttributes redirectAttribute) {
		model.addAllAttributes(redirectAttribute.getFlashAttributes());
		String studyNo = request.getSession().getAttribute("activeStudyNo").toString();
		StudyMaster study = studyService.findByStudyNo(studyNo);
		List<IpRequest> ipRequests = randomizationService.ipRequestsByStudy(study);
		model.addAttribute("ipRequests", ipRequests);
		return "ipRequest.tiles";
	}
	@RequestMapping(value="/newIpRequest", method=RequestMethod.GET)
	public String newIpRequest(HttpServletRequest request, 
			ModelMap model, RedirectAttributes redirectAttribute) {
		model.addAllAttributes(redirectAttribute.getFlashAttributes());
		String studyNo = request.getSession().getAttribute("activeStudyNo").toString();
		StudyMaster study = studyService.findByStudyNo(studyNo);
		String username = request.getSession().getAttribute("userName").toString();
		model.addAttribute("study",  study);
		List<StudyPeriodMaster> splist  =  randomizationService.onlyStudyPeriodsWithSubEle(study);
		
		Long siteId = (Long) request.getSession().getAttribute("siteId");
		if(siteId != null) {
			StudySite site = studyService.studySiteId(siteId);
			
//			List<Integer> subjects = new ArrayList<>();
//			List<IpRequest> ipRequests = randomizationService.ipRequestsBySite(site);
//			Map<Integer, List<Long>> subjectspMap =  new HashedMap<>();
//			for(IpRequest ip : ipRequests) {
//				List<Long> periodIds = subjectspMap.get(ip.getSubject());
//				if(periodIds == null) periodIds = new ArrayList<>();
//				if(!periodIds.contains(ip.getPeriod().getId()))
//					periodIds.add(ip.getPeriod().getId());
//				subjectspMap.put(ip.getSubject(), periodIds);
//			}
//			
//			for(IpRequest ip : ipRequests) {
//				List<Long> periodIds = subjectspMap.get(ip.getSubject());
//				boolean flag = true;
//				for(StudyPeriodMaster sp : splist) {
//					if(!periodIds.contains(sp.getId())) 
//						flag = false;
//				}
//				if()
//				subjects.add(ip.getSubject());
//			}
//			
			List<StudySiteSubject> siteSubs = new ArrayList<>();
			
			List<StudySiteSubject> siteSub = randomizationService.siteWiseSubjects(site);
			for(StudySiteSubject sss : siteSub) {
//				if(!subjects.contains(sss.getSubjectno())) {
					siteSubs.add(sss);
//				}
			}
			model.addAttribute("siteSubs", siteSubs);
			model.addAttribute("periods",  randomizationService.onlyStudyPeriodsWithSubEle(study));
			return "newIpRequest.tiles";
		}else {
			redirectAttribute.addFlashAttribute("pageError", "Please, Select Site.");
			return "redirect:/dashboard/userWiseActiveStudySites";
		}

	}
	
	
	@RequestMapping(value="/createIpRequest", method=RequestMethod.POST)
	public String createIpRequest(HttpServletRequest request, 
			ModelMap model, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		String studyNo = request.getSession().getAttribute("activeStudyNo").toString();
		StudyMaster study = studyService.findByStudyNo(studyNo);
		Long subjectId = Long.parseLong(request.getParameter("subject"));
		StudySiteSubject sss = randomizationService.studySiteSubject(subjectId);
		StudyPeriodMaster sp = studyService.studyPeriodMaster(Long.parseLong(request.getParameter("period")));
		
		String username = request.getSession().getAttribute("userName").toString();
		Randomization ra = randomizationService.randomizationBySubPeriod(sss.getSubjectno(), sp);
		if(ra == null) {
			redirectAttributes.addFlashAttribute("pageError", "Randamization Required.");
			return "redirect:/randomization/ipRequest";
		}
		
		IpRequest irTemp = randomizationService.ipRequests(sss.getSubjectno(), sp, sss.getSite());
		if(irTemp != null) {
			redirectAttributes.addFlashAttribute("pageError", "Ip Request done already.");
			return "redirect:/randomization/ipRequest";
		}
		
		
		boolean flag = false;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date d1 = sdf.parse(sss.getEnrollmentDate());
			String d11 = sdf.format(new Date());
			Date currentDate = sdf.parse(d11);
			Date d3 = null;
			if(sp.getWindow().equals("+")) {
				Calendar ca = Calendar.getInstance(); 
				ca.setTime(d1); 
				ca.add(Calendar.DATE, sp.getNoOfDays());
				d1 = ca.getTime();
				if (currentDate.compareTo(d1) >= 0) {
//	                  if (d2.compareTo(d3) <= 0) {
	                         flag = true;
//	                  } 
	            }
			}else if(sp.getWindow().equals("-")) {
				Calendar ca = Calendar.getInstance(); 
				ca.setTime(d1); 
				int days = -(sp.getNoOfDays());
				ca.add(Calendar.DATE, days);
				d3 = ca.getTime();
				if (currentDate.compareTo(d1) >= 0) {
//	                  if (d2.compareTo(d3) <= 0) {
	                         flag = true;
//	                  } 
	            }
			}else {
				boolean f1 = false;
				boolean f2 = false;
				Date d0 = d1;
				Calendar ca = Calendar.getInstance(); 
				ca.setTime(d1); 
				ca.add(Calendar.DATE, sp.getNoOfDays());
				d3 = ca.getTime();
				System.out.println(sdf.format(d3));
				if (currentDate.compareTo(d3) >= 0) flag = true;
				
				Calendar ca2 = Calendar.getInstance(); 
				ca2.setTime(d0); 
				int days = -(sp.getNoOfDays());
				ca2.add(Calendar.DATE, days);
				d1 = ca2.getTime();
				System.out.println(sdf.format(d1));
				System.out.println(sdf.format(currentDate));
				if (currentDate.compareTo(d1) >= 0) f1 = true;
	            
				if(f1 || f2)	flag = true; 
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		
		if(flag) {
			IpRequest ir = new IpRequest();
			ir.setCreatedBy(username);
			ir.setCreatedOn(new Date());
			ir.setPeriod(sp);
			ir.setStd(study);
			ir.setRandomization(ra);
			ir.setTestSeq(ra.getTestSeq());
			ir.setSubject(sss.getSubjectno());
			ir.setSite(sss.getSite());
			try {
				randomizationService.saveIpRequest(ir);
				redirectAttributes.addFlashAttribute("pageMessage", "Ip Request Data Saved successfully");
			}catch (Exception e) {
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("pageError", "Ip Request Data Faild.");
			}
			return "redirect:/randomization/newIpRequest";
		}else {
			redirectAttributes.addFlashAttribute("pageError", "Not allowed today.");
			return "redirect:/randomization/newIpRequest";
		}
		
		
		
	}

	@RequestMapping(value="/ipResponse", method=RequestMethod.GET)
	public String ipResponse(HttpServletRequest request, 
			ModelMap model, RedirectAttributes redirectAttribute) {
		model.addAllAttributes(redirectAttribute.getFlashAttributes());
		String studyNo = request.getSession().getAttribute("activeStudyNo").toString();
		Long siteId = (Long) request.getSession().getAttribute("siteId");
		if(siteId != null) {
			StudySite site = studyService.studySiteId(siteId);
			StudyMaster study = studyService.findByStudyNo(studyNo);
			List<IpRequest> ipRequests = randomizationService.ipRequestsBySite(site);
			model.addAttribute("ipRequests", ipRequests);
			return "ipResponse.tiles";
		}else {
			redirectAttribute.addFlashAttribute("pageError", "Please, Select Site.");
			return "redirect:/dashboard/userWiseActiveStudySites";
		}
		
	}
	
	@RequestMapping(value="/allocation", method=RequestMethod.GET)
	public String allocation(HttpServletRequest request, 
			ModelMap model, RedirectAttributes redirectAttribute) {
		model.addAllAttributes(redirectAttribute.getFlashAttributes());
		String studyNo = request.getSession().getAttribute("activeStudyNo").toString();
		StudyMaster study = studyService.findByStudyNo(studyNo);
		List<IpRequest> ipRequests = randomizationService.ipRequestsByStudy(study);
		model.addAttribute("ipRequests", ipRequests);
		return "allocation.tiles";
	}
	
	@RequestMapping(value="/createIpAllocation", method=RequestMethod.POST)
	public String createIpAllocation(HttpServletRequest request, 
			ModelMap model, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		String studyNo = request.getSession().getAttribute("activeStudyNo").toString();
		StudyMaster study = studyService.findByStudyNo(studyNo);
		Long id = Long.parseLong(request.getParameter("ipId"));
		
		String username = request.getSession().getAttribute("userName").toString();
		try {
			randomizationService.updateIpRequest(username, id);
			model.addAttribute("pageMessage", "Ip Allocation Done successfully");
		}catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("pageError", "Ip Allocation Failed");
		}
		return "redirect:/randomization/allocation";
	}
	
	@RequestMapping(value="/exitSite", method=RequestMethod.GET)
	public String exitSite(HttpServletRequest request, 
			ModelMap model, RedirectAttributes redirectAttribute) {
		model.addAllAttributes(redirectAttribute.getFlashAttributes());
		String username = request.getSession().getAttribute("userName").toString();
		Long userId = (Long) request.getSession().getAttribute("userId");
		LoginUsers users = userService.findById(userId);
		users.setActiveSite(null);
		users.setUpdatedBy(username);
		users.setUpdatedOn(new Date());
		try {
			randomizationService.updateLoginUsers(users);
			request.getSession().removeAttribute("siteId");
			request.getSession().removeAttribute("siteName");
			request.getSession().removeAttribute("protocalId");
			request.getSession().removeAttribute("siteNo");
			model.addAttribute("pageMessage", "Exit from Sites Done successfully");
		}catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("pageError", "Exit from Sites Failed");
		}
		return "redirect:/dashboard/userWiseActiveStudySites";
	}
	
}