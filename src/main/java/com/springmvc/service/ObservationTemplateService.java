package com.springmvc.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.covide.template.dto.TemplateAuditTrailDto;
import com.covide.template.dto.TemplateCommentsDto;
import com.covide.template.dto.TemplateFilesDto;

public interface ObservationTemplateService {

	TemplateFilesDto getTempleteForView(Long studyId, Long groupIdVal, Long subGroupIdVal, Long subGroupanimalId, Long templateId,
			HttpServletRequest request);

	String saveOrupdateTemplateFile(long studyId, long groupId, long subGroupId, long subGroupanimalId, long templateId,
			String modifidDoc, String username, HttpServletRequest request);

	TemplateFilesDto getTemplateFileDetails(Long studyId, Long groupId, Long subGroupId, Long subGroupanimalId,
			Long templateId);

	boolean completeReviewForTemplate(Long templateId, String statusStr, String comments, String username);

	TemplateAuditTrailDto getTemplateAuditTrailData(Long templateId);

	List<TemplateCommentsDto> getTemplateCommentsData(Long templateId, String type);
	


}
