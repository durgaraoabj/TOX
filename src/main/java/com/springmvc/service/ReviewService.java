package com.springmvc.service;

import java.util.List;

import com.covide.crf.dto.AccessionAnimalDataviewDto;
import com.covide.crf.dto.ObservationAnimalDataviewDto;
import com.covide.dto.StudyDesignReviewDto;
import com.springmvc.model.StudyAcclamatizationData;

public interface ReviewService {

	StudyDesignReviewDto getStudyDesignReviewDto(Long studyId);

	String obserVationReviewProcessSaving(String username, Long studyId, String type, String comments);

	AccessionAnimalDataviewDto getAccessionAnimalDataDtoDetails(Long studyId, Long crfId, Long userId, String dateString, String gender, Long studyAcclamatizationDatesId);
	

	List<StudyAcclamatizationData> getStudyAcclamatizationDataRecordsList(Long studyId);

	String reviewSelectedDataEntryFroms(List<Long> checkedIds, Long userId, String  reviewType, String reviewComment);

	String saveRandamizationData(Long redamizationId, Long userId, String reviewType, String reviewComment);

	ObservationAnimalDataviewDto observatoinAnimalDataDtoDetails(Long studyId, Long studyTreatmentDataDatesId, Long stdSubGroupObservationCrfsId,
			Long userId, boolean export, String gender, String schduleType);

	String reviewSelectedObservationDataEntryFroms(List<Long> checkedIds, Long userId, String reviewType,
			String reviewComment);

	AccessionAnimalDataviewDto getAccessionAnimalDataDtoDetailsView(Long studyAcclamatizationDataId);

	boolean saveRandamizationReview(Long studyId, Long userId, String reviewType, String reviewComment);

	String studyReviewProcessSaving(String username, Long studyId, String reviewType, String reviewComment);

}
