package com.springmvc.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.covide.crf.dto.Crf;
import com.springmvc.model.StdSubGroupObservationCrfs;
import com.springmvc.model.StudyAcclamatizationData;
import com.springmvc.model.StudyAcclamatizationDates;
import com.springmvc.model.StudyCageAnimal;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudySubGroupAnimal;
import com.springmvc.model.StudyTreatmentDataDates;

public interface AcclimatizationService {

	Map<Long, StudyAcclamatizationData> getStudyAcclamatizationDataList(Long studyId);

//	String saveAcclimatizationData(Long studyId, List<Long> crfIds, List<String> typeList, String daysDataStr, String windowPeriod,
//			List<Long> unchkIds, String userName);

	Map<Long, String> allAnimals(Long studyId);

	String saveAnimalCageing(String cageNameOrNo, List<Long> animalIds, Long studyId, Long userId);

	Map<Long, String> allUnCagedAnimals(Long studyId);

	Map<String, List<StudyCageAnimal>> cagedAnimals(Long studyId);

	Map<Long, List<StudySubGroupAnimal>> studySubGroupAnimal(Long studyId);

	String saveAcclimatizationData(Long studyId, List<Long> crfIds, List<Long> crfIds2, Map<Long, String> typeVals,
			Map<Long, String> daysOrWeeksVals, Map<Long, String> windowPeriod, Map<Long, Integer> windowMap,
			String userName);

	StudyAcclamatizationData studyAcclamatizationData(Long crfId, Long studyId);

	StudyAcclamatizationDates studyAcclamatizationDates(Long crfId, Long studyId, Date parse, Long userId, Boolean genderBase, String gender );

	boolean changeAcclimatizationConfig(Crf crf, StudyMaster study, String status, String userName);

	boolean changeAcclimatizationConfigType(Long crfId, Long studyId, String value, String userName, String field);

	boolean removeStudyAcclamatizationDatesDetails(Long studyAcclamatizationDatesId, Long userId );

	boolean updateStudyAcclamatizationDatesDetails(Long studyAcclamatizationDatesId, String value, Long userId);

	List<StudyAcclamatizationDates> studyAcclamatizationDates(Long crfId, Long studyId);

	StdSubGroupObservationCrfs studyTreatmentData(Long crfId, Long studyId, Long subGroupId);

	List<StudyTreatmentDataDates> studyTreatmentnDates(Long crfId, Long studyId, Long subGroupId);

	StudyTreatmentDataDates studyTreatmentDataDates(Long crfId, Long studyId, Date parse, Long userId, Long subGroupId, Boolean genderBase, String gender);

	StudyAcclamatizationDates studyAcclamatizationDatesWithId(Long studyAcclamatizationDatesId);

	List<StudyAcclamatizationData> studyAcclamatizationData(Long studyId);



	
}
