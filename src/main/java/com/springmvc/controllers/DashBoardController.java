package com.springmvc.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springmvc.model.LoginUsers;
import com.springmvc.model.RolesWiseModules;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudySite;
import com.springmvc.model.UserWiseSitesAsignMaster;
import com.springmvc.model.UserWiseStudiesAsignMaster;
import com.springmvc.service.StudyService;
import com.springmvc.service.UserService;
import com.springmvc.service.UserWiseStudiesAsignService;

@Controller
@RequestMapping("/dashboard")
@PropertySource(value = { "classpath:application.properties" })
public class DashBoardController {

	@Autowired
	private Environment environment;

	@Autowired
	UserService userService;

	@Autowired
	StudyService studyService;

	@Autowired
	UserWiseStudiesAsignService userWiseStudiesAsignService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String dashboardHomePage(HttpServletRequest request, RedirectAttributes redirectAttributes, ModelMap model) {
		System.out.println(environment.getRequiredProperty("dateFormatJsp"));

		request.getSession().setAttribute("dateFormatJsp", environment.getRequiredProperty("dateFormatJsp"));
		try {
			String username = request.getSession().getAttribute("userName").toString();
			System.out.println("Username : " + username);
			LoginUsers users = userService.findByUsername(username);

			if (users != null) {
				System.out.println(environment.getRequiredProperty("mainUrl"));
				request.getSession().setAttribute("mainUrl", environment.getRequiredProperty("mainUrl"));
				System.out.println(users.getUsername());
				request.getSession().setAttribute("userId", users.getId());
				request.getSession().setAttribute("userName", users.getUsername());
				request.getSession().setAttribute("userRole", users.getRole().getRole());
				request.getSession().setAttribute("userRoleId", users.getRole().getId());
				request.getSession().setAttribute("tranPassword", users.getTranPassword());

				/*
				 * if(users.getRole().getRole().equals("ADMIN")) return
				 * "redirect:/administration/";
				 */
				List<RolesWiseModules> rwmList = null;
				if (users.getActiveStudy() != null) {
					UserWiseStudiesAsignMaster uwsam = userService
							.getUserWiseSitesAsignMasterRecord(users.getActiveStudy().getId(), users.getId());
					if (uwsam != null) {
						if (uwsam.getRoleId() != null) {
							request.getSession().setAttribute("userRole", uwsam.getRoleId().getRole());
							request.getSession().setAttribute("userRoleId", uwsam.getRoleId().getId());
							rwmList = userService.getApplicationMenusBasedOnStudy(uwsam.getRoleId());
						} else
							rwmList = userService.getApplicationMenus(users);
					} else
						rwmList = userService.getApplicationMenus(users);
					request.getSession().setAttribute("activeStudyNo", users.getActiveStudy().getStudyNo());
					request.getSession().setAttribute("activeStudyId", users.getActiveStudy().getId());
					request.getSession().setAttribute("activeStudy", users.getActiveStudy());
					if (users.getActiveSite() != null) {
						request.getSession().setAttribute("siteId", users.getActiveSite().getId());
						request.getSession().setAttribute("siteName", users.getActiveSite().getSiteName());
						request.getSession().setAttribute("protocalId", users.getActiveSite().getProtocalId());
						request.getSession().setAttribute("siteNo", users.getActiveSite().getSiteNo());
					}
					if (rwmList.size() > 0) {
						releaseAllMenuLinks(request);
						for (RolesWiseModules rwm : rwmList) {
//							request.getSession().setAttribute("Menu"+rwm.getAppsideMenu().getAppsideMenu().getAppMenu().getId(), true);
							request.getSession()
									.setAttribute("SMenu_" + rwm.getAppsideMenu().getAppsideMenu().getCode(), true);
							System.out.println("SMenu" + rwm.getAppsideMenu().getAppsideMenu().getCode());
							request.getSession().setAttribute("SMenuL_" + rwm.getAppsideMenu().getCode(), true);
						}
					}
				} else {
					redirectAttributes.addFlashAttribute("pageWarning", "Please select study");
					return "redirect:/dashboard/userWiseActiveStudies";
				}
				if (users.getRole().getRole().equals("SD")) {
					return "redirect:/dashboard/studyDirectorDashBoard";
				} else
					return "dashboard.tiles";
			} else {
				return "redirect:/logout";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/logout";
		}
	}

	@RequestMapping(value = "/studyDirectorDashBoard", method = RequestMethod.GET)
	public String studyDirectorDashBoard(ModelMap model, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		Long userId = (Long) request.getSession().getAttribute("userId");
		List<StudyMaster> studys = studyService.allNewActiveStudysOfStudyDirector(userId);
		model.addAttribute("studys", studys);
		List<StudyMaster> directorStudys = studyService.allActiveStudysOfStudyDirector(userId);
		model.addAttribute("directorStudys", directorStudys);

		return "studyDirectorDashBoard";
	}

	private void releaseAllMenuLinks(HttpServletRequest request) {
		List<RolesWiseModules> rwmList = userService.getRolesWiseModulesList();
		if (rwmList.size() > 0) {
			for (RolesWiseModules rwm : rwmList) {
//				request.getSession().setAttribute("Menu"+rwm.getAppsideMenu().getAppsideMenu().getAppMenu().getId(), false);
				request.getSession().setAttribute("SMenu" + rwm.getAppsideMenu().getAppsideMenu().getId(), false);
				request.getSession().setAttribute("SMenuL" + rwm.getAppsideMenu().getId(), false);
			}
		}

	}

	@RequestMapping(value = "/userWiseActiveStudies", method = RequestMethod.GET)
	public String userWiseActiveStudies(ModelMap model, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		String username = request.getSession().getAttribute("userName").toString();
		String role = (String) request.getSession().getAttribute("userRole");
		Long userId = (Long) request.getSession().getAttribute("userId");
		Long acstdId = (Long) request.getSession().getAttribute("activeStudyId");
		List<StudyMaster> allStudies = null;
		if (role != null && role != "") {
			if (role.equals("TFM") || role.equals("SUPERADMIN"))
				allStudies = studyService.findAll();
			else
				allStudies = studyService.getAllStudyDesignStatusStudiesList(userId);
		}
		/*
		 * List<UserWiseStudiesAsignMaster> userWiseStudies =
		 * userWiseStudiesAsignService.findUserWiseStudies(username);
		 * Collections.sort(userWiseStudies, Collections.reverseOrder());
		 */
		Map<Long, String> sdMap = studyService.getStudyDirectorsDetails(allStudies);
		model.addAttribute("sdMap", sdMap);
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		model.addAttribute("userWiseStudies", allStudies);
		model.addAttribute("acstdId", acstdId);
		return "userWiseActiveStudies.tiles";
	}

	@RequestMapping(value = "/changeStudy/{studyId}", method = RequestMethod.GET)
	public String changeStudy(@PathVariable("studyId") Long studyId, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		System.out.println("Study ID : " + studyId);
		String username = request.getSession().getAttribute("userName").toString();
		StudyMaster study = userService.changeStudy(studyId, username);
		Long userId = (Long) request.getSession().getAttribute("userId");
		LoginUsers user = userService.findById(userId);
		UserWiseStudiesAsignMaster uwsam = userService.getUserWiseSitesAsignMasterRecord(studyId, userId);
		List<RolesWiseModules> rwmList = null;
		if (uwsam != null) {
			if (uwsam.getRoleId() != null) {
				request.getSession().setAttribute("userRole", uwsam.getRoleId().getRole());
				rwmList = userService.getApplicationMenusBasedOnStudy(uwsam.getRoleId());
			} else
				rwmList = userService.getApplicationMenus(user);
		} else
			rwmList = userService.getApplicationMenus(user);

		if (rwmList.size() > 0) {
			for (RolesWiseModules rwm : rwmList) {
				releaseAllMenuLinks(request);
				request.getSession().setAttribute("SMenu_" + rwm.getAppsideMenu().getAppsideMenu().getCode(), true);
				System.out.println("SMenu_" + rwm.getAppsideMenu().getAppsideMenu().getCode());
				request.getSession().setAttribute("SMenuL_" + rwm.getAppsideMenu().getCode(), true);
			}
		}
		if (study != null) {
			redirectAttributes.addFlashAttribute("pageMessage", "Study No :'" + study.getStudyNo() + "' Changed.");
			request.getSession().setAttribute("activeStudyNo", study.getStudyNo());
			request.getSession().setAttribute("activeStudyId", study.getId());
			request.getSession().removeAttribute("siteId");
			request.getSession().removeAttribute("siteName");
			request.getSession().removeAttribute("protocalId");
			request.getSession().removeAttribute("siteNo");
//			return "redirect:/dashboard/userWiseActiveStudies";
			return "redirect:/dashboard/";
		} else {
			redirectAttributes.addFlashAttribute("pageError", "Study Selection failed, please try again.");
			return "redirect:/dashboard/userWiseActiveStudies";
		}
	}

	@RequestMapping(value = "/userWiseActiveStudySites", method = RequestMethod.GET)
	public String userWiseActiveStudySites(ModelMap model, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		String studyNo = request.getSession().getAttribute("activeStudyNo").toString();
		StudyMaster study = studyService.findByStudyNo(studyNo);
		String username = request.getSession().getAttribute("userName").toString();
		LoginUsers users = userService.findByUsername(username);

		List<UserWiseSitesAsignMaster> activeUsers = userWiseStudiesAsignService.findByStudyWiseSitesListByUser(study,
				users);
		List<StudySite> sites = new ArrayList<StudySite>();
		for (UserWiseSitesAsignMaster usa : activeUsers)
			if (usa.getStatus() == 'T') {
				sites.add(usa.getSite());
			}
		Collections.sort(sites);
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		if (users.getActiveSite() != null)
			model.addAttribute("currentSite", users.getActiveSite().getId());
		else
			model.addAttribute("currentSite", "");
//		model.addAttribute("userWiseStudies", userWiseStudies);
		model.addAttribute("sites", sites);
		return "userWiseActiveSites.tiles";
	}

	@RequestMapping(value = "/changeSite/{siteId}", method = RequestMethod.GET)
	public String changeSite(@PathVariable("siteId") Long siteId, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		System.out.println("Site ID : " + siteId);
		String username = request.getSession().getAttribute("userName").toString();
		StudySite site = userService.changeSite(siteId, username);
		if (site != null) {
			redirectAttributes.addFlashAttribute("pageMessage", "Site  :'" + site.getSiteName() + "' Changed.");

			request.getSession().setAttribute("siteId", site.getId());
			request.getSession().setAttribute("siteName", site.getSiteName());
			request.getSession().setAttribute("protocalId", site.getProtocalId());
			request.getSession().setAttribute("siteNo", site.getSiteNo());
			return "redirect:/site/subjectMatrix";
//			return "redirect:/dashboard/userWiseActiveStudySites";
		} else {
			redirectAttributes.addFlashAttribute("pageError", "Site Selection failed, please try again.");
			return "redirect:/dashboard/userWiseActiveStudySites";
		}
	}

}
