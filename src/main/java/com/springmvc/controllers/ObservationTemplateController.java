package com.springmvc.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Blob;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.covide.template.dto.TemplateAuditTrailDto;
import com.covide.template.dto.TemplateCommentsDto;
import com.covide.template.dto.TemplateFilesDto;
import com.springmvc.service.ObservationTemplateService;

import Decoder.BASE64Decoder;

//import sun.misc.BASE64Decoder;

@Controller
@RequestMapping("/obtemplate")
public class ObservationTemplateController {
	
	@Autowired
	ObservationTemplateService obstempService;
	
	@RequestMapping(value="/templateFileDetails", method=RequestMethod.GET)
	public String templateFileDetails(ModelMap map, @RequestParam("studyId")Long studyId,@RequestParam("groupIdVal")Long groupId,
			@RequestParam("subGroupIdVal")Long subGroupId, @RequestParam("subGroupanimalId")Long subGroupanimalId, 
			@RequestParam("templateId")Long templateId, @RequestParam("ObservationName")String ObservationName, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		TemplateFilesDto tfdto = obstempService.getTemplateFileDetails(studyId, groupId, subGroupId, subGroupanimalId, templateId);
		map.addAttribute("tfdto", tfdto);
		map.addAttribute("templateId", templateId);
		map.addAttribute("ObservationName", ObservationName);
		return "templateFileDetails";
	}
	
	@RequestMapping(value="/templateView", method=RequestMethod.GET)
	public String templateView(ModelMap map, @RequestParam("studyId")Long studyId,@RequestParam("groupIdVal")Long groupId,
			@RequestParam("subGroupIdVal")Long subGroupId, @RequestParam("subGroupanimalId")Long subGroupanimalId, 
			@RequestParam("templateId")Long templateId, @RequestParam("ObservationName")String ObservationName,HttpServletRequest request) {
		
		TemplateFilesDto tfDto = obstempService.getTempleteForView(studyId, groupId, subGroupId, subGroupanimalId, templateId, request);
		map.addAttribute("tfDto", tfDto);
		map.addAttribute("studyId", studyId);
		map.addAttribute("groupId", groupId);
		map.addAttribute("subGroupId", subGroupId);
		map.addAttribute("subGroupanimalId", subGroupanimalId);
		map.addAttribute("templateId", templateId);
		map.addAttribute("ObservationName", ObservationName);
//		return "templateViewPage";
		return "/pages/templateFiles/templatePreviewPage";
	}
	
	@RequestMapping(value="/saveExcelBolob", method=RequestMethod.GET)
	@ResponseBody
	public String saveExcelBolob(@RequestParam("fileName")String fileName, @RequestParam("dataStr")String  dataStr, HttpServletRequest request) {
		System.out.println("fileName is : "+fileName);
		String finalStr = "";
		String realPath = request.getServletContext().getRealPath("/");
		String location = realPath +"/DataFiles/";
		File file = new File(location);
		if(!file.exists())
			file.mkdirs();
		try {
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] decodedByte = null;
			String[] parts = dataStr.split(",");
	        finalStr = parts[1];
			decodedByte = decoder.decodeBuffer(finalStr);
	        @SuppressWarnings("unused")
			Blob blob = new SerialBlob(decodedByte);
	        FileOutputStream fos=new FileOutputStream(location+fileName);
			fos.write(decodedByte);
			fos.close();	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return finalStr;
	}
	@RequestMapping(value="/saveOrupdateTemplateFile", method=RequestMethod.POST)
	public String saveOrupdateTemplateFile(@RequestParam("studyId")long studyId, @RequestParam("groupId")long groupId,
			@RequestParam("subGroupId")long subGroupId, @RequestParam("subGroupanimalId")long subGroupanimalId,
			@RequestParam("templateId")long templateId,@RequestParam("modifidDoc")String modifidDoc,
			@RequestParam("ObservationName")String ObservationName, HttpServletRequest request,RedirectAttributes redirectAttributes) {
		String username = request.getSession().getAttribute("userName").toString();
		String result ="";
		try {
			result = obstempService.saveOrupdateTemplateFile(studyId,groupId,subGroupId,subGroupanimalId,templateId, modifidDoc, username, request);
			if(result.equals("success"))
				 redirectAttributes.addFlashAttribute("pageMessage", "Template updated successful....!");
			else if(result.equals("auditFailed"))
				 redirectAttributes.addFlashAttribute("pageError", "Audit Trail not saved please try again..!");
			else if(result.equals("Failed"))
				 redirectAttributes.addFlashAttribute("pageError", "Temaplate Updatation Failed please try again..!");
			else
				redirectAttributes.addFlashAttribute("pageError", "File source Not available...!");
			redirectAttributes.addAttribute("studyId", studyId);
			redirectAttributes.addAttribute("groupIdVal", groupId);
			redirectAttributes.addAttribute("subGroupIdVal", subGroupId);
			redirectAttributes.addAttribute("subGroupanimalId", subGroupanimalId);
			redirectAttributes.addAttribute("templateId", templateId);
			redirectAttributes.addAttribute("ObservationName", ObservationName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/obtemplate/templateFileDetails";
		
	}
	
	@RequestMapping(value="/modifyTemplateStatus/{templateId}/{statusStr}/{comments}", method=RequestMethod.GET)
	public String compleReviewTemplate(ModelMap model, @PathVariable("templateId")Long templateId, 
			@PathVariable("statusStr")String statusStr,@PathVariable("comments")String comments, HttpServletRequest request) {
		String username = request.getSession().getAttribute("userName").toString();
		boolean flag = obstempService.completeReviewForTemplate(templateId, statusStr, comments, username);
		if(flag)
			model.addAttribute("result", "Done");
		else 
			model.addAttribute("result", "Failed");
		return "/pages/templateFiles/templateResultPage";
	}
	
	@RequestMapping(value="/fieldLevelAudit/{templateId}", method=RequestMethod.GET)
	public String fieldLevelAudit(ModelMap model, @PathVariable("templateId")Long templateId) {
		TemplateAuditTrailDto tatDto = obstempService.getTemplateAuditTrailData(templateId);
		model.addAttribute("tatDto", tatDto);
		return "/pages/templateFiles/templateFieldLevelAudit";
	}
	
	@RequestMapping(value="/getCommentsInformation/{templateId}/{type}", method=RequestMethod.GET)
	public String getCommentsInformation(ModelMap model, @PathVariable("templateId")Long templateId, @PathVariable("type")String type) {
		List<TemplateCommentsDto> comList = obstempService.getTemplateCommentsData(templateId, type);
		model.addAttribute("comList", comList);
		model.addAttribute("type", type);
		return "/pages/templateFiles/templateCommentsPage";
	}
}
