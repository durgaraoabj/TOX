package com.springmvc.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.covide.crf.dao.CrfDAO;
import com.covide.crf.dto.Crf;
import com.covide.crf.dto.CrfDescrpency;
import com.covide.crf.dto.CrfDescrpencyAudit;
import com.covide.crf.dto.CrfGroup;
import com.covide.crf.dto.CrfGroupElement;
import com.covide.crf.dto.CrfRule;
import com.covide.crf.dto.CrfRuleWithOther;
import com.covide.crf.dto.CrfSection;
import com.covide.crf.dto.CrfSectionElement;
import com.covide.crf.dto.CrfSectionElementData;
import com.covide.crf.dto.CrfSectionElementDataAudit;
import com.covide.crf.dto.CrfSectionElementInstrumentValue;
import com.covide.crf.dto.ObservationAnimalDataviewDto;
import com.covide.dto.CrfDataEntryDto;
import com.covide.dto.CrfFormulaDto;
import com.covide.dto.ExpermentalDto;
import com.covide.dto.SatusAndWorkFlowDetailsDto;
import com.covide.enums.StatusMasterCodes;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.springmvc.dao.ExpermentalDesignDao;
import com.springmvc.dao.StatusDao;
import com.springmvc.dao.StudyDao;
import com.springmvc.dao.UserDao;
import com.springmvc.dao.impl.ClinicalCodesDaoImple;
import com.springmvc.dto.FileInformation;
import com.springmvc.model.AmendmentDetails;
import com.springmvc.model.AnimalCage;
import com.springmvc.model.ApplicationAuditDetails;
import com.springmvc.model.GroupInfo;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.ObservationAnimalDataDto;
import com.springmvc.model.ReviewLevel;
import com.springmvc.model.StatusMaster;
import com.springmvc.model.StdSubGroupObservationCrfs;
import com.springmvc.model.StudyAcclamatizationData;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyCageAnimal;
import com.springmvc.model.StudyDesignStatus;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyTreatmentDataDates;
import com.springmvc.model.SubGroupAnimalsInfo;
import com.springmvc.model.SubGroupAnimalsInfoAll;
import com.springmvc.model.SubGroupAnimalsInfoCrfDataCount;
import com.springmvc.model.SubGroupAnimalsInfoCrfDataCountReviewLevel;
import com.springmvc.model.SubGroupInfo;
import com.springmvc.model.SubjectDataEntryDetails;
import com.springmvc.model.SysmexAnimalCode;
import com.springmvc.model.WorkFlow;
import com.springmvc.model.WorkFlowReviewAudit;
import com.springmvc.model.WorkFlowReviewStages;
import com.springmvc.model.WorkFlowStatusDetails;
import com.springmvc.service.ExpermentalDesignService;
import com.springmvc.service.StudyService;
import com.springmvc.util.ClinicalObservationRecordTimePoint;
import com.springmvc.util.ClinicalObservationRecordTimePointElements;
import com.springmvc.util.DailyClinicalObservation;
import com.springmvc.util.DetailedClinicalObservations;
import com.springmvc.util.DetailedClinicalObservationsAnimlas;
import com.springmvc.util.ExprementalData;
import com.springmvc.util.ExprementalDataExport;
import com.springmvc.util.MotorActivityDataOfIndividualAnimal;
import com.springmvc.util.MotorActivityDataOfIndividualAnimalData;
import com.springmvc.util.NeurobehavioralObservationsofIndividual;
import com.springmvc.util.NeurobehavioralObservationsofIndividualAnimal;
import com.springmvc.util.ObservationData;
import com.springmvc.util.OphthalmologicalExamination;
import com.springmvc.util.OphthalmologicalExaminationAnimlas;
import com.springmvc.util.RecordforMortalityMorbidity;
import com.springmvc.util.SensoryReactivityOfIndividual;
import com.springmvc.util.SensoryReactivityOfIndividualData;

@Service("expermentalDesignService")
@PropertySource(value = { "classpath:application.properties" })
public class ExpermentalDesignServiceImpl implements ExpermentalDesignService {
	@Autowired
	private Environment environment;
	@Autowired
	private StatusDao statusDao;
	@Autowired
	private ExpermentalDesignDao expermentalDesignDao;

	@Autowired
	CrfDAO crfDAO;

	@Autowired
	StudyDao studyDao;
	@Autowired
	UserDao userDao;
	@Autowired
	ClinicalCodesDaoImple clinicalCodeDao;

	@Override
	public List<GroupInfo> studyGroupInfo(StudyMaster sm) {
		return expermentalDesignDao.studyGroupInfo(sm);
	}

	@Override
	public String saveExpermentalDesign(StudyMaster sm, List<SubGroupInfo> subGroups,
			List<SubGroupAnimalsInfo> animalInfo, List<SubGroupAnimalsInfoAll> animalInfoAll, StudyDesignStatus sds,
			ApplicationAuditDetails apad) {
		return expermentalDesignDao.saveExpermentalDesign(sm, subGroups, animalInfo, animalInfoAll, sds, apad);

	}

	@Override
	public List<SubGroupAnimalsInfo> subGroupAnimalsInfo(SubGroupInfo sgi) {
		// TODO Auto-generated method stub
		return expermentalDesignDao.subGroupAnimalsInfo(sgi);
	}

	@Override
	public List<SubGroupInfo> studySubGroupInfo(GroupInfo gi) {
		// TODO Auto-generated method stub
		return expermentalDesignDao.studySubGroupInfo(gi);
	}

	@Override
	public List<SubGroupInfo> studySubGroupInfo(StudyMaster sm) {
		// TODO Auto-generated method stub
		return expermentalDesignDao.studySubGroupInfo(sm);
	}

	@Override
	public List<GroupInfo> studyGroupInfoWithChaild(StudyMaster sm) {
		// TODO Auto-generated method stub
		return expermentalDesignDao.studyGroupInfoWithChaild(sm);
	}
//	@Override
//	public List<GroupInfo> studyGroupInfoWithChaildReview(StudyMaster sm) {
//		// TODO Auto-generated method stub
//		return expermentalDesignDao.studyGroupInfoWithChaildReview(sm);
//	}

	@Override
	public List<Crf> findAllCrfsOfsubGroup(StudyMaster sm, Long id) {
		// TODO Auto-generated method stub
		return expermentalDesignDao.findAllCrfsOfsubGroup(sm, id);
	}

	@Override
	public SubGroupInfo subGroupInfoById(StudyMaster sm, Long id) {
		// TODO Auto-generated method stub
		return expermentalDesignDao.subGroupInfoById(sm, id);
	}

	@Override
	public Crf studyCrf(StudyMaster sm, Long subGroupId, Long crfId) {
		// TODO Auto-generated method stub
		return expermentalDesignDao.studyCrf(sm, subGroupId, crfId);
	}

	@Override
	public Map<String, String> allElementIdsTypesJspStd(Crf crf) {
		Map<String, String> map = new HashMap<>();
		for (CrfSection sec : crf.getSections()) {
			if (sec.getElement() != null && sec.getElement().size() > 0) {
				for (CrfSectionElement ele : sec.getElement()) {
					map.put(ele.getId() + "_" + ele.getName(), ele.getType());
				}
			}
			CrfGroup group = sec.getGroup();
			if (group != null) {
				int rowCount = group.getMinRows();
				if (group.getElement() != null && group.getElement().size() > 0) {
					for (CrfGroupElement ele : group.getElement()) {
						for (int i = 1; i <= rowCount; i++) {
							String id = "g_" + ele.getId() + "_" + ele.getName() + "_" + i;
							map.put(id, ele.getType());
						}
					}
				}
			}
		}
		return map;
	}

	@Override
	public Map<String, String> requiredElementIdInJspStd(Crf crf, String userRole) {
		Map<String, String> map = new HashMap<>();
		for (CrfSection sec : crf.getSections()) {
			String[] roles = sec.getRoles().split("\\,");
			List<String> rolesList = new ArrayList<>();
			for (String role : roles)
				rolesList.add(role.trim());
			if (sec.getRoles().equals("ALL") || rolesList.contains(userRole)) {
				if (sec.getElement() != null && sec.getElement().size() > 0) {
					for (CrfSectionElement ele : sec.getElement()) {
						if (ele.isRequired())
							map.put(ele.getId() + "_" + ele.getName(), ele.getType());
					}
				}
				CrfGroup group = sec.getGroup();
				if (group != null) {
					int rowCount = group.getMinRows();
					if (group.getElement() != null && group.getElement().size() > 0) {
						for (CrfGroupElement ele : group.getElement()) {
							if (ele.isRequired()) {
								for (int i = 1; i <= rowCount; i++) {
									String id = "g_" + ele.getId() + "_" + ele.getName() + "_" + i;
									map.put(id, ele.getType());
								}
							}
						}
					}
				}
			}
		}
		return map;
	}

	@Override
	public Map<String, String> pattrenIdsAndPattrenStd(Crf crf) {
		Map<String, String> map = new HashMap<>();
		for (CrfSection sec : crf.getSections()) {
			if (sec.getElement() != null && sec.getElement().size() > 0) {
				for (CrfSectionElement ele : sec.getElement()) {
					if (ele.getPattren() != null && !ele.getPattren().trim().equals("")) {
						map.put(ele.getId() + "_" + ele.getName(), ele.getPattren());
					}
				}
			}
			CrfGroup group = sec.getGroup();
			if (group != null) {
				int rowCount = group.getMinRows();
				if (group.getElement() != null && group.getElement().size() > 0) {
					for (CrfGroupElement ele : group.getElement()) {
						if (ele.getPattren() != null && !ele.getPattren().trim().equals("")) {
							for (int i = 1; i <= rowCount; i++) {
								String id = "g_" + ele.getId() + "_" + ele.getName() + "_" + i;
								map.put(id, ele.getPattren());
							}
						}
					}
				}
			}
		}
		return map;
	}

	@Override
	public SubjectDataEntryDetails studyTreatmentDataEntry(Long stdSubGroupObservationCrfsId,
			Long studyTreatmentDataDatesId, Long crfId, Long subGroupId, String seletedGender, String type,
			int noOfEntry, Long userId, StudyMaster sm, Long animalId, String selecteDate, String discrebencyFields,
			String deviationMessage, String frequntlyMessage, HttpServletRequest request, String type2)
			throws ParseException {
		StatusMaster dataEntered = statusDao.statusMaster(StatusMasterCodes.DATAENTRY.toString());
		StatusMaster senttoReview = statusDao.statusMaster(StatusMasterCodes.SENDTORIVEW.toString());
		StudyAnimal animal = studyDao.studyAnimalsById(animalId);
		LoginUsers user = userDao.findById(userId);
		Set<String> discrebencyFieldsSet = new HashSet<>();
		if (discrebencyFields != null && !discrebencyFields.trim().equals("")) {
			String[] ss = discrebencyFields.split(",");
			for (String s : ss)
				discrebencyFieldsSet.add(s);
		}
		List<CrfSectionElementData> secList = new ArrayList<>();
		List<Long> secIdList = new ArrayList<>();

		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		Date fromDate = new Date();
		try {
			if (selecteDate == null || selecteDate.trim().equals(""))
				fromDate = sdf.parse(sdf.format(fromDate));
			else
				fromDate = sdf.parse(selecteDate);
		} catch (Exception e) {
			// TODO: handle exception
		}

		StdSubGroupObservationCrfs stdCrf = crfDAO.stdSubGroupObservationCrfsWithCrf(stdSubGroupObservationCrfsId);
		StudyTreatmentDataDates date = null;
		if (studyTreatmentDataDatesId != null)
			date = expermentalDesignDao.studyTreatmentDataDates(studyTreatmentDataDatesId);
		else
			date = expermentalDesignDao.studyTreatmentDataDates(stdCrf.getId(), true, fromDate, seletedGender);

		Crf crf = stdCrf.getCrf();
//		SubGroupAnimalsInfoAll animalInfoAll = expermentalDesignDao.subGroupAnimalsInfoAll(subGroupInfoAllId);

		SubjectDataEntryDetails sded = new SubjectDataEntryDetails();// expermentalDesignDao.getSubjectDataEntryDetailsRecord(animalInfoAll,

		sded.setStudy(sm);
		sded.setGroup(stdCrf.getSubGroupInfo().getGroup());
		sded.setSubGroup(stdCrf.getSubGroupInfo());
		SubGroupAnimalsInfo subGroupAnimalsInfo = null;
		if (animal != null)
			subGroupAnimalsInfo = expermentalDesignDao.subGroupAnimalsInfo(sm.getId(), stdCrf.getSubGroupInfo(),
					animal.getGender());
		sded.setSubGroupAnaimal(subGroupAnimalsInfo);
//		sded.setStudyAnimalCage(studyCageAnimal);
		sded.setAnimal(animal);
		sded.setObservationCrf(stdCrf);
		sded.setStudyTreatmentDataDates(date);
		sded.setNoOfEntry(noOfEntry); // crfId);
		sded.setEntredby(user);
		sded.setEntredAs(type);
		if (animal != null)
			sded.setAnimalno(animal.getAnimalId());
		sded.setEntryType(type);
		sded.setStatus(dataEntered);
		sded.setGender(seletedGender);
		sded.setStudyTreatmentDataDates(date);
		String status = "success";
		List<CrfSectionElementDataAudit> audit = new ArrayList<>();
		List<CrfSectionElementData> sectionData = new ArrayList<>();
		List<Long> sectionEleIds = new ArrayList<>();

		for (CrfSection sec : crf.getSections()) {
			if (sec.getElement() != null && sec.getElement().size() > 0) {
				for (CrfSectionElement ele : sec.getElement()) {
					sectionEleIds.add(ele.getId());
					String id = ele.getId() + "_" + ele.getName();
					String values = "";
					if (ele.getType().equals("checkBox")) {
						String[] s = request.getParameterValues(id);
						boolean flag = true;
						if (s != null && s.length > 0) {
							for (String sv : s) {
								if (flag) {
									values = sv.trim();
									flag = false;
								} else
									values += "#####" + sv.trim();
							}
						}
					} else {
						if (request.getParameter(id) != null)
							values = request.getParameter(id).trim();
					}
					CrfSectionElementData data = new CrfSectionElementData();
					data.setStudyId(sm.getId());
					data.setAnimal(animal);
					data.setCrf(crf);
					data.setSubjectDataEntryDetails(sded);
					data.setKayName(id);
					data.setValue(values);
					data.setElement(ele);
					data.setEntredBy(user);
					data.setEntredOn(new Date());
					data.setEntryType(type);
					if (ele.getClinicalCode() != null && !ele.getClinicalCode().equals("")
							&& !ele.getClinicalCode().equals("Animal Number")) {
						data.setClinicalCodes(clinicalCodeDao.getByKey(Long.parseLong(data.getValue())));
					}

					sectionData.add(data);
					CrfSectionElementDataAudit saudit = new CrfSectionElementDataAudit();
					saudit.setStudy(sm);
					saudit.setSubjectDataEntryDetails(sded);
					saudit.setAnimalObsercationElementData(data);
					saudit.setElement(data.getElement());
					saudit.setKayName(data.getKayName());
					saudit.setNewValue(values);
					saudit.setLoginUsers(user);
					saudit.setCreatedOn(new Date());
					audit.add(saudit);

					if (discrebencyFieldsSet.contains(id)) {
						secList.add(data);
						secIdList.add(ele.getId());
					}
				}
			}
		}

		// save sectionData and groupoData
		crfDAO.saveStudySectionData2(sectionData, sded);
		crfDAO.saveCrfSectionElementDataAuditList(audit);
		List<CrfDescrpency> desc = checkCrfRules1(stdCrf, null, secList, crf, user.getUsername(), true, secIdList);

		crfDAO.saveStudyCrfDescrpencyList(desc);
		for (CrfDescrpency scd : desc) {
			CrfDescrpencyAudit descAudit = new CrfDescrpencyAudit();
			descAudit.setStdSubGroupObservationCrfs(stdCrf);
			descAudit.setDesc(scd);
			descAudit.setCrf(scd.getCrf());
			descAudit.setVolPeriodCrf(scd.getVolPeriodCrf());
			descAudit.setKayName(scd.getKayName());
			descAudit.setSecElement(scd.getSecElement());
			descAudit.setSecEleData(scd.getSecEleData());
			descAudit.setCrfRule(scd.getCrfRule());
			descAudit.setStatus(scd.getStatus());
			descAudit.setAssingnedTo(scd.getAssingnedTo());
			descAudit.setRisedBy(scd.getRisedBy());
			descAudit.setOldValue(scd.getOldValue());
			descAudit.setOldStatus(scd.getOldStatus());
			crfDAO.saveCrfDescrpencyAudit(descAudit);
		}

		StatusMaster activeStatus = statusDao.statusMaster("ACTIVE");
		WorkFlow accessionWorkFlow = userDao.accessionWorkFlow("OBSERCVATION", activeStatus);
		WorkFlowReviewStages workFlowReviewStages = userDao.workFlowReviewStages(user, accessionWorkFlow);
		if (workFlowReviewStages != null) {
			WorkFlowReviewAudit workFlowReviewAudit = new WorkFlowReviewAudit();
			workFlowReviewAudit.setSubjectDataEntryDetails(sded);
			workFlowReviewAudit.setReviewState(workFlowReviewStages);
			workFlowReviewAudit.setRole(workFlowReviewStages.getFromRole());
			workFlowReviewAudit.setUser(user);
			workFlowReviewAudit.setDateOfActivity(new Date());
			crfDAO.saveWorkFlowReviewAudit(workFlowReviewAudit);
			sded.setCurrentReviewRole(workFlowReviewStages.getToRole());
			sded.setStatus(senttoReview);
			crfDAO.updateSubjectDataEntryDetails(sded);
		}
		return sded;
	}

//	@Override
	public SubjectDataEntryDetails studyCrfDataEntry(Long studyCageAnimalId, Long subGroupInfoAllId, Long groupId,
			Long subGroupId, Long subGroupInfoId, Long crfId, Long stdSubGroupObservationCrfsId, Long userId,
			StudyMaster sm, String discrebencyFields, String deviationMessage, String frequntlyMessage,
			HttpServletRequest request, String type) {
//		StatusMaster dataEntered = statusDao.statusMaster(StatusMasterCodes.DATAENTRY.toString());
//		StatusMaster senttoReview = statusDao.statusMaster("Sent to Review");
//		StudyCageAnimal studyCageAnimal = expermentalDesignDao.studyCageAnimalWithId(studyCageAnimalId);
//		LoginUsers user = userDao.findById(userId);
//		Set<String> discrebencyFieldsSet = new HashSet<>();
//		if (!discrebencyFields.trim().equals("")) {
//			String[] ss = discrebencyFields.split(",");
//			for (String s : ss)
//				discrebencyFieldsSet.add(s);
//		}
//		List<CrfSectionElementData> secList = new ArrayList<>();
//		List<Long> secIdList = new ArrayList<>();
//
//		StdSubGroupObservationCrfs stdCrf = crfDAO.stdSubGroupObservationCrfsWithCrf(stdSubGroupObservationCrfsId);
//		Crf crf = stdCrf.getCrf();
////		SubGroupAnimalsInfoAll animalInfoAll = expermentalDesignDao.subGroupAnimalsInfoAll(subGroupInfoAllId);
//
//		SubjectDataEntryDetails sded = new SubjectDataEntryDetails();// expermentalDesignDao.getSubjectDataEntryDetailsRecord(animalInfoAll,
//																		// crfId);
//		sded.setStudy(studyCageAnimal.getStudy());
//		sded.setGroup(stdCrf.getSubGroupInfo().getGroup());
//		sded.setSubGroup(stdCrf.getSubGroupInfo());
//		SubGroupAnimalsInfo subGroupAnimalsInfo = expermentalDesignDao.subGroupAnimalsInfo(
//				studyCageAnimal.getStudy().getId(), stdCrf.getSubGroupInfo(), studyCageAnimal.getAnimal().getGender());
//		sded.setSubGroupAnaimal(subGroupAnimalsInfo);
//		sded.setCage(studyCageAnimal.getCage());
//		sded.setStudyAnimalCage(studyCageAnimal);
//		sded.setAnimal(studyCageAnimal.getAnimal());
//		sded.setObservationCrf(stdCrf);
//		sded.setEntredby(user);
//		sded.setEntredAs(type);
//		sded.setAnimalno(studyCageAnimal.getAnimal().getAnimalId());
//		sded.setEntryType(type);
//		sded.setStatus(dataEntered);
//
//		String status = "success";
//		List<CrfSectionElementDataAudit> audit = new ArrayList<>();
//		List<CrfSectionElementData> sectionData = new ArrayList<>();
//		List<Long> sectionEleIds = new ArrayList<>();
//
//		for (CrfSection sec : crf.getSections()) {
//			if (sec.getElement() != null && sec.getElement().size() > 0) {
//				for (CrfSectionElement ele : sec.getElement()) {
//					sectionEleIds.add(ele.getId());
//					String id = ele.getId() + "_" + ele.getName();
//					String values = "";
//					if (ele.getType().equals("checkBox")) {
//						String[] s = request.getParameterValues(id);
//						boolean flag = true;
//						if (s != null && s.length > 0) {
//							for (String sv : s) {
//								if (flag) {
//									values = sv.trim();
//									flag = false;
//								} else
//									values += "#####" + sv.trim();
//							}
//						}
//					} else {
//						if (request.getParameter(id) != null)
//							values = request.getParameter(id).trim();
//					}
//					CrfSectionElementData data = new CrfSectionElementData();
//					data.setStudyId(sm.getId());
//					data.setAnimal(studyCageAnimal.getAnimal());
//					data.setCrf(crf);
//					data.setSubjectDataEntryDetails(sded);
//					data.setKayName(id);
//					data.setValue(values);
//					data.setElement(ele);
//					data.setEntredBy(user);
//					data.setEntredOn(new Date());
//					data.setEntryType(type);
//
//					sectionData.add(data);
//					CrfSectionElementDataAudit saudit = new CrfSectionElementDataAudit();
//					saudit.setStudy(sm);
//					saudit.setSubjectDataEntryDetails(sded);
//					saudit.setAnimalObsercationElementData(data);
//					saudit.setElement(data.getElement());
//					saudit.setKayName(data.getKayName());
//					saudit.setNewValue(values);
//					saudit.setLoginUsers(user);
//					saudit.setCreatedOn(new Date());
//					audit.add(saudit);
//
//					if (discrebencyFieldsSet.contains(id)) {
//						secList.add(data);
//						secIdList.add(ele.getId());
//					}
//				}
//			}
//		}
//
//		// save sectionData and groupoData
//		crfDAO.saveStudySectionData2(sectionData, sded);
//		crfDAO.saveCrfSectionElementDataAuditList(audit);
//		List<CrfDescrpency> desc = checkCrfRules1(stdCrf, null, secList, crf, user.getUsername(), true, secIdList);
//
//		crfDAO.saveStudyCrfDescrpencyList(desc);
//		for (CrfDescrpency scd : desc) {
//			CrfDescrpencyAudit descAudit = new CrfDescrpencyAudit();
//			descAudit.setStdSubGroupObservationCrfs(stdCrf);
//			descAudit.setDesc(scd);
//			descAudit.setCrf(scd.getCrf());
//			descAudit.setVolPeriodCrf(scd.getVolPeriodCrf());
//			descAudit.setKayName(scd.getKayName());
//			descAudit.setSecElement(scd.getSecElement());
//			descAudit.setSecEleData(scd.getSecEleData());
//			descAudit.setCrfRule(scd.getCrfRule());
//			descAudit.setStatus(scd.getStatus());
//			descAudit.setAssingnedTo(scd.getAssingnedTo());
//			descAudit.setRisedBy(scd.getRisedBy());
//			descAudit.setOldValue(scd.getOldValue());
//			descAudit.setOldStatus(scd.getOldStatus());
//			crfDAO.saveCrfDescrpencyAudit(descAudit);
//		}
//
//		StatusMaster activeStatus = statusDao.statusMaster("ACTIVE");
//		WorkFlow accessionWorkFlow = userDao.accessionWorkFlow("OBSERCVATION", activeStatus);
//		WorkFlowReviewStages workFlowReviewStages = userDao.workFlowReviewStages(user, accessionWorkFlow);
//		if (workFlowReviewStages != null) {
//			WorkFlowReviewAudit workFlowReviewAudit = new WorkFlowReviewAudit();
//			workFlowReviewAudit.setSubjectDataEntryDetails(sded);
//			workFlowReviewAudit.setReviewState(workFlowReviewStages);
//			workFlowReviewAudit.setRole(workFlowReviewStages.getFromRole());
//			workFlowReviewAudit.setUser(user);
//			workFlowReviewAudit.setDateOfActivity(new Date());
//			crfDAO.saveWorkFlowReviewAudit(workFlowReviewAudit);
//			sded.setCurrentReviewRole(workFlowReviewStages.getToRole());
//			sded.setStatus(senttoReview);
//			crfDAO.updateSubjectDataEntryDetails(sded);
//		}
//		return sded;
		return null;
	}

	private List<CrfDescrpency> checkCrfRules1(StdSubGroupObservationCrfs stdSubGroupObservationCrfs,
			SubGroupAnimalsInfoAll animalInfo1, List<CrfSectionElementData> sectionData, Crf crf, String username,
			boolean dataFlag, List<Long> sectionIdsList) {
		List<CrfDescrpency> descList = new ArrayList<>();
		Map<String, CrfDescrpency> map = new HashMap<>();
		List<CrfRule> rules = crfDAO.crfRuleOfSubElements(crf, sectionIdsList);
		if (rules.size() > 0) {
			Map<String, CrfSectionElementData> secData = new HashMap<>();
			for (CrfSectionElementData s : sectionData)
				secData.put(s.getKayName(), s);

			for (CrfRule rule : rules) {
				if (rule.getSecEle() != null) {
					CrfSectionElementData data = secData
							.get(rule.getSecEle().getId() + "_" + rule.getSecEle().getName());
					CrfDescrpency scd = null;
					if (dataFlag)
						scd = expermentalDesignDao.studyCrfDescrpencySec(stdSubGroupObservationCrfs, crf, null, data);
					if (scd == null) {
						scd = new CrfDescrpency();
						scd.setStudy(stdSubGroupObservationCrfs.getStudy());
						scd.setValue(data.getValue());
						scd.setCrf(crf);
						scd.setStdSubGroupObservationCrfs(stdSubGroupObservationCrfs);
						scd.setKayName(data.getKayName());
						scd.setSecElement(data.getElement());
						scd.setSecEleData(data);
						scd.setCrfRule(rule);

						scd.setCreatedBy(username);
						scd.setCreatedOn(new Date());
						if (map.get(data.getKayName()) == null) {
							descList.add(scd);
						}
					}
					map.put(data.getKayName(), scd);
				}
			}
		}

		return descList;
	}

	private List<CrfDescrpency> checkCrfRules(SubGroupAnimalsInfoCrfDataCount animalData,
			SubGroupAnimalsInfoAll animalInfo, List<CrfSectionElementData> sectionData, Crf crf, String username,
			boolean dataFlag) {
		List<CrfDescrpency> descList = new ArrayList<>();
		Map<String, CrfDescrpency> map = new HashMap<>();
		List<CrfRule> rules = crfDAO.crfRuleWithCrfAndSubElements(crf);
		if (rules.size() > 0) {
			Map<String, CrfSectionElementData> secData = new HashMap<>();
			for (CrfSectionElementData s : sectionData)
				secData.put(s.getKayName(), s);

			for (CrfRule rule : rules) {

				if (rule.getSecEle() != null) {
					boolean result = false;
					CrfSectionElementData data = secData
							.get(rule.getSecEle().getId() + "_" + rule.getSecEle().getName());
					CrfSectionElementData sdata = null;
//					CrfGroupElementData gdata = null;
					if (data != null && data.getValue() != null && !data.getValue().equals("")) {
						String value = data.getValue();
						if (!rule.getCondtion1().equals("-1")) {
							result = checkCondition(value, rule.getValue1(), rule.getCondtion1(),
									rule.getSecEle().getType(), rule.getSecEle().getDataType());
						}
						if (!rule.getCondtion2().equals("-1")) {
							boolean result2 = checkCondition(value, rule.getValue2(), rule.getCondtion2(),
									rule.getSecEle().getType(), rule.getSecEle().getDataType());
							if (rule.getName().equals("and"))
								result = result && result2;
							else
								result = result || result2;
						}

						List<CrfRuleWithOther> otherCrf = rule.getOtherCrf();
						for (CrfRuleWithOther scrwo : otherCrf) {
							String v2 = null;
							if (scrwo.getCrf().getId() == crf.getId()) {
								if (scrwo.getSecEle() != null) {
									sdata = secData.get(scrwo.getSecEle().getId() + "_" + scrwo.getSecEle().getName());
									if (sdata != null)
										v2 = sdata.getValue();
								}

							} else {
								// check with other crf logic
								if (scrwo.getSecEle() != null)
									v2 = expermentalDesignDao.studyCrfSectionElementData(animalData, scrwo.getSecEle());
//								if(scrwo.getGroupEle() != null) v2 = crfDAO.studyCrfGroupElementData(vpc, scrwo.getGroupEle(), "g_"+scrwo.getGroupEle().getId()+"_"+scrwo.getGroupEle()+"_"+scrwo.getRowNo());
							}
							if (v2 != null) {
								boolean result2 = checkCondition(value, v2, scrwo.getCondtion(),
										rule.getSecEle().getType(), rule.getSecEle().getDataType());
								if (scrwo.getNcondtion().equals("and"))
									result = result && result2;
								else
									result = result || result2;
							}
						}
					}
					if (result) {
						CrfDescrpency scd = null;
//						if(dataFlag)   swami
//							scd = expermentalDesignDao.studyCrfDescrpencySec(animalData, crf, null, data);
//						if(scd == null) {
//							scd =  new CrfDescrpency();
//							scd.setCrf(crf);
//							scd.setStdSubGroupObservationCrfs(stdSubGroupObservationCrfs);
//							scd.setKayName(data.getKayName());
//							scd.setSecElement(data.getElement());
//							scd.setSecEleData(data);
//							scd.setCrfRule(rule);
//							scd.setCreatedBy(username);
//							scd.setCreatedOn(new Date());
//							if(map.get(data.getKayName()) == null) {
//								descList.add(scd);
//							}
//
//							
//						}
						map.put(data.getKayName(), scd);
					}
				}
			}

		}

		return descList;
	}

	private boolean checkCondition(String value, String value1, String condtion, String type, String dataType) {
		boolean flag = false;
//		text/textArea/radio/select/checkBox/
		if (type.equals("text") || type.equals("textArea") || type.equals("select") || type.equals("checkBox")) {
			switch (condtion) {
			case "eq":
				if (value.equals(value1))
					flag = true;
				break;
			case "ne":
				if (!value.equals(value1))
					flag = true;
				break;
			case "le":
				if (dataType.equals("Number")) {
					if (Double.parseDouble(value) < Double.parseDouble(value1))
						flag = true;
				} else {
					if (value.length() < Double.parseDouble(value1))
						flag = true;
				}
				break;
			case "leq":
				if (dataType.equals("Number")) {
					if (Double.parseDouble(value) <= Double.parseDouble(value1))
						flag = true;
				} else {
					if (value.length() <= Double.parseDouble(value1))
						flag = true;
				}
				break;
			case "gt":
				if (dataType.equals("Number")) {

					if (Double.parseDouble(value) > Double.parseDouble(value1))
						flag = true;
				} else {
					if (value.length() > Double.parseDouble(value1))
						flag = true;
				}
				break;
			case "gtq":
				if (dataType.equals("Number")) {
					if (Double.parseDouble(value) >= Double.parseDouble(value1))
						flag = true;
				} else {
					if (value.length() >= Double.parseDouble(value1))
						flag = true;
				}
				break;
			case "gte":
				if (dataType.equals("Number")) {
					if (Double.parseDouble(value) >= Double.parseDouble(value1))
						flag = true;
				} else {
					if (value.length() >= Double.parseDouble(value1))
						flag = true;
				}
				break;
			default:
				break;
			}
		} else {
//			date/dateAndTime/
			if (type.equals("date") || type.equals("date")) {
				SimpleDateFormat sdfo = null;
				if (type.equals("date"))
					sdfo = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
				else
					sdfo = new SimpleDateFormat(environment.getRequiredProperty("dateTimeFormat"));
				try {
					Date d1 = sdfo.parse(value);
					Date d2 = sdfo.parse(value1);
					switch (condtion) {
					case "eq":
						if (d1.compareTo(d2) == 0) {
							flag = true;
						}
					case "ne":
						if (d1.compareTo(d2) != 0) {
							flag = true;
						}
					case "le":
						if (d1.compareTo(d2) < 0) {
							flag = true;
						}
						break;
					case "leq":
						if (d1.compareTo(d2) < 0 || d1.compareTo(d2) == 0) {
							flag = true;
						}
						break;
					case "gt":
						if (d1.compareTo(d2) > 0) {
							flag = true;
						}
						break;
					case "gtq":
						if (d1.compareTo(d2) > 0 || d1.compareTo(d2) >= 0) {
							flag = true;
						}
						break;
					case "gte":
						if (d1.compareTo(d2) > 0 || d1.compareTo(d2) >= 0) {
							flag = true;
						}
						break;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		return flag;
	}

	@Override
	public ObservationData observationData(Long subGroupInfoId, Long stdSubGroupObservationCrfsId, LoginUsers user) {
		return expermentalDesignDao.observationData(subGroupInfoId, stdSubGroupObservationCrfsId, user);
	}

	@Override
	public SubGroupInfo subGroupInfoAllById(StudyMaster sm, Long subGroupId, Long gorupId) {
		if (gorupId != null) {
			subGroupId = expermentalDesignDao.subGroupId(gorupId);
		}
		return expermentalDesignDao.subGroupInfoAllById(sm, gorupId, subGroupId);
	}

	@Override
	public SubGroupInfo subGroupInfoAllByIdReview(StudyMaster sm, Long id) {
		return expermentalDesignDao.subGroupInfoAllByIdReview(sm, id);
	}

	@Override
	public File exportDataToExcel(ObservationAnimalDataviewDto data, HttpServletRequest request) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("dd_MMM_yyyy");
		String dateinSting = sdf.format(new Date());

		// Create a Workbook
		Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

		/*
		 * CreationHelper helps us create instances of various things like DataFormat,
		 * Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way
		 */
		CreationHelper createHelper = workbook.getCreationHelper();

		// Create a Sheet
		// Sheet sheet = workbook.createSheet(data.getAnimal()+"_"+dateinSting);
		Sheet sheet = workbook.createSheet("Export Data");

		// Create a Font for styling header cells
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		headerFont.setColor(IndexedColors.RED.getIndex());

		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);

		int rowCount = 0;
		int cellCount = 0;
		// Create a Row
		Row headerRow = sheet.createRow(rowCount++);
//        private StudyMaster study;
		CellStyle dateCellStyle = workbook.createCellStyle();
		dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MMM-yyyy"));
		CellStyle dateCellStyle2 = workbook.createCellStyle();
		dateCellStyle2.setDataFormat(createHelper.createDataFormat().getFormat("dd-MMM-yyyy  hh:mm"));

		Cell cell = headerRow.createCell(cellCount++);
		cell.setCellValue("S.No.");
		cell = headerRow.createCell(cellCount++);
		cell.setCellValue("Animal ID");
		cell = headerRow.createCell(cellCount++);
		cell.setCellValue("Entry Type");
//		cell = headerRow.createCell(cellCount++);
//		cell.setCellValue("Entered By");
		cell = headerRow.createCell(cellCount++);
		cell.setCellValue("Entered On");

		Map<Long, CrfSectionElement> elements = data.getElements();
		for (Map.Entry<Long, CrfSectionElement> m : elements.entrySet()) {
			cell = headerRow.createCell(cellCount++);
			if (m.getValue().getLeftDesc() != null && !m.getValue().getLeftDesc().trim().equals("")) {
				cell.setCellValue(m.getValue().getLeftDesc());
			} else if (m.getValue().getRigtDesc() != null && !m.getValue().getRigtDesc().trim().equals("")) {
				cell.setCellValue(m.getValue().getRigtDesc());
			} else if (m.getValue().getTopDesc() != null && !m.getValue().getTopDesc().trim().equals("")) {
				cell.setCellValue(m.getValue().getTopDesc());
			} else if (m.getValue().getBottemDesc() != null && !m.getValue().getBottemDesc().trim().equals("")) {
				cell.setCellValue(m.getValue().getBottemDesc());
			} else if (m.getValue().getMiddeDesc() != null && !m.getValue().getMiddeDesc().trim().equals("")) {
				cell.setCellValue(m.getValue().getMiddeDesc());
			} else {
				cell.setCellValue(m.getValue().getName());
			}
		}

		int count = 1;
		List<ObservationAnimalDataDto> animalData = data.getAnimalData();
		for (ObservationAnimalDataDto obj : animalData) {
			cellCount = 0;
			headerRow = sheet.createRow(rowCount++);

			cell = headerRow.createCell(cellCount++);
			cell.setCellValue(count++);
			cell = headerRow.createCell(cellCount++);
			cell.setCellValue(obj.getAnimalId());
			cell = headerRow.createCell(cellCount++);
			cell.setCellValue(obj.getEntryType());
//			cell = headerRow.createCell(cellCount++);
//			cell.setCellValue(obj.getDeviationMessage());
//			cell = headerRow.createCell(cellCount++);
//			cell.setCellValue(obj.getUserName());
			cell.setCellStyle(dateCellStyle2);
			cell = headerRow.createCell(cellCount++);
			cell.setCellValue(sdf.format(obj.getDate()));

			Map<Long, CrfSectionElementData> elementData = obj.getElementData();
			for (Entry<Long, CrfSectionElementData> element : elementData.entrySet()) {
				cell = headerRow.createCell(cellCount++);
				cell.setCellValue(element.getValue().getValue());
			}
		}

		String path = request.getSession().getServletContext().getRealPath("/") + "Observatoin_" + dateinSting;
		System.out.println(path);

		File file = new File(path + ".xlsx");

		if (!file.exists()) {
			file.createNewFile();
		} else {
			file.delete();
			file.createNewFile();
		}

		// Write the output to a file
		FileOutputStream fileOut = new FileOutputStream(path + ".xlsx");
		workbook.write(fileOut);
		fileOut.close();
		// Closing the workbook
		workbook.close();

		return file;
	}

	@Override
	public SubGroupAnimalsInfoAll subGroupAnimalsInfoAll(Long subGroupInfoId) {
		// TODO Auto-generated method stub
		return expermentalDesignDao.subGroupAnimalsInfoAll(subGroupInfoId);
	}

	@Override
	public int subGroupAnimalsInfoCrfDataCount(SubGroupAnimalsInfoAll animal, StdSubGroupObservationCrfs crf,
			Long sgoupId) {
		return expermentalDesignDao.subGroupAnimalsInfoCrfDataCountForDuplicate(animal, crf, sgoupId);
	}

	@Override
	public Map<String, String> weightWithNameValue(Long crfid, Long subGroupAnimalsInfoAllId, String userName) {

		return expermentalDesignDao.weightWithNameValue(crfid, subGroupAnimalsInfoAllId, userName);
	}

	@Override
	public List<StdSubGroupObservationCrfs> findAllStdSubGroupObservationCrfs(StudyMaster sm, Long subGroupId) {
		// TODO Auto-generated method stub
		return crfDAO.findAllStdSubGroupObservationCrfs(sm, subGroupId);
	}

	@Override
	public List<StdSubGroupObservationCrfs> findAllActiveStdSubGroupObservationCrfs(StudyMaster sm, Long subGroupId) {
		// TODO Auto-generated method stub
		return crfDAO.findAllActiveStdSubGroupObservationCrfs(sm, subGroupId);
	}

	@Override
	public List<SubGroupAnimalsInfoAll> subGroupAnimalsInfoInStudy(Long id) {
		return crfDAO.subGroupAnimalsInfoInStudy(id);
	}

	@Override
	public String stdSubGroupObservationCrfsWithAnimal(Long animalId) {
		// TODO Auto-generated method stub
		SubGroupAnimalsInfoAll animal = crfDAO.subGroupAnimalsInfoAllById(animalId);
		SubGroupInfo subGroup = animal.getSubGroup();
		List<StdSubGroupObservationCrfs> list = crfDAO.stdSubGroupObservationCrfs(subGroup);

//		<select name="crf" class="form-control" id="crf" onchange="getcrfsElements(this.value)">
//		<option value="-1">--Select--</option>
//	</select>
		StringBuilder sb = new StringBuilder();
		sb.append("<select class='form-control' name='crf' id='crf' onchange='getcrfsElements(this.value)'>")
				.append("<option value='-1' selected='selected' >--Select--</option>");
		for (StdSubGroupObservationCrfs ele : list) {
			sb.append(
					"<option value='" + ele.getCrf().getId() + "'>" + ele.getCrf().getObservationName() + "</option>");
		}
		sb.append("</select> <font color='red' id='crfmsg'></font>");
		return sb.toString();
	}

	@Override
	public String crfElements(Long crfId) {
		List<CrfSectionElement> list = crfDAO.sectionElemets(crfId);
		StringBuilder sb = new StringBuilder();
		sb.append(
				"<select class='form-control' name='crfSecEle' id='crfSecEle' onchange='getcrfsElements(this.value)'>")
				.append("<option value='-1' selected='selected' >--Select--</option>");
		for (CrfSectionElement ele : list) {
			sb.append("<option value='" + ele.getId() + "'>" + ele.getName() + "</option>");
		}
		sb.append("</select> <font color='red' id='crfSecElemsg'></font>");
		return sb.toString();
	}

	@Override
	public List<ExprementalData> expressionData(StudyMaster sm) {
		List<ExprementalData> subgroupInfo = expermentalDesignDao.subGroupAnimalsInfoWithStudy(sm);
		return subgroupInfo;
	}

	@Override
	public Map<String, List<StdSubGroupObservationCrfs>> expressionDataCalender(StudyMaster sm) {
		Map<String, List<StdSubGroupObservationCrfs>> subgroupInfo = expermentalDesignDao
				.subGroupAnimalsInfoWithStudyCalender(sm);
		return subgroupInfo;
	}

	@Override
	public ObservationData allObservationData1(StudyMaster sm) {
		// TODO Auto-generated method stub
		return expermentalDesignDao.allObservationData1(sm);
	}

	@Override
	public File exportDataToExcelExport(StudyMaster sm, HttpServletRequest request) {
		ReviewLevel rl = crfDAO.reviewLevel();
		List<ExprementalDataExport> expData = exprementalDataExport(sm, rl);
		int max = 0;
		for (ExprementalDataExport e : expData) {
			if (e.isHedding()) {
				int size = e.getElements().size();
				if (max < size)
					max = size;
			}
		}

		SimpleDateFormat sdf = new SimpleDateFormat("dd_MMM_yyyy");
		String dateinSting = sdf.format(new Date());

		// Create a Workbook
		Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

		/*
		 * CreationHelper helps us create instances of various things like DataFormat,
		 * Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way
		 */
		CreationHelper createHelper = workbook.getCreationHelper();

		// Create a Sheet
		Sheet sheet = workbook.createSheet(sm.getStudyNo() + "_" + dateinSting);

		// Create a Font for styling header cells
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		headerFont.setColor(IndexedColors.RED.getIndex());

		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);

		int rowCount = 0;
		// Create a Row
		Row headerRow = sheet.createRow(rowCount++);
		Cell cell = headerRow.createCell(0);
		cell.setCellValue("Study Number :");
		cell.setCellStyle(headerCellStyle);
		cell = headerRow.createCell(1);
		cell.setCellValue(sm.getStudyNo());
		cell.setCellStyle(headerCellStyle);

		// Create Cell Style for formatting Date
		Font boldFont = workbook.createFont();
		boldFont.setBold(true);
		CellStyle boldCellStyle = workbook.createCellStyle();
		boldCellStyle.setFont(boldFont);

		CellStyle dateCellStyle = workbook.createCellStyle();
		dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MMM-yyyy"));
		headerRow = sheet.createRow(rowCount++);
		cell = headerRow.createCell(0);
		cell.setCellValue("Group");
		cell.setCellStyle(boldCellStyle);
		cell = headerRow.createCell(1);
		cell.setCellValue("Sub Group");
		cell.setCellStyle(boldCellStyle);
		cell = headerRow.createCell(2);
		cell.setCellValue("Animal");
		cell.setCellStyle(boldCellStyle);
		cell = headerRow.createCell(3);
		cell.setCellValue("Observation Name");
		cell.setCellStyle(boldCellStyle);
		cell = headerRow.createCell(4);
		cell.setCellValue("Gender");
		cell.setCellStyle(boldCellStyle);
		cell = headerRow.createCell(5);
		cell.setCellValue("Day/Week");
		cell.setCellStyle(boldCellStyle);
		cell = headerRow.createCell(6);
		cell.setCellValue("Element");
		cell.setCellStyle(boldCellStyle);
		cell = headerRow.createCell(7);
		cell.setCellValue("Created By");
		cell.setCellStyle(boldCellStyle);
		cell = headerRow.createCell(8);
		cell.setCellValue("Created On");
		cell.setCellStyle(boldCellStyle);
		int j = 1;
		if (rl != null) {
			for (int i = 1; i <= rl.getObservationApprovelLevel(); i++) {
				cell = headerRow.createCell(8 + j);
				cell.setCellValue("Review " + i);
				cell.setCellStyle(dateCellStyle);
				j++;
				cell = headerRow.createCell(8 + j);
				cell.setCellValue("Review " + i + " Date");
				cell.setCellStyle(dateCellStyle);
				j++;
			}

			max = max + 7 + (rl.getObservationApprovelLevel() * 2) + 2;
			for (int i = 1 + (rl.getObservationApprovelLevel() * 2); i <= max; i++) {
				cell = headerRow.createCell(8 + i);
				cell.setCellValue("data");
				cell.setCellStyle(dateCellStyle);
			}
		}
		for (ExprementalDataExport d : expData) {
			int i = 0;
			headerRow = sheet.createRow(rowCount++);
			cell = headerRow.createCell(i);
			cell.setCellValue(d.getGroupName());
			cell.setCellStyle(dateCellStyle);
			i++;
			cell = headerRow.createCell(i);
			cell.setCellValue(d.getSubGroupName());
			cell.setCellStyle(dateCellStyle);
			i++;
			cell = headerRow.createCell(i);
			cell.setCellValue(d.getAnimalNo());
			cell.setCellStyle(dateCellStyle);
			i++;
			cell = headerRow.createCell(i);
			cell.setCellValue(d.getObserVationName());
			cell.setCellStyle(dateCellStyle);
			i++;
			cell = headerRow.createCell(i);
			cell.setCellValue(d.getGender());
			cell.setCellStyle(dateCellStyle);
			i++;
			cell = headerRow.createCell(i);
			cell.setCellValue(d.getDayOrWeek());
			cell.setCellStyle(dateCellStyle);
			i++;
			cell = headerRow.createCell(i);
			cell.setCellValue(d.isHedding());
			cell.setCellStyle(dateCellStyle);
			i++;
			cell = headerRow.createCell(i);
			cell.setCellValue(d.getCreatedBy());
			cell.setCellStyle(dateCellStyle);
			i++;
			cell = headerRow.createCell(i);
			cell.setCellValue(d.getCreatedOn());
			cell.setCellStyle(dateCellStyle);
			i++;
			for (Map.Entry<String, String> m : d.getReviews().entrySet()) {
				cell = headerRow.createCell(i);
				cell.setCellValue(m.getKey());
				cell.setCellStyle(boldCellStyle);
				i++;

				cell = headerRow.createCell(i);
				cell.setCellValue(m.getValue());
				cell.setCellStyle(boldCellStyle);
				i++;
			}
			if (d.isHedding()) {
				for (Map.Entry<Long, CrfSectionElement> m : d.getElements().entrySet()) {
					cell = headerRow.createCell(i);
//                    cell.setCellValue(m.getKey() + "..." + m.getValue().getName());
					cell.setCellValue(m.getValue().getName());
					cell.setCellStyle(boldCellStyle);
					i++;
				}
			} else {
				for (Map.Entry<Long, CrfSectionElementData> m : d.getElementData().entrySet()) {
					cell = headerRow.createCell(i);
//                    cell.setCellValue(m.getKey() + "..." + m.getValue().getValue());
					cell.setCellValue(m.getValue().getValue());
					cell.setCellStyle(dateCellStyle);
					i++;
				}
			}
		}
		String path = request.getSession().getServletContext().getRealPath("/") + sm.getStudyNo() + "_"
				+ sdf.format(new Date());
		System.out.println(path);
		File file = new File(path + ".xlsx");
		try {
			if (!file.exists()) {
				file.createNewFile();
			} else {
				file.delete();
				file.createNewFile();
			}

			// Write the output to a file
			FileOutputStream fileOut = new FileOutputStream(path + ".xlsx");
			workbook.write(fileOut);
			fileOut.close();
			// Closing the workbook
			workbook.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return file;
	}

	private List<ExprementalDataExport> exprementalDataExport(StudyMaster sm, ReviewLevel rl) {
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateTimeFormat"));
		List<ExprementalDataExport> export = new ArrayList<>();
		List<GroupInfo> groups = studyGroupInfo(sm);
		for (GroupInfo g : groups) {
			List<SubGroupInfo> subGroups = studySubGroupInfo(g);
			for (SubGroupInfo sg : subGroups) {
				List<StdSubGroupObservationCrfs> obserVations = crfDAO.findAllActiveStdSubGroupObservationCrfs(sg);
				List<SubGroupAnimalsInfoAll> animals = crfDAO.subGroupAnimalsInfoAll(sg);
				for (SubGroupAnimalsInfoAll animal : animals) {
					for (StdSubGroupObservationCrfs obs : obserVations) {
						Crf crf = obs.getCrf();
						Map<Long, CrfSectionElement> elements = new HashMap<>();
						// observation hedding
						ExprementalDataExport exp0 = groupInfoAdd(g, sg, animal, obs);
						exp0.setHedding(true);
						Map<String, String> review0 = new HashMap<>();
						if (rl != null) {
							for (int i = 1; i <= rl.getObservationApprovelLevel(); i++) {
								List<String> ls = new ArrayList<>();
								ls.add(" ");
								ls.add(" ");
								StringBuilder sb = new StringBuilder();
								for (int m = 0; m < i; m++)
									sb.append(" ");
								review0.put(sb.toString(), "");
							}
						}
						exp0.setReviews(review0);
						for (CrfSection sec : crf.getSections()) {
							for (CrfSectionElement ele : sec.getElement()) {
								if (!ele.getType().equals("non")) {
									elements.put(ele.getId(), ele);
								}
							}
						}
						exp0.setElements(elements);
						List<SubGroupAnimalsInfoCrfDataCount> entries = crfDAO.subGroupAnimalsInfoCrfDataCount(sg,
								animal, obs);

						if (entries.size() > 0)
							export.add(exp0);
						for (SubGroupAnimalsInfoCrfDataCount entry : entries) {
							ExprementalDataExport exp = groupInfoAdd(g, sg, animal, obs);
							exp.setReviewStatus(entry.getCrfStatus());
							Map<String, String> review = new HashMap<>();
							List<SubGroupAnimalsInfoCrfDataCountReviewLevel> reviews = crfDAO
									.subGroupAnimalsInfoCrfDataCountReviewLevels(entry);
							for (SubGroupAnimalsInfoCrfDataCountReviewLevel r : reviews) {
								review.put(r.getUser().getUsername(), sdf.format(r.getCreatedOn()));
							}
							if (rl != null) {
								if (rl.getObservationApprovelLevel() != reviews.size()) {
									for (int l = reviews.size(); l < rl.getObservationApprovelLevel(); l++) {
										StringBuilder sb = new StringBuilder();
										for (int m = 0; m < l; m++)
											sb.append(" ");
										review.put(sb.toString(), "");
									}
								}
							}
							exp.setReviews(review);
							exp.setCreatedBy(entry.getCreatedBy());
							exp.setCreatedOn(sdf.format(entry.getCreatedOn()));
							Map<Long, CrfSectionElementData> elementData = new HashMap<>();
							for (CrfSection sec : crf.getSections()) {
								for (CrfSectionElement ele : sec.getElement()) {
									if (!ele.getType().equals("non")) {
										CrfSectionElementData crfdata = crfDAO.crfSectionElementData(entry, ele);
										if (crfdata == null) {
											crfdata = new CrfSectionElementData();
											crfdata.setValue("");
											crfdata.setId(0L);
										}
										elementData.put(ele.getId(), crfdata);
									}
								}
							}
							exp.setElementData(elementData);
							export.add(exp);
						}
					}
				}
			}
		}
		return export;
	}

	private ExprementalDataExport groupInfoAdd(GroupInfo g, SubGroupInfo sg, SubGroupAnimalsInfoAll animal,
			StdSubGroupObservationCrfs obs) {
		SubGroupAnimalsInfo s = animal.getSubGroupAnimalsInfo();
		ExprementalDataExport ed = new ExprementalDataExport();
		ed.setGroupId(g.getId());
		ed.setGroupName(g.getGroupName());
		ed.setSubgroupId(sg.getId());
		ed.setSubGroupName(sg.getName());
		ed.setGender(s.getGender());
		ed.setAnimalNo(animal.getAnimalNo());
		ed.setObserVationId(obs.getId());
		ed.setObserVationName(obs.getObservationName());
		ed.setDayOrWeek(obs.getDayType());
		return ed;
	}

	@Override
	public String saveInstrumentWeight(Long animalId, Long crfId, Long crfSecEle, String weight, String userName) {
		try {
			StudyAnimal animal = expermentalDesignDao.studyAnimalWithId(animalId);
			Crf crf = crfDAO.getCrf(crfId);
			CrfSectionElement ele = crfDAO.sectionElement(crfSecEle);
			CrfSectionElementInstrumentValue ins = new CrfSectionElementInstrumentValue();
			ins.setAnimal(animal);
			ins.setCrf(crf);
			ins.setElement(ele);
			ins.setWeight(weight);
			ins.setCreatedBy(userName);
			ins.setWeightStatus(true);
			crfDAO.saveCrfSectionElementInstrumentValue(ins);
			return "success";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public Map<String, String> crfSectionElementInstrumentValues(StudyAnimal animal, Crf crf) {
		Map<String, String> map = new HashMap<>();
		List<CrfSectionElementInstrumentValue> list = crfDAO.crfSectionElementInstrumentValues(animal, crf);
		for (CrfSectionElementInstrumentValue ins : list) {
			map.put(ins.getElement().getId() + "_" + ins.getElement().getName(), ins.getWeight());
			System.out.println(ins.getElement().getId() + "_" + ins.getElement().getName() + "   ---------     "
					+ ins.getWeight());
		}
		return map;
	}

	@Override
	public Map<Long, Map<String, String>> crfSectionElementInstrumentValues(List<StudyAnimal> animals, Crf crf) {
		Map<Long, Map<String, String>> maps = new HashMap<>();
		for (StudyAnimal animal : animals) {
			Map<String, String> map = new HashMap<>();
			List<CrfSectionElementInstrumentValue> list = crfDAO.crfSectionElementInstrumentValues(animal, crf);
			for (CrfSectionElementInstrumentValue ins : list) {
				map.put(ins.getElement().getId() + "_" + ins.getElement().getName(), ins.getWeight());
				System.out.println(ins.getElement().getId() + "_" + ins.getElement().getName() + "   ---------     "
						+ ins.getWeight());
			}
			maps.put(animal.getId(), map);
		}
		return maps;
	}

	@Override
	public StudyDesignStatus getStudyDesignStatusRecord(Long studyId) {
		return expermentalDesignDao.getStudyDesignStatusRecord(studyId);
	}

	@Override
	public List<SubGroupInfo> getSubGroupInfoList(StudyMaster sm) {
		return expermentalDesignDao.getSubGroupInfoList(sm);
	}

	@Override
	public List<SubGroupAnimalsInfoAll> getSubGroupAnimalsInfoAllRecordsList(SubGroupAnimalsInfo sgai) {
		return expermentalDesignDao.getSubGroupAnimalsInfoAllRecordsList(sgai);
	}

	@Override
	public String updateExpermentalDesignDetails(Long studyId, String userName, StudyService studyService,
			List<String> doseList, List<String> concList, List<String> countList, List<String> fromList,
			List<String> toList, List<String> subNamesList, List<String> newGroupNameList,
			List<String> newGroupDoseList, List<String> newGroupConcList, List<String> newGroupCountList,
			List<String> newGroupFromList, List<String> newGroupToList, List<String> newGroupgenderList) {
		String result = "Failed";
		Map<Long, Map<Long, String>> subNamesMap = new HashMap<>();
		Map<Long, String> namesTemp = null;
		Map<Long, Map<Long, String>> doseMap = new HashMap<>();
		Map<Long, String> doseTemp = null;
		Map<Long, Map<Long, String>> concMap = new HashMap<>();
		Map<Long, String> concTemp = null;
		Map<Long, List<Integer>> countMap = new HashMap<>();
		List<Integer> animalsList = null;
		Map<Long, List<String>> fromMap = new HashMap<>();
		Map<Long, List<String>> toMap = new HashMap<>();
		List<String> tempList = null;
		StudyDesignStatus sds = null;
		try {
			SatusAndWorkFlowDetailsDto sawfdDto = studyService.satusAndWorkFlowDetailsDtoForUpdation(studyId, "RRSD",
					"EDOROD");
			StatusMaster stm = null;
			WorkFlowStatusDetails wfsd = null;
			StudyMaster sm = null;
			if (sawfdDto != null) {
				stm = sawfdDto.getSm();
				wfsd = sawfdDto.getWfsd();
				sm = sawfdDto.getStudy();
				sds = sawfdDto.getSds();
			}
			// Names
			if (subNamesList.size() > 0) {
				for (String st : subNamesList) {
					String[] temp = st.split("\\###");
					String[] temp2 = temp[0].split("\\_");
					if (subNamesMap.get(Long.parseLong(temp2[0])) != null) {
						namesTemp.put(Long.parseLong(temp2[1]), temp[1]);
						subNamesMap.put(Long.parseLong(temp2[0]), namesTemp);
					} else {
						namesTemp = new HashMap<>();
						namesTemp.put(Long.parseLong(temp2[1]), temp[1]);
						subNamesMap.put(Long.parseLong(temp2[0]), namesTemp);
					}
				}
			}
			// Dose
			if (doseList.size() > 0) {
				for (String st : doseList) {
					String[] temp = st.split("\\###");
					String[] temp2 = temp[0].split("\\_");
					if (doseMap.get(Long.parseLong(temp2[0])) != null) {
						doseTemp.put(Long.parseLong(temp2[1]), temp[1]);
						doseMap.put(Long.parseLong(temp2[0]), doseTemp);
					} else {
						doseTemp = new HashMap<>();
						doseTemp.put(Long.parseLong(temp2[1]), temp[1]);
						doseMap.put(Long.parseLong(temp2[0]), doseTemp);
					}
				}
			}
			// conc
			if (concList.size() > 0) {
				for (String st : concList) {
					String[] temp = st.split("\\###");
					String[] temp2 = temp[0].split("\\_");
					if (concMap.get(Long.parseLong(temp2[0])) != null) {
						concTemp = doseMap.get(Long.parseLong(temp2[0]));
						concMap.put(Long.parseLong(temp2[0]), concTemp);
					} else {
						concTemp = new HashMap<>();
						concTemp.put(Long.parseLong(temp2[1]), temp[1]);
						concMap.put(Long.parseLong(temp2[0]), concTemp);
					}
				}
			}
			// Count
			if (countList.size() > 0) {
				for (String st : countList) {
					String[] temp = st.split("\\###");
					String[] temp2 = temp[0].split("\\_");
					if (countMap.get(Long.parseLong(temp2[1])) != null) {
						animalsList = countMap.get(Long.parseLong(temp2[1]));
						animalsList.add(Integer.parseInt(temp[1]));
						countMap.put(Long.parseLong(temp2[1]), animalsList);
					} else {
						animalsList = new ArrayList<>();
						animalsList.add(Integer.parseInt(temp[1]));
						countMap.put(Long.parseLong(temp2[1]), animalsList);
					}
				}
			}
			// from
			if (fromList.size() > 0) {
				for (String st : fromList) {
					String[] temp = st.split("\\###");
					String[] temp2 = temp[0].split("\\_");
					tempList = fromMap.get(Long.parseLong(temp2[1]));
					if (tempList != null) {
						tempList.add(temp[1]);
						fromMap.put(Long.parseLong(temp2[1]), tempList);
					} else {
						tempList = new ArrayList<>();
						tempList.add(temp[1]);
						fromMap.put(Long.parseLong(temp2[1]), tempList);
					}
				}
			}
			// toList
			if (toList.size() > 0) {
				for (String st : toList) {
					String[] temp = st.split("\\###");
					String[] temp2 = temp[0].split("\\_");
					tempList = toMap.get(Long.parseLong(temp2[1]));
					if (tempList != null) {
						tempList.add(temp[1]);
						toMap.put(Long.parseLong(temp2[1]), tempList);
					} else {
						tempList = new ArrayList<>();
						tempList.add(temp[1]);
						toMap.put(Long.parseLong(temp2[1]), tempList);
					}
				}
			}
			List<SubGroupInfo> subGroups = expermentalDesignDao.getSubGroupInfoList(sm);
			List<SubGroupInfo> updateSubList = new ArrayList<SubGroupInfo>();
			List<SubGroupAnimalsInfo> updateAnimalList = new ArrayList<>();
			List<SubGroupAnimalsInfoAll> updateanimalInfoAll = new ArrayList<>();
			if (subGroups != null && subGroups.size() > 0) {
				for (SubGroupInfo sgi : subGroups) {
					boolean sgiFlag = false;
					Map<Long, String> namesMap = subNamesMap.get(sgi.getGroup().getId());
					Map<Long, String> doseValMap = doseMap.get(sgi.getGroup().getId());
					Map<Long, String> consValMap = concMap.get(sgi.getGroup().getId());
					if (!sgi.getName().equals(namesMap.get(sgi.getId()))) {
						sgi.setName(namesMap.get(sgi.getId()));
						sgiFlag = true;
					}
					if (!sgi.getDose().equals(doseValMap.get(sgi.getId()))) {
						sgi.setDose(doseValMap.get(sgi.getId()));
						sgiFlag = true;
					}
					if (!sgi.getConc().equals(consValMap.get(sgi.getId()))) {
						sgi.setConc(consValMap.get(sgi.getId()));
						sgiFlag = true;
					}
					if (sgiFlag)
						updateSubList.add(sgi);

					List<SubGroupAnimalsInfo> animalInfo = sgi.getAnimalInfo();
					Map<Long, List<SubGroupAnimalsInfo>> sgaiMap = new HashMap<>();
					List<SubGroupAnimalsInfo> sgaitempList = null;
					for (SubGroupAnimalsInfo sgai : animalInfo) {
						if (sgaiMap.containsKey(sgai.getSubGroup().getId())) {
							sgaitempList = sgaiMap.get(sgai.getSubGroup().getId());
							sgaitempList.add(sgai);
							sgaiMap.put(sgai.getSubGroup().getId(), sgaitempList);
						} else {
							sgaitempList = new ArrayList<>();
							sgaitempList.add(sgai);
							sgaiMap.put(sgai.getSubGroup().getId(), sgaitempList);
						}
					}
					if (animalInfo != null && animalInfo.size() > 0) {
						for (Map.Entry<Long, List<SubGroupAnimalsInfo>> map : sgaiMap.entrySet()) {
							boolean saiFlag = false;
							List<Integer> intList = countMap.get(map.getKey());
							List<String> fromlist = fromMap.get(map.getKey());
							List<String> tolist = toMap.get(map.getKey());
							if (intList != null) {
								List<SubGroupAnimalsInfo> saiList = sgaiMap.get(map.getKey());
								for (int i = 0; i < saiList.size(); i++) {
									SubGroupAnimalsInfo sai = saiList.get(i);
									if (sai.getCount() != intList.get(i)) {
										sai.setCount(intList.get(i));
										saiFlag = true;
									}
									if (!sai.getFormId().equals(fromlist.get(i))) {
										sai.setFormId(fromlist.get(i));
										saiFlag = true;
									}
									if (!sai.getToId().equals(tolist.get(i))) {
										sai.setToId(tolist.get(i));
										saiFlag = true;
									}
									String[] f = sai.getFormId().split("\\/");
									if (!sai.getFrom().equals(f[1])) {
										sai.setFrom(f[1]);
										saiFlag = true;
									}
									String[] t = sai.getToId().split("\\/");
									if (!sai.getTo().equals(t[1])) {
										sai.setTo(t[1]);
										saiFlag = true;
									}
									if (!sai.getGender().equals(sai.getGroup().getGender())) {
										sai.setGender(sai.getGroup().getGender());
										saiFlag = true;
									}
									if (saiFlag)
										updateAnimalList.add(sai);
									List<SubGroupAnimalsInfoAll> sgaiallList = expermentalDesignDao
											.getSubGroupAnimalsInfoAllRecordsList(sai);
									int start = Integer.parseInt(sai.getFrom());
									int end = Integer.parseInt(sai.getTo());
									for (SubGroupAnimalsInfoAll sgainall : sgaiallList) {
										boolean sgainallFlag = false;
										if (sgainall.getNo() != start) {
											sgainall.setNo(start);
											sgainallFlag = true;
										}
										if (start < 10) {
											if (!sgainall.getAnimalNo().equals("TOXX/00" + start)) {
												sgainall.setAnimalNo("TOXX/00" + start);
												sgainallFlag = true;
											}
										} else if (start < 100) {
											if (!sgainall.getAnimalNo().equals("TOXX/0" + start)) {
												sgainall.setAnimalNo("TOXX/0" + start);
												sgainallFlag = true;
											}
										} else {
											if (!sgainall.getAnimalNo().equals("TOXX/" + start)) {
												sgainall.setAnimalNo("TOXX/" + start);
												sgainallFlag = true;
											}
										}
										if (sgainallFlag)
											updateanimalInfoAll.add(sgainall);
										start++;
									}
								}
							}
						}
					}
				}
			}
			Map<Long, List<String>> newGsNameMap = new HashMap<>();
			Map<Long, List<String>> newSgDoseMap = new HashMap<>();
			Map<Long, List<String>> newSgConcMap = new HashMap<>();
			Map<Long, List<Integer>> newSgCountMap = new HashMap<>();
			List<Integer> intList = null;
			Map<Long, List<String>> newFromMap = new HashMap<>();
			Map<Long, List<String>> newToMap = new HashMap<>();
			Map<Long, List<String>> newGenderMap = new HashMap<>();
			List<String> newStrList = null;
			for (String st : newGroupNameList) {
				if (!st.equals("0")) {
					String[] strArr = st.split("\\###");
					if (newGsNameMap.containsKey(Long.parseLong(strArr[0]))) {
						newStrList = newGsNameMap.get(Long.parseLong(strArr[0]));
						newStrList.add(strArr[1]);
						newGsNameMap.put(Long.parseLong(strArr[0]), newStrList);
					} else {
						newStrList = new ArrayList<>();
						newStrList.add(strArr[1]);
						newGsNameMap.put(Long.parseLong(strArr[0]), newStrList);
					}
				}
			}
			for (String st : newGroupDoseList) {
				if (!st.equals("0")) {
					String[] strArr = st.split("\\###");
					if (newSgDoseMap.containsKey(Long.parseLong(strArr[0]))) {
						newStrList = newSgDoseMap.get(Long.parseLong(strArr[0]));
						newStrList.add(strArr[1]);
						newSgDoseMap.put(Long.parseLong(strArr[0]), newStrList);
					} else {
						newStrList = new ArrayList<>();
						newStrList.add(strArr[1]);
						newSgDoseMap.put(Long.parseLong(strArr[0]), newStrList);
					}
				}
			}
			for (String st : newGroupConcList) {
				if (!st.equals("0")) {
					String[] strArr = st.split("\\###");
					if (newSgConcMap.containsKey(Long.parseLong(strArr[0]))) {
						newStrList = newSgConcMap.get(Long.parseLong(strArr[0]));
						newStrList.add(strArr[1]);
						newSgConcMap.put(Long.parseLong(strArr[0]), newStrList);
					} else {
						newStrList = new ArrayList<>();
						newStrList.add(strArr[1]);
						newSgConcMap.put(Long.parseLong(strArr[0]), newStrList);
					}
				}
			}
			for (String st : newGroupCountList) {
				if (!st.equals("0")) {
					String[] strArr = st.split("\\_");
					String[] str2Arr = strArr[1].split("\\###");
					if (newSgCountMap.containsKey(Long.parseLong(strArr[0]))) {
						intList = newSgCountMap.get(Long.parseLong(strArr[0]));
						intList.add(Integer.parseInt(str2Arr[1]));
						newSgCountMap.put(Long.parseLong(strArr[0]), intList);
					} else {
						intList = new ArrayList<>();
						intList.add(Integer.parseInt(str2Arr[1]));
						newSgCountMap.put(Long.parseLong(strArr[0]), intList);
					}
				}
			}
			for (String st : newGroupFromList) {
				if (!st.equals("0")) {
					String[] strArr = st.split("\\_");
					String[] str2Arr = strArr[1].split("\\###");
					if (newFromMap.containsKey(Long.parseLong(strArr[0]))) {
						newStrList = newFromMap.get(Long.parseLong(strArr[0]));
						newStrList.add(str2Arr[1]);
						newFromMap.put(Long.parseLong(strArr[0]), newStrList);
					} else {
						newStrList = new ArrayList<>();
						newStrList.add(str2Arr[1]);
						newFromMap.put(Long.parseLong(strArr[0]), newStrList);
					}
				}
			}
			for (String st : newGroupToList) {
				if (!st.equals("0")) {
					String[] strArr = st.split("\\_");
					String[] str2Arr = strArr[1].split("\\###");
					if (newToMap.containsKey(Long.parseLong(strArr[0]))) {
						newStrList = newToMap.get(Long.parseLong(strArr[0]));
						newStrList.add(str2Arr[1]);
						newToMap.put(Long.parseLong(strArr[0]), newStrList);
					} else {
						newStrList = new ArrayList<>();
						newStrList.add(str2Arr[1]);
						newToMap.put(Long.parseLong(strArr[0]), newStrList);
					}
				}
			}
			for (String st : newGroupgenderList) {
				if (!st.equals("0")) {
					String[] strArr = st.split("\\_");
					String[] str2Arr = strArr[1].split("\\###");
					if (newGenderMap.containsKey(Long.parseLong(strArr[0]))) {
						newStrList = newGenderMap.get(Long.parseLong(strArr[0]));
						newStrList.add(str2Arr[1]);
						newGenderMap.put(Long.parseLong(strArr[0]), newStrList);
					} else {
						newStrList = new ArrayList<>();
						newStrList.add(str2Arr[1]);
						newGenderMap.put(Long.parseLong(strArr[0]), newStrList);
					}
				}
			}
			sds.setCreatedBy(userName);
			sds.setCreatedOn(new Date());
			sds.setStatus(stm);
			sds.setStudyId(sm.getId());
			sds.setCount(1);

			ApplicationAuditDetails apad = new ApplicationAuditDetails();
			apad.setAction("Expermental Design");
			apad.setCreatedBy(userName);
			apad.setCreatedOn(new Date());
			apad.setStudyId(sm);
			apad.setWfsdId(wfsd);

//				boolean newSgFlag = expermentalDesignDao.saveNewSubGroupDetails(newGsNameMap, newSgDoseMap, newSgConcMap, newSgCountMap, newFromMap, newToMap, newGenderMap, userName, sm);
			result = expermentalDesignDao.updateExpermentalDesign(updateSubList, updateAnimalList, updateanimalInfoAll,
					sds, apad, newGsNameMap, newSgDoseMap, newSgConcMap, newSgCountMap, newFromMap, newToMap,
					newGenderMap, userName, sm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public ExpermentalDto getExpermentalDtoDetails(Long studyId) {
		return expermentalDesignDao.getExpermentalDtoDetails(studyId);
	}

	@Override
	public Map<Long, String> getNewGroupDataDetails(List<GroupInfo> gi) {
		Map<Long, String> newGroupMap = new HashMap<>();
		try {
			for (GroupInfo g : gi) {
				List<SubGroupInfo> subgList = expermentalDesignDao.getSubGroupRecrodsList(g);
				if (subgList.size() == 0)
					newGroupMap.put(g.getId(), g.getGroupTest() + "##" + g.getGender());
				else {
					if (subgList.size() != g.getGroupTest()) {
						newGroupMap.put(g.getId(), g.getGroupTest() + "##" + g.getGender());
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newGroupMap;
	}

	@Override
	public List<StdSubGroupObservationCrfs> getSubGroupObservations(Long studyId, Long groupId, Long subGroupId) {
		List<StdSubGroupObservationCrfs> ssgocList = null;
		List<AmendmentDetails> amdList = null;
		Map<Long, AmendmentDetails> amdMap = new HashMap<>();

		List<StdSubGroupObservationCrfs> finalSsgocList = new ArrayList<>();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
			StudyMaster study = studyDao.findByStudyId(studyId);
			if (groupId != null && subGroupId == null) {
				subGroupId = expermentalDesignDao.subGroupId(groupId);
			}
			ssgocList = expermentalDesignDao.getSubGroupObservations(studyId, subGroupId);
			amdList = expermentalDesignDao.getAmendmentDetailsRecordsList(studyId);
			if (amdList != null && amdList.size() > 0) {
				for (AmendmentDetails amd : amdList) {
					amdMap.put(amd.getObvId().getId(), amd);
				}
			}
			if (ssgocList != null && ssgocList.size() > 0) {

				for (StdSubGroupObservationCrfs form : ssgocList) {
					String[] days = form.getDays().split(",");
					form.setAllowDataEntry(true);
					if (amdMap.containsKey(form.getId())) {
						AmendmentDetails amd = amdMap.get(form.getId());
						if (amd.getStatus().equals("Approved"))
							finalSsgocList.add(form);
					} else
						finalSsgocList.add(form);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return finalSsgocList;
	}

	@Override
	public List<SubGroupAnimalsInfoAll> getsSbGroupAnimalsInfoAll(Long animalId) {
		return expermentalDesignDao.getsSbGroupAnimalsInfoAll(animalId);
	}

	@Override
	public CrfDataEntryDto getCrfDataEntryDetails(Long studyId, Long animalId, Long crfId, Long subGroupId) {
		CrfDataEntryDto cdeDto = null;
		SubGroupAnimalsInfoAll saia = null;
		List<SubjectDataEntryDetails> sdedList = null;
		try {
			cdeDto = expermentalDesignDao.getCrfDataEntryDetails(studyId, animalId, crfId, subGroupId);
			if (cdeDto != null) {
				sdedList = cdeDto.getSdedList();
				if (sdedList != null && sdedList.size() > 0) {
					SubjectDataEntryDetails sdedPojo = sdedList.get(0);
//					String [] days = sdedPojo.getDays().split("\\,");  swami
//					int count = days.length;
//					for(int i=0; i<count; i++) {
//						boolean flag = false;
//						for(SubjectDataEntryDetails sded : sdedList) {
//						  String[] dataEnry = sded.getDataEntry().split("\\,");
//						  if(dataEnry[i].equals("Not Done")) {
//							  saia = sded.getAnimalId();
//							  flag = true;
//							  break;
//						  }
//						}
//						if(flag)
//						  break;
//					}
				}
				cdeDto.setAnimal(saia);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cdeDto;
	}

	public ObservationData observationDataAllAnimal(Long subGroupInfoId, Long stdSubGroupObservationCrfsId,
			LoginUsers user, Long groupId, Long subGroupId) {
		return expermentalDesignDao.observationDataAllAnimal(subGroupInfoId, stdSubGroupObservationCrfsId, user,
				groupId, subGroupId);
	}

	@Override
	public SubjectDataEntryDetails getSubjectDataEntryDetailsRecord(StdSubGroupObservationCrfs stdCrf, Long studyId,
			SubGroupAnimalsInfoAll animal, Long subGroupId) {
		return expermentalDesignDao.getSubjectDataEntryDetailsRecord(stdCrf, studyId, animal, subGroupId);
	}

	@Override
	public List<String> getFrormulaDataofCurrentCrf(Long crfId, Long studyId) {
		List<String> finalList = new ArrayList<>();
		CrfFormulaDto cfDto = null;
		List<String> eleList = new ArrayList<>();
		try {
			cfDto = expermentalDesignDao.getFrormulaDataofCurrentCrf(crfId, studyId);
			if (cfDto != null) {
				List<String> formulasList = cfDto.getFormulsList();
				if (formulasList != null && formulasList.size() > 0) {
					for (String formula : formulasList) {
						String[] currentArr = formula.split("CURRENT");
						if (currentArr.length > 0) {
							for (String str : currentArr) {
								if (!str.equals("")) {
									if (!str.contains("MEAN") && !str.contains("SD")) {
										str = str.replaceAll("\\(", "");
										str = str.replaceAll("\\)", "");
//										str = str.replaceAll("valOf", "@");
										String[] arr = str.split("valOf");
										for (int i = 0; i < arr.length; i++) {
//											System.out.println("arr["+i+"]  :"+arr[i]);
											String st = arr[i];
											st = st.replaceAll("[-+^*/]", " ");
											if (!st.equals("")) {
												String[] temp = st.split("\\!");
												eleList.add(temp[1].trim());
											}
										}
									} else {
										if (str.contains("MEAN")) {
											String[] meanArr = str.split("MEAN");
											List<String> meanList = new ArrayList<>();
											for (int i = 0; i < meanArr.length; i++) {
												if (!meanArr[i].equals("")) {
													meanList.add(meanArr[i]);
													String st = meanArr[i];
													st = st.replaceAll("\\(", "");
													st = st.replaceAll("\\)", "");
													st = st.replaceAll("[-+^*/]", " ");
													System.out.println("final String of " + i + " is :" + st);
													if (!st.equals("")) {
														String[] tempArr = st.split("\\,");
														if (tempArr.length > 0) {
															for (int j = 0; j < tempArr.length; j++) {
																String[] tempStr = tempArr[j].trim().split("!");
																eleList.add(tempStr[1].trim());
															}
														}
													}
												}
											}
										}
										if (str.contains("SD")) {
											String[] sdArr = str.split("SD");
											List<String> sdList = new ArrayList<>();
											for (int i = 0; i < sdArr.length; i++) {
												if (!sdArr[i].equals("")) {
													sdList.add(sdArr[i]);
//													System.out.println("meanArr["+i+"]  is :"+meanArr[i]);
													String st = sdArr[i];
													st = st.replaceAll("\\(", "");
													st = st.replaceAll("\\)", "");
													st = st.replaceAll("[-+^*/]", " ");
//													System.out.println("final String of "+i+" is :"+st);
													if (!st.equals("")) {
														String[] tempArr = st.split("\\,");
														if (tempArr.length > 0) {
															for (int j = 0; j < tempArr.length; j++) {
																String[] tempStr = tempArr[j].trim().split("!");
																eleList.add(tempStr[1].trim());
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
				if (eleList.size() > 0) {
					Map<String, String> map = new HashMap<>();
					List<CrfSectionElement> secList = cfDto.getSecList();
					if (secList != null && secList.size() > 0) {
						for (CrfSectionElement cse : secList) {
							map.put(cse.getName(), cse.getId() + "_" + cse.getName());
						}
					}
					for (String st : eleList) {
						finalList.add(map.get(st));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return finalList;
	}

	@Override
	public List<SubGroupAnimalsInfoAll> getSubGroupAnimalsInfoAllRecords(List<SubjectDataEntryDetails> sdedList) {
		List<SubGroupAnimalsInfoAll> animalList = new ArrayList<>();
//		List<Long> ids = new ArrayList<>(); swami
//		try {
//			if(sdedList != null && sdedList.size() > 0) {
//				SubjectDataEntryDetails sdedPojo = sdedList.get(0);
//				String [] days = sdedPojo.getDays().split("\\,");
//				int count = days.length;
//				for(int i=0; i<count; i++) {
//					for(SubjectDataEntryDetails sded : sdedList) {
//					  String[] dataEnry = sded.getDataEntry().split("\\,");
//					  if(dataEnry[i].equals("Not Done")) {
//						  if(!ids.contains(sded.getAnimalId().getId())) {
//							  ids.add(sded.getAnimalId().getId());
//							  animalList.add(sded.getAnimalId());
//						  }
//					  }
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return animalList;
	}

	@Override
	public List<SubGroupAnimalsInfoAll> getSubGroupAnimalsInfoAllRecordsList(Long subGroupId) {
		return expermentalDesignDao.getSubGroupAnimalsInfoAllRecordsList(subGroupId);
	}

	@Override
	public List<GroupInfo> studyGroupInfoWithSubGroup(StudyMaster sm) {
		// TODO Auto-generated method stub
		return expermentalDesignDao.studyGroupInfoWithSubGroup(sm);
	}

	@Override
	public StudyCageAnimal nextAnimalForObservationData(Long studyId, Long stdSubGroupObservationCrfsId,
			Long subGroupId, Long cageId) {

		return expermentalDesignDao.nextAnimalForObservationData(stdSubGroupObservationCrfsId, cageId, studyId,
				subGroupId);
	}

	@Override
	public List<AnimalCage> animalCages(Long studyId) {

		return expermentalDesignDao.animalCages(studyId);
	}

	@Override
	public List<StudyCageAnimal> studyCageAnimal(Long studyId, Long cageId) {
		return expermentalDesignDao.studyCageAnimal(studyId, cageId);
	}

	@Override
	public List<StudyAnimal> studyAnimalWithGender(Long studyId, String gender) {
		return expermentalDesignDao.studyAnimalWithGender(studyId, gender);
	}

	@Override
	public StdSubGroupObservationCrfs stdSubGroupObservationCrfsWithId(Long stdSubGroupObservationCrfsId) {
		// TODO Auto-generated method stub
		return expermentalDesignDao.stdSubGroupObservationCrfsWithId(stdSubGroupObservationCrfsId);
	}

	@Override
	public boolean checkDataEntryEligible(StudyMaster sm) {
		return expermentalDesignDao.checkDataEntryEligible(sm);
	}

	@Override
	public AnimalCage cages(Long cageId) {
		// TODO Auto-generated method stub
		return expermentalDesignDao.cages(cageId);
	}

	@Override
	public List<AnimalCage> groupAnimalCages(Long studyId, Long subGroupId) {
		// TODO Auto-generated method stub
		return expermentalDesignDao.groupAnimalCages(studyId, subGroupId);
	}

	@Override
	public List<SysmexAnimalCode> sysmexAnimalCodes() {
		// TODO Auto-generated method stub
		return expermentalDesignDao.sysmexAnimalCodes();
	}

	@Override
	public boolean changeTreatmentConfig(Crf crf, StudyMaster study, String status, String userName, Long subGroupId) {
		try {
			StdSubGroupObservationCrfs sad = expermentalDesignDao.getStudyTreatmentConfigList(crf.getId(),
					study.getId(), subGroupId);
			if (sad == null) {
				SubGroupInfo subGroup = studyDao.stdGroupInfo(subGroupId);
				sad = new StdSubGroupObservationCrfs();
				sad.setSubGroupInfo(subGroup);
				sad.setCreatedBy(userName);
				sad.setCreatedOn(new Date());
				sad.setCrf(crf);
				sad.setStudy(study);
				sad.setDayType("day");
				expermentalDesignDao.saveStudyTreatmentData(sad);
			} else {
				if (status.equalsIgnoreCase("active"))
					sad.setActive(true);
				else
					sad.setActive(false);
				sad.setUpdatedBy(userName);
				sad.setUpdatedOn(new Date());
				expermentalDesignDao.updateStudyTreatmentData(sad);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean changeTreatmentConfigType(Long crfId, Long studyId, String value, String userName, String field,
			Long subGroupId) {
		try {
			StdSubGroupObservationCrfs sad = expermentalDesignDao.getStudyTreatmentConfigList(crfId, studyId,
					subGroupId);
			if (sad != null) {
				sad.setUpdatedBy(userName);
				sad.setUpdatedOn(new Date());
				if (field.equals("type"))
					sad.setDayType(value);
				else if (field.equals("deviationSign")) {
					if (value.equals("plus"))
						sad.setWindowSign("+");
					else if (value.equals("mines"))
						sad.setWindowSign("-");
					else if (value.equals("bouth"))
						sad.setWindowSign("+/-");
					else
						sad.setWindowSign(value);
				} else if (field.equals("deviation"))
					sad.setWindow(Integer.parseInt(value));
				expermentalDesignDao.updateStudyTreatmentData(sad);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateStudyTreatmentDataDatesDetails(Long studyTreatmentDataDatesId, String value, Long userId) {
		// TODO Auto-generated method stub
		return expermentalDesignDao.updateStudyTreatmentDataDatesDetails(studyTreatmentDataDatesId, value, userId);
	}

	@Override
	public boolean removeStudyTreatmentDataDatesDetails(Long studyTreatmentDataDatesId, Long userId) {
		// TODO Auto-generated method stub
		return expermentalDesignDao.removeStudyTreatmentDataDatesDetails(studyTreatmentDataDatesId, userId);
	}

	@Override
	public List<StdSubGroupObservationCrfs> stdSubGroupObservationCrfs(Long studyId, Long userRoleId) {
		return expermentalDesignDao.stdSubGroupObservationCrf(studyId, userRoleId);
	}

	@Override
	public Map<String, List<StudyTreatmentDataDates>> expressionTreatmentDataCalenderForGenderSpecific(
			List<StdSubGroupObservationCrfs> list, StudyMaster sm) {
		Map<String, List<StudyTreatmentDataDates>> map = new HashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		for (StdSubGroupObservationCrfs ob : list) {
			List<StudyTreatmentDataDates> acclamatizationDates = ob.getTreatmentDates();
			if (sm.getGender().equals("Male") || sm.getGender().equals("Female")
					|| (sm.getGender().equals("Both") && sm.isSplitStudyByGender())) {
				for (StudyTreatmentDataDates accdate : acclamatizationDates) {
					String date = sdf.format(accdate.getAccDate());
					accdate.setDateString(date);
					if (ob.getDayType().equalsIgnoreCase("day")) {
						List<StudyTreatmentDataDates> dateWise = map.get(date);
						if (dateWise == null)
							dateWise = new ArrayList<>();

						if (!sm.getGender().equals("Both")) {
							accdate.setDisplayGender(sm.getGender());
							dateWise.add(accdate);
							map.put(date, dateWise);
						} else {
							accdate.setDisplayGender("Male");
							dateWise.add(accdate);
							StudyTreatmentDataDates forFemale = new StudyTreatmentDataDates();
							try {
								BeanUtils.copyProperties(forFemale, accdate);
							} catch (IllegalAccessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							forFemale.setDisplayGender("Female");
							dateWise.add(forFemale);
							map.put(date, dateWise);
						}
					} else {
						// By week need add code. Because, it will take more time
					}

				}
			} else {
				Map<String, List<StudyTreatmentDataDates>> acclamatizationDatesGender = new HashMap<>();
				List<StudyTreatmentDataDates> acclamatizationDatesMale = new ArrayList<>();
				List<StudyTreatmentDataDates> acclamatizationDatesFemale = new ArrayList<>();
				for (StudyTreatmentDataDates accdate : acclamatizationDates) {
					if (accdate.getGender().equals("Male"))
						acclamatizationDatesMale.add(accdate);
					else
						acclamatizationDatesFemale.add(accdate);
				}
				acclamatizationDatesGender.put("Male", acclamatizationDatesMale);
				acclamatizationDatesGender.put("Female", acclamatizationDatesFemale);
				for (Map.Entry<String, List<StudyTreatmentDataDates>> eachGender : acclamatizationDatesGender
						.entrySet()) {
					for (StudyTreatmentDataDates accdate : eachGender.getValue()) {
						String date = sdf.format(accdate.getAccDate());
						accdate.setDateString(date);
						if (ob.getDayType().equalsIgnoreCase("day")) {
							List<StudyTreatmentDataDates> dateWise = map.get(date);
							if (dateWise == null)
								dateWise = new ArrayList<>();
							accdate.setDisplayGender(eachGender.getKey());
							dateWise.add(accdate);
							map.put(date, dateWise);
						} else {
							// By week need add code. Because, it will take more time
						}

					}
				}
			}
		}
		return map;
	}

	@Override
	public Map<String, List<StdSubGroupObservationCrfs>> expressionTreatmentDataCalender(
			List<StdSubGroupObservationCrfs> list, StudyMaster sm) {
		Map<String, List<StdSubGroupObservationCrfs>> map = new HashMap<>();
		Calendar cal = Calendar.getInstance();
		Date d = sm.getTreatmentStarDate();
		Date studyDate = sm.getTreatmentStarDate();
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));

		for (StdSubGroupObservationCrfs ob : list) {
			List<Integer> ds = new ArrayList<>();
			List<StudyTreatmentDataDates> acclamatizationDates = ob.getTreatmentDates();
			acclamatizationDates.forEach((accdate) -> {
				ds.add(accdate.getDayNo());
			});
			List<String> listDates = new ArrayList<>();
			for (Integer dcount : ds) {
				dcount = dcount - 1;
				if (ob.getDayType().equalsIgnoreCase("day")) {
					cal.setTime(d);
					cal.add(Calendar.DAY_OF_MONTH, dcount);
					listDates.add(sdf.format(cal.getTime()));
				} else if (ob.getDayType().equalsIgnoreCase("week")) {
					List<String> tempDates = new ArrayList<>();
					int days = dcount;
					if (days > -1) {
						if (days == 0)
							days = 1;
						for (int c = (days - 1) * 7; c < days * 7; c++) {
							cal.setTime(studyDate);
							cal.add(Calendar.DAY_OF_MONTH, c);
							tempDates.add(sdf.format(cal.getTime()));
						}
					} else {
						days = -(days);
						int c = days * 7;
						for (int j = 1; j <= 7; j++) {
							System.out.println(c);
							cal.setTime(studyDate);
							cal.add(Calendar.DAY_OF_MONTH, -(c));
							tempDates.add(sdf.format(cal.getTime()));
							c--;
							System.out.println(listDates);
						}
					}

					if (tempDates.size() > 1) {
						String datef = tempDates.get(0) + " to " + tempDates.get(tempDates.size() - 1);
						listDates.add(datef);
					} else
						listDates.addAll(tempDates);
				}

			}
			System.out.println(listDates);
			for (String sd : listDates) {
				List<StdSubGroupObservationCrfs> listOfObj = map.get(sd);
				if (listOfObj == null)
					listOfObj = new ArrayList<>();
				listOfObj.add(ob);
				map.put(sd, listOfObj);
			}
		}
		return map;
	}

	@Override
	public StudyAnimal nextAnimalForTreatmentData(Long studyTreatmentDataDatesId, Long stdSubGroupObservationCrfsId,
			Long studyId, Long crfId, String seletedGender, int noOfEntry, String selecteDate) {
		return expermentalDesignDao.nextAnimalForTreatmentData(studyTreatmentDataDatesId, stdSubGroupObservationCrfsId,
				studyId, crfId, seletedGender, noOfEntry, selecteDate);
	}

	@Override
	public List<StudyAnimal> studyAnimal(Long studyId) {
		// TODO Auto-generated method stub
		return expermentalDesignDao.studyAnimal(studyId);
	}

	@Override
	public List<SubGroupInfo> studySubGroupInfo(Long groupId) {
		// TODO Auto-generated method stub
		return expermentalDesignDao.studySubGroupInfo(groupId);
	}

	@Override
	public Long subGrouId(Long groupId) {
		// TODO Auto-generated method stub
		return expermentalDesignDao.subGrouId(groupId);
	}

	@Override
	public Map<String, Map<Long, StdSubGroupObservationCrfs>> unscheduelTreatmentData(StudyMaster sm) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		List<SubjectDataEntryDetails> list = expermentalDesignDao.unscheduelTreatmentData(sm);
		Map<String, Map<Long, StdSubGroupObservationCrfs>> result = new HashMap<>();
		list.stream().forEach((obj) -> {
			String date = sdf.format(obj.getEntredOn());
			Map<Long, StdSubGroupObservationCrfs> tempList = result.get(date);
			if (tempList == null)
				tempList = new HashMap<>();
			tempList.put(obj.getObservationCrf().getId(), obj.getObservationCrf());
			result.put(date, tempList);
		});
		return result;
	}

	@Override
	public List<StdSubGroupObservationCrfs> stdSubGroupObservationCrfs(Long studyId) {
		// TODO Auto-generated method stub
		return expermentalDesignDao.stdSubGroupObservationCrfs(studyId);
	}

	@Override
	public FileInformation treatmentObservatinExport(Long observationId, ModelMap model, HttpServletRequest request,
			RedirectAttributes redirectAttributes, HttpServletResponse response)
			throws IOException, DocumentException, ParseException {
		// TODO Auto-generated method stub

		FileInformation fi = new FileInformation();
		// TODO Auto-generated method stub
		String realPath = request.getSession().getServletContext().getRealPath("/");
		StdSubGroupObservationCrfs stdSubGroupObservationCrfs = expermentalDesignDao
				.stdSubGroupObservationCrfsById(observationId);
		fi.setFileName(stdSubGroupObservationCrfs.getCrf().getObservationDesc() + ".pdf");
		String file = checktheFile(realPath, fi.getFileName());
		System.out.println(file);
		fi.setFilePath(file);

		List<StudyAnimal> animals = null;
		if (!stdSubGroupObservationCrfs.getCrf().getObservationName()
				.equalsIgnoreCase("Record for Mortality Morbidity"))
			animals = studyDao.studyAnimals(stdSubGroupObservationCrfs.getStudy().getId());
		Crf crf = crfDAO.getCrfForView(stdSubGroupObservationCrfs.getCrf().getId());

		if (crf.getType().equalsIgnoreCase("IN-LIFE OBSERVATIONS")
				|| crf.getType().equalsIgnoreCase("TERMINAL OBSERVATIONS")) {
			if (crf.getSubType().equalsIgnoreCase("Clinical Observations") || crf.getSubType().equalsIgnoreCase("CNS/PNS")) {
				if (crf.getObservationName().equalsIgnoreCase("Daily Clinical Observations")) {
					CrfSectionElement dayElement = null;
					CrfSectionElement valueElement = null;
					for (CrfSection section : crf.getSections()) {
						System.out.println(section.getElement().size());
						for (CrfSectionElement ele : section.getElement()) {
							System.out.println(ele.getName());
							if (ele.getName().equals("day")) {
								dayElement = ele;
							} else if (ele.getName().equals("value")) {
								valueElement = ele;
							}
							if (dayElement != null && valueElement != null)
								break;
						}
					}
					DailyClinicalObservation dco = expermentalDesignDao.dailyClinicalObservation(dayElement,
							valueElement, stdSubGroupObservationCrfs, animals);
					exportClinicalObservationsDataToPdf(dco, request, response, file, stdSubGroupObservationCrfs);
				} else if (crf.getObservationName().equalsIgnoreCase("Clinical Observation Record (Time Point)")) {
					CrfSectionElement timePointElement = null;
					CrfSectionElement observationElement = null;
					CrfSectionElement timeHrElement = null;
					for (CrfSection section : crf.getSections()) {
						System.out.println(section.getElement().size());
						for (CrfSectionElement ele : section.getElement()) {
							System.out.println(ele.getName());
							if (ele.getName().equals("timePoint")) {
								timePointElement = ele;
							} else if (ele.getName().equals("observation")) {
								observationElement = ele;
							} else if (ele.getName().equals("time")) {
								timeHrElement = ele;
							}

							if (timePointElement != null && observationElement != null && timeHrElement != null)
								break;
						}
					}
					Map<Integer, ClinicalObservationRecordTimePoint> map = expermentalDesignDao
							.clinicalObservationRecordTimePoint(timePointElement, observationElement, timeHrElement,
									stdSubGroupObservationCrfs, animals);
					exportClinicalObservationsDataTimePointToPdf(map, request, response, file,
							stdSubGroupObservationCrfs);
				} else if (crf.getObservationName().equalsIgnoreCase("Record for Mortality Morbidity")) {
					CrfSectionElement mortalityElement = null;
					CrfSectionElement morbidityElement = null;
					CrfSectionElement mortalitySelect1Element = null;
					for (CrfSection section : crf.getSections()) {
						System.out.println(section.getElement().size());
						for (CrfSectionElement ele : section.getElement()) {
							System.out.println(ele.getName());
							if (ele.getName().equals("mortality")) {
								mortalityElement = ele;
							} else if (ele.getName().equals("morbidity")) {
								morbidityElement = ele;
							} else if (ele.getName().equals("mortalitySelect1")) {
								mortalitySelect1Element = ele;
							}

							if (mortalityElement != null && morbidityElement != null && mortalitySelect1Element != null)
								break;
						}
					}
					List<RecordforMortalityMorbidity> recordforMortalityMorbidity = expermentalDesignDao
							.recordforMortalityMorbidity(mortalityElement, morbidityElement, mortalitySelect1Element,
									stdSubGroupObservationCrfs);

					exportRecordforMortalityMorbidityToPdf(recordforMortalityMorbidity, request, response, file,
							stdSubGroupObservationCrfs, null);
				} else if (crf.getObservationName().equalsIgnoreCase("Detailed Clinical Observations")) {
					CrfSectionElement valueElement = null;
					for (CrfSection section : crf.getSections()) {
						System.out.println(section.getElement().size());
						for (CrfSectionElement ele : section.getElement()) {
							System.out.println(ele.getName());
							if (ele.getName().equals("observation")) {
								valueElement = ele;
								break;
							}
						}
					}

					DetailedClinicalObservations dco = expermentalDesignDao.detailedClinicalObservation(valueElement,
							stdSubGroupObservationCrfs, animals);
					exportDetailedClinicalObservationDataToPdf(dco, request, response, file);
				} else if (crf.getObservationName().equalsIgnoreCase("Ophthalmological Examination")) {
					CrfSectionElement leftEyeElement = null;
					CrfSectionElement rightEyeElement = null;
					for (CrfSection section : crf.getSections()) {
						System.out.println(section.getElement().size());
						for (CrfSectionElement ele : section.getElement()) {
							System.out.println(ele.getName());
							if (ele.getName().equals("leftEye")) {
								leftEyeElement = ele;
							} else if (ele.getName().equals("rightEye")) {
								rightEyeElement = ele;
							}
							if (leftEyeElement != null && rightEyeElement != null) {
								break;
							}
						}
					}

					OphthalmologicalExamination dco = expermentalDesignDao.ophthalmologicalExamination(leftEyeElement,
							rightEyeElement, stdSubGroupObservationCrfs, animals);
					exportOphthalmologicalExaminationDataToPdf(dco, request, response, file);
				} else if (crf.getObservationName()
						.equalsIgnoreCase("Neurobehavioral Observations of Individual Animal")) {
					CrfSectionElement posture = null, convulsion = null;
					CrfSectionElement handiling = null, easeOfRemove = null, palpebral = null, lacrimation = null,
							eyeExamination = null, piloerection = null, salivation = null;
					CrfSectionElement mobility = null, gait = null, respiration = null, arousal = null,
							clonicMovement = null, tonicMovement = null, stereotypy = null, bizzareBehaviour = null,
							vocalisationNo = null, noofRears = null, urinePoolsNo = null, faecalBolusNo = null;
					for (CrfSection section : crf.getSections()) {
						System.out.println(section.getElement().size());
						for (CrfSectionElement ele : section.getElement()) {
							System.out.println(ele.getName());
							switch (ele.getName()) {
							case "posture":
								posture = ele;
								break;
							case "convulsion":
								convulsion = ele;
								break;
							case "easeOfRemove":
								easeOfRemove = ele;
								break;
							case "palpebral":
								palpebral = ele;
								break;
							case "lacrimation":
								lacrimation = ele;
								break;
							case "eyeExamination":
								eyeExamination = ele;
								break;
							case "piloerection":
								piloerection = ele;
								break;
							case "salivation":
								salivation = ele;
								break;
							case "mobility":
								mobility = ele;
								break;
							case "gait":
								gait = ele;
								break;
							case "respiration":
								respiration = ele;
								break;
							case "arousal":
								arousal = ele;
								break;
							case "clonicMovement":
								clonicMovement = ele;
								break;
							case "tonicMovement":
								tonicMovement = ele;
								break;
							case "stereotypy":
								stereotypy = ele;
								break;
							case "bizzareBehaviour":
								bizzareBehaviour = ele;
								break;
							case "vocalisationNo":
								vocalisationNo = ele;
								break;
							case "noofRears":
								noofRears = ele;
								break;
							case "urinePoolsNo":
								urinePoolsNo = ele;
								break;
							case "faecalBolusNo":
								faecalBolusNo = ele;
								break;
							default:
								break;
							}
						}
					}

					List<NeurobehavioralObservationsofIndividual> dco = expermentalDesignDao
							.neurobehavioralObservationsofIndividual(posture, convulsion, handiling, easeOfRemove,
									palpebral, lacrimation, eyeExamination, piloerection, salivation, mobility, gait,
									respiration, arousal, clonicMovement, tonicMovement, stereotypy, bizzareBehaviour,
									vocalisationNo, noofRears, urinePoolsNo, faecalBolusNo, stdSubGroupObservationCrfs,
									animals);
					exportNeurobehavioralObservationsofIndividualDataToPdf(dco, request, response, file);
				} else if (crf.getObservationName().equalsIgnoreCase("Sensory Reactivity of Individual")) {
					CrfSectionElement approachResponse = null, touchResponse = null, clickResponse = null,
							tailPinchResponse = null, pupilResponse = null, airRightingReflex = null,
							airRighting = null;
					for (CrfSection section : crf.getSections()) {
						System.out.println(section.getElement().size());
						for (CrfSectionElement ele : section.getElement()) {
							System.out.println(ele.getName());
							switch (ele.getName()) {
							case "approachResponse":
								approachResponse = ele;
								break;
							case "touchResponse":
								touchResponse = ele;
								break;
							case "clickResponse":
								clickResponse = ele;
								break;
							case "tailPinchResponse":
								tailPinchResponse = ele;
								break;
							case "pupilResponse":
								pupilResponse = ele;
								break;
							case "airRightingReflex":
								airRightingReflex = ele;
								break;
							default:
								break;
							}
						}
					}

					List<SensoryReactivityOfIndividual> dco = expermentalDesignDao.sensoryReactivityOfIndividual(
							approachResponse, touchResponse, clickResponse, tailPinchResponse, pupilResponse,
							airRightingReflex, stdSubGroupObservationCrfs, animals);
					exportSensoryReactivityOfIndividualDataToPdf(dco, request, response, file);
				} else if (crf.getObservationName().equalsIgnoreCase("Motor Activity Data of Individual Animal")) {
					CrfSectionElement total = null, ambulatory = null, stereotypic = null;
					for (CrfSection section : crf.getSections()) {
						System.out.println(section.getElement().size());
						for (CrfSectionElement ele : section.getElement()) {
							System.out.println(ele.getName());
							switch (ele.getName()) {
							case "total":
								total = ele;
								break;
							case "ambulatory":
								ambulatory = ele;
								break;
							case "stereotypic":
								stereotypic = ele;
								break;
							default:
								break;
							}
						}
					}

					MotorActivityDataOfIndividualAnimal dco = expermentalDesignDao.motorActivityDataOfIndividualAnimal(
							total, ambulatory, stereotypic,  stdSubGroupObservationCrfs, animals);
					exportMotorActivityDataOfIndividualAnimallDataToPdf(dco, request, response, file);
				}
			}
		}
		return fi;
	}

	

	private void exportNeurobehavioralObservationsofIndividualDataToPdf(
			List<NeurobehavioralObservationsofIndividual> dco, HttpServletRequest request, HttpServletResponse response,
			String file) throws IOException, DocumentException, ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		SimpleDateFormat sdftime = new SimpleDateFormat(environment.getRequiredProperty("dateTimeSecondsFormat"));
		SimpleDateFormat time = new SimpleDateFormat(environment.getRequiredProperty("timeSecondsFormat"));
		String studyNo = null;

		String dateinSting = sdf.format(new Date());
		Document document = new Document();

		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();
		document.setPageSize(PageSize.A4);
		for (NeurobehavioralObservationsofIndividual each : dco) {
			document.newPage();
			includeLogo(request, document);

			com.itextpdf.text.Font regular = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10);
			com.itextpdf.text.Font heading = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 12,
					com.itextpdf.text.Font.BOLD);
			com.itextpdf.text.Font sideheading = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10,
					com.itextpdf.text.Font.BOLD);
			PdfPCell cell = null;
			PdfPTable table = null;
			table = new PdfPTable(1);
			table.setWidthPercentage(95f);
			int count = 1;
			Paragraph pa = new Paragraph("VIMTA LABS LTD", heading);
			pa.setAlignment(Element.ALIGN_CENTER);
			cell = new PdfPCell(pa);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setFixedHeight(30f);
//	         cell.setColspan(3);
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
			document.add(table);

			table = new PdfPTable(1);
			table.setWidthPercentage(95f);
			pa = new Paragraph("Neurobehavioral Observations of Individual Animal (Treatment)", heading);
			pa.setAlignment(Element.ALIGN_CENTER);
			cell = new PdfPCell(pa);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setFixedHeight(30f);
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
			document.add(table);

//			document.add(new Paragraph("\n"));
//			table = new PdfPTable(1);
//			table.setWidthPercentage(95f);
//			cell = new PdfPCell(new Paragraph(""));
//			cell.setBorder(Rectangle.TOP);
//			table.addCell(cell);
//			document.add(table);

			animalNeurobehavioralObservationsofIndividualHeadderPart(each, document);
			PdfPTable hstable = null;

			hstable = new PdfPTable(each.getAnimalNumbers().size() + 2);
			hstable.setWidthPercentage(95);
			generateNeurobehavioralObservationsPeramValues(hstable, each.getHomeCageObservations(), sideheading,
					regular, each.getAnimalNumbers().size(), "Home Cage Observation");
			generateNeurobehavioralObservationsPeramValues(hstable, each.getHandlingObservations(), sideheading,
					regular, each.getAnimalNumbers().size(), "Handling Observations");
			generateNeurobehavioralObservationsPeramValues(hstable, each.getOpenFiledObservations(), sideheading,
					regular, each.getAnimalNumbers().size(), "Open Field Observation");
			document.add(hstable);

			table = new PdfPTable(1);
			table.setWidthPercentage(95);
			cell = new PdfPCell(new Phrase("End of the Report ", regular));
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
			document.add(table);
		}

		document.close();

	}

	private void generateNeurobehavioralObservationsPeramValues(PdfPTable hstable,
			Map<Integer, NeurobehavioralObservationsofIndividualAnimal> homeCageObservations,
			com.itextpdf.text.Font sideheading, com.itextpdf.text.Font regular, int size, String hedding) {
		// TODO Auto-generated method stub
		PdfPCell cell = new PdfPCell(new Phrase(hedding, sideheading));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(size + 2);
		hstable.addCell(cell);
		for (Map.Entry<Integer, NeurobehavioralObservationsofIndividualAnimal> mp : homeCageObservations.entrySet()) {
			Map<Integer, String> animalvalue = mp.getValue().getAnimalvalue();
			cell = new PdfPCell(new Phrase(mp.getValue().getPerameter(), regular));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setColspan(2);
			hstable.addCell(cell);
			for (Map.Entry<Integer, String> v : animalvalue.entrySet()) {
				cell = new PdfPCell(new Phrase(v.getValue(), regular));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				hstable.addCell(cell);
			}
		}
	}

	private void exportDetailedClinicalObservationDataToPdf(DetailedClinicalObservations dco,
			HttpServletRequest request, HttpServletResponse response, String file)
			throws IOException, DocumentException, ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		SimpleDateFormat sdftime = new SimpleDateFormat(environment.getRequiredProperty("dateTimeSecondsFormat"));
		SimpleDateFormat time = new SimpleDateFormat(environment.getRequiredProperty("timeSecondsFormat"));
		String studyNo = null;

		String dateinSting = sdf.format(new Date());
		Document document = new Document();

		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();
		document.setPageSize(PageSize.A4);
		document.newPage();
		includeLogo(request, document);

		com.itextpdf.text.Font regular = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10);
		com.itextpdf.text.Font heading = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 12,
				com.itextpdf.text.Font.BOLD);
		com.itextpdf.text.Font sideheading = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10,
				com.itextpdf.text.Font.BOLD);
		PdfPCell cell = null;
		PdfPTable table = null;
		table = new PdfPTable(1);
		table.setWidthPercentage(95f);
		int count = 1;
		Paragraph pa = new Paragraph("VIMTA LABS LTD", heading);
		pa.setAlignment(Element.ALIGN_CENTER);
		cell = new PdfPCell(pa);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setFixedHeight(30f);
//         cell.setColspan(3);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		document.add(table);

		table = new PdfPTable(1);
		table.setWidthPercentage(95f);
		if (dco.getType().equals("Treatment"))
			pa = new Paragraph("Detailed Clinical Observations of Small Saboratory Animals (Treatment)", heading);
		else if (dco.getType().equals("Acclamatization"))
			pa = new Paragraph("Detailed Clinical Observations of Small Saboratory Animals (Acclamatization)", heading);
		pa.setAlignment(Element.ALIGN_CENTER);
		cell = new PdfPCell(pa);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setFixedHeight(30f);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		document.add(table);

		String doneBy = "";
		String doneDate = "";

		animalDetailedClinicalObservationsHeadderPart(dco, document);
		document.add(new Paragraph("\n"));
		PdfPTable hstable = null;

		table = new PdfPTable(1);
		table.setWidthPercentage(95f);
		cell = new PdfPCell(new Paragraph(""));
		cell.setBorder(Rectangle.TOP);
		table.addCell(cell);
		document.add(table);

		if (dco.getType().equals("Treatment")) {
			hstable = new PdfPTable(5);
		} else
			hstable = new PdfPTable(4);

		hstable.setWidthPercentage(95);
		cell = new PdfPCell(new Phrase("S.No.", sideheading));
		hstable.addCell(cell);
		cell = new PdfPCell(new Phrase("Animal No", sideheading));
		hstable.addCell(cell);
		if (dco.getType().equals("Treatment")) {
			cell = new PdfPCell(new Phrase("Group", sideheading));
			hstable.addCell(cell);
		}
		cell = new PdfPCell(new Phrase("Sex", sideheading));
		hstable.addCell(cell);
		cell = new PdfPCell(new Phrase("Observation", sideheading));
		hstable.addCell(cell);

		List<DetailedClinicalObservationsAnimlas> eachAnimal = dco.getEachAnimal();
		for (DetailedClinicalObservationsAnimlas obj : eachAnimal) {
			cell = new PdfPCell(new Phrase(obj.getSno() + "", regular));
			hstable.addCell(cell);
			if (dco.getType().equals("Treatment")) {
				cell = new PdfPCell(new Phrase("" + obj.getAnimal().getPermanentNo(), regular));
				hstable.addCell(cell);
				cell = new PdfPCell(new Phrase("" + obj.getAnimal().getGroupInfo().getGroupName(), regular));
				hstable.addCell(cell);
			} else {
				cell = new PdfPCell(new Phrase("" + obj.getAnimal().getAnimalNo(), regular));
				hstable.addCell(cell);
			}
			cell = new PdfPCell(new Phrase("" + obj.getAnimal().getGender(), regular));
			hstable.addCell(cell);
			cell = new PdfPCell(new Phrase("" + obj.getValue(), regular));
			hstable.addCell(cell);
		}
		hstable.setHeaderRows(1);
		document.add(hstable);

		table = new PdfPTable(1);
		table.setWidthPercentage(95);
		cell = new PdfPCell(new Phrase("Sign and Date : ", regular));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		document.add(table);

		document.close();

	}

	private void animalDetailedClinicalObservationsHeadderPart(DetailedClinicalObservations dco, Document document)
			throws DocumentException {
		com.itextpdf.text.Font regular = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10);
		PdfPTable hstable = null;
		PdfPCell cell = null;
		hstable = new PdfPTable(2);
		hstable.setWidthPercentage(95);
		cell = new PdfPCell(new Phrase("Study : " + dco.getStudy().getStudyNo(), regular));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_LEFT);
		hstable.addCell(cell);

		cell = new PdfPCell(new Phrase("Species : " + dco.getStudy().getSpecies().getName(), regular));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_LEFT);
		hstable.addCell(cell);
		document.add(hstable);
	}

	private void exportClinicalObservationsDataToPdf(DailyClinicalObservation dco, HttpServletRequest request,
			HttpServletResponse response, String file, StdSubGroupObservationCrfs stdSubGroupObservationCrfs)
			throws IOException, DocumentException, ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		SimpleDateFormat sdftime = new SimpleDateFormat(environment.getRequiredProperty("dateTimeSecondsFormat"));
		SimpleDateFormat time = new SimpleDateFormat(environment.getRequiredProperty("timeSecondsFormat"));
		String studyNo = null;

		String dateinSting = sdf.format(new Date());
		Document document = new Document();

		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();
		document.setPageSize(PageSize.A4.rotate());
		document.newPage();
		includeLogo(request, document);
//		document.setMargins(10f, 10f, 100f, 0f);
//		document.addTitle("Total Roles List");
//		document.setPageSize(PageSize.A4);
//		document.setMargins(80, 80, 50, 80);
//		document.setMarginMirroring(false);
//		document.open();
		com.itextpdf.text.Font regular = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10);
		com.itextpdf.text.Font heading = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 12,
				com.itextpdf.text.Font.BOLD);
		com.itextpdf.text.Font sideheading = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10,
				com.itextpdf.text.Font.BOLD);
		PdfPCell cell = null;
		PdfPTable table = null;
		table = new PdfPTable(1);
		table.setWidthPercentage(95f);
		int count = 1;
		Paragraph pa = new Paragraph("VIMTA LABS LTD", heading);
		pa.setAlignment(Element.ALIGN_CENTER);
		cell = new PdfPCell(pa);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setFixedHeight(30f);
//         cell.setColspan(3);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		document.add(table);

		table = new PdfPTable(1);
		table.setWidthPercentage(95f);
		pa = new Paragraph("Daily Observation Record (Treatment)", heading);
		pa.setAlignment(Element.ALIGN_CENTER);
		cell = new PdfPCell(pa);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setFixedHeight(30f);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		document.add(table);

		String doneBy = "";
		String doneDate = "";

		animalHeadderPart(dco, document, stdSubGroupObservationCrfs);
		document.add(new Paragraph("\n"));
		PdfPTable hstable = null;

		table = new PdfPTable(1);
		table.setWidthPercentage(95f);
		cell = new PdfPCell(new Paragraph(""));
		cell.setBorder(Rectangle.TOP);
		table.addCell(cell);
		document.add(table);

		hstable = new PdfPTable(2 + dco.getAllDays().size());
		hstable.setWidthPercentage(95);
		cell = new PdfPCell(new Phrase("Animal No.", regular));
//		cell.setBorder(Rectangle.NO_BORDER);
		hstable.addCell(cell);
		cell = new PdfPCell(new Phrase("Sex", regular));
//		cell.setBorder(Rectangle.NO_BORDER);
		hstable.addCell(cell);
		Set<Integer> allDays = dco.getAllDays();
		for (Integer day : allDays) {
			cell = new PdfPCell(new Phrase("Day : " + day, regular));
//			cell.setBorder(Rectangle.NO_BORDER);
			hstable.addCell(cell);
		}
		Map<Integer, Long> eachAnimalOder = dco.getAnimalOderMap();
		Map<Long, Map<Integer, String>> eachAnimalDataMap = dco.getEachAnimalData();
		Map<Long, StudyAnimal> eachAnimal = dco.getAnimalMap();
		for (Map.Entry<Integer, Long> animalMp : eachAnimalOder.entrySet()) {
			StudyAnimal animal = eachAnimal.get(animalMp.getValue());
			cell = new PdfPCell(new Phrase(animal.getPermanentNo(), regular));
			hstable.addCell(cell);
			cell = new PdfPCell(new Phrase("" + animal.getGender(), regular));
			hstable.addCell(cell);
			Map<Integer, String> mapp = eachAnimalDataMap.get(animalMp.getValue());
			for (Map.Entry<Integer, String> mp : mapp.entrySet()) {
				cell = new PdfPCell(new Phrase(mp.getValue(), regular));
				hstable.addCell(cell);
			}
		}
		hstable.setHeaderRows(1);

		cell = new PdfPCell(new Phrase("Time (hrs)", regular));
//		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(2);
		hstable.addCell(cell);
		for (Integer day : allDays) {
			cell = new PdfPCell(new Phrase("", regular));
//			cell.setBorder(Rectangle.NO_BORDER);
			hstable.addCell(cell);
		}

		cell = new PdfPCell(new Phrase("Remarks", regular));
//		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(2);
		hstable.addCell(cell);
		for (Integer day : allDays) {
			cell = new PdfPCell(new Phrase("", regular));
//			cell.setBorder(Rectangle.NO_BORDER);
			hstable.addCell(cell);
		}

		cell = new PdfPCell(new Phrase("Observed By(Sign & Date)", regular));
//		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(2);
		hstable.addCell(cell);
		for (Integer day : allDays) {
			cell = new PdfPCell(new Phrase("", regular));
//			cell.setBorder(Rectangle.NO_BORDER);
			hstable.addCell(cell);
		}
		document.add(hstable);

		table.setSpacingAfter(20f);
//		document.add(new Paragraph("\n"));
//		document.add(Chunk.NEWLINE);
//		document.add(Chunk.NEWLINE);
//		document.add(Chunk.NEWLINE);
		table = new PdfPTable(1);
		table.setWidthPercentage(95f);
		cell = new PdfPCell(new Paragraph(""));
		cell.setBorder(Rectangle.TOP);
		table.addCell(cell);
		document.add(table);
//		document.add(new Paragraph("\n"));
//		document.add(new Paragraph("\n\n"));
//		document.add(new Paragraph("\n\n"));

		table = new PdfPTable(1);
		table.setWidthPercentage(95);
		cell = new PdfPCell(new Phrase("End of the Report ", regular));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		document.add(table);

		document.close();

	}

	private void animalHeadderPart(DailyClinicalObservation dco, Document document,
			StdSubGroupObservationCrfs stdSubGroupObservationCrfs) throws DocumentException {
		com.itextpdf.text.Font regular = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10);
		PdfPTable hstable = null;
		PdfPCell cell = null;
		hstable = new PdfPTable(4);
		hstable.setWidthPercentage(95);
		cell = new PdfPCell(new Phrase("Study : " + dco.getStudy().getStudyNo(), regular));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_LEFT);
		hstable.addCell(cell);

		cell = new PdfPCell(new Phrase("Species : " + dco.getStudy().getSpecies().getName(), regular));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_LEFT);
		hstable.addCell(cell);

		cell = new PdfPCell(new Phrase(
				"Group : " + stdSubGroupObservationCrfs.getSubGroupInfo().getGroup().getGroupName(), regular));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_LEFT);
		hstable.addCell(cell);

		cell = new PdfPCell(new Phrase("Dose : ", regular));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_LEFT);
		hstable.addCell(cell);
		document.add(hstable);
		document.add(Chunk.NEWLINE);

	}

	private void includeLogo(HttpServletRequest request, Document document)
			throws MalformedURLException, IOException, DocumentException {
		// TODO Auto-generated method stub
		String logo = request.getServletContext().getRealPath("/static/images/vimta.png");
		Image logoimg = Image.getInstance(logo);
		logoimg.scaleAbsolute(100, 40);

//		logoimg.setAbsolutePosition(200, 510); 
//		logoimg.scaleAbsolute(100, 60);
//		
////		logoimg.setAbsolutePosition(293, 530); 
////        logoimg.scaleAbsolute(100, 60);		
		document.add(logoimg);

	}

	private String checktheFile(String pdfpath, String fileName) {
		FileOutputStream fos = null;

		try {
			pdfpath = "C:\\instrumenData";
			File file = new File(pdfpath);
			if (!file.exists())
				file.mkdirs();
			pdfpath = pdfpath + "\\" + fileName;
			System.out.println("File Path : " + pdfpath);
			// read only file with same name already exists
			file = new File(pdfpath);
			file.createNewFile();
			/*
			 * Make sure that if the file exists, it is writable first
			 */
			if (file.exists() && !file.canWrite()) {

				System.out.println("File exists and it is read only, making it writable");
				file.setWritable(true);
			}
			if (!file.canRead())
				file.setReadable(true);
			fos = new FileOutputStream(file);

			System.out.println("File can be overwritten now!");
			return pdfpath;
		} catch (IOException fnfe) {
			fnfe.printStackTrace();

		} finally {

			try {
				if (fos != null)
					fos.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public FileInformation acclamatizationObservatinExport(Long observationId, ModelMap model,
			HttpServletRequest request, RedirectAttributes redirectAttributes, HttpServletResponse response)
			throws IOException, DocumentException, ParseException {
		FileInformation fi = new FileInformation();
		// TODO Auto-generated method stub
		String realPath = request.getSession().getServletContext().getRealPath("/");
		StudyAcclamatizationData studyAcclamatizationData = expermentalDesignDao
				.studyAcclamatizationData(observationId);
		fi.setFileName(studyAcclamatizationData.getCrf().getObservationDesc() + ".pdf");
		String file = checktheFile(realPath, fi.getFileName());
		System.out.println(file);
		fi.setFilePath(file);

		List<StudyAnimal> animals = null;
		if (!studyAcclamatizationData.getCrf().getObservationName().equalsIgnoreCase("Record for Mortality Morbidity"))
			animals = studyDao.studyAnimals(studyAcclamatizationData.getStudy().getId());
		Crf crf = crfDAO.getCrfForView(studyAcclamatizationData.getCrf().getId());
		CrfSectionElement dayElement = null;
		CrfSectionElement valueElement = null;
		for (CrfSection section : crf.getSections()) {
			System.out.println(section.getElement().size());
			for (CrfSectionElement ele : section.getElement()) {
				System.out.println(ele.getName());
				if (ele.getName().equals("day")) {
					dayElement = ele;
				} else if (ele.getName().equals("value")) {
					valueElement = ele;
				}
				if (dayElement != null && valueElement != null)
					break;
			}
		}

		if (crf.getType().equalsIgnoreCase("IN-LIFE OBSERVATIONS")
				|| crf.getType().equalsIgnoreCase("TERMINAL OBSERVATIONS")) {
			if (crf.getSubType().equalsIgnoreCase("Clinical Observations")) {
				if (crf.getObservationName().equalsIgnoreCase("Daily Clinical Observations")) {
					DailyClinicalObservation dco = expermentalDesignDao.acclamatiztionDailyClinicalObservation(
							dayElement, valueElement, studyAcclamatizationData, animals);
					exportAcclamatiztionClinicalObservationsDataToPdf(dco, request, response, file,
							studyAcclamatizationData);
				} else if (crf.getObservationName().equalsIgnoreCase("Record for Mortality Morbidity")) {
					CrfSectionElement mortalityElement = null;
					CrfSectionElement morbidityElement = null;
					CrfSectionElement mortalitySelect1Element = null;
					for (CrfSection section : crf.getSections()) {
						System.out.println(section.getElement().size());
						for (CrfSectionElement ele : section.getElement()) {
							System.out.println(ele.getName());
							if (ele.getName().equals("mortality")) {
								mortalityElement = ele;
							} else if (ele.getName().equals("morbidity")) {
								morbidityElement = ele;
							} else if (ele.getName().equals("mortalitySelect1")) {
								mortalitySelect1Element = ele;
							}

							if (mortalityElement != null && morbidityElement != null && mortalitySelect1Element != null)
								break;
						}
					}
					List<RecordforMortalityMorbidity> recordforMortalityMorbidity = expermentalDesignDao
							.acclamatizationrecordforMortalityMorbidity(mortalityElement, morbidityElement,
									mortalitySelect1Element, studyAcclamatizationData);

					exportRecordforMortalityMorbidityToPdf(recordforMortalityMorbidity, request, response, file, null,
							studyAcclamatizationData);
				} else if (crf.getObservationName().equalsIgnoreCase("Detailed Clinical Observations")) {
					valueElement = null;
					for (CrfSection section : crf.getSections()) {
						System.out.println(section.getElement().size());
						for (CrfSectionElement ele : section.getElement()) {
							System.out.println(ele.getName());
							if (ele.getName().equals("observation")) {
								valueElement = ele;
								break;
							}
						}
					}

					DetailedClinicalObservations dco = expermentalDesignDao.acclamatizationDetailedClinicalObservation(
							valueElement, studyAcclamatizationData, animals);
					exportDetailedClinicalObservationDataToPdf(dco, request, response, file);
				} else if (crf.getObservationName().equalsIgnoreCase("Ophthalmological Examination")) {
					CrfSectionElement leftEyeElement = null;
					CrfSectionElement rightEyeElement = null;
					for (CrfSection section : crf.getSections()) {
						System.out.println(section.getElement().size());
						for (CrfSectionElement ele : section.getElement()) {
							System.out.println(ele.getName());
							if (ele.getName().equals("leftEye")) {
								leftEyeElement = ele;
							} else if (ele.getName().equals("rightEye")) {
								rightEyeElement = ele;
							}
							if (leftEyeElement != null && rightEyeElement != null) {
								break;
							}
						}
					}

					OphthalmologicalExamination dco = expermentalDesignDao.acclamatizationOphthalmologicalExamination(
							leftEyeElement, rightEyeElement, studyAcclamatizationData, animals);
					exportOphthalmologicalExaminationDataToPdf(dco, request, response, file);
				}
			} else if (crf.getSubType().equalsIgnoreCase("CNS/PNS")) {
				if (crf.getObservationName().equalsIgnoreCase("Neurobehavioral Observations of Individual Animal")) {
//					NeurobehavioralObservationsOfIndividualAnimal dto = expermentalDesignDao
//							.neurobehavioralObservationsOfIndividualAnimal(dayElement, valueElement,
//									studyAcclamatizationData, animals);
//					DailyClinicalObservation dco = expermentalDesignDao.acclamatiztionDailyClinicalObservation(
//							dayElement, valueElement, studyAcclamatizationData, animals);
//					exportAcclamatiztionClinicalObservationsDataToPdf(dco, request, response, file,
//							studyAcclamatizationData);
				}
			}
		}

		return fi;
	}

	private void exportAcclamatiztionClinicalObservationsDataToPdf(DailyClinicalObservation dco,
			HttpServletRequest request, HttpServletResponse response, String file,
			StudyAcclamatizationData studyAcclamatizationData) throws IOException, DocumentException, ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		SimpleDateFormat sdftime = new SimpleDateFormat(environment.getRequiredProperty("dateTimeSecondsFormat"));
		SimpleDateFormat time = new SimpleDateFormat(environment.getRequiredProperty("timeSecondsFormat"));
		String studyNo = null;

		String dateinSting = sdf.format(new Date());
		Document document = new Document();

		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();
		document.setPageSize(PageSize.A4.rotate());
		document.newPage();
		includeLogo(request, document);
		com.itextpdf.text.Font regular = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10);
		com.itextpdf.text.Font heading = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 12,
				com.itextpdf.text.Font.BOLD);
		com.itextpdf.text.Font sideheading = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10,
				com.itextpdf.text.Font.BOLD);
		PdfPCell cell = null;
		PdfPTable table = null;
		table = new PdfPTable(1);
		table.setWidthPercentage(95f);
		int count = 1;
		Paragraph pa = new Paragraph("VIMTA LABS LTD", heading);
		pa.setAlignment(Element.ALIGN_CENTER);
		cell = new PdfPCell(pa);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setFixedHeight(30f);
//         cell.setColspan(3);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		document.add(table);

		table = new PdfPTable(1);
		table.setWidthPercentage(95f);
		pa = new Paragraph("Daily Observation Record (Acclamatization)", heading);
		pa.setAlignment(Element.ALIGN_CENTER);
		cell = new PdfPCell(pa);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setFixedHeight(30f);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		document.add(table);

		String doneBy = "";
		String doneDate = "";

		animalHeadderPart(dco, document, studyAcclamatizationData);
		document.add(new Paragraph("\n"));
		PdfPTable hstable = null;

		table = new PdfPTable(1);
		table.setWidthPercentage(95f);
		cell = new PdfPCell(new Paragraph(""));
		cell.setBorder(Rectangle.TOP);
		table.addCell(cell);
		document.add(table);

		hstable = new PdfPTable(2 + dco.getAllDays().size());
		hstable.setWidthPercentage(95);
		cell = new PdfPCell(new Phrase("Animal No.", regular));
//		cell.setBorder(Rectangle.NO_BORDER);
		hstable.addCell(cell);
		cell = new PdfPCell(new Phrase("Sex", regular));
//		cell.setBorder(Rectangle.NO_BORDER);
		hstable.addCell(cell);
		Set<Integer> allDays = dco.getAllDays();
		for (Integer day : allDays) {
			cell = new PdfPCell(new Phrase("Day : " + day, regular));
//			cell.setBorder(Rectangle.NO_BORDER);
			hstable.addCell(cell);
		}
		Map<Integer, Long> eachAnimalOder = dco.getAnimalOderMap();
		Map<Long, Map<Integer, String>> eachAnimalDataMap = dco.getEachAnimalData();
		Map<Long, StudyAnimal> eachAnimal = dco.getAnimalMap();
		for (Map.Entry<Integer, Long> animalMp : eachAnimalOder.entrySet()) {
			StudyAnimal animal = eachAnimal.get(animalMp.getValue());
			cell = new PdfPCell(new Phrase(animal.getAnimalNo(), regular));
			hstable.addCell(cell);
			cell = new PdfPCell(new Phrase("" + animal.getGender(), regular));
			hstable.addCell(cell);
			Map<Integer, String> mapp = eachAnimalDataMap.get(animalMp.getValue());
			for (Map.Entry<Integer, String> mp : mapp.entrySet()) {
				cell = new PdfPCell(new Phrase(mp.getValue(), regular));
				hstable.addCell(cell);
			}
		}
		hstable.setHeaderRows(1);

		cell = new PdfPCell(new Phrase("Time (hrs)", regular));
//		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(2);
		hstable.addCell(cell);
		for (Integer day : allDays) {
			cell = new PdfPCell(new Phrase("", regular));
//			cell.setBorder(Rectangle.NO_BORDER);
			hstable.addCell(cell);
		}

		cell = new PdfPCell(new Phrase("Remarks", regular));
//		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(2);
		hstable.addCell(cell);
		for (Integer day : allDays) {
			cell = new PdfPCell(new Phrase("", regular));
//			cell.setBorder(Rectangle.NO_BORDER);
			hstable.addCell(cell);
		}

		cell = new PdfPCell(new Phrase("Observed By(Sign & Date)", regular));
//		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(2);
		hstable.addCell(cell);
		for (Integer day : allDays) {
			cell = new PdfPCell(new Phrase("", regular));
//			cell.setBorder(Rectangle.NO_BORDER);
			hstable.addCell(cell);
		}
		document.add(hstable);

		table.setSpacingAfter(20f);
//		document.add(new Paragraph("\n"));
//		document.add(Chunk.NEWLINE);
//		document.add(Chunk.NEWLINE);
//		document.add(Chunk.NEWLINE);
		table = new PdfPTable(1);
		table.setWidthPercentage(95f);
		cell = new PdfPCell(new Paragraph(""));
		cell.setBorder(Rectangle.TOP);
		table.addCell(cell);
		document.add(table);
//		document.add(new Paragraph("\n"));
//		document.add(new Paragraph("\n\n"));
//		document.add(new Paragraph("\n\n"));

		table = new PdfPTable(1);
		table.setWidthPercentage(95);
		cell = new PdfPCell(new Phrase("End of the Report ", regular));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		document.add(table);

		document.close();

	}

	private void animalHeadderPart(DailyClinicalObservation dco, Document document,
			StudyAcclamatizationData studyAcclamatizationData) throws DocumentException {
		com.itextpdf.text.Font regular = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10);
		PdfPTable hstable = null;
		PdfPCell cell = null;
		hstable = new PdfPTable(2);
		hstable.setWidthPercentage(95);
		cell = new PdfPCell(new Phrase("Study : " + dco.getStudy().getStudyNo(), regular));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_LEFT);
		hstable.addCell(cell);

		cell = new PdfPCell(new Phrase("Species : " + dco.getStudy().getSpecies().getName(), regular));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_LEFT);
		hstable.addCell(cell);

//		cell = new PdfPCell(new Phrase("Group : " + studyAcclamatizationData.getSubGroupInfo().getGroup().getGroupName(), regular));
//		cell.setBorder(Rectangle.NO_BORDER);
//		cell.setVerticalAlignment(Element.ALIGN_LEFT);
//		hstable.addCell(cell);
//
//		cell = new PdfPCell(new Phrase("Dose : ", regular));
//		cell.setBorder(Rectangle.NO_BORDER);
//		cell.setVerticalAlignment(Element.ALIGN_LEFT);
//		hstable.addCell(cell);
		document.add(hstable);
		document.add(Chunk.NEWLINE);

	}

	private void exportClinicalObservationsDataTimePointToPdf(Map<Integer, ClinicalObservationRecordTimePoint> map,
			HttpServletRequest request, HttpServletResponse response, String file,
			StdSubGroupObservationCrfs stdSubGroupObservationCrfs)
			throws IOException, DocumentException, ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		Document document = new Document();

		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();
		document.setPageSize(PageSize.A4.rotate());
		for (Map.Entry<Integer, ClinicalObservationRecordTimePoint> mp : map.entrySet()) {
			ClinicalObservationRecordTimePoint obj = mp.getValue();
			document.newPage();
			includeLogo(request, document);
			com.itextpdf.text.Font regular = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10);
			com.itextpdf.text.Font heading = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 12,
					com.itextpdf.text.Font.BOLD);
			com.itextpdf.text.Font sideheading = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10,
					com.itextpdf.text.Font.BOLD);
			PdfPCell cell = null;
			PdfPTable table = null;
			table = new PdfPTable(1);
			table.setWidthPercentage(95f);
			int count = 1;
			Paragraph pa = new Paragraph("VIMTA LABS LTD", heading);
			pa.setAlignment(Element.ALIGN_CENTER);
			cell = new PdfPCell(pa);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setFixedHeight(30f);
//	         cell.setColspan(3);
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
			document.add(table);

			table = new PdfPTable(1);
			table.setWidthPercentage(95f);
			pa = new Paragraph("Clnical Observation Record (Time Point)", heading);
			pa.setAlignment(Element.ALIGN_CENTER);
			cell = new PdfPCell(pa);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setFixedHeight(30f);
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
			document.add(table);

			String doneBy = "";
			String doneDate = "";

			animalClnicalObservationTimePointHeadderPart(obj, document, stdSubGroupObservationCrfs, mp.getKey());
			document.add(new Paragraph("\n"));
			PdfPTable hstable = null;

			table = new PdfPTable(1);
			table.setWidthPercentage(95f);
			cell = new PdfPCell(new Paragraph(""));
			cell.setBorder(Rectangle.TOP);
			table.addCell(cell);
			document.add(table);

			hstable = new PdfPTable(2 + (obj.getAllTimePoitns().size() * 2));
			hstable.setWidthPercentage(95);
			cell = new PdfPCell(new Phrase("Animal No.", regular));
//			cell.setBorder(Rectangle.NO_BORDER);
			hstable.addCell(cell);
			cell = new PdfPCell(new Phrase("Sex", regular));
//			cell.setBorder(Rectangle.NO_BORDER);
			hstable.addCell(cell);
			Set<String> allTimepoints = obj.getAllTimePoitns();
			for (String timePoint : allTimepoints) {
				table = new PdfPTable(2);
				cell = new PdfPCell(new Phrase("TimePoint : " + timePoint, regular));
				cell.setColspan(2);
				table.addCell(cell);
				cell = new PdfPCell(new Phrase("Observation", regular));
				table.addCell(cell);
				cell = new PdfPCell(new Phrase("Time (hrs)", regular));
				table.addCell(cell);

				cell = new PdfPCell(table);
				cell.setColspan(2);
				cell.setBorder(Rectangle.NO_BORDER);
				hstable.addCell(cell);
			}
			Map<Integer, Long> eachAnimalOder = obj.getAnimalOderMap();
			Map<Long, Map<String, ClinicalObservationRecordTimePointElements>> eachAnimalDataMap = obj
					.getEachAnimalData();
			Map<Long, StudyAnimal> eachAnimal = obj.getAnimalMap();
			for (Map.Entry<Integer, Long> animalMp : eachAnimalOder.entrySet()) {
				StudyAnimal animal = eachAnimal.get(animalMp.getValue());
				cell = new PdfPCell(new Phrase(animal.getPermanentNo(), regular));
				hstable.addCell(cell);
				cell = new PdfPCell(new Phrase("" + animal.getGender(), regular));
				hstable.addCell(cell);
				Map<String, ClinicalObservationRecordTimePointElements> mapp = eachAnimalDataMap
						.get(animalMp.getValue());
				for (Map.Entry<String, ClinicalObservationRecordTimePointElements> mpp : mapp.entrySet()) {
					cell = new PdfPCell(new Phrase(mpp.getValue().getObservation(), regular));
					hstable.addCell(cell);
					cell = new PdfPCell(new Phrase(mpp.getValue().getTimeInHr(), regular));
					hstable.addCell(cell);
				}
			}
			hstable.setHeaderRows(1);

			cell = new PdfPCell(new Phrase("Remarks", regular));
//			cell.setBorder(Rectangle.NO_BORDER);
			cell.setColspan(2);
			hstable.addCell(cell);
			for (String day : allTimepoints) {
				cell = new PdfPCell(new Phrase("", regular));
//				cell.setBorder(Rectangle.NO_BORDER);
				cell.setColspan(2);
				hstable.addCell(cell);
			}

			cell = new PdfPCell(new Phrase("Observed By(Sign & Date)", regular));
//			cell.setBorder(Rectangle.NO_BORDER);
			cell.setColspan(2);
			hstable.addCell(cell);
			for (String day : allTimepoints) {
				cell = new PdfPCell(new Phrase("", regular));
//				cell.setBorder(Rectangle.NO_BORDER);
				cell.setColspan(2);
				hstable.addCell(cell);
			}
			document.add(hstable);

			table.setSpacingAfter(20f);
			table = new PdfPTable(1);
			table.setWidthPercentage(95f);
			cell = new PdfPCell(new Paragraph(""));
			cell.setBorder(Rectangle.TOP);
			table.addCell(cell);
			document.add(table);

			table = new PdfPTable(1);
			table.setWidthPercentage(95);
			cell = new PdfPCell(new Phrase("End of the Report ", regular));
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
			document.add(table);

		}
		document.close();

	}

	private void animalClnicalObservationTimePointHeadderPart(ClinicalObservationRecordTimePoint dco, Document document,
			StdSubGroupObservationCrfs stdSubGroupObservationCrfs, int day) throws DocumentException {
		com.itextpdf.text.Font regular = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10);
		PdfPTable hstable = null;
		PdfPCell cell = null;
		hstable = new PdfPTable(5);
		hstable.setWidthPercentage(95);
		cell = new PdfPCell(new Phrase("Study : " + dco.getStudy().getStudyNo(), regular));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_LEFT);
		hstable.addCell(cell);

		cell = new PdfPCell(new Phrase("Species : " + dco.getStudy().getSpecies().getName(), regular));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_LEFT);
		hstable.addCell(cell);

		cell = new PdfPCell(new Phrase(
				"Group : " + stdSubGroupObservationCrfs.getSubGroupInfo().getGroup().getGroupName(), regular));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_LEFT);
		hstable.addCell(cell);

		cell = new PdfPCell(new Phrase("Day : " + day, regular));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_LEFT);
		hstable.addCell(cell);

		cell = new PdfPCell(new Phrase("Dose : ", regular));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_LEFT);
		hstable.addCell(cell);
		document.add(hstable);
		document.add(Chunk.NEWLINE);

	}

	private void exportRecordforMortalityMorbidityToPdf(List<RecordforMortalityMorbidity> recordforMortalityMorbidity,
			HttpServletRequest request, HttpServletResponse response, String file,
			StdSubGroupObservationCrfs stdSubGroupObservationCrfs, StudyAcclamatizationData studyAcclamatizationData)
			throws IOException, DocumentException, ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		Document document = new Document();

		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();
		document.setPageSize(PageSize.A4.rotate());
		document.newPage();
		includeLogo(request, document);

		com.itextpdf.text.Font regular = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10);
		com.itextpdf.text.Font heading = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 12,
				com.itextpdf.text.Font.BOLD);
		com.itextpdf.text.Font sideheading = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10,
				com.itextpdf.text.Font.BOLD);
		PdfPCell cell = null;
		PdfPTable table = null;
		table = new PdfPTable(1);
		table.setWidthPercentage(95f);
		Paragraph pa = new Paragraph("VIMTA LABS LTD", heading);
		pa.setAlignment(Element.ALIGN_CENTER);
		cell = new PdfPCell(pa);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setFixedHeight(30f);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		document.add(table);

		table = new PdfPTable(1);
		table.setWidthPercentage(95f);
		if (stdSubGroupObservationCrfs != null)
			pa = new Paragraph("Record for Mortality/Morbidity (Treatment)", heading);
		else
			pa = new Paragraph("Record for Mortality/Morbidity (Acclamatization)", heading);
		pa.setAlignment(Element.ALIGN_CENTER);
		cell = new PdfPCell(pa);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setFixedHeight(30f);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		document.add(table);

		recordForMortalityMorbidityHeadderPart(document, stdSubGroupObservationCrfs, studyAcclamatizationData);
		PdfPTable hstable = null;

		hstable = new PdfPTable(10);
		hstable.setWidthPercentage(95);
		cell = new PdfPCell(new Phrase("Date", regular));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		hstable.addCell(cell);

		table = new PdfPTable(4);
		cell = new PdfPCell(new Phrase("Morning", sideheading));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(4);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("Time", regular));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		cell = new PdfPCell(new Phrase("Mortality", regular));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		cell = new PdfPCell(new Phrase("Morbidity", regular));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		cell = new PdfPCell(new Phrase("Sign & Date", regular));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		cell = new PdfPCell(table);
		cell.setColspan(4);
		hstable.addCell(cell);

		table = new PdfPTable(4);
		cell = new PdfPCell(new Phrase("Evening/Afternoon", sideheading));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(4);
		table.addCell(cell);
		cell = new PdfPCell(new Phrase("Time", regular));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		cell = new PdfPCell(new Phrase("Mortality", regular));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		cell = new PdfPCell(new Phrase("Morbidity", regular));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		cell = new PdfPCell(new Phrase("Sign & Date", regular));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		cell = new PdfPCell(table);
		cell.setColspan(4);
		hstable.addCell(cell);

		cell = new PdfPCell(new Phrase("Remarks", regular));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		hstable.addCell(cell);

		for (RecordforMortalityMorbidity rmm : recordforMortalityMorbidity) {
			cell = new PdfPCell(new Phrase(rmm.getDate(), regular));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hstable.addCell(cell);
			cell = new PdfPCell(new Phrase(rmm.getMorningTime(), regular));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hstable.addCell(cell);
			cell = new PdfPCell(new Phrase(rmm.getMorningMortality(), regular));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hstable.addCell(cell);
			cell = new PdfPCell(new Phrase(rmm.getMorningMorblility(), regular));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hstable.addCell(cell);
			if (!rmm.getMorningMortality().equals("") || !rmm.getMorningMorblility().equals(""))
				cell = new PdfPCell(new Phrase(rmm.getMorningUserName() + "\n" + rmm.getDate(), regular));
			else
				cell = new PdfPCell(new Phrase("", regular));

			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hstable.addCell(cell);

			cell = new PdfPCell(new Phrase(rmm.getEveninngTime(), regular));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hstable.addCell(cell);
			cell = new PdfPCell(new Phrase(rmm.getEveninngMortality(), regular));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hstable.addCell(cell);
			cell = new PdfPCell(new Phrase(rmm.getEveninngMorblility(), regular));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hstable.addCell(cell);
			if (!rmm.getEveninngMorblility().equals("") || !rmm.getEveninngMortality().equals(""))
				cell = new PdfPCell(new Phrase(rmm.getEveninngUserName() + "\n" + rmm.getDate(), regular));
			else
				cell = new PdfPCell(new Phrase("", regular));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hstable.addCell(cell);
			cell = new PdfPCell(new Phrase("", regular));
			hstable.addCell(cell);
		}
		hstable.setHeaderRows(1);
		document.add(hstable);

		table = new PdfPTable(1);
		table.setWidthPercentage(95f);
		cell = new PdfPCell(new Paragraph(""));
		cell.setBorder(Rectangle.TOP);
		table.addCell(cell);
		document.add(table);

		table = new PdfPTable(1);
		table.setWidthPercentage(95);
		cell = new PdfPCell(new Phrase("End of the Report ", regular));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		document.add(table);

		document.close();
	}

	private void recordForMortalityMorbidityHeadderPart(Document document,
			StdSubGroupObservationCrfs stdSubGroupObservationCrfs, StudyAcclamatizationData studyAcclamatizationData)
			throws DocumentException {
		com.itextpdf.text.Font regular = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10);
		PdfPTable hstable = null;
		PdfPCell cell = null;
		hstable = new PdfPTable(3);
		hstable.setWidthPercentage(95);
		if (stdSubGroupObservationCrfs != null)
			cell = new PdfPCell(new Phrase("Study : " + stdSubGroupObservationCrfs.getStudy().getStudyNo(), regular));
		else
			cell = new PdfPCell(new Phrase("Study : " + studyAcclamatizationData.getStudy().getStudyNo(), regular));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_LEFT);
		hstable.addCell(cell);

		if (stdSubGroupObservationCrfs != null)
			cell = new PdfPCell(
					new Phrase("Species : " + stdSubGroupObservationCrfs.getStudy().getSpecies().getName(), regular));
		else
			cell = new PdfPCell(
					new Phrase("Species : " + studyAcclamatizationData.getStudy().getSpecies().getName(), regular));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_LEFT);
		hstable.addCell(cell);

		cell = new PdfPCell(new Phrase("Strain : ", regular));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_LEFT);
		hstable.addCell(cell);
		document.add(hstable);
	}

	private void exportOphthalmologicalExaminationDataToPdf(OphthalmologicalExamination dco, HttpServletRequest request,
			HttpServletResponse response, String file) throws IOException, DocumentException, ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		SimpleDateFormat sdftime = new SimpleDateFormat(environment.getRequiredProperty("dateTimeSecondsFormat"));
		SimpleDateFormat time = new SimpleDateFormat(environment.getRequiredProperty("timeSecondsFormat"));
		String studyNo = null;

		String dateinSting = sdf.format(new Date());
		Document document = new Document();

		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();
		document.setPageSize(PageSize.A4);
		document.newPage();
		includeLogo(request, document);

		com.itextpdf.text.Font regular = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10);
		com.itextpdf.text.Font heading = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 12,
				com.itextpdf.text.Font.BOLD);
		com.itextpdf.text.Font sideheading = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10,
				com.itextpdf.text.Font.BOLD);
		PdfPCell cell = null;
		PdfPTable table = null;
		table = new PdfPTable(1);
		table.setWidthPercentage(95f);
		int count = 1;
		Paragraph pa = new Paragraph("VIMTA LABS LTD", heading);
		pa.setAlignment(Element.ALIGN_CENTER);
		cell = new PdfPCell(pa);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setFixedHeight(30f);
//         cell.setColspan(3);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		document.add(table);

		table = new PdfPTable(1);
		table.setWidthPercentage(95f);
		if (dco.getType().equals("Treatment"))
			pa = new Paragraph("Individual Animal Ophthalmological Examination (Treatment)", heading);
		else if (dco.getType().equals("Acclamatization"))
			pa = new Paragraph("Individual Animal Ophthalmological Examination (Acclamatization)", heading);
		pa.setAlignment(Element.ALIGN_CENTER);
		cell = new PdfPCell(pa);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setFixedHeight(30f);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		document.add(table);

		String doneBy = "";
		String doneDate = "";

		animalOphthalmologicalExaminationHeadderPart(dco, document);
		document.add(new Paragraph("\n"));
		PdfPTable hstable = null;

		table = new PdfPTable(1);
		table.setWidthPercentage(95f);
		cell = new PdfPCell(new Paragraph(""));
		cell.setBorder(Rectangle.TOP);
		table.addCell(cell);
		document.add(table);

		hstable = new PdfPTable(3);
		hstable.setWidthPercentage(95);
		cell = new PdfPCell(new Phrase("Animal No", sideheading));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		hstable.addCell(cell);

		table = new PdfPTable(2);
		if (dco.getType().equals("Treatment"))
			cell = new PdfPCell(new Phrase("Post Dose", sideheading));
		else
			cell = new PdfPCell(new Phrase("Pre Dose", sideheading));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(2);
		table.addCell(cell);
		cell = new PdfPCell(new Phrase("Left Eye", sideheading));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		cell = new PdfPCell(new Phrase("Right Eye", sideheading));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		cell = new PdfPCell(table);
		cell.setColspan(2);
		hstable.addCell(cell);

		List<OphthalmologicalExaminationAnimlas> eachAnimal = dco.getEachAnimal();
		for (OphthalmologicalExaminationAnimlas obj : eachAnimal) {
			if (dco.getType().equals("Treatment")) {
				cell = new PdfPCell(new Phrase("" + obj.getAnimal().getPermanentNo(), regular));
				hstable.addCell(cell);
			} else {
				cell = new PdfPCell(new Phrase("" + obj.getAnimal().getAnimalNo(), regular));
				hstable.addCell(cell);
			}

			cell = new PdfPCell(new Phrase("" + obj.getLeftEye(), regular));
			hstable.addCell(cell);
			cell = new PdfPCell(new Phrase("" + obj.getRightEye(), regular));
			hstable.addCell(cell);
		}
		hstable.setHeaderRows(1);
		document.add(hstable);

		table = new PdfPTable(1);
		table.setWidthPercentage(95);
		cell = new PdfPCell(new Phrase("End of the Report ", regular));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		document.add(table);

		document.close();

	}

	private void animalOphthalmologicalExaminationHeadderPart(OphthalmologicalExamination dco, Document document)
			throws DocumentException {
		com.itextpdf.text.Font regular = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10);
		PdfPTable hstable = null;
		PdfPCell cell = null;
		hstable = new PdfPTable(2);
		hstable.setWidthPercentage(95);
		cell = new PdfPCell(new Phrase("Study : " + dco.getStudy().getStudyNo(), regular));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_LEFT);
		hstable.addCell(cell);

		cell = new PdfPCell(
				new Phrase("Group & Dose : " + dco.getStudy().getDoseVolume() + dco.getStudy().getDoseMgKg(), regular));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_LEFT);
		hstable.addCell(cell);

		cell = new PdfPCell(new Phrase("Gender : " + dco.getStudy().getGender(), regular));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_LEFT);
		hstable.addCell(cell);
		document.add(hstable);
	}

	private void animalNeurobehavioralObservationsofIndividualHeadderPart(NeurobehavioralObservationsofIndividual dco,
			Document document) throws DocumentException {
		com.itextpdf.text.Font regular = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10);
		com.itextpdf.text.Font sideheading = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10,
				com.itextpdf.text.Font.BOLD);
		PdfPTable hstable = null;
		PdfPCell cell = null;
		hstable = new PdfPTable(3);
		hstable.setWidthPercentage(95);
		cell = new PdfPCell(new Phrase("Week N°: " + dco.getStudy().getStudyNo(), regular));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_LEFT);
		hstable.addCell(cell);

		cell = new PdfPCell(new Phrase("Gender : " + dco.getStudy().getGender(), regular));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_LEFT);
		hstable.addCell(cell);

		cell = new PdfPCell(new Phrase("Group :" + dco.getGroupNumber() + " & Dose : G1(0 mg/kg b.w.t) : ", regular));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_LEFT);
		hstable.addCell(cell);
		document.add(hstable);

		hstable = new PdfPTable(dco.getAnimalNumbers().size() + 2);
		hstable.setWidthPercentage(95);

		cell = new PdfPCell(new Phrase("Perameter", sideheading));
		cell.setVerticalAlignment(Element.ALIGN_LEFT);
		cell.setRowspan(2);
		cell.setColspan(2);
		hstable.addCell(cell);
		cell = new PdfPCell(new Phrase("Animal N°", sideheading));
		cell.setVerticalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(dco.getAnimalNumbers().size());
		hstable.addCell(cell);
		Set<Integer> nos = dco.getAnimalNumbers();
		for (Integer no : nos) {
			cell = new PdfPCell(new Phrase(no + "", sideheading));
			cell.setVerticalAlignment(Element.ALIGN_LEFT);
			hstable.addCell(cell);
		}
		document.add(hstable);
	}

	private void exportSensoryReactivityOfIndividualDataToPdf(List<SensoryReactivityOfIndividual> dco,
			HttpServletRequest request, HttpServletResponse response, String file)
			throws IOException, DocumentException, ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		SimpleDateFormat sdftime = new SimpleDateFormat(environment.getRequiredProperty("dateTimeSecondsFormat"));
		SimpleDateFormat time = new SimpleDateFormat(environment.getRequiredProperty("timeSecondsFormat"));
		String studyNo = null;

		String dateinSting = sdf.format(new Date());
		Document document = new Document();

		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();
		document.setPageSize(PageSize.A4);
		for (SensoryReactivityOfIndividual each : dco) {
			document.newPage();
			includeLogo(request, document);

			com.itextpdf.text.Font regular = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10);
			com.itextpdf.text.Font heading = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 12,
					com.itextpdf.text.Font.BOLD);
			com.itextpdf.text.Font sideheading = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10,
					com.itextpdf.text.Font.BOLD);
			PdfPCell cell = null;
			PdfPTable table = null;
			table = new PdfPTable(1);
			table.setWidthPercentage(95f);
			int count = 1;
			Paragraph pa = new Paragraph("VIMTA LABS LTD", heading);
			pa.setAlignment(Element.ALIGN_CENTER);
			cell = new PdfPCell(pa);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setFixedHeight(30f);
//	         cell.setColspan(3);
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
			document.add(table);

			table = new PdfPTable(1);
			table.setWidthPercentage(95f);
			pa = new Paragraph("Sensory Reactivity of Individual (Treatment)", heading);
			pa.setAlignment(Element.ALIGN_CENTER);
			cell = new PdfPCell(pa);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setFixedHeight(30f);
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
			document.add(table);

			animalSensoryReactivityOfIndividualHeadderPart(each, document);

			document.add(generateSensoryReactivityOfIndividualPeramValues(each.getData(), sideheading, regular,
					each.getAnimalNumbers().size()));

			table = new PdfPTable(1);
			table.setWidthPercentage(95);
			cell = new PdfPCell(new Phrase("End of the Report ", regular));
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
			document.add(table);
		}

		document.close();

	}

	private void animalSensoryReactivityOfIndividualHeadderPart(SensoryReactivityOfIndividual dco, Document document)
			throws DocumentException {
		com.itextpdf.text.Font regular = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10);
		com.itextpdf.text.Font sideheading = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10,
				com.itextpdf.text.Font.BOLD);
		PdfPTable hstable = null;
		PdfPCell cell = null;
		hstable = new PdfPTable(3);
		hstable.setWidthPercentage(95);
		cell = new PdfPCell(new Phrase("Week N°: " + dco.getStudy().getStudyNo(), regular));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_LEFT);
		hstable.addCell(cell);

		cell = new PdfPCell(new Phrase("Gender : " + dco.getStudy().getGender(), regular));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_LEFT);
		hstable.addCell(cell);

		cell = new PdfPCell(new Phrase("Group :" + dco.getGroupNumber() + " & Dose : G1(0 mg/kg b.w.t) : ", regular));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_LEFT);
		hstable.addCell(cell);
		document.add(hstable);

		
	}

	private PdfPTable generateSensoryReactivityOfIndividualPeramValues(
			Map<Integer, SensoryReactivityOfIndividualData> each, com.itextpdf.text.Font sideheading,
			com.itextpdf.text.Font regular, int size) {
		PdfPTable hstable = new PdfPTable(7);
		hstable.setWidthPercentage(95);
		PdfPCell cell = new PdfPCell(new Phrase("Animla N°", sideheading));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setRowspan(2);
		hstable.addCell(cell);
		cell = new PdfPCell(new Phrase("Parameters", sideheading));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(6);
		hstable.addCell(cell);
		cell = new PdfPCell(new Phrase("Approach Response", sideheading));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		hstable.addCell(cell);
		cell = new PdfPCell(new Phrase("Touch Response", sideheading));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		hstable.addCell(cell);
		cell = new PdfPCell(new Phrase("Click Response", sideheading));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		hstable.addCell(cell);
		cell = new PdfPCell(new Phrase("Tail Pinch Response", sideheading));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		hstable.addCell(cell);
		cell = new PdfPCell(new Phrase("Pupil Response", sideheading));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		hstable.addCell(cell);
		cell = new PdfPCell(new Phrase("Air Righting Reflex", sideheading));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		hstable.addCell(cell);

		for (Map.Entry<Integer, SensoryReactivityOfIndividualData> mp : each.entrySet()) {
			cell = new PdfPCell(new Phrase(mp.getKey() + "", regular));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hstable.addCell(cell);
			cell = new PdfPCell(new Phrase(mp.getValue().getApproachResponse() + "", regular));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hstable.addCell(cell);
			cell = new PdfPCell(new Phrase(mp.getValue().getTouchResponse() + "", regular));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hstable.addCell(cell);
			cell = new PdfPCell(new Phrase(mp.getValue().getClickResponse() + "", regular));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hstable.addCell(cell);
			cell = new PdfPCell(new Phrase(mp.getValue().getTailPinchResponse() + "", regular));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hstable.addCell(cell);
			cell = new PdfPCell(new Phrase(mp.getValue().getPupilResponse() + "", regular));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hstable.addCell(cell);
			cell = new PdfPCell(new Phrase(mp.getValue().getAirRightingReflex() + "", regular));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hstable.addCell(cell);

		}
		return hstable;
	}
	
	private void exportMotorActivityDataOfIndividualAnimallDataToPdf(MotorActivityDataOfIndividualAnimal dco,
			HttpServletRequest request, HttpServletResponse response, String file) 
			throws IOException, DocumentException, ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		SimpleDateFormat sdftime = new SimpleDateFormat(environment.getRequiredProperty("dateTimeSecondsFormat"));
		SimpleDateFormat time = new SimpleDateFormat(environment.getRequiredProperty("timeSecondsFormat"));
		String studyNo = null;

		String dateinSting = sdf.format(new Date());
		Document document = new Document();

		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();
		document.setPageSize(PageSize.A4);
		Map<Integer, MotorActivityDataOfIndividualAnimalData> data = dco.getData();
		
			document.newPage();
			includeLogo(request, document);

			com.itextpdf.text.Font regular = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10);
			com.itextpdf.text.Font heading = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 12,
					com.itextpdf.text.Font.BOLD);
			com.itextpdf.text.Font sideheading = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10,
					com.itextpdf.text.Font.BOLD);
			PdfPCell cell = null;
			PdfPTable table = null;
			table = new PdfPTable(1);
			table.setWidthPercentage(95f);
			int count = 1;
			Paragraph pa = new Paragraph("VIMTA LABS LTD", heading);
			pa.setAlignment(Element.ALIGN_CENTER);
			cell = new PdfPCell(pa);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setFixedHeight(30f);
//	         cell.setColspan(3);
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
			document.add(table);

			table = new PdfPTable(1);
			table.setWidthPercentage(95f);
			pa = new Paragraph("Motor Activity Data of Individual Animal (Treatment)", heading);
			pa.setAlignment(Element.ALIGN_CENTER);
			cell = new PdfPCell(pa);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setFixedHeight(30f);
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
			document.add(table);

			animalMotorActivityDataOfIndividualAnimalHeadderPart(dco, document);

			document.add(generateMotorActivityDataOfIndividualAnimalPeramValues(dco.getData(), sideheading, regular));

			table = new PdfPTable(1);
			table.setWidthPercentage(95);
			cell = new PdfPCell(new Phrase("End of the Report ", regular));
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
			document.add(table);
		

		document.close();

	}
	
	
	private void animalMotorActivityDataOfIndividualAnimalHeadderPart(MotorActivityDataOfIndividualAnimal dco, Document document)
			throws DocumentException {
		com.itextpdf.text.Font regular = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10);
		com.itextpdf.text.Font sideheading = new com.itextpdf.text.Font(FontFamily.TIMES_ROMAN, 10,
				com.itextpdf.text.Font.BOLD);
		PdfPTable hstable = null;
		PdfPCell cell = null;
		hstable = new PdfPTable(3);
		hstable.setWidthPercentage(95);
		cell = new PdfPCell(new Phrase("Week N°: " + dco.getStudy().getStudyNo(), regular));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_LEFT);
		hstable.addCell(cell);

		cell = new PdfPCell(new Phrase("Gender : " + dco.getStudy().getGender(), regular));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_LEFT);
		hstable.addCell(cell);

		cell = new PdfPCell(new Phrase("Group :" + dco.getGroupNumber() + " & Dose : G1(0 mg/kg b.w.t) : ", regular));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setVerticalAlignment(Element.ALIGN_LEFT);
		hstable.addCell(cell);
		document.add(hstable);

		
	}
	
	
	private PdfPTable generateMotorActivityDataOfIndividualAnimalPeramValues(
			Map<Integer, MotorActivityDataOfIndividualAnimalData> each, com.itextpdf.text.Font sideheading,
			com.itextpdf.text.Font regular) {
		PdfPTable hstable = new PdfPTable(5);
		hstable.setWidthPercentage(95);
		PdfPCell cell = new PdfPCell(new Phrase("Animla N°", sideheading));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		hstable.addCell(cell);
		cell = new PdfPCell(new Phrase("Gender", sideheading));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		hstable.addCell(cell);
		cell = new PdfPCell(new Phrase("Total", sideheading));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		hstable.addCell(cell);
		cell = new PdfPCell(new Phrase("Ambulatory", sideheading));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		hstable.addCell(cell);
		cell = new PdfPCell(new Phrase("Stereotypic", sideheading));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		hstable.addCell(cell);
		hstable.setHeaderRows(1);
		for (Map.Entry<Integer, MotorActivityDataOfIndividualAnimalData> mp : each.entrySet()) {
			cell = new PdfPCell(new Phrase(mp.getValue().getAnimalNo() + "", regular));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hstable.addCell(cell);
			cell = new PdfPCell(new Phrase(mp.getValue().getGender() + "", regular));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hstable.addCell(cell);
			cell = new PdfPCell(new Phrase(mp.getValue().getTotal() + "", regular));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hstable.addCell(cell);
			cell = new PdfPCell(new Phrase(mp.getValue().getAmbulatory() + "", regular));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hstable.addCell(cell);
			cell = new PdfPCell(new Phrase(mp.getValue().getStereotypic() + "", regular));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			hstable.addCell(cell);
		}
		return hstable;
	}
}
