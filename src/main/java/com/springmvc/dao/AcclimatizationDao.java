package com.springmvc.dao;

import java.util.Date;
import java.util.List;

import com.covide.crf.dto.Crf;
import com.springmvc.model.AmendmentDetails;
import com.springmvc.model.StdSubGroupObservationCrfs;
import com.springmvc.model.StudyAcclamatizationData;
import com.springmvc.model.StudyAcclamatizationDates;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyCageAnimal;
import com.springmvc.model.StudySubGroupAnimal;
import com.springmvc.model.StudyTreatmentDataDates;

public interface AcclimatizationDao {

	List<StudyAcclamatizationData> getStudyAcclamatizationDataList(Long studyId);

	List<Crf> getCrfRecordsList(List<Long> crfIds);

	List<StudyAcclamatizationData> getStudyAcclamatizationDataRecords(List<Long> unchkIds, Long studyId);

	boolean saveStudyAcclamatizationData(List<StudyAcclamatizationData> saveList,
			List<StudyAcclamatizationData> updateList, Long studyId);

	List<StudyAnimal> allStudyAnimals(Long studyId);

	List<StudyAnimal> allUnCagedAnimals(Long studyId);

	List<StudyCageAnimal> allCagedAnimals(Long studyId);

	List<StudySubGroupAnimal> studySubGroupAnimal(Long studyId);

	StudyAcclamatizationData studyAcclamatizationData(Long crfId, Long studyId);

	StudyAcclamatizationDates studyAcclamatizationDates(Long crfId, Long studyId, Date date, Long userId, Boolean genderBase, String gender);

	StudyAcclamatizationData getStudyAcclamatizationDataList(Long id, Long id2);

	void saveStudyAcclamatizationData(StudyAcclamatizationData sad, AmendmentDetails ad);

	void updateStudyAcclamatizationData(StudyAcclamatizationData sad);

	boolean removeStudyAcclamatizationDatesDetails(Long studyAcclamatizationDatesId, Long userId );

	boolean updateStudyAcclamatizationDatesDetails(Long studyAcclamatizationDatesId, String value, Long userId);

	List<StudyAcclamatizationDates> studyAcclamatizationDates(Long crfId, Long studyId);

	StdSubGroupObservationCrfs getStudyTreatmentDataList(Long crfId, Long studyId, Long subGroupId);

	StdSubGroupObservationCrfs studyTreatmentData(Long crfId, Long studyId, Long subGroupId);

	List<StudyTreatmentDataDates> studyTreatmentDataDates(Long crfId, Long studyId, Long subGroupId);

	StudyTreatmentDataDates studyTreatmentDataDates(Long crfId, Long studyId, Date date, Long userId, Long subGroupId, Boolean genderBase, String gender);

	StudyAcclamatizationDates studyAcclamatizationDatesById(Long studyAcclamatizationDatesId);


	StudyTreatmentDataDates studyTreatmentDataDatesById(Long observationDatesId);

	List<StudyAcclamatizationData> studyAcclamatizationData(Long studyId);
}
