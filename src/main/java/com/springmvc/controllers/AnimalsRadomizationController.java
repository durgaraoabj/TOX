package com.springmvc.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.covide.crf.dto.AccessionAnimalDataviewDto;
import com.covide.crf.service.CrfService;
import com.covide.dto.FinalRandomizationDto;
import com.covide.dto.RadomizationDto;
import com.covide.dto.RandomizationGenerationDto;
import com.covide.enums.StatusMasterCodes;
import com.springmvc.model.RadamizationAllDataReview;
import com.springmvc.model.RadomizationReviewDto;
import com.springmvc.model.RandamizationDto;
import com.springmvc.model.StatusMaster;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.dummy.RadamizationAllData;
import com.springmvc.service.AnimalRandomizationService;
import com.springmvc.service.ReviewService;
import com.springmvc.service.StudyService;
import com.springmvc.service.impl.AnimalRandomizationServiceImpl;

@Controller
@RequestMapping("/animalRadomization")
@PropertySource(value = { "classpath:application.properties" })
public class AnimalsRadomizationController {
	@Autowired
	private Environment environment;
	@Autowired
	AnimalRandomizationService animalRadService;
	@Autowired
	ReviewService reviewService;
	@Autowired
	CrfService crfService;
	@Autowired
	StudyService studyService;
	public static Map<Long, RadomizationDto> randamizations = new HashMap<>();

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String randomization(ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) throws ParseException {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		request.getSession().removeAttribute("randamizationGender");
		request.getSession().removeAttribute("maledto");
		request.getSession().removeAttribute("femaledto");
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster study = studyService.findByStudyId(studyId);
		model.addAttribute("study", study);
		if (!study.getStatus().getStatusCode().equals(StatusMasterCodes.IN.toString())) {
			StatusMaster randamizationstatus = study.getRadamizationStatus();
			if(study.getGender().equals("Both") && !study.isSplitStudyByGender()) {
				if(!study.isMaleRandamizationStatus() || !study.isFemaleRandamizationStatus()) {
					study.setRadamizationStatus(null);
				}
			}
			if (study.getRadamizationStatus() == null
					|| study.getRadamizationStatus().getStatusCode().equals(StatusMasterCodes.REJECTED.toString())) {
				study.setRadamizationStatus(randamizationstatus);
				boolean fg = true;
				boolean fgflag = false;
				String generatedFor =  "";
				if(study.getGender().equals("Both") && !study.isSplitStudyByGender()) {
					if(study.isMaleRandamizationStatus() && study.isFemaleRandamizationStatus()) {
						fg = false;
					}else {
						SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
						Date maleAcccdate = study.getAcclimatizationStarDate();
						Date femaleAcccdate = study.getAcclimatizationStarDateFemale();
						Date currentDate = sdf.parse(sdf.format(new Date()));
						if(!study.isMaleRandamizationStatus() && !study.isFemaleRandamizationStatus()) {
							if(currentDate.compareTo(maleAcccdate) >= 0 && currentDate.compareTo(femaleAcccdate) >= 0) {
								generatedFor = study.getGender();
							}else if(currentDate.compareTo(maleAcccdate) >= 0){
								generatedFor = "Male";
							}else if(currentDate.compareTo(maleAcccdate) >= 0){
								generatedFor = "Female";
							}
						}else if(!study.isMaleRandamizationStatus()){
							if(currentDate.compareTo(maleAcccdate) >= 0){
								generatedFor = "Male";
							}
						}else if(!study.isFemaleRandamizationStatus()){
							if(currentDate.compareTo(maleAcccdate) >= 0){
								generatedFor = "Female";
							}
						}

					}
					String msg = "";
					if(study.isMaleRandamizationStatus()) {
//						generatedFor = "Male";
						model.addAttribute("tempRandamization", generatedFor);
						RandamizationDto dto = animalRadService.generatedRandamization(study, "Male"); 
						request.getSession().setAttribute("maledto", dto);
						msg = "Male Animals :" + dto.getApprovalStatus().getStatusDesc();
						fgflag = true;
					}
					if(study.isFemaleRandamizationStatus()) {
//						generatedFor = "Female";
						model.addAttribute("tempRandamization", generatedFor);
						RandamizationDto dto = animalRadService.generatedRandamization(study, "Female");
						request.getSession().setAttribute("femaledto", dto);
						if(msg.equals(""))
							msg = "Female Animals :" + dto.getApprovalStatus().getStatusDesc();
						else
							msg = msg+"<br/>"+ "Female Animals :" + dto.getApprovalStatus().getStatusDesc();
						fgflag = true;
					}
					if(!msg.equals("")) {
						model.addAttribute("pageMessage", msg);
					}
				}else generatedFor = study.getGender();
				
				if(fg && !generatedFor.equals("")) {
					String result = animalRadService.newRandamization(study, model, request, generatedFor);
					if (result.indexOf(0) == 1 || result.indexOf(0) == 2)
						model.addAttribute("rendamization", false);
					else 
						model.addAttribute("rendamization", true);
					if(result.indexOf(0) != 2)
						model.addAttribute("pageMessage", result.substring(1));				
					if(fgflag)
						model.addAttribute("randamizationGender", study.getGender());
				}else {
					model.addAttribute("pageMessage", "Randamizatoin not possible");	
				}

				
				return "animalRandomizationViewPage";
			} else {
				
				model.addAttribute("rendamization", false);
				model.addAttribute("tempRandamization", study.getGender());
				if(study.getGender().equals("Both")) {
					String msg = "";
					model.addAttribute("tempRandamization", "Both");
					RandamizationDto dto = animalRadService.generatedRandamization(study, "Male"); 
					if(dto != null) {
						request.getSession().setAttribute("tempmaledto", dto);
						msg = "Male Animals :" + dto.getApprovalStatus().getStatusDesc();
					}
					dto = animalRadService.generatedRandamization(study, "Female");
					if(dto != null) {
						request.getSession().setAttribute("tempfemaledto", dto);
						msg = msg+"<br/>"+ "Female Animals :" + dto.getApprovalStatus().getStatusDesc();
					}
					
					model.addAttribute("pageMessage", msg);
					if(study.getGender().equals("Both") && !study.getRadamizationStatus().getStatusCode().equals(StatusMasterCodes.APPROVED.toString())) {
						if(study.isMaleRandamizationStatus())
							model.addAttribute("randamizationGender", "Male");
						else
							model.addAttribute("randamizationGender", "Female");
					}
				}else {
					RandamizationDto dto = animalRadService.generatedRandamization(study, study.getGender());
					if(study.getGender().equals("Male"))
						request.getSession().setAttribute("tempmaledto", dto);
					else
						request.getSession().setAttribute("tempfemaledto", dto);
					model.addAttribute("randamizationGender", study.getGender());
					model.addAttribute("pageMessage", study.getGender() + " Animals :" + dto.getApprovalStatus().getStatusDesc());
				}
				return "animalRandomizationViewPage";
			}

		} else {
			model.addAttribute("pageMessage", "Study Meta data not avilable.");
			return "animalRandomizationViewPage";
		}
	}

	@RequestMapping(value = "/randomizationSendToReview", method = RequestMethod.POST)
	public String randomizationSendToReview(ModelMap model, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster study = studyService.findByStudyId(studyId);
		String gender = (String) request.getSession().getAttribute("randamizationGender");
		List<RandamizationDto> dtoList = new ArrayList<>();
		StatusMaster sendToReview = studyService.statusMaster(StatusMasterCodes.SENDTORIVEW.toString());
		if (gender.equals("Male")) {
			RandamizationDto dto = (RandamizationDto) request.getSession().getAttribute("maledto");
			dto.setApprovalStatus(sendToReview);
			dtoList.add(dto);
			study.setMaleRandamizationStatus(true);
		} else if (gender.equals("Female")) {
			RandamizationDto dto = (RandamizationDto) request.getSession().getAttribute("femaledto");
			dto.setApprovalStatus(sendToReview);
			dtoList.add(dto);
			study.setFemaleRandamizationStatus(true);
		} else {
			RandamizationDto dto = (RandamizationDto) request.getSession().getAttribute("maledto");
			dto.setApprovalStatus(sendToReview);
			dtoList.add(dto);
			study.setMaleRandamizationStatus(true);
			dto = (RandamizationDto) request.getSession().getAttribute("femaledto");
			dto.setApprovalStatus(sendToReview);
			dtoList.add(dto);
			study.setFemaleRandamizationStatus(true);
		}
		if(!study.getGender().equals("Both")) {
			study.setRadamizationStatus(sendToReview);
			study.setRandamizattionStatus(true);
		}else {
			if(study.isMaleRandamizationStatus() && study.isFemaleRandamizationStatus()) {
				study.setRandamizattionStatus(true);
				study.setRadamizationStatus(sendToReview);
			}
		}
		request.getSession().removeAttribute("randamizationGender");
		request.getSession().removeAttribute("maledto");
		request.getSession().removeAttribute("femaledto");
		if (!study.isSplitStudyByGender())
			study.setRadamizationStatus(sendToReview);
		else {
			if (study.isFemaleRandamizationStatus() && study.isMaleRandamizationStatus())
				study.setRadamizationStatus(sendToReview);
		}
		Long userId = (Long) request.getSession().getAttribute("userId");
		boolean flag = animalRadService.saverandomizationSendToReview(study, dtoList, userId);
		if (flag)
			redirectAttributes.addAttribute("pageMessage", "Randamizatoin sent to Review");
		else
			redirectAttributes.addAttribute("pageMessage", "Randamizatoin Faild to sent Review");
		return "redirect:/animalRadomization/";
	}
	
	@RequestMapping(value = "/old", method = RequestMethod.GET)
	public String generateRandomization(ModelMap model, HttpServletRequest request) {
		AnimalRandomizationServiceImpl.animalNo = 1;
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");
		StudyMaster study = studyService.findByStudyId(studyId);
		if (!study.getStatus().getStatusCode().equals(StatusMasterCodes.IN.toString())) {
			if (study.getRadamizationStatus() != null
					&& !study.getRadamizationStatus().getStatusCode().equals(StatusMasterCodes.REJECTED.toString())) {
				if (study.getRadamizationStatus().getStatusCode().equals(StatusMasterCodes.SENDTORIVEW.toString())
						|| study.getRadamizationStatus().getStatusCode().equals(StatusMasterCodes.INREVIEW.toString())
						|| study.getRadamizationStatus().getStatusCode().equals(StatusMasterCodes.REVIEWED.toString())
						|| study.getRadamizationStatus().getStatusCode()
								.equals(StatusMasterCodes.APPROVED.toString())) {

					boolean fg = false;
					if (study.getGender().equals("Both") && study.isSplitStudyByGender()) {
						Date currentDAte = new Date();
						if (currentDAte.after(study.getAcclimatizationStarDate())
								&& currentDAte.before(study.getAcclimatizationEndDate())) {
							if (!study.isMaleRandamizationReviewStatus())
								fg = true;
						} else if (currentDAte.after(study.getAcclimatizationStarDateFemale())
								&& currentDAte.before(study.getAcclimatizationEndDateFemale())) {
							if (!study.isFemaleRandamizationReviewStatus())
								fg = true;
						}
					}
					if (fg) {
						return genarateRandamization(studyId, model, request);
					} else {
						RadomizationReviewDto rdmDto = animalRadService.getRadomizationReviewDtoDetails(studyId);
						request.getSession().setAttribute("randamizationData", rdmDto);
						model.addAttribute("rdmDto", rdmDto);
						model.addAttribute("sendToReview", false);
						if (study.getRadamizationStatus().getStatusCode().equals(StatusMasterCodes.REJECTED.toString())
								|| study.getRadamizationStatus().getStatusCode()
										.equals(StatusMasterCodes.APPROVED.toString()))
							model.addAttribute("pageMessage", "Randomization has Reviewd .");
						else
							model.addAttribute("pageMessage", "Randomization is In Review .");

						request.getSession().setAttribute("randamization", rdmDto);
						return "animalRandomizationStatusList";
					}

				} else {
					return "animalRandomizationStatusList";
				}
			} else {
				return genarateRandamization(studyId, model, request);
			}
		} else {
			model.addAttribute("pageMessage", "Study Meta data not avilable.");
			return "animalRandomizationStatusList";
		}

	}
	private String genarateRandamization(Long studyId, ModelMap model, HttpServletRequest request) {
		RadomizationDto rdmDto = animalRadService.generateRandamization(studyId);
		request.getSession().setAttribute("randamizationData", rdmDto);
		model.addAttribute("rdmDto", rdmDto);
		model.addAttribute("studyId", studyId);
		model.addAttribute("sendToReview", true);
		randamizations.put(studyId, rdmDto);
//		request.getSession().setAttribute("randamization", rdmDto);
		if (rdmDto.getErrorCode() == 0 || rdmDto.getErrorCode() == 5)
			return "animalRandomizationStatusList";
		else {
			Long crfId = crfService
					.crfIdByName(environment.getRequiredProperty("accessionAnimalWeightFrom").toString());
			if (crfId != null) {
				Long userId = (Long) request.getSession().getAttribute("userId");
				AccessionAnimalDataviewDto aadDto = reviewService.getAccessionAnimalDataDtoDetails(studyId, crfId,
						userId, null, "", null);
				model.addAttribute("aadDto", aadDto);
				model.addAttribute("crfId", crfId);
			} else {
				model.addAttribute("aadDto", new AccessionAnimalDataviewDto());
				model.addAttribute("crfId", 0);
			}
			return "viewAccessionAnimalsDataForRandamization";
		}
	}

	@RequestMapping(value = "/generateRandomization/{studyId}", method = RequestMethod.GET)
	public String generateRandomization(ModelMap model, @PathVariable("studyId") Long studyId,
			HttpServletRequest request) {
		String userName = request.getSession().getAttribute("userName").toString();

		RandomizationGenerationDto rdmDto = animalRadService.getRadomizationDetails(studyId, userName);
		request.getSession().setAttribute("randmizedData", rdmDto.getSgsgList());
		model.addAttribute("rdmDto", rdmDto);
		model.addAttribute("stuydId", studyId);
		return "animalRandomization";
	}

	@RequestMapping(value = "/generateGropRandomization", method = RequestMethod.GET)
	public String generateGropRandomization(ModelMap model, @RequestParam("studyId") Long studyId,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		model.addAllAttributes(redirectAttributes.getFlashAttributes());
		FinalRandomizationDto frmd = animalRadService.generateFinalRandomizationData(studyId, request);
		model.addAttribute("frmd", frmd);
		request.getSession().setAttribute("randmizedData", frmd);
		model.addAttribute("stuydId", studyId);
		return "generateGropRandomization";
	}

	@RequestMapping(value = "/savegenerateGropRandomizationData", method = RequestMethod.POST)
	public String savegenerateGropRandomizationData(ModelMap model, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		Long userId = (Long) request.getSession().getAttribute("userId");
		Long studyId = (Long) request.getSession().getAttribute("activeStudyId");

		RadomizationDto rdmDto = (RadomizationDto) randamizations.get(studyId);
		randamizations.remove(studyId);
//		RadomizationDto rdmDto = (RadomizationDto) request.getSession().getAttribute("randamization");

		RadomizationReviewDto reviewrdmDto = new RadomizationReviewDto();
		reviewrdmDto.setGender(rdmDto.getGender());
		String result = null;
		try {
			if (rdmDto.getRandamizaitonSheetMale() != null) {
				RadamizationAllDataReview randamizaitonSheetMale = new RadamizationAllDataReview();
				BeanUtils.copyProperties(randamizaitonSheetMale, rdmDto.getRandamizaitonSheetMale());
				reviewrdmDto.setRandamizaitonSheetMale(randamizaitonSheetMale);
			}
			if (rdmDto.getRandamizaitonSheetFemale() != null) {
				RadamizationAllDataReview randamizaitonSheetFemale = new RadamizationAllDataReview();
				BeanUtils.copyProperties(randamizaitonSheetFemale, rdmDto.getRandamizaitonSheetFemale());
				reviewrdmDto.setRandamizaitonSheetFemale(randamizaitonSheetFemale);
			}

			if (rdmDto.getRandamizaitonSheetMaleAscedning() != null) {
				RadamizationAllDataReview randamizaitonSheetMaleAscedning = new RadamizationAllDataReview();
				BeanUtils.copyProperties(randamizaitonSheetMaleAscedning, rdmDto.getRandamizaitonSheetMaleAscedning());
				reviewrdmDto.setRandamizaitonSheetMaleAscedning(randamizaitonSheetMaleAscedning);
			}
			if (rdmDto.getRandamizaitonSheetFemaleAscedning() != null) {
				RadamizationAllDataReview randamizaitonSheetFemaleAscedning = new RadamizationAllDataReview();
				BeanUtils.copyProperties(randamizaitonSheetFemaleAscedning,
						rdmDto.getRandamizaitonSheetFemaleAscedning());
				reviewrdmDto.setRandamizaitonSheetFemaleAscedning(randamizaitonSheetFemaleAscedning);
			}
			if (rdmDto.getRandamizaitonSheetMaleGruoup() != null
					&& rdmDto.getRandamizaitonSheetMaleGruoup().size() > 0) {
				List<RadamizationAllDataReview> randamizaitonSheetMaleGruoup = new ArrayList<>();
				List<RadamizationAllData> randamizaitonSheetMaleGruoupold = rdmDto.getRandamizaitonSheetMaleGruoup();
				for (RadamizationAllData rad : randamizaitonSheetMaleGruoupold) {
					RadamizationAllDataReview radw = new RadamizationAllDataReview();
					BeanUtils.copyProperties(radw, rad);
					randamizaitonSheetMaleGruoup.add(radw);
				}
				reviewrdmDto.setRandamizaitonSheetMaleGruoup(randamizaitonSheetMaleGruoup);
			}

			if (rdmDto.getRandamizaitonSheetFemaleGruoup() != null
					&& rdmDto.getRandamizaitonSheetFemaleGruoup().size() > 0) {
				List<RadamizationAllDataReview> randamizaitonSheetFemaleGruoup = new ArrayList<>();
				List<RadamizationAllData> randamizaitonSheetFemaleGruoupold = rdmDto
						.getRandamizaitonSheetFemaleGruoup();
				for (RadamizationAllData rad : randamizaitonSheetFemaleGruoupold) {
					RadamizationAllDataReview radw = new RadamizationAllDataReview();
					BeanUtils.copyProperties(radw, rad);
					randamizaitonSheetFemaleGruoup.add(radw);
				}
				reviewrdmDto.setRandamizaitonSheetFemaleGruoup(randamizaitonSheetFemaleGruoup);
			}

			result = animalRadService.sendRandamizationToReview(reviewrdmDto, studyId, userId);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

//		String result = animalRadService.saveRandamization(rdmDto, studyId, userId);
//		String result = animalRadService.savesavegenerateGropRandomizationDataDetails(studyId, userName, request);
		if (result != null && result.equals("success"))
			redirectAttributes.addFlashAttribute("pageMessage", "Randomization Sent to Review Successfully...!");
		else
			redirectAttributes.addFlashAttribute("pageError", "Randomization Saving Failed. Please try again.");
		redirectAttributes.addAttribute("studyId", studyId);
		return "redirect:/animalRadomization/";
//		return "redirect:/animalRadomization/generateGropRandomization";
	}

}
