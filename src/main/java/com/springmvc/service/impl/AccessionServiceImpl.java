package com.springmvc.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
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

import com.covide.crf.dao.CrfDAO;
import com.covide.crf.dto.AccessionAnimalDataviewDto;
import com.covide.crf.dto.Crf;
import com.covide.crf.dto.CrfSection;
import com.covide.crf.dto.CrfSectionElement;
import com.covide.crf.dto.CrfSectionElementData;
import com.covide.crf.dto.CrfSectionElementDataAudit;
import com.covide.crf.dto.CrfSectionElementInstrumentValue;
import com.covide.dto.AccessionDataDto;
import com.covide.dto.AccessionDataEntryDto;
import com.covide.dto.CrfSectionElementDataAuditDto;
import com.covide.dto.CrfSectionElementDto;
import com.covide.dto.CrfSectionElementInstrumentValueDto;
import com.covide.dto.StudyAccessionCrfSectionElementDataUpdateDto;
import com.covide.dto.StudyCrfSectionElementDataDto;
import com.covide.enums.StatusMasterCodes;
import com.springmvc.dao.AccessionDao;
import com.springmvc.dao.StatusDao;
import com.springmvc.dao.StudyDao;
import com.springmvc.dao.UserDao;
import com.springmvc.dao.impl.ClinicalCodesDaoImple;
import com.springmvc.model.AccessionAnimalDataDto;
import com.springmvc.model.AccessionAnimalsDataEntryDetails;
import com.springmvc.model.AnimalCage;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.StatusMaster;
import com.springmvc.model.StdSubGroupObservationCrfs;
import com.springmvc.model.StudyAccessionAnimals;
import com.springmvc.model.StudyAccessionCrfSectionElementData;
import com.springmvc.model.StudyAcclamatizationData;
import com.springmvc.model.StudyAcclamatizationDates;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyCageAnimal;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.SubjectDataEntryDetails;
import com.springmvc.service.AccessionService;
import com.springmvc.service.ExpermentalDesignService;

@Service("accessionService")
@PropertySource(value = { "classpath:application.properties" })
public class AccessionServiceImpl implements AccessionService {
	@Autowired
	private Environment environment;
	@Autowired
	AccessionDao accessionDao;
	@Autowired
	StatusDao statusDao;
	@Autowired
	StudyDao studyDao;
	@Autowired
	UserDao userDao;
	@Autowired
	CrfDAO crfDao;
	@Autowired
	private ExpermentalDesignService expermentalDesignService;
	@Autowired
	ClinicalCodesDaoImple clinicalCodeDao;
	@Override
	public Long getMaxRecordNo(Long studyId) {
		return accessionDao.getMaxRecordNo(studyId);
	}

	@Override
	public List<StudyAccessionAnimals> getStudyAccessionAnimalsList(Long studyId) {
		return accessionDao.getStudyAccessionAnimalsList(studyId);
	}

	@Override
	public List<StudyAcclamatizationData> getStudyAcclamatizationDataRecordsList(Long studyId, Long userRoleId) {
		return accessionDao.getStudyAcclamatizationDataRecordsList(studyId, userRoleId);
	}

	@Override
	public List<StdSubGroupObservationCrfs> stdSubGroupObservationCrfs(Long studyId, Long userRoleId) {
		return expermentalDesignService.stdSubGroupObservationCrfs(studyId, userRoleId);
	}

	@Override
	public AccessionDataEntryDto getAccessionDataEntryDtoDetails(Long studyId, Long crfId) {
		return accessionDao.getAccessionDataEntryDtoDetails(studyId, crfId);
	}

	@Override
	public Map<String, String> getAccessionAnimalsList(Long studyId) {
		Map<String, String> animalMap = new HashMap<>();
		List<StudyAccessionAnimals> saaList = null;
		try {
			saaList = accessionDao.getStudyAccessionAnimalsList(studyId);
			if (saaList != null && saaList.size() > 0) {
				for (StudyAccessionAnimals saa : saaList) {
					String key = saa.getId() + "";
					int start = Integer.parseInt(saa.getAnimalsFrom());
					int end = Integer.parseInt(saa.getAnimalsTo());
					String gender = saa.getGender();
					String prefix = saa.getPrefix();
					for (int i = start; i <= end; i++) {
						if (i <= 9) {
							key = key + "@@" + i;
							String animal = "";
							if (gender.equals("Male"))
								animal = prefix + "-000" + i + "M";
							else
								animal = prefix + "-000" + i + "F";
							animalMap.put(key, animal);
						} else if (i > 9 && i <= 99) {
							key = key + "@@" + i;
							String animal = "";
							if (gender.equals("Male"))
								animal = prefix + "-00" + i + "M";
							else
								animal = prefix + "-00" + i + "F";
							animalMap.put(key, animal);
						} else if (i > 99 && i <= 999) {
							key = key + "@@" + i;
							String animal = "";
							if (gender.equals("Male"))
								animal = prefix + "-0" + i + "M";
							else
								animal = prefix + "-0" + i + "F";
							animalMap.put(key, animal);
						} else {
							key = key + "@@" + i;
							String animal = "";
							if (gender.equals("Male"))
								animal = prefix + "-" + i + "M";
							else
								animal = prefix + "-" + i + "F";
							animalMap.put(key, animal);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return animalMap;
	}

	@Override
	public String saveStudyAccessionCrfDataEntryDetails(Long animalId, Long crfId, Long studyId, Long userId,
			String discrebencyFields, String deviationMessage, HttpServletRequest request, String type, int noOfEntry,
			String selecteDate, String gender, Long studyAcclamatizationDateId) {
		String status = "Failed";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
			LoginUsers user = userDao.findById(userId);
			StudyAnimal animal = accessionDao.studyAnimal(animalId);
			StatusMaster dataEntered = statusDao.statusMaster(StatusMasterCodes.DATAENTRY.toString());
			StatusMaster senttoReview = statusDao.statusMaster(StatusMasterCodes.SENDTORIVEW.toString());
//			StudyAcclamatizationData studyAcclamatizationData = accessionDao(studyId, crfId);

			StudyMaster study = studyDao.findByStudyId(studyId);
			Set<String> discrebencyFieldsSet = new HashSet<>();
			if (!discrebencyFields.trim().equals("")) {
				String[] ss = discrebencyFields.split(",");
				for (String s : ss)
					discrebencyFieldsSet.add(s);
			}
			List<StudyAccessionCrfSectionElementData> secList = new ArrayList<>();

			List<Long> secIdList = new ArrayList<>();
//			AccessionDataEntryDto acdto = accessionDao.getAccessionDataEntryDtoDetails(studyId, crfId);
			Crf crf = crfDao.crfForView(crfId);
			List<StudyAccessionAnimals> saaList = null;
			StudyAcclamatizationData studyAcclamatizationData = accessionDao.studyAcclamatizationData(crfId, studyId);
			StudyAcclamatizationDates studyAcclamatizationDates = accessionDao.studyAcclamatizationDates(studyAcclamatizationDateId);
			if (type.equals("scheduled")) {
//				studyAcclamatizationDates = accessionDao.studyAcclamatizationDatesOfCurrentDate(study,
//						studyAcclamatizationData.getId(), sdf.parse(selecteDate), gender);
				studyAcclamatizationDates = accessionDao.studyAcclamatizationDates(studyAcclamatizationDateId);
			}
			AccessionAnimalsDataEntryDetails accaded = new AccessionAnimalsDataEntryDetails();
			accaded.setStudyAcclamatizationData(studyAcclamatizationData);
			accaded.setStudyAcclamatizationDates(studyAcclamatizationDates);
			accaded.setNoOfEntry(noOfEntry);
			accaded.setAnimal(animal);
			if(animal != null) {
				accaded.setGender(animal.getGender());
				accaded.setAnimalTempId(animal.getAnimalNo());
			}
			accaded.setCreatedBy(user.getUsername());
			accaded.setCreatedOn(new Date());
			accaded.setCrf(crf);
			accaded.setDeviation(deviationMessage);
			accaded.setEntryType(type);
			accaded.setStudy(study);
			accaded.setStatus(dataEntered);
			List<StudyAccessionCrfSectionElementData> sectionData = new ArrayList<>();
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
						StudyAccessionCrfSectionElementData data = new StudyAccessionCrfSectionElementData();
						data.setAnimal(animal);
						data.setKayName(id);
						data.setValue(values);
						data.setStudyId(studyId);
						data.setCrf(crf);
						data.setElement(ele);
						data.setCreatedBy(user.getUsername());
						data.setCreatedOn(new Date());
						data.setEntryType(type);
						if(ele.getClinicalCode() != null && !ele.getClinicalCode().equals("") && !ele.getClinicalCode().equals("Animal Number")) {
							data.setClinicalCodes(clinicalCodeDao.getByKey(Long.parseLong(data.getValue())));
						}
						sectionData.add(data);
						if (discrebencyFieldsSet.contains(id)) {
							secList.add(data);
							secIdList.add(ele.getId());
						}
					}
				}
			}

			boolean flag = accessionDao.saveStudyAccessionAnimalsFormDetails(accaded, sectionData, secList, crf, user,
					true, secIdList, studyId);
			if (flag)
				status = "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public AccessionAnimalDataviewDto getAccessionAnimalDataDtoDetails(Long crfId, Long studyId) {
		return accessionDao.getAccessionAnimalDataviewDtoDetails(crfId, studyId);
	}

	@Override
	public String saveAnimalAccession(Long studyId, Long userId, String[] animalId,
			List<StudyAccessionAnimals> animalsmeataData) {
		// TODO Auto-generated method stub
		StudyMaster sm = studyDao.getStudyMasterRecord(studyId);
		LoginUsers user = userDao.findById(userId);

		List<StudyAnimal> animals = new ArrayList<>();
		StatusMaster activeStatus = statusDao.statusMaster(StatusMasterCodes.ACTIVE.toString());
		animalsmeataData.forEach((access) -> {
			access.setNoOfAnimals(
					(Integer.parseInt(access.getAnimalsTo()) - Integer.parseInt(access.getAnimalsFrom()) + 1) + "");
			access.setStudy(sm);
			access.setCreatedBy(user.getUsername());
			int seqNo = Integer.parseInt(access.getAnimalsFrom());
			int to = Integer.parseInt(access.getAnimalsTo());
			while (seqNo <= to) {
				StudyAnimal animal = new StudyAnimal();
				animal.setSequnceNo(seqNo);
				if (seqNo < 10)
					animal.setAnimalNo(access.getPrefix() + "000" + seqNo);
				else if (seqNo < 100)
					animal.setAnimalNo(access.getPrefix() + "00" + seqNo);
				else if (seqNo < 1000)
					animal.setAnimalNo(access.getPrefix() + "0" + seqNo);
				else
					animal.setAnimalNo(access.getPrefix() + seqNo);
				animal.setGender(access.getGender());
				if(access.getGender().equals("Male"))
					animal.setGenderCode("M");	
				else
					animal.setGenderCode("F");
				animal.setStudy(sm);
				animal.setCreatedBy(user.getUsername());
				animal.setAnimalStatus(activeStatus);
				animals.add(animal);
				seqNo ++;
			}
		});
		return accessionDao.saveStudyAccessionAnimals(animalsmeataData, animals);
	}

	@Override
	public Map<String, List<StudyAnimal>> studyAnimals(Long studyId) {
		Map<String, List<StudyAnimal>> map = new HashMap<>();
		map.put("Male", accessionDao.studyAnimalsGenderBase(studyId, "Male"));
		map.put("Female", accessionDao.studyAnimalsGenderBase(studyId, "Female"));
		return map;
	}

	@Override
	public StudyAnimal nextAnimalForAccessionData(Long studyAcclamatizationDateId, Long studyAcclamatizationDataId,
			Long studyId, Long crfId, String gender, int noOfEntry, String selecteDate) {
		return accessionDao.nextAnimalForAccessionData(studyAcclamatizationDateId, studyAcclamatizationDataId, studyId,
				crfId, gender, noOfEntry, selecteDate);
	}

	@Override
	public StudyAcclamatizationData studyAcclamatizationData(Long crf, Long sm) {
		// TODO Auto-generated method stub
		return accessionDao.studyAcclamatizationData(crf, sm);
	}

	@Override
	public AccessionDataDto getAnimalId(StudyAcclamatizationData sad, AccessionDataEntryDto adDto, StudyMaster sm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StudyAnimal> allStudyAnimals(Long studyId) {
		// TODO Auto-generated method stub
		return accessionDao.allStudyAnimals(studyId);
	}

	@Override
	public void saveAnimalCageing(Long studyId) {
		// TODO Auto-generated method stub
		int size = accessionDao.studyCageList(studyId);
		if (size == 0) {
			List<StudyAnimal> allAnimals = accessionDao.allStudyAnimals(studyId);
			List<AnimalCage> cageList = accessionDao.allAnimalCage((allAnimals.size() / 20) + 1);
			List<StudyCageAnimal> cageAnimals = new ArrayList<>();
			int index = 0;
			int count = 0;
			for (StudyAnimal ani : allAnimals) {
				if (index % 20 == 0 && index != 0) {
					count++;
				}
				AnimalCage cage = cageList.get(count);
				StudyCageAnimal sca = new StudyCageAnimal();
				sca.setAnimal(ani);
				sca.setCage(cage);
				sca.setStudy(ani.getStudy());
				cageAnimals.add(sca);
				index++;
			}
			accessionDao.studyCageAnimals(cageAnimals);
		}

	}

	@Override
	public Long noOfAvailableStudyAnimalsCount(Long studyId) {
		// TODO Auto-generated method stub
		return accessionDao.noOfAvailableStudyAnimalsCount(studyId);
	}

	@Override
	public StudyCrfSectionElementDataDto accessionActivityUpdateView(Long eleId, String activityType) {
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat(environment.getRequiredProperty("dateTimeFormat"));
		// TODO Auto-generated method stub
		StudyCrfSectionElementDataDto dto = new StudyCrfSectionElementDataDto();
		StudyAccessionCrfSectionElementData studyAccessionCrfSectionElementData = accessionDao
				.studyAccessionCrfSectionElementWithId(eleId);
		dto.setStudyAccessionCrfSectionElementDataId(studyAccessionCrfSectionElementData.getId());
		dto.setStudyId(studyAccessionCrfSectionElementData.getAnimal().getStudy().getId());
		dto.setStudyNumber(studyAccessionCrfSectionElementData.getAnimal().getStudy().getStudyNo());
		dto.setTempararayAnimalNumber(studyAccessionCrfSectionElementData.getAnimal().getAnimalNo());
		dto.setParmenentAnimalNumber(studyAccessionCrfSectionElementData.getAnimal().getPermanentNo());
		dto.setActivityId(studyAccessionCrfSectionElementData.getCrf().getId());
		dto.setActivityName(studyAccessionCrfSectionElementData.getCrf().getObservationName());
		dto.setValue(studyAccessionCrfSectionElementData.getValue());
		dto.setEntryType(studyAccessionCrfSectionElementData.getEntryType());
		dto.setCreatedBy(studyAccessionCrfSectionElementData.getCreatedBy());
		dto.setCreatedOn(dateTimeFormat.format(studyAccessionCrfSectionElementData.getCreatedOn()));
		CrfSectionElementDto element = new CrfSectionElementDto();
		element.setCrfSectionElementId(studyAccessionCrfSectionElementData.getElement().getId());
		element.setName(studyAccessionCrfSectionElementData.getElement().getName());
		element.setLeftDesc(studyAccessionCrfSectionElementData.getElement().getLeftDesc());
		element.setRigtDesc(studyAccessionCrfSectionElementData.getElement().getRigtDesc());
		element.setMiddeDesc(studyAccessionCrfSectionElementData.getElement().getMiddeDesc());
		element.setTotalDesc(studyAccessionCrfSectionElementData.getElement().getTotalDesc());
		element.setTopDesc(studyAccessionCrfSectionElementData.getElement().getTopDesc());
		element.setBottemDesc(studyAccessionCrfSectionElementData.getElement().getBottemDesc());
		element.setType(studyAccessionCrfSectionElementData.getElement().getType());
		element.setDataType(studyAccessionCrfSectionElementData.getElement().getDataType());
		element.setResponceType(studyAccessionCrfSectionElementData.getElement().getResponceType());
		element.setValues(studyAccessionCrfSectionElementData.getElement().getValues());
		element.setRequired(studyAccessionCrfSectionElementData.getElement().isRequired());
		element.setElementValues(crfDao.crfSectionElementValue(studyAccessionCrfSectionElementData.getElement()));
		element.setFormula(studyAccessionCrfSectionElementData.getElement().getFormula());

		List<CrfSectionElementInstrumentValue> instrumentData = crfDao.crfSectionElementInstrumentValues(
				studyAccessionCrfSectionElementData.getAnimal().getId(),
				studyAccessionCrfSectionElementData.getElement().getId());
		if (instrumentData.size() > 0) {
			element.setType("instrumentData");
			List<CrfSectionElementInstrumentValueDto> instumentDtaDto = new ArrayList<>();
			instrumentData.forEach((ins) -> {
				CrfSectionElementInstrumentValueDto instrument = new CrfSectionElementInstrumentValueDto();
				instrument.setId(ins.getId());
				instrument.setWeight(ins.getWeight());
				instrument.setUserName(ins.getCreatedBy());
				instrument.setIpAddress(ins.getIpAddress());
				instrument.setMacAddress(ins.getMacAddress());
				instrument.setCreationDate(dateTimeFormat.format(ins.getCreatedOn()));
				instumentDtaDto.add(instrument);
			});
			element.setInstumentDat(instumentDtaDto);
		} else
			element.setType(studyAccessionCrfSectionElementData.getElement().getType());

		dto.setElement(element);
		List<CrfSectionElementDataAuditDto> auditDtos = new ArrayList<>();
		List<CrfSectionElementDataAudit> audits = accessionDao
				.crfSectionElementDataAudit(studyAccessionCrfSectionElementData.getId(), activityType);
		audits.stream().forEach((audit) -> {
			CrfSectionElementDataAuditDto auditDto = new CrfSectionElementDataAuditDto();
			auditDto.setId(audit.getId());
			auditDto.setElementValue(audit.getNewValue());
			auditDto.setElementOldValue(audit.getOldValue());
			auditDto.setLoginUsersId(audit.getLoginUsers().getId());
			auditDto.setLoginUserName(audit.getLoginUsers().getUsername());
			if (audit.getCreatedOn() != null)
				auditDto.setCreatedOn(dateTimeFormat.format(audit.getCreatedOn()));
			auditDto.setUserComment(audit.getUserComment());
			auditDto.setDataEntryType(audit.getDataEntryType());
			if (dto.getValue().equals(auditDto.getElementValue()))
				auditDto.setAllowToSelect(false);
			auditDtos.add(auditDto);
		});
		dto.setAudit(auditDtos);
		return dto;
	}

	@Override
	public StudyCrfSectionElementDataDto observationActivityUpdateView(Long eleId, String activityType) {
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat(environment.getRequiredProperty("dateTimeFormat"));
		// TODO Auto-generated method stub
		StudyCrfSectionElementDataDto dto = new StudyCrfSectionElementDataDto();
//		StudyAccessionCrfSectionElementDatanElementData studyAccessionCrfSectionElementDatad = accessionDao
//				.studyAccessionCrfSectionElementWithId(eleId);
		CrfSectionElementData studyAccessionCrfSectionElementData = accessionDao.crfSectionElementWithId(eleId);
		dto.setStudyAccessionCrfSectionElementDataId(studyAccessionCrfSectionElementData.getId());
		dto.setStudyId(studyAccessionCrfSectionElementData.getAnimal().getStudy().getId());
		dto.setStudyNumber(studyAccessionCrfSectionElementData.getAnimal().getStudy().getStudyNo());
		dto.setTempararayAnimalNumber(studyAccessionCrfSectionElementData.getAnimal().getAnimalNo());
		dto.setParmenentAnimalNumber(studyAccessionCrfSectionElementData.getAnimal().getPermanentNo());
		dto.setActivityId(studyAccessionCrfSectionElementData.getCrf().getId());
		dto.setActivityName(studyAccessionCrfSectionElementData.getCrf().getObservationName());
		dto.setValue(studyAccessionCrfSectionElementData.getValue());
		dto.setEntryType(studyAccessionCrfSectionElementData.getEntryType());
		dto.setCreatedBy(studyAccessionCrfSectionElementData.getCreatedBy());
		dto.setCreatedOn(dateTimeFormat.format(studyAccessionCrfSectionElementData.getCreatedOn()));

		dto.setGroup(studyAccessionCrfSectionElementData.getSubjectDataEntryDetails().getGroup().getGroupName());
		dto.setSubjGroup(studyAccessionCrfSectionElementData.getSubjectDataEntryDetails().getSubGroup().getName());
		dto.setContainsSubGroups(
				studyAccessionCrfSectionElementData.getSubjectDataEntryDetails().getStudy().isRequiredSubGroup());

		CrfSectionElementDto element = new CrfSectionElementDto();
		element.setCrfSectionElementId(studyAccessionCrfSectionElementData.getElement().getId());
		element.setName(studyAccessionCrfSectionElementData.getElement().getName());
		element.setLeftDesc(studyAccessionCrfSectionElementData.getElement().getLeftDesc());
		element.setRigtDesc(studyAccessionCrfSectionElementData.getElement().getRigtDesc());
		element.setMiddeDesc(studyAccessionCrfSectionElementData.getElement().getMiddeDesc());
		element.setTotalDesc(studyAccessionCrfSectionElementData.getElement().getTotalDesc());
		element.setTopDesc(studyAccessionCrfSectionElementData.getElement().getTopDesc());
		element.setBottemDesc(studyAccessionCrfSectionElementData.getElement().getBottemDesc());
		element.setType(studyAccessionCrfSectionElementData.getElement().getType());
		element.setDataType(studyAccessionCrfSectionElementData.getElement().getDataType());
		element.setResponceType(studyAccessionCrfSectionElementData.getElement().getResponceType());
		element.setValues(studyAccessionCrfSectionElementData.getElement().getValues());
		element.setRequired(studyAccessionCrfSectionElementData.getElement().isRequired());
		element.setElementValues(crfDao.crfSectionElementValue(studyAccessionCrfSectionElementData.getElement()));
		element.setFormula(studyAccessionCrfSectionElementData.getElement().getFormula());
		dto.setElement(element);
		List<CrfSectionElementDataAuditDto> auditDtos = new ArrayList<>();
		List<CrfSectionElementDataAudit> audits = accessionDao
				.crfSectionElementDataAudit(studyAccessionCrfSectionElementData.getId(), activityType);
		audits.stream().forEach((audit) -> {
			CrfSectionElementDataAuditDto auditDto = new CrfSectionElementDataAuditDto();
			auditDto.setId(audit.getId());
			auditDto.setElementValue(audit.getNewValue());
			auditDto.setElementOldValue(audit.getOldValue());
			auditDto.setLoginUsersId(audit.getLoginUsers().getId());
			auditDto.setLoginUserName(audit.getLoginUsers().getUsername());
			if (audit.getCreatedOn() != null)
				auditDto.setCreatedOn(dateTimeFormat.format(audit.getCreatedOn()));
			auditDto.setUserComment(audit.getUserComment());
			auditDto.setDataEntryType(audit.getDataEntryType());
			if (dto.getValue().equals(auditDto.getElementValue()))
				auditDto.setAllowToSelect(false);
			auditDtos.add(auditDto);
		});
		dto.setAudit(auditDtos);
		return dto;
	}

	@Override
	public String accessionActivityElementUpdate(StudyAccessionCrfSectionElementDataUpdateDto updateEleData,
			Long userId, String activityType) {
		LoginUsers user = userDao.findById(userId);
		StudyAccessionCrfSectionElementData element = null;
		CrfSectionElementData obsercationElement = null;
		if (activityType.equals("accession")) {
			element = accessionDao.studyAccessionCrfSectionElementWithId(updateEleData.getEleId());
		} else if (activityType.equals("observation")) {
			obsercationElement = accessionDao.crfSectionElementWithId(updateEleData.getEleId());
		}
		CrfSectionElementDataAudit saudit = new CrfSectionElementDataAudit();
		if (activityType.equals("accession")) {
			saudit.setStudy(element.getAnimal().getStudy());
			saudit.setData(element);
			saudit.setAnimalObsercationElementData(obsercationElement);
			saudit.setElement(element.getElement());
			saudit.setKayName(element.getKayName());
			saudit.setOldValue(element.getValue());
		} else if (activityType.equals("observation")) {
			saudit.setStudy(obsercationElement.getAnimal().getStudy());
			saudit.setAnimalObsercationElementData(obsercationElement);
			saudit.setElement(obsercationElement.getElement());
			saudit.setKayName(obsercationElement.getKayName());
			saudit.setOldValue(obsercationElement.getValue());
		}

		if (updateEleData.getEleType().equals("text") || updateEleData.getEleType().equals("textArea")
				|| updateEleData.getEleType().equals("select") || updateEleData.getEleType().equals("selectTable")
				|| updateEleData.getEleType().equals("date") || updateEleData.getEleType().equals("dateAndTime")
				|| updateEleData.getEleType().equals("radio")) {
			saudit.setNewValue(updateEleData.getNewValue());
		} else {
			if (updateEleData.getNewValues() != null && updateEleData.getNewValues().size() > 0) {
				saudit.setNewValue(updateEleData.getNewValues().stream().collect(Collectors.joining(",")));
			} else {
				saudit.setNewValue(updateEleData.getNewValue());
			}
		}
		saudit.setLoginUsers(user);
		saudit.setCreatedOn(new Date());
		saudit.setUserComment(updateEleData.getComment());
		saudit.setDataEntryType("Update");
		if (activityType.equals("accession")) {
			element.setValue(saudit.getNewValue());
			element.setDataUpdate(true);
			element.setUpdatedBy(user.getUsername());
			element.setUpdatedOn(new Date());
		} else if (activityType.equals("observation")) {
			obsercationElement.setValue(saudit.getNewValue());
			obsercationElement.setDataUpdate(true);
			obsercationElement.setUpdateBy(user.getUsername());
			obsercationElement.setUpdateOn(new Date());
		}
		String value = accessionDao.accessionActivityElementUpdate(element, obsercationElement, saudit);
		if (activityType.equals("accession")) {
			if (value != null) {
				return "{\"Success\": \"" + true
						+ "\",\"Message\":\"Animal Accession Data Updated successfully\", \"vlaue\":\"" + value + "\"}";
			} else
				return "{\"Success\": \"" + false + "\",\"Message\":\"Failed to Update Animal Accession Data\"}";
		} else if (activityType.equals("observation")) {
			if (value != null) {
				return "{\"Success\": \"" + true
						+ "\",\"Message\":\"Animal Boservation Data Updated successfully\", \"vlaue\":\"" + value
						+ "\"}";
			} else
				return "{\"Success\": \"" + false + "\",\"Message\":\"Failed to Update Animal Boservation Data\"}";
		} else {
			return "{\"Success\": \"" + false + "\",\"Message\":\"Failed to Update Animal Data\"}";
		}

	}

//	Date wise Acclimatization activity observation's view based on data entry date and window   
	@Override
	public Map<String, List<StudyAcclamatizationDates>> expressionAcclamatizationDataCalender(
			List<StudyAcclamatizationData> list, StudyMaster sm) {
		Map<String, List<StudyAcclamatizationDates>> map = new HashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		for (StudyAcclamatizationData ob : list) {
			List<StudyAcclamatizationDates> acclamatizationDates = ob.getAcclamatizationDates();
			if (sm.getGender().equals("Male") || sm.getGender().equals("Female")
					|| (sm.getGender().equals("Both") && sm.isSplitStudyByGender())) {
				for (StudyAcclamatizationDates accdate : acclamatizationDates) {
					String date = sdf.format(accdate.getAccDate());
					accdate.setDateString(date);
					if (ob.getType().equalsIgnoreCase("day")) {
						List<StudyAcclamatizationDates> dateWise = map.get(date);
						if (dateWise == null)
							dateWise = new ArrayList<>();

						if (!sm.getGender().equals("Both")) {
							accdate.setDisplayGender(sm.getGender());
							dateWise.add(accdate);
							map.put(date, dateWise);
						} else {
							accdate.setDisplayGender("Male");
							dateWise.add(accdate);
							StudyAcclamatizationDates forFemale = new StudyAcclamatizationDates();
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
				Map<String, List<StudyAcclamatizationDates>> acclamatizationDatesGender = new HashMap<>();
				List<StudyAcclamatizationDates> acclamatizationDatesMale = new ArrayList<>();
				List<StudyAcclamatizationDates> acclamatizationDatesFemale = new ArrayList<>();
				for (StudyAcclamatizationDates accdate : acclamatizationDates) {
					if (accdate.getGender().equals("Male"))
						acclamatizationDatesMale.add(accdate);
					else
						acclamatizationDatesFemale.add(accdate);
				}
				acclamatizationDatesGender.put("Male", acclamatizationDatesMale);
				acclamatizationDatesGender.put("Female", acclamatizationDatesFemale);
				for (Map.Entry<String, List<StudyAcclamatizationDates>> eachGender : acclamatizationDatesGender
						.entrySet()) {
					for (StudyAcclamatizationDates accdate : eachGender.getValue()) {
						String date = sdf.format(accdate.getAccDate());
						accdate.setDateString(date);
						if (ob.getType().equalsIgnoreCase("day")) {
							List<StudyAcclamatizationDates> dateWise = map.get(date);
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
//	@Override
//	public Map<String, List<StudyAcclamatizationData>> expressionAcclamatizationDataCalender(
//			List<StudyAcclamatizationData> list, StudyMaster sm) {
//		Map<String, List<StudyAcclamatizationData>> map = new HashMap<>();
//		Calendar cal = Calendar.getInstance();
//		Date d = sm.getStartDate();
//		Date studyDate = sm.getStartDate();
//		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
//
//		for (StudyAcclamatizationData ob : list) {
//			List<Integer> ds = new ArrayList<>();
//			List<StudyAcclamatizationDates> acclamatizationDates = ob.getAcclamatizationDates();
//			acclamatizationDates.forEach((accdate) -> {
//				ds.add(accdate.getDayNo());
//			});
//
////			String[] ds = ob.getEntryValues().split("\\,");
//			List<String> listDates = new ArrayList<>();
//			for (Integer dcount : ds) {
//				dcount = dcount - 1;
//				if (ob.getType().equalsIgnoreCase("Day")) {
//					// CHECKING DATE LIKE 2-3 OR 2-8. IF ANY EXCEPTION WE WONT CONSIDER THIS DAYS
//					cal.setTime(d);
//					cal.add(Calendar.DAY_OF_MONTH, dcount);
//					listDates.add(sdf.format(cal.getTime()));
//				} else if (ob.getType().equalsIgnoreCase("Week")) {
//					List<String> tempDates = new ArrayList<>();
//					int days = dcount;
//					if (days > -1) {
//						if (days == 0)
//							days = 1;
//						for (int c = (days - 1) * 7; c < days * 7; c++) {
//							cal.setTime(studyDate);
//							cal.add(Calendar.DAY_OF_MONTH, c);
//							tempDates.add(sdf.format(cal.getTime()));
//						}
//					} else {
//						days = -(days);
//						int c = days * 7;
//						for (int j = 1; j <= 7; j++) {
//							System.out.println(c);
//							cal.setTime(studyDate);
//							cal.add(Calendar.DAY_OF_MONTH, -(c));
//							tempDates.add(sdf.format(cal.getTime()));
//							c--;
//							System.out.println(listDates);
//						}
//					}
//
//					if (tempDates.size() > 1) {
//						String datef = tempDates.get(0) + " to " + tempDates.get(tempDates.size() - 1);
//						listDates.add(datef);
//					} else
//						listDates.addAll(tempDates);
//				}
//
//			}
//			System.out.println(listDates);
//			for (String sd : listDates) {
//				List<StudyAcclamatizationData> listOfObj = map.get(sd);
//				if (listOfObj == null)
//					listOfObj = new ArrayList<>();
//				listOfObj.add(ob);
//				map.put(sd, listOfObj);
//			}
//		}
//		return map;
//	}

	@Override
	public File exportAccessionData(HttpServletRequest request, AccessionAnimalDataviewDto aadDto) throws Exception {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat(environment.getRequiredProperty("dateFormat"));
		String dateinSting = sdf.format(new Date());
		// Create a Workbook
		Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file
		/*
		 * CreationHelper helps us create instances of various things like DataFormat,
		 * Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way
		 */
		CreationHelper createHelper = workbook.getCreationHelper();
		// Create a Sheet
		Sheet sheet = workbook.createSheet("Accession_Data_" + dateinSting);
		// Create a Font for styling header cells
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		headerFont.setColor(IndexedColors.RED.getIndex());

		DataFormat format = workbook.createDataFormat();
		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		CellStyle numberCellStyle = workbook.createCellStyle();
		numberCellStyle.setDataFormat(format.getFormat(""));

		Cell cell = null;
		CellStyle dateCellStyle = workbook.createCellStyle();
		dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MMM-yyyy"));

		int rowCount = 0;
		Row headerRow = null;
		// Create a Row
		int cellNo = 0;
		headerRow = sheet.createRow(rowCount++);
		cell = headerRow.createCell(cellNo++);
		cell.setCellValue("Study");
		cell.setCellStyle(headerCellStyle);
		cell = headerRow.createCell(cellNo++);
		cell.setCellValue(aadDto.getStudy().getStudyNo());
		cell.setCellStyle(headerCellStyle);

		cellNo = 0;
		headerRow = sheet.createRow(rowCount++);
		cell = headerRow.createCell(cellNo++);
		cell.setCellValue("Date");
		cell.setCellStyle(headerCellStyle);
		cell = headerRow.createCell(cellNo++);
		cell.setCellValue(dateinSting);
		cell.setCellStyle(headerCellStyle);

		// Test perameter row
		cellNo = 0;
		headerRow = sheet.createRow(rowCount++);
		cell = headerRow.createCell(cellNo++);
		cell.setCellValue("Animal ID");
		cell = headerRow.createCell(cellNo++);
		cell.setCellValue("Status");
		cell = headerRow.createCell(cellNo++);
		cell.setCellValue("Review Status");
		cell = headerRow.createCell(cellNo++);
		cell.setCellValue("Entry Type");
		cell = headerRow.createCell(cellNo++);
		cell.setCellValue("Entered By");
		cell = headerRow.createCell(cellNo++);
		cell.setCellValue("Entered On");

		int cols = cellNo;
		if (aadDto.getElements() != null && aadDto.getElements().size() > 0) {
			cols = aadDto.getElements().size() + cellNo;
		}

		for (Map.Entry<Long, CrfSectionElement> m : aadDto.getElements().entrySet()) {
			cell = headerRow.createCell(cellNo++);
			if (m.getValue().getLeftDesc() != null && !m.getValue().getLeftDesc().equals(""))
				cell.setCellValue(m.getValue().getLeftDesc());
			else if (m.getValue().getRigtDesc() != null && !m.getValue().getRigtDesc().equals(""))
				cell.setCellValue(m.getValue().getRigtDesc());
			else if (m.getValue().getTotalDesc() != null && !m.getValue().getTotalDesc().equals(""))
				cell.setCellValue(m.getValue().getTotalDesc());
			else if (m.getValue().getBottemDesc() != null && !m.getValue().getBottemDesc().equals(""))
				cell.setCellValue(m.getValue().getBottemDesc());
			else if (m.getValue().getMiddeDesc() != null && !m.getValue().getMiddeDesc().equals(""))
				cell.setCellValue(m.getValue().getMiddeDesc());
			else if (m.getValue().getTotalDesc() != null && !m.getValue().getTotalDesc().equals(""))
				cell.setCellValue(m.getValue().getTotalDesc());
			// cell.setCellStyle(dateCellStyle);
		}

		// units row

		for (AccessionAnimalDataDto aadd : aadDto.getAnimalData()) {
			cellNo = 0;
			headerRow = sheet.createRow(rowCount++);
			cell = headerRow.createCell(cellNo++);
			cell.setCellValue(aadd.getAnimalId());
			cell = headerRow.createCell(cellNo++);
			cell.setCellValue(aadd.getStatus());
			cell = headerRow.createCell(cellNo++);
			if (aadd.isReviewed())
				cell.setCellValue("Done");
			else
				cell.setCellValue("Not Done");
			cell = headerRow.createCell(cellNo++);
			cell.setCellValue(aadd.getEntryType());
			cell = headerRow.createCell(cellNo++);
			cell.setCellValue(aadd.getUserName());
			cell = headerRow.createCell(cellNo++);
			cell.setCellValue(sdf.format(aadd.getDate()));
			Map<Long, StudyAccessionCrfSectionElementData> elementData = aadd.getElementData();
			for (Map.Entry<Long, StudyAccessionCrfSectionElementData> m : elementData.entrySet()) {
				cell = headerRow.createCell(cellNo++);
				cell.setCellValue(m.getValue().getValue());
			}
		}

		for (int j = 0; j < cols; j++) {
			sheet.autoSizeColumn(j);
		}

		String path = request.getSession().getServletContext().getRealPath("/") + "Accession Data_" + dateinSting;
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
	public StudyAcclamatizationDates studyAcclamatizationDates(Long studyAcclamatizationDateId) {
		// TODO Auto-generated method stub
		return accessionDao.studyAcclamatizationDates(studyAcclamatizationDateId);
	}

	
}
