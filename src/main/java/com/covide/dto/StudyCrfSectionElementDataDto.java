package com.covide.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class StudyCrfSectionElementDataDto implements Serializable{
	private static final long serialVersionUID = 4442755179149969544L;
	private Long studyAccessionCrfSectionElementDataId;
	private Long studyId;
	private String studyNumber = "";
	private String tempararayAnimalNumber = "";
	private String parmenentAnimalNumber = "";
	private Long activityId;
	private String activityName = "";
	private CrfSectionElementDto element;
	private String value = "";
	private String entryType = "";
	private String createdBy = "";
	private String createdOn = "";
	private List<CrfSectionElementDataAuditDto> audit;
	
	//Observation Info
	private String group;
	private String subjGroup;
	private boolean containsSubGroups;
	
	
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getSubjGroup() {
		return subjGroup;
	}
	public void setSubjGroup(String subjGroup) {
		this.subjGroup = subjGroup;
	}
	public boolean isContainsSubGroups() {
		return containsSubGroups;
	}
	public void setContainsSubGroups(boolean containsSubGroups) {
		this.containsSubGroups = containsSubGroups;
	}
	public Long getStudyAccessionCrfSectionElementDataId() {
		return studyAccessionCrfSectionElementDataId;
	}
	public void setStudyAccessionCrfSectionElementDataId(Long studyAccessionCrfSectionElementDataId) {
		this.studyAccessionCrfSectionElementDataId = studyAccessionCrfSectionElementDataId;
	}
	public Long getStudyId() {
		return studyId;
	}
	public void setStudyId(Long studyId) {
		this.studyId = studyId;
	}
	public String getStudyNumber() {
		return studyNumber;
	}
	public void setStudyNumber(String studyNumber) {
		this.studyNumber = studyNumber;
	}
	public String getTempararayAnimalNumber() {
		return tempararayAnimalNumber;
	}
	public void setTempararayAnimalNumber(String tempararayAnimalNumber) {
		this.tempararayAnimalNumber = tempararayAnimalNumber;
	}
	public String getParmenentAnimalNumber() {
		return parmenentAnimalNumber;
	}
	public void setParmenentAnimalNumber(String parmenentAnimalNumber) {
		this.parmenentAnimalNumber = parmenentAnimalNumber;
	}
	public Long getActivityId() {
		return activityId;
	}
	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public CrfSectionElementDto getElement() {
		return element;
	}
	public void setElement(CrfSectionElementDto element) {
		this.element = element;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getEntryType() {
		return entryType;
	}
	public void setEntryType(String entryType) {
		this.entryType = entryType;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public List<CrfSectionElementDataAuditDto> getAudit() {
		return audit;
	}
	public void setAudit(List<CrfSectionElementDataAuditDto> audit) {
		this.audit = audit;
	}
	
	
}
