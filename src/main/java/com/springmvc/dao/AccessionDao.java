package com.springmvc.dao;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.covide.crf.dto.AccessionAnimalDataviewDto;
import com.covide.crf.dto.Crf;
import com.covide.crf.dto.CrfRule;
import com.covide.crf.dto.CrfSectionElementData;
import com.covide.crf.dto.CrfSectionElementDataAudit;
import com.covide.dto.AccessionDataEntryDto;
import com.springmvc.model.AccessionAnimalsDataEntryDetails;
import com.springmvc.model.AnimalCage;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.RadomizationReviewDto;
import com.springmvc.model.StatusMaster;
import com.springmvc.model.StudyAccessionAnimals;
import com.springmvc.model.StudyAccessionCrfDescrpency;
import com.springmvc.model.StudyAccessionCrfSectionElementData;
import com.springmvc.model.StudyAcclamatizationData;
import com.springmvc.model.StudyAcclamatizationDates;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyCageAnimal;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.SubGroupInfo;

public interface AccessionDao {

	Long getMaxRecordNo(Long studyId);

	String saveStudyAccessionAnimalsData(List<StudyAccessionAnimals> saveList);

	List<StudyAccessionAnimals> getStudyAccessionAnimalsList(Long studyId);

	List<StudyAcclamatizationData> getStudyAcclamatizationDataRecordsList(Long studyId, Long userRoleId);

	AccessionDataEntryDto getAccessionDataEntryDtoDetails(Long studyId, Long crfId);

	StudyAccessionCrfDescrpency studyAccessCrfDescrpencySec(AccessionAnimalsDataEntryDetails animalData, Crf crf,
			Object object, StudyAccessionCrfSectionElementData data);

	boolean saveStudyAccessionAnimalsFormDetails(AccessionAnimalsDataEntryDetails accaded,
			List<StudyAccessionCrfSectionElementData> sectionData, List<StudyAccessionCrfSectionElementData> secList,
			Crf crf, LoginUsers user, boolean b, List<Long> secIdList, Long studyId);

	List<CrfRule> crfRuleOfSubElements(Crf crf, List<Long> sectionIdsList);

	AccessionAnimalDataviewDto getAccessionAnimalDataviewDtoDetails(Long crfId, Long studyId);

	String saveStudyAccessionAnimals(List<StudyAccessionAnimals> animalsmeataData, List<StudyAnimal> animals);

	List<StudyAnimal> studyAnimalsGenderBase(Long studyId, String string);

	StudyAnimal nextAnimalForAccessionData(Long studyAcclamatizationDateId, Long studyAcclamatizationDataId, Long studyId, Long crfId, String gender, int noOfEntry, String selecteDate);

	StudyAcclamatizationData studyAcclamatizationData(Long crf, Long sm);

	List<StudyAnimal> allStudyAnimals(Long studyId);

	StudyAnimal studyAnimal(Long animalId);

	List<Long> studyActiveAnimalIds(StatusMaster activeStatus, Long studyId);

	List<AccessionAnimalsDataEntryDetails> approvedAccessionAnimalsDataEntryDetails(Crf crf, List<Long> studyAnimalIds,
			StatusMaster approveStatus);

	List<AccessionAnimalsDataEntryDetails> accessionAnimalsDataEntryDetails(Crf crf, List<Long> studyAnimalIds,
			StatusMaster approveStatus);

	List<AccessionAnimalsDataEntryDetails> accessionAnimalsDataEntryDetails(Crf crf, List<Long> studyAnimalIds,
			StatusMaster approveStatus, StatusMaster sentToReview);

	StudyAccessionCrfSectionElementData weightStudyAccessionCrfSectionElementData(AccessionAnimalsDataEntryDetails each,
			String fieldValue, String fieldValue2);

	List<AnimalCage> allAnimalCage(int i);

	void studyCageAnimals(List<StudyCageAnimal> cageAnimals);

	int studyCageList(Long studyId);

	List<StudyAnimal> studyAnimalsOf(List<Long> animalIds);

	AnimalCage animalCage(String cageNameOrNo);

	void studyCageAnimals(List<StudyCageAnimal> cageAnimals, List<StudyAnimal> allAnimals);

	RadomizationReviewDto radomizationReviewDto(Long studyId);

	RadomizationReviewDto radomizationReviewDto(Long studyId, Long userId, String gender);

	Long noOfAvailableStudyAnimalsCount(Long studyId);

	StudyAccessionCrfSectionElementData studyAccessionCrfSectionElementWithId(Long eleId);

	List<CrfSectionElementDataAudit> crfSectionElementDataAudit(Long id, String activityType);

	String accessionActivityElementUpdate(StudyAccessionCrfSectionElementData element, CrfSectionElementData obsercationElement,
			CrfSectionElementDataAudit saudit);

	CrfSectionElementData crfSectionElementWithId(Long eleId);

	StudyAcclamatizationDates studyAcclamatizationDatesOfCurrentDate(StudyMaster study, Long id, Date fromDate, String gender)throws ParseException ;

	Map<Integer, SubGroupInfo>  studyAllSubgroups(StudyMaster study);

	StudyAcclamatizationDates studyAcclamatizationDates(Long studyAcclamatizationDatesId);


}
