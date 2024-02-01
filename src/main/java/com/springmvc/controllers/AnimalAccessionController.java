package com.springmvc.controllers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.covide.crf.dto.AccessionAnimalDataviewDto;
import com.covide.crf.dto.Crf;
import com.covide.crf.dto.CrfRule;
import com.covide.crf.service.CrfService;
import com.covide.dto.StudyAccessionCrfSectionElementDataUpdateDto;
import com.covide.dto.StudyCrfSectionElementDataDto;
import com.springmvc.dao.StatusDao;
import com.springmvc.model.StudyAccessionAnimals;
import com.springmvc.model.StudyAcclamatizationData;
import com.springmvc.model.StudyAcclamatizationDates;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyMaster;
import com.springmvc.reports.RulesInfoTemp;
import com.springmvc.service.AccessionService;
import com.springmvc.service.ExpermentalDesignService;
import com.springmvc.service.InstrumentService;
import com.springmvc.service.ReviewService;
import com.springmvc.service.StudyService;

@Controller
@RequestMapping("/accession")
@PropertySource(value = { "classpath:application.properties" })
public class AnimalAccessionController {
	@Autowired
	ReviewService reviewService;
	@Autowired
	private Environment environment;
	@Autowired
	CrfService crfService;

	@Autowired
	AccessionService accessionService;

	@Autowired
	private ExpermentalDesignService expermentalDesignService;
	@Autowired
	private StudyService studyService;
	@Autowired
	private StatusDao statusDao;
	@Autowired
	private InstrumentService instrumentService;
	@RequestMapping(value = "/animalAccession", method = RequestMethod.GET)
	public String animalAccession(ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
		List<StudyAccessionAnimals> saaList = accessionService.getStudyAccessionAnimalsList(studyId);

		Long maxId = accessionService.getMaxRecordNo(studyId);
		if (maxId == null)
			maxId = 1L;
		else
			maxId = maxId + 1;
		model.addAttribute("saaList", saaList);
		model.addAttribute("studyId", studyId);
		model.addAttribute("maxId", maxId);
		
		model.addAttribute("viewAnimals", false);
		
		if (saaList != null && saaList.size() > 0) {
			Map<String, Integer> total = new HashMap<>();
			for(StudyAccessionAnimals saa : saaList) {
				if(total.containsKey(saa.getGender())) {
					total.put(saa.getGender(), total.get(saa.getGender())+(Integer.parseInt(saa.getNoOfAnimals())));
				}else {
					total.put(saa.getGender(), Integer.parseInt(saa.getNoOfAnimals()));
				}
			}
			model.addAttribute("total", total);
			Map<String, List<StudyAnimal>> map = accessionService.studyAnimals(studyId);
			Map<String, List<List<StudyAnimal>>> mapList = new HashMap<>();
			map.forEach((k, v) -> {
				List<StudyAnimal> anis = v;
				List<StudyAnimal> anisNew = new ArrayList<>();
				List<List<StudyAnimal>> anisNewList = new ArrayList<>();
				for (int i = 0; i < anis.size(); i++) {
					if (i % 5 == 0) {
						if (anisNew.size() > 0)
							anisNewList.add(anisNew);
						anisNew = new ArrayList<>();
					}
					anisNew.add(anis.get(i));
				}
				if (anisNew.size() > 0)
					anisNewList.add(anisNew);
				mapList.put(k, anisNewList);
			});

			model.addAttribute("animals", mapList);
			model.addAttribute("viewAnimals", true);
//			return "animalsAccessionView";
		} 
//		else
			return "animalAccessionPage";
	}

	@RequestMapping(value = "/saveAccessData", method = RequestMethod.POST)
	public String saveAccessData(ModelMap model, HttpServletRequest request, @RequestParam("prefix") String prefixStr,
			@RequestParam("rows") String rows, RedirectAttributes redirectAttributes) {
		Long userId = Long.parseLong(request.getSession().getAttribute("userId").toString());
		String[] animalId = request.getParameterValues("animalId");
		System.out.println(animalId);
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
//		Enumeration<String> en = request.getParameterNames();
//		while(en.hasMoreElements()) {
//			String ele = en.nextElement();
//			System.out.println(en.nextElement()+"\t"+request.getParameter(ele));
//		}
		List<StudyAccessionAnimals> animals = new ArrayList<>();
		String[] row = rows.split(",");
		for (String r : row) {
			StudyAccessionAnimals saa = new StudyAccessionAnimals();
			saa.setPrefix(prefixStr);
			saa.setGender(request.getParameter("gender" + r));
			System.out.println(request.getParameter("from" + r));
			saa.setAnimalsFrom(request.getParameter("from" + r));
			saa.setAnimalsTo(request.getParameter("to" + r));
			animals.add(saa);
		}
		String result = accessionService.saveAnimalAccession(studyId, userId, animalId, animals);
		if (result.equals("success"))
			redirectAttributes.addFlashAttribute("pageMessage", "Animal Accession Data Saved Successfully....!");
		else
			redirectAttributes.addFlashAttribute("pageError", "Animal Accession Data Saving Failed. Please Try Again.");
		return "redirect:/accession/animalAccession";
	}

	@RequestMapping(value = "/viewDataEntryForms", method = RequestMethod.GET)
	public String viewDataEntryForms(ModelMap model, HttpServletRequest request) {
		model.addAttribute("hideElements", true);
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
		Long userRoleId = (Long) request.getSession().getAttribute("userRoleId");
		StudyMaster study = studyService.findByStudyId(studyId);
		List<StudyAcclamatizationData> sadList = accessionService.getStudyAcclamatizationDataRecordsList(studyId, userRoleId);
		model.addAttribute("sadList", sadList);
		model.addAttribute("studyId", studyId);
		model.addAttribute("study", study);
		if (sadList.size() == 0)
			model.addAttribute("pageMessage", "Acclimatization Configuration Not Done.");
		else {
			Long animals = accessionService.noOfAvailableStudyAnimalsCount(studyId);
			if (animals == 0l) {
				model.addAttribute("pageMessage", "Animal Accession Not Done");
				model.addAttribute("hideElements", false);
			}
		}
		return "viewDataEntryForms";
	}

	@RequestMapping(value = "/viewEntryForm", method = RequestMethod.POST)
	public String vieEntryForm(ModelMap model,
			@RequestParam("clinPaht") boolean clinPaht,
			@RequestParam("studyAcclamatizationDateId") Long studyAcclamatizationDateId,
			@RequestParam("studyAcclamatizationDataId") Long studyAcclamatizationDataId,
			@RequestParam("crfId") Long crfId, @RequestParam("type") String type,
			@RequestParam("seletedGender") String seletedGender, @RequestParam("studyId") Long studyId,
			@RequestParam("selecteDate") String selecteDate, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		if(!clinPaht)
			return entryForm(studyAcclamatizationDateId, studyAcclamatizationDataId, crfId, type, studyId, request, model, seletedGender, 0, selecteDate);
		else {
//			return instrumentService.instrumentDataCaptur(studyAcclamatizationDateId, selecteDate, request, model, "NA");
			return instrumentService.instrumentDataCapturPage(studyAcclamatizationDateId, null, selecteDate, request, model, "NA");
		}
	}

	@RequestMapping(value = "/viewEntryForm/{studyAcclamatizationDateId}/{studyAcclamatizationDataId}/{crfId}/{type}/{studyId}/{seletedGender}/{noOfEntry}/{selecteDate}", method = RequestMethod.GET)
	public String vieEntryFormGet(ModelMap model,
			@PathVariable("studyAcclamatizationDateId") Long studyAcclamatizationDateId,
			@PathVariable("studyAcclamatizationDataId") Long studyAcclamatizationDataId,
			@PathVariable("crfId") Long crfId, @PathVariable("type") String type, @PathVariable("studyId") Long studyId,
			@PathVariable("seletedGender") String seletedGender, @PathVariable("noOfEntry") int noOfEntry,
			@PathVariable("selecteDate") String selecteDate, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		return entryForm(studyAcclamatizationDateId, studyAcclamatizationDataId, crfId, type, studyId, request, model, seletedGender, noOfEntry,
				selecteDate);
	}

	private String entryForm(Long studyAcclamatizationDateId, Long studyAcclamatizationDataId, Long crfId, String type, Long studyId,
			HttpServletRequest request, ModelMap model, String seletedGender, int noOfEntry, String selecteDate) {
		model.addAttribute("studyAcclamatizationDateId", studyAcclamatizationDateId);
		model.addAttribute("studyAcclamatizationDataId", studyAcclamatizationDataId);
		model.addAttribute("selecteDate", selecteDate);
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		StudyMaster sm = studyService.findByStudyId(studyId);
		String userRole = (String) request.getSession().getAttribute("userRole");
		StudyAcclamatizationDates studyAcclamatizationDate = accessionService.studyAcclamatizationDates(studyAcclamatizationDateId);
		StudyAnimal animal = null;
		if(type.equals("scheduled")  && !studyAcclamatizationDate.getCrf().getName().equals("Record for Mortality Morbidity"))
			animal = accessionService.nextAnimalForAccessionData(studyAcclamatizationDateId, studyAcclamatizationDataId, studyId, crfId,
				seletedGender, noOfEntry, selecteDate);
		if (animal == null && !type.equals("unscheduled") && !studyAcclamatizationDate.getCrf().getName().equals("Record for Mortality Morbidity")) {
			model.addAttribute("pageMessage", "Form Data Entry Completed.");
//			return "dataEntryMessage";
			return "redirect:/studyExecution/viewExpermantalInCalender";
		} else {
			model.addAttribute("staticData", statusDao.staticDataList());
//			Crf crf = crfService.crfForView(crfId);
			Crf crf = crfService.getCrfForDataEntryView(crfId, sm,
					studyAcclamatizationDateId, animal, "Acclamatization", type);
			if (animal != null)
				model.addAttribute("noOfEntry", animal.getNoOfEntry());
			else
				model.addAttribute("noOfEntry", noOfEntry);
			model.addAttribute("seletedGender", seletedGender);

			if (sm.getStartDate().compareTo(new Date()) <= 0) {
				List<String> frEleList = expermentalDesignService.getFrormulaDataofCurrentCrf(crf.getId(), studyId);
				Map<String, String> caliculationFieldSec = new HashMap<String, String>();
				caliculationFieldSec = crfService.caliculationFieldSec(crf);
				model.addAttribute("caliculationFieldSec", caliculationFieldSec);

				Map<String, String> allElementIdsTypesJspStd = new HashMap<String, String>(); // key-id and value is
																								// type
				// requied field element id-key in list
				Map<String, String> requiredElementIdInJsp = new HashMap<String, String>(); // key-id and value is
																							// type
				Map<String, String> pattrenIdsAndPattren = new HashMap<String, String>(); // key-id and value is
																							// type@pattren

				allElementIdsTypesJspStd = expermentalDesignService.allElementIdsTypesJspStd(crf);
				requiredElementIdInJsp = expermentalDesignService.requiredElementIdInJspStd(crf, userRole);
				pattrenIdsAndPattren = expermentalDesignService.pattrenIdsAndPattrenStd(crf);
				model.addAttribute("requiredElementIdInJsp", requiredElementIdInJsp);
				model.addAttribute("allElementIdsTypesJspStd", allElementIdsTypesJspStd);
				model.addAttribute("pattrenIdsAndPattren", pattrenIdsAndPattren);

				// E-Form rules filed
				List<CrfRule> rules = crfService.crfRuleWithCrfAndSubElements(crf);
				List<RulesInfoTemp> rulesFieldAll = crfService.rulesFields(crf, rules);
				model.addAttribute("rulesFieldAll", rulesFieldAll);

				// Weight Data to CRF ele
				if(animal != null)
				model.addAttribute("weightData",
						expermentalDesignService.crfSectionElementInstrumentValues(animal, crf));
				if (type.equals("unscheduled")) {
					List<StudyAnimal> allAnimals = accessionService.allStudyAnimals(studyId);
					model.addAttribute("unscheduledAnimals", allAnimals);
				}
				model.addAttribute("autoFillCrfData", "no");
				model.addAttribute("frEleList", frEleList);
				model.addAttribute("type", type);
				model.addAttribute("animal", animal);
				model.addAttribute("crf", crf);
				model.addAttribute("duplicateData", "no");
				model.addAttribute("sm", sm);
				model.addAttribute("crf", crf);
				model.addAttribute("duplicateData", "no");
				return "accessionAnimalDataEntryView";

			} else {
				model.addAttribute("formMessage", "Study will Start from  : " + sdf.format(sm.getStartDate()));
				return "dataEntryMessage";
			}
		}
	}

	@RequestMapping(value = "/saveStudyAccessionCrfData", method = RequestMethod.POST)
	public String studyCrfSave(HttpServletRequest request, ModelMap model, RedirectAttributes redirectAttributes,
			@RequestParam("studyAcclamatizationDateId") Long studyAcclamatizationDateId,@RequestParam("animalId") Long animalId, @RequestParam("crfId") Long crfId,
			@RequestParam("discrebencyFields") String discrebencyFields,
			@RequestParam("deviationMessage") String deviationMessage, @RequestParam("type") String type,
			@RequestParam("studyId") Long studyId, @RequestParam("noOfEntry") int noOfEntry,
			@RequestParam("seletedGender") String seletedGender, @RequestParam("selecteDate") String selecteDate,
			@RequestParam("studyAcclamatizationDataId") Long studyAcclamatizationDataId) {
		Long userId = (Long) request.getSession().getAttribute("userId");
		String status = "";
		try {
			status = accessionService.saveStudyAccessionCrfDataEntryDetails(animalId, crfId, studyId, userId,
					discrebencyFields, deviationMessage, request, type, noOfEntry, selecteDate, seletedGender, studyAcclamatizationDateId);
			if (status.equals("success"))
				redirectAttributes.addFlashAttribute("pageMessage", "Form Saved Successfully.");
			else
				redirectAttributes.addFlashAttribute("pageMessage", "Form Saving Failed. Please Try Again.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/accession/viewEntryForm/" +studyAcclamatizationDateId+"/"+ studyAcclamatizationDataId + "/" + crfId + "/" + type + "/"
				+ studyId + "/" + seletedGender + "/" + noOfEntry + "/" + selecteDate;
	}

	@RequestMapping(value = "/viewFormData/{studyAcclamatizationDataId}", method = RequestMethod.GET)
	public String viewFormData(ModelMap model, @PathVariable("studyAcclamatizationDataId") Long studyAcclamatizationDataId,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Long userId = (Long) request.getSession().getAttribute("userId");
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		AccessionAnimalDataviewDto aadDto = reviewService.getAccessionAnimalDataDtoDetailsView(studyAcclamatizationDataId);
		model.addAttribute("aadDto", aadDto);
//		model.addAttribute("crfId", crfId);
		return "viewAccessionAnimalsData";
	}
	
	@RequestMapping(value = "/accessionDataExport/{studyAcclamatizationDataId}", method = RequestMethod.GET)
	public void accessionDataExport(ModelMap model, @PathVariable("studyAcclamatizationDataId") Long studyAcclamatizationDataId,
			HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			AccessionAnimalDataviewDto aadDto = reviewService.getAccessionAnimalDataDtoDetailsView(studyAcclamatizationDataId);
			File file = accessionService.exportAccessionData(request, aadDto);
//			File file = studyService.exportVistrosDataToExcel(request, studyId, startDate, sampleType);
			response.setContentType("application/vitros.ms-excel");
			response.addHeader("Content-Disposition", "attachment; filename=Accesstion_Data.xlsx");
			response.setContentLength((int) file.length());
			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
			// Copy bytes from source to destination(outputstream in this example), closes
			// both streams.
			FileCopyUtils.copy(inputStream, response.getOutputStream());
			redirectAttributes.addFlashAttribute("pageMessage", "Data Exported Successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("pageMessage", "Data Export Faild.");
		}
	}
	@RequestMapping(value = "/accessionActivityUpdateView/{eleId}/{activityType}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody StudyCrfSectionElementDataDto accessionActivityUpdateView(ModelMap model,
			@PathVariable("eleId") Long eleId, @PathVariable("activityType") String activityType,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		return accessionService.accessionActivityUpdateView(eleId, activityType);
	}

	@RequestMapping(value = "/accessionActivityElementUpdate", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String accessionActivityUpdate(StudyAccessionCrfSectionElementDataUpdateDto updateEleData,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Long userId = (Long) request.getSession().getAttribute("userId");
		return accessionService.accessionActivityElementUpdate(updateEleData, userId, "accession");
	}

	@RequestMapping(value = "/cageing", method = RequestMethod.GET)
	public String cageing(ModelMap model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
//		accessionService.saveAnimalCageing(studyId);
//		model.addAttribute("aadDto", aadDto);
		return "cageing";
	}

}
