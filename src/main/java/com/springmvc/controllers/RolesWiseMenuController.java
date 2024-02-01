package com.springmvc.controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.covide.advity.reports.InputTextPdf;
import com.covide.advity.reports.RoleWiseLinksPdfGeneration;
import com.covide.advity.reports.RolesPdfGeneration;
import com.covide.advity.reports.TextPdf;
import com.springmvc.model.ApplicationSideMenuLinks;
import com.springmvc.model.RoleMaster;
import com.springmvc.model.RolesWiseModules;
import com.springmvc.service.RolesWiseMenuService;

@Controller
@RequestMapping("/roleMenu")
public class RolesWiseMenuController {
	
	@Autowired
	RolesWiseMenuService roleWisemenuService;
	
	@RequestMapping(value="/showRolesWiseMenu", method=RequestMethod.GET)
	public String showRolesWiseMenu(ModelMap model, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		List<RoleMaster> rolesList = roleWisemenuService.getRolesMasterRecords();
		model.addAttribute("rolesList", rolesList);
		return "rolesWisePage";
	}
	
	
	@RequestMapping(value="/viewRolewiseModules/{roleId}", method=RequestMethod.GET)
	public String viewRolewiseModules(ModelMap model, @PathVariable("roleId") Long roleId, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		Map<String, Map<String, List<RolesWiseModules>>> menusMap = roleWisemenuService.getRolesWiseModulesRecordsList(roleId);
		model.addAttribute("menusMap", menusMap);
		model.addAttribute("roleId", roleId);
		return "pages/rolesMenu/roleWiseMenuDetials";
	}
	
	@RequestMapping(value="/getAllModuleLinksRole/{roleId}", method=RequestMethod.GET)
	public String getAllModuleLinksRole(ModelMap model, @PathVariable("roleId") Long roleId) {
		Map<String, List<ApplicationSideMenuLinks>> map = roleWisemenuService.getApplicationSideMenusList(roleId);
		model.addAttribute("roleId", roleId);
		model.addAttribute("map", map);
		return "pages/rolesMenu/allModulesList";
	}
	
	@RequestMapping(value="/AddSubLinksToRole/{roleId}/{addlinks}", method=RequestMethod.GET)
	public String AddSubLinksToRole(ModelMap model, @PathVariable("roleId") Long roleId, 
			@PathVariable("addlinks")List<Long>linkIds, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String userName = request.getSession().getAttribute("userName").toString();
		RoleMaster rolePojo = roleWisemenuService.getRoleMasterRecordBasedOnId(roleId);
		if(rolePojo != null) { 
			boolean flag  = roleWisemenuService.saveRoleBasedLinks(rolePojo, linkIds, userName);
			if(flag)
				redirectAttributes.addFlashAttribute("pageMessage", " Selected Links Added to "+rolePojo.getRole()+" Role.");
			else
				redirectAttributes.addFlashAttribute("pageError", " Selected Links Addeding Failed to "+rolePojo.getRole()+" Role.");
			
		}
		model.addAttribute("roleId", roleId);
		return "redirect:/roleMenu/viewRolewiseModules/"+roleId;
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value="/inactiveSideMenu/{roleId}/{smid}", method=RequestMethod.GET)
	public String inactiveSideMenu(ModelMap model, @PathVariable("roleId") Long roleId, 
			@PathVariable("smid")Long linkId, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String userName = request.getSession().getAttribute("userName").toString();
		RoleMaster rolePojo = roleWisemenuService.getRoleMasterRecordBasedOnId(roleId);
		if(rolePojo != null) { 
			boolean flag  = roleWisemenuService.inactiveApplicationSideLink(rolePojo, linkId, userName);
		}
		model.addAttribute("roleId", roleId);
		return "redirect:/roleMenu/viewRolewiseModules/"+roleId;
	}
	
	@RequestMapping(value="/printRolewiseConfiguredLinks/{roleId}/{roleName}", method=RequestMethod.GET)
	public void printRolewiseConfiguredLinks(ModelMap model, @PathVariable("roleId") Long roleId,@PathVariable("roleName") String roleName, 
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Map<String, List<RolesWiseModules>>> menusMap = roleWisemenuService.getRolesWiseModulesRecordsList(roleId);
		Map<String, List<RolesWiseModules>> map = menusMap.get(roleName);
		if(map != null && map.size() > 0) {
			try {
				RoleWiseLinksPdfGeneration rwlpg = new RoleWiseLinksPdfGeneration();
				rwlpg.generateReport(response, request, menusMap);
			} catch (Exception e) {
				e.printStackTrace();
				TextPdf tp = new TextPdf();
				tp.generateTextPdf(request, response);
			}
		}else {
			InputTextPdf itp = new InputTextPdf();
			itp.generateTextPdf(request, response, "There is No Roles Assigned to "+roleName);
		}
	}
	
	@RequestMapping(value="/printRolesinPdf", method=RequestMethod.GET)
	public void printRolesinPdf(HttpServletRequest request, HttpServletResponse response) {
		List<RoleMaster> rolesList = roleWisemenuService.getRolesMasterRecords();
		if(rolesList.size() > 0) {
			try {
				RolesPdfGeneration rpg = new RolesPdfGeneration();
				rpg.generateReport(response, request, rolesList);
			} catch (Exception e) {
				e.printStackTrace();
				TextPdf tp = new TextPdf();
				tp.generateTextPdf(request, response);
			}
		}else {
			InputTextPdf itp = new InputTextPdf();
			itp.generateTextPdf(request, response, "There is No Roles Avaliable.");
		}
		
	}
}
