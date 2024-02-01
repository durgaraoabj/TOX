package com.springmvc.dao;

import java.util.List;

import com.covide.crf.dto.AccessionAnimalDataviewDto;
import com.covide.crf.dto.ObservationAnimalDataviewDto;
import com.covide.dto.SatusAndWorkFlowDetailsDto;
import com.covide.dto.StudyDesignReviewDto;
import com.springmvc.model.ApplicationAuditDetails;
import com.springmvc.model.StatusMaster;
import com.springmvc.model.StudyAcclamatizationData;
import com.springmvc.model.StudyDesignStatus;
import com.springmvc.model.StudyMaster;

public interface ReviewDao {

	StudyDesignReviewDto getStudyDesignReviewDto(Long studyId);

	SatusAndWorkFlowDetailsDto getSatusAndWorkFlowDetailsDtoDetails(Long studyId, String fromStatus, String toStatus);

	boolean updateStudyDesignStatusDetails(StudyDesignStatus sds, ApplicationAuditDetails aad, StudyMaster sm);

	StatusMaster getStatusMasterRecord(String string);

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

}
