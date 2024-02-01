package com.covide.template.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.springmvc.model.StudyLevelObservationTemplateData;
import com.springmvc.model.StudyLevelObservationTemplateDataLog;

@SuppressWarnings("serial")
public class TemplateCommentsDto implements Serializable {
	
	private String comments; 
	private String createdBy;
	private Date createdOn;
	private String sheetName;
	private StudyLevelObservationTemplateData slotdata;
	private List<StudyLevelObservationTemplateDataLog> slotLogList;
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public StudyLevelObservationTemplateData getSlotdata() {
		return slotdata;
	}
	public void setSlotdata(StudyLevelObservationTemplateData slotdata) {
		this.slotdata = slotdata;
	}
	public List<StudyLevelObservationTemplateDataLog> getSlotLogList() {
		return slotLogList;
	}
	public void setSlotLogList(List<StudyLevelObservationTemplateDataLog> slotLogList) {
		this.slotLogList = slotLogList;
	}
	
	
	
	

}
