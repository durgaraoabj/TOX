package com.springmvc.dao;

import java.util.List;

import com.covide.template.dto.TemplateAuditTrailDto;
import com.covide.template.dto.TemplateCommentsDto;
import com.covide.template.dto.TemplateFilesDto;
import com.springmvc.model.StudyLevelObservationTemplateData;
import com.springmvc.model.StudyLevelObservationTemplateDataLog;
import com.springmvc.model.TemplateFileAuditTrail;
import com.springmvc.model.TemplateFileAuditTrailLog;

public interface ObservationTemplateDao {

	
	TemplateFilesDto getTemplateFilesRecords(Long studyId, Long groupId, Long subGroupId, Long subgroupAnimalId,
			Long templateId);

	List<TemplateFileAuditTrail> getDraftTemplateAuditRecord(List<String> positions,
			StudyLevelObservationTemplateData slotdata);

	boolean saveTemplateAuditTrailLogRecords(List<TemplateFileAuditTrailLog> tfatLogList);

	boolean updateTemplateAuditTrailRecords(List<TemplateFileAuditTrail> updatList);

	int saveTemplateAuditTrailRecordList(List<TemplateFileAuditTrail> datList);

	long saveTemplateLogRecord(StudyLevelObservationTemplateDataLog slotdLog);

	boolean updateTemplateRecord(StudyLevelObservationTemplateData slotdata);

	long saveStudyLevelObservationTemplateDataRecord(StudyLevelObservationTemplateData slotdata);

	TemplateFilesDto getTemplateFileDetails(Long studyId, Long groupId, Long subGroupId, Long subGroupanimalId,
			Long templateId);

	boolean completeReviewForTemplate(Long templateId, String statusStr, String comments, String userName);

	TemplateAuditTrailDto getTemplateAuditTrailData(Long templateId);

	TemplateCommentsDto getTemplateCommentsData(Long templateId);

}
