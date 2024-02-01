package com.covide.template.dto;

import java.io.Serializable;

import com.springmvc.model.ObserVationTemplates;
import com.springmvc.model.StudyLevelObservationTemplateData;
import com.springmvc.model.SubGroupAnimalsInfoAll;

@SuppressWarnings("serial")
public class TemplateFilesDto implements Serializable {
	
	private ObserVationTemplates obvt;
	private StudyLevelObservationTemplateData slotdata;
	private String file;
	private Long studyId;
	private Long groupId;
	private Long subGroupId;
	private Long subgroupAnimalId;
	private SubGroupAnimalsInfoAll sgaia;
	private String fileStatus;
	
	public ObserVationTemplates getObvt() {
		return obvt;
	}
	public void setObvt(ObserVationTemplates obvt) {
		this.obvt = obvt;
	}
	public StudyLevelObservationTemplateData getSlotdata() {
		return slotdata;
	}
	public void setSlotdata(StudyLevelObservationTemplateData slotdata) {
		this.slotdata = slotdata;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public Long getSubGroupId() {
		return subGroupId;
	}
	public void setSubGroupId(Long subGroupId) {
		this.subGroupId = subGroupId;
	}
	public Long getSubgroupAnimalId() {
		return subgroupAnimalId;
	}
	public void setSubgroupAnimalId(Long subgroupAnimalId) {
		this.subgroupAnimalId = subgroupAnimalId;
	}
	public Long getStudyId() {
		return studyId;
	}
	public void setStudyId(Long studyId) {
		this.studyId = studyId;
	}
	public SubGroupAnimalsInfoAll getSgaia() {
		return sgaia;
	}
	public void setSgaia(SubGroupAnimalsInfoAll sgaia) {
		this.sgaia = sgaia;
	}
	public String getFileStatus() {
		return fileStatus;
	}
	public void setFileStatus(String fileStatus) {
		this.fileStatus = fileStatus;
	}
	
	
	

}
