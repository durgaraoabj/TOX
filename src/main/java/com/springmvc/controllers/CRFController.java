package com.springmvc.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.covide.crf.bo.CrfReaddingBo;
import com.covide.crf.dao.CrfDAO;
import com.covide.crf.dao.impl.CrfDAOImpl;
import com.covide.crf.dto.CRFGroupItem;
import com.covide.crf.dto.CRFSections;
import com.covide.crf.dto.CrfItems;
import com.covide.crf.dto.CrfMetaData;
import com.covide.crf.dto.DTNAME;
import com.covide.validator.CrfValidator;
import com.springmvc.dao.EmployeeDao;
import com.springmvc.model.ReviewLevel;

@Controller
@RequestMapping("/administration/crf")
public class CRFController {

	@Autowired
	EmployeeDao empdao;
	@RequestMapping(value = "/crfuploadpage", method = RequestMethod.GET)
	public String createUser(ModelMap model, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		
		return "crfuploadpage.tiles";
	}

	public static List<CRFSections> sections;
	public static  List<CRFSections> crfSections;
	public static DTNAME dt; 
	public static  Map<String, CRFSections> sectionsMap;
	public static  Map<String, List<CrfItems>> sectionsItems;
	public static  Map<String, CRFGroupItem> groupsMap;
	public static  Map<String, List<CrfItems>> groupItems;
	public static  int maxRows = 0;
	public static  Set<String> rowContainsGroup;
	
	public static Map<Integer, CRFSections> crfsections;
	public static List<CRFGroupItem> groups;
	public static List<CrfItems> items;
	public static Map<String, CrfItems> crfItems;
	
	public static int maxSections  = 0;
	@RequestMapping(value = "/crfupload", method = RequestMethod.POST)
	public String upload(@RequestParam CommonsMultipartFile file,
			@Valid @ModelAttribute("crfpojo") CrfMetaData crf, HttpSession session,
			RedirectAttributes redirectAttributes, BindingResult results,Model model) {
		String path = session.getServletContext().getRealPath("/");
		String filename = file.getOriginalFilename();
		System.out.println(path + " " + filename);
		
		// mandatory file upload check
		if (!crf.getFile().getOriginalFilename().equals("")) {
			new CrfValidator().validate(crf, results);
			if (crf.getId() == 0) {
				// upload the file
				//FileUtil.uploadFile(request, crf.getFile(), crf.getCode());
				try {
					byte barr[] = file.getBytes();
					BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(path + "/" + filename));
					bout.write(barr);
					bout.flush();
					bout.close();
					redirectAttributes.addFlashAttribute("pageMessage", "uploaded Successfully");
					
				} catch (Exception e) {
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
				
				CrfMetaData crf1 = null;
				try {
					if (fis != null) {
						HSSFWorkbook wb = new HSSFWorkbook(fis);
						CrfReaddingBo crfbo = new CrfReaddingBo();
						crf1 = crfbo.getDisplayableItemList(wb);
						
						model.addAttribute("crfviewTest", "testCrfViewTest");
						
						if (crf1 != null) {
							//crfDAO.add(crf);
							model.addAttribute("title", "CRF Viiew");
							model.addAttribute("viiewcrf", true);
							model.addAttribute("crf", crf1);
							
							model.addAttribute("maxSections", maxSections);
							model.addAttribute("sections", crfsections);
							model.addAttribute("items", crfItems);
							
							model.addAttribute("message", "please check the crf");
							model.addAttribute(crf);							
							session.setAttribute("crf", crf1);
							session.setAttribute("crf", crf1);
							return "crfview.tiles";
						}
					} else {
						model.addAttribute("message", "Validation fails for upload the Crf!");
						return "crfuploadpage.tiles";
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "crfuploadpage.tiles";
	}

	@RequestMapping(value = "/crfuploadsave", method = RequestMethod.GET)
	public String crfuploadsave(ModelMap model, HttpSession session) {
		CrfMetaData crf = (CrfMetaData) session.getAttribute("crf");
		CrfDAO crfDAO= new CrfDAOImpl();
//		crfDAO.saveCrf(crf, dt);
		empdao.saveCrf(crf, dt);
		model.addAttribute("message", "CRF Saved Succfully");
		return "crfuploadpage.tiles";
	}
	
	@RequestMapping(value="/observationApprovelLevel", method=RequestMethod.GET)
	public String observationApprovelLevel(ModelMap model, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		ReviewLevel rl = empdao.reviewLevel();
//		model.addAttribute("PageHedding", "CRF Rule");
//		model.addAttribute("activeUrl", "admini/crfRule/");
		model.addAttribute("rl", rl);
		return "observationApprovelLevel.tiles";
	}
	
	@RequestMapping(value="/observationApprovelLevel", method=RequestMethod.POST)
	public String observationApprovelLevelPost(
			@ModelAttribute("observationApprovelLevel") int observationApprovelLevel,
			ModelMap model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		String userName = request.getSession().getAttribute("userName").toString();
		boolean flag = empdao.saveReviewLevel(observationApprovelLevel, userName);
		if(flag)
			redirectAttributes.addFlashAttribute("pageMessage", "Observation Approval Level saved Successfully.");
		else
			redirectAttributes.addFlashAttribute("pageError", "Failed to save Observation Approval Level.");
		return "redirect:/administration/crf/observationApprovelLevel";
	}
}
