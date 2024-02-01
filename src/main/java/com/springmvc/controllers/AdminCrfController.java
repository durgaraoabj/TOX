package com.springmvc.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.covide.crf.dto.Crf;
import com.covide.crf.dto.CrfEleCaliculation;
import com.covide.crf.dto.CrfGroup;
import com.covide.crf.dto.CrfMapplingTableColumnMap;
import com.covide.crf.dto.CrfSection;
import com.covide.crf.service.CrfService;
import com.covide.validator.CrfValidator;
import com.springmvc.model.RoleMaster;
import com.springmvc.service.RoleMasterService;
import com.springmvc.service.StudyService;
import com.springmvc.service.TemplateFileService;

@Controller
@RequestMapping("/admini/crf")
public class AdminCrfController {
	@Autowired
	RoleMasterService roleMasterService;
	@Autowired
	StudyService studyService;

	@Autowired
	CrfService crfSer;
	
	@Autowired
	TemplateFileService tempFileService;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String crfHomePage(HttpServletRequest request, 
			ModelMap model, RedirectAttributes redirectAttributes) {
			List<Crf> crfList = crfSer.findAllCrfs();
			model.addAttribute("PageHedding", "New Upload Crf");
			model.addAttribute("activeUrl", "admini/crf/");
			model.addAttribute("crfList", crfList);
			// at time of upload input taking from user for role wise access of Observation
			model.addAttribute("roles", roleMasterService.findAll());
			return "crfupload.tiles";
		
	}
	
	@RequestMapping(value = "/crfupload", method = RequestMethod.POST)
	public String upload(@RequestParam CommonsMultipartFile file, 
			@RequestParam("type") String type,
			@RequestParam("subType") String subType,
			@RequestParam CommonsMultipartFile tempfile,
			@Valid @ModelAttribute("crfpojo") Crf crf, HttpSession session, @RequestParam("roles") Long[] roles,
			RedirectAttributes redirectAttributes, BindingResult results,Model model) {
		List<Long> roleList = new ArrayList<>();
		for(Long r : roles) {
			roleList.add(r);
		}
		String userName = session.getAttribute("userName").toString();
		String path = session.getServletContext().getRealPath("/");
		String filename = file.getOriginalFilename();
		System.out.println(path + " " + filename);
		
		String filePath = "";
		if(tempfile.getSize() !=0) {
			session.removeAttribute("templateFile");
			filePath = tempFileService.writeTemplateFile(tempfile, session);
			
		}
		
		if (!crf.getFile().getOriginalFilename().equals("")) {
			new CrfValidator().validate(crf, results);
			System.out.println(results.getErrorCount());
			if (crf.getId() == null || crf.getId() == 0 ) {
				try {
					byte barr[] = file.getBytes();
					BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(path + "/" + filename));
					bout.write(barr);
					bout.flush();
					bout.close();
					redirectAttributes.addFlashAttribute("pageMessage", "Group Observation "+crf.getObservationName()+" saved Successfully.");
					
				} catch (Exception e) {
					List<Crf> crfList = crfSer.findAllCrfs();
					model.addAttribute("PageHedding", "New Upload Crf");
					model.addAttribute("activeUrl", "admini/crf/");
					
					model.addAttribute("crfList", crfList);
					System.out.println(e);
					model.addAttribute("message", "Failde uploade");
					return "crfuploadpage.tiles";
				}
				
				FileInputStream fis = null;
				try {
					File   fileforfis = new File(path + "/" + filename);
					fis = new FileInputStream(fileforfis);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				
				Crf crf1 = null;
				try {
					
					if (fis != null) {
						session.removeAttribute("crfToSave");
						crf1 = crfSer.readCrfExcelFile(fis, filename);
						crf1.setType(type);
						crf1.setSubType(subType);
						crf1.setPrefix(crf.getPrefix());
						crf1.setObservationName(crf.getObservationName());
						crf1.setObservationDesc(crf.getObservationDesc());
						crf1.setConfigurationFor(crf.getConfigurationFor());
						model.addAttribute("PageHedding", "New Upload Crf");
						model.addAttribute("activeUrl", "admini/crf/");
						
//						if(!crf1.getMessage().equals("")) {
//							redirectAttributes.addFlashAttribute("pageError", "Crf "+ crf1.getObservationName() +" Already Avilable Please, upload Other Crf");
//							return "redirect:/admini/crf/";
//						}
						
						if(crf1.isFlag()) {
							model.addAttribute("crfviewTest", crf1);
							List<CrfSection> sec = crf1.getSections();
							for(CrfSection se : sec) {
								System.out.println(se.getName());
								CrfGroup group = se.getGroup();
								System.out.println(group);
							}
							session.setAttribute("crfToSave", crf1);
							session.setAttribute("roles", roleList);
							session.setAttribute("roleMasters", crfSer.allRoleMastersOfIds(roleList));
							return "crfuploadTestView.tiles";
						}else {
							model.addAttribute("exclDataErrors", crf1.getExclData());
							return "crfuploadTestViewError.tiles";
						}
					} else {
						
						List<Crf> crfList = crfSer.findAllCrfs();
						model.addAttribute("PageHedding", "New Upload Crf");
						model.addAttribute("activeUrl", "admini/crf/");
						
						model.addAttribute("crfList", crfList);
						model.addAttribute("message", "Validation fails for upload the Crf!");
						return "crfupload.tiles";
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		
		}else {
			String result = tempFileService.saveObservationTemplateFile(filePath, userName, crf, roleList);
		    if(result.equals("success")) {
		    	session.removeAttribute("templateFile");
		    	redirectAttributes.addFlashAttribute("pageMessage", "Group Observation Template Created Successfully...!");
		    }else{
		    	redirectAttributes.addFlashAttribute("pageError", "Group Observation Template Template Creation Failed. Please Try Again.");
		    }
		}
		return "crfupload.tiles";
	}
	
	
	@RequestMapping(value="/crfFieldCaliculation", method=RequestMethod.GET)
	public String crfFieldCaliculationPage(HttpServletRequest request, 
			ModelMap model,
			RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		
		List<CrfEleCaliculation> list = crfSer.crfEleCaliculationList();
		
		model.addAttribute("PageHedding", "Upload E-Form Calculation");
		model.addAttribute("activeUrl", "admini/crf/crfFieldCaliculation");
		model.addAttribute("list", list);
		return "crfFieldCaliculation.tiles";

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
					File   fileforfis = new File(path);
					fis = new FileInputStream(fileforfis);
					crfSer.uploadCrfCaliculationFile(path, username);
					
					redirectAttributes.addFlashAttribute("pageMessage", "Xml File uploaded Successfully" );
				} catch (Exception e) {
					e.printStackTrace();
					model.addAttribute("message", "Failde uploade");
				}
			}
			
		}
		
		List<Crf> crfList = crfSer.findAllCrfs();
		model.addAttribute("crfList", crfList);
		
		return "redirect:/admini/crf/crfFieldCaliculation";
//		return "crfFieldCaliculation.tiles";
	}
	
	@RequestMapping(value = "/crfuploadsave", method = RequestMethod.GET)
	public String crfuploadsave(ModelMap model,
			HttpServletRequest request,
			HttpSession session,
			RedirectAttributes redirectAttributes) {
		Crf crf = (Crf) session.getAttribute("crfToSave");
		List<Long> roles = (List<Long>) session.getAttribute("roles");
		
		String username = request.getSession().getAttribute("userName").toString();
		crf.setCreatedBy(username);
		crf.setCreatedOn(new Date());
		
		List<CrfSection> sec = crf.getSections();
		for(CrfSection se : sec) {
			System.out.println(se.getName());
			CrfGroup group = se.getGroup();
			System.out.println(group);
		}
//		crfSer.saveCrf(crf);
		String result = crfSer.saveCrfRecord(crf, roles);
		String tempResult = "Failed";
		session.removeAttribute("crfToSave");
		if(result.equals("success")) {
			String templatePath = (String) session.getAttribute("templateFile");
			if(templatePath != null) {
				tempResult = tempFileService.saveObservationTemplateFile(templatePath, username, crf, roles);
				session.removeAttribute("templateFile");
				if(result.equals("success") && tempResult.equals("success")) {
			    	model.addAttribute("message", "Observation '"+crf.getObservationName()+"'Saved Successfully...!");
			    	redirectAttributes.addFlashAttribute("pageMessage", "Group Observation Crf And Template Creation Done Successfully...!");
			    }else if(result.equals("success") && !tempResult.equals("success")){
			    	redirectAttributes.addFlashAttribute("pageMessage", "Group Observation Crf "+ crf.getName()+" Saving Done Successfully...!");
			    	redirectAttributes.addFlashAttribute("pageMessage", "Group Observation Template Saving Failed. Please Try Again.");
			    }else {
			    	redirectAttributes.addFlashAttribute("pageMessage", "Group Observation Crf "+crf.getName()+" And Template Saving Failed. Please Try Again.");
			    }
			}else {
				model.addAttribute("message", "CRF Saved Successfully...!");
		    	redirectAttributes.addFlashAttribute("pageMessage", "Group Observation Crf And Template Creation Done Successfully...!");
			}
			
		}else {
			model.addAttribute("message", "Failed to save Observation '"+crf.getObservationName()+"'");
	    	redirectAttributes.addFlashAttribute("pageError", "Group Observation Crf And Template Creation Done Successfully...!");
		}
		return "redirect:/admini/crf/";
	}
	
	@RequestMapping(value="/crfChangeStatus/{crfId}", method=RequestMethod.GET)
	public String changeStudy(@PathVariable("crfId") Long crfId, 
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		System.out.println("CRF ID : "+crfId);
		String username = request.getSession().getAttribute("userName").toString();
		Crf crf = crfSer.getCrf(crfId);
		Crf crf1 = null;
		if(crf.isActive()) {
			crf1 = crfSer.changeCrfStatus(crfId, username);
		}else {
			List<Crf> crfList = crfSer.crfsByName(crf.getName());
			boolean flag = true;
			for(Crf crf2 : crfList) {
				if(crf2.isActive()) flag = false;
			}
			if(flag) {
				crf1 = crfSer.changeCrfStatus(crfId, username);
			}else {
				redirectAttributes.addFlashAttribute("pageError", "Some of the Crfs With Name '" + crf.getName()+"' is on Active Status. Please In-active Those.");
			}
		}

		if(crf1 != null)
			redirectAttributes.addFlashAttribute("pageMessage", "Crf '"+crf.getName()+"' Status changed.");
		else
			redirectAttributes.addFlashAttribute("pageMessage", "Faild to Change Crf Status.");
		return "redirect:/admini/crf/";
	}
	
	
	@RequestMapping(value="/viewCrf/{crfId}", method=RequestMethod.GET)
	public String viewCrf(@PathVariable("crfId") Long crfId, ModelMap model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		System.out.println("CRF ID : "+crfId);
		Crf crf = crfSer.crfForView(crfId);
		model.addAttribute("crf", crf );
		model.addAttribute("crfId", crfId );
		return "crfuploadView.tiles";
	}
	@RequestMapping(value="/checkObserVationNameStatus/{obserName}/{type}", method=RequestMethod.GET)
	public String checkObserVationNameStatus(ModelMap map, @PathVariable("obserName")String obserName, @PathVariable("type")String type){
		String result = tempFileService.checkObservationNameValidationStatus(obserName, type);
		map.addAttribute("result", result);
		return "/pages/resultPage";
	}
	
	@RequestMapping(value="/mapTableToCrf", method=RequestMethod.GET)
	public String mapTableToCrf(ModelMap model, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		model.addAttribute("PageHedding", "Administration");
		model.addAttribute("activeUrl", "administration/");
		model.addAttribute("crfs", crfSer.findAllCrfs());
		model.addAttribute("tables", crfSer.findAllMappingTables());
		model.addAttribute("mppedElements", crfSer.allCrfMapplingTableColumnMap());
		return "mapTableToCrf";
	}
	@RequestMapping(value="/mapTableToCrfSave", method=RequestMethod.POST)
	public String mapTableToCrfSave(ModelMap model, RedirectAttributes redirectAttributes, HttpSession session,
			@RequestParam("crfid") Long crfid, @RequestParam("secEleId") Long secEleId, @RequestParam("tableId") Long tableId, @RequestParam("columnId") Long columnId) {
		String userName = session.getAttribute("userName").toString();
		CrfMapplingTableColumnMap map =  crfSer.mapTableToCrfSave(crfid, secEleId, tableId, columnId, userName);
		if(map != null)
			redirectAttributes.addFlashAttribute("pageMessage", "TableToCrf Saved Successfully.");
		else
			redirectAttributes.addFlashAttribute("pageMessage", "Faild to save TableToCrf.");
		return "redirect:/admini/crf/mapTableToCrf";
	}
	
}
