package com.covide.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
public class StagoDto implements Serializable{
	
	private String animalSerialId;
	private String weight;
	private Long studyId;
	private String studyName;
	private String weightStatus;
	private String ip_address;
	private String mac_address;
	private String user_name;
	private String ptVal;
	private String apttVal;
	
	private String createdBy;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createdOn;
	
	public String getAnimalSerialId() {
		return animalSerialId;
	}
	public void setAnimalSerialId(String animalSerialId) {
		this.animalSerialId = animalSerialId;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public Long getStudyId() {
		return studyId;
	}
	public void setStudyId(Long studyId) {
		this.studyId = studyId;
	}
	public String getStudyName() {
		return studyName;
	}
	public void setStudyName(String studyName) {
		this.studyName = studyName;
	}
	public String getWeightStatus() {
		return weightStatus;
	}
	public void setWeightStatus(String weightStatus) {
		this.weightStatus = weightStatus;
	}
	public String getIp_address() {
		return ip_address;
	}
	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}
	public String getMac_address() {
		return mac_address;
	}
	public void setMac_address(String mac_address) {
		this.mac_address = mac_address;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	
	public String getPtVal() {
		return ptVal;
	}
	public void setPtVal(String ptVal) {
		this.ptVal = ptVal;
	}
	public String getApttVal() {
		return apttVal;
	}
	public void setApttVal(String apttVal) {
		this.apttVal = apttVal;
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
	

}
