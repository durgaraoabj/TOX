package com.covide.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
public class SysmaxDto implements Serializable{
	
	private String animalSerialId;
	private Long studyId;
	private String studyName;
	private String weightStatus;
	private String ip_address;
	private String mac_address;
	private String user_name;
	private String type;
	
	private String wbc;
	private String rcb;
	private String hgb;
	private String hct;
	
	private String createdBy;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createdOn;
	
	public String getAnimalSerialId() {
		return animalSerialId;
	}
	public void setAnimalSerialId(String animalSerialId) {
		this.animalSerialId = animalSerialId;
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
	
	
	public String getWbc() {
		return wbc;
	}
	public void setWbc(String wbc) {
		this.wbc = wbc;
	}
	public String getRcb() {
		return rcb;
	}
	public void setRcb(String rcb) {
		this.rcb = rcb;
	}
	public String getHgb() {
		return hgb;
	}
	public void setHgb(String hgb) {
		this.hgb = hgb;
	}
	public String getHct() {
		return hct;
	}
	public void setHct(String hct) {
		this.hct = hct;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	

}
