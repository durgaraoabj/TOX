package com.covide.template.dto;

import java.io.Serializable;
import java.util.List;

import com.springmvc.model.StudyLevelObservationTemplateData;
import com.springmvc.model.TemplateFileAuditTrail;
import com.springmvc.model.TemplateFileAuditTrailLog;

@SuppressWarnings("serial")
public class TemplateAuditTrailDto implements Serializable {
	
	List<TemplateFileAuditTrail> tempAuditList = null;
	StudyLevelObservationTemplateData fileDetails = null;
	List<TemplateFileAuditTrailLog> tempAuditListLog = null;
	public List<TemplateFileAuditTrail> getTempAuditList() {
		return tempAuditList;
	}
	public void setTempAuditList(List<TemplateFileAuditTrail> tempAuditList) {
		this.tempAuditList = tempAuditList;
	}
	public StudyLevelObservationTemplateData getFileDetails() {
		return fileDetails;
	}
	public void setFileDetails(StudyLevelObservationTemplateData fileDetails) {
		this.fileDetails = fileDetails;
	}
	public List<TemplateFileAuditTrailLog> getTempAuditListLog() {
		return tempAuditListLog;
	}
	public void setTempAuditListLog(List<TemplateFileAuditTrailLog> tempAuditListLog) {
		this.tempAuditListLog = tempAuditListLog;
	}
	
	

}
