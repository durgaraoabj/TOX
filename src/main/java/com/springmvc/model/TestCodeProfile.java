package com.springmvc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="TESTCODE_PROFILE")
public class TestCodeProfile extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="TestCodeProfile_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	private String profileName;
	@ManyToOne
	@JoinColumn(name="instrumentId")
	private InstrumentIpAddress insturment;

	private boolean activeStatus = true;


	@Transient
	List<TestCodeProfileParameters> parameters = new ArrayList<>();


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
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


	public boolean isActiveStatus() {
		return activeStatus;
	}


	public void setActiveStatus(boolean activeStatus) {
		this.activeStatus = activeStatus;
	}


	public List<TestCodeProfileParameters> getParameters() {
		return parameters;
	}


	public void setParameters(List<TestCodeProfileParameters> parameters) {
		this.parameters = parameters;
	}
	
	
	
}
