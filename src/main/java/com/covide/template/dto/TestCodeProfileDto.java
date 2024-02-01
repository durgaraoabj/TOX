package com.covide.template.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.springmvc.model.InstrumentIpAddress;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.TestCodeProfileParameters;

public class TestCodeProfileDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3376668946009281037L;
	private Long profileId;
	private String profileName;
	private InstrumentIpAddress insturment;
	private String createdBy;
	private String createdOn;
	private boolean activeStatus = true;
	private List<TestCodeProfileParametersDto> parameters = new ArrayList<>();
	public TestCodeProfileDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TestCodeProfileDto(Long profileId, String profileName, InstrumentIpAddress insturment, String createdBy,
			String createdOn, boolean activeStatus) {
		super();
		this.profileId = profileId;
		this.profileName = profileName;
		this.insturment = insturment;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.activeStatus = activeStatus;
	}
	
	public List<TestCodeProfileParametersDto> getParameters() {
		return parameters;
	}
	public void setParameters(List<TestCodeProfileParametersDto> parameters) {
		this.parameters = parameters;
	}
	public Long getProfileId() {
		return profileId;
	}
	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}
	public String getProfileName() {
		return profileName;
	}
	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}
	public InstrumentIpAddress getInsturment() {
		return insturment;
	}
	public void setInsturment(InstrumentIpAddress insturment) {
		this.insturment = insturment;
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
	public boolean isActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(boolean activeStatus) {
		this.activeStatus = activeStatus;
	}
	
	
}
