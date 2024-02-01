package com.springmvc.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.covide.crf.dto.AccessionAnimalDataviewDto;
import com.covide.dto.AccessionDataDto;
import com.covide.dto.AccessionDataEntryDto;
import com.covide.dto.StudyAccessionCrfSectionElementDataUpdateDto;
import com.covide.dto.StudyCrfSectionElementDataDto;
import com.springmvc.model.StdSubGroupObservationCrfs;
import com.springmvc.model.StudyAccessionAnimals;
import com.springmvc.model.StudyAcclamatizationData;
import com.springmvc.model.StudyAcclamatizationDates;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.SubjectDataEntryDetails;

public interface AccessionService {

	Long getMaxRecordNo(Long studyId);

	List<StudyAccessionAnimals> getStudyAccessionAnimalsList(Long studyId);

	List<StudyAcclamatizationData> getStudyAcclamatizationDataRecordsList(Long studyId, Long userRoleId);

	AccessionDataEntryDto getAccessionDataEntryDtoDetails(Long studyId, Long crfId);

	AccessionDataDto getAnimalId(StudyAcclamatizationData sad, AccessionDataEntryDto adDto, StudyMaster sm);

	Map<String, String> getAccessionAnimalsList(Long studyId);

	String saveStudyAccessionCrfDataEntryDetails(Long animalId, Long crfId, Long studyId, Long userId,
			String discrebencyFields, String deviationMessage, HttpServletRequest request, String type, int noOfEntry,
			String selecteDate, String gender, Long studyAcclamatizationDateId);

	AccessionAnimalDataviewDto getAccessionAnimalDataDtoDetails(Long crfId, Long studyId);

	String saveAnimalAccession(Long studyId, Long userId, String[] animalId, List<StudyAccessionAnimals> animals);

	Map<String, List<StudyAnimal>> studyAnimals(Long studyId);

	StudyAnimal nextAnimalForAccessionData(Long studyAcclamatizationDateId, Long studyAcclamatizationDataId, Long studyId, Long crfId, String gender,
			int noOfEntry, String selecteDate);

	StudyAcclamatizationData studyAcclamatizationData(Long crf, Long sm);

	List<StudyAnimal> allStudyAnimals(Long studyId);

	void saveAnimalCageing(Long studyId);

	Long noOfAvailableStudyAnimalsCount(Long studyId);

	StudyCrfSectionElementDataDto accessionActivityUpdateView(Long eleId, String activityType);

	String accessionActivityElementUpdate(StudyAccessionCrfSectionElementDataUpdateDto updateEleData, Long userId,
			String activityType);

	StudyCrfSectionElementDataDto observationActivityUpdateView(Long eleId, String activityType);

	Map<String, List<StudyAcclamatizationDates>> expressionAcclamatizationDataCalender(
			List<StudyAcclamatizationData> list, StudyMaster sm);

	List<StdSubGroupObservationCrfs> stdSubGroupObservationCrfs(Long id, Long userRoleId);

	File exportAccessionData(HttpServletRequest request, AccessionAnimalDataviewDto aadDto) throws Exception;

	StudyAcclamatizationDates studyAcclamatizationDates(Long studyAcclamatizationDateId);


}
