package com.springmvc.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="TESTCODE_PROFILE_LOG")
public class TestCodeProfileLog extends CommonMaster  implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="TestCodeProfileLog_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name="testCodeProfileId")
	private TestCodeProfile testCodeProfile;
	private String profileName;
	@ManyToOne
	@JoinColumn(name="instrumentId")
	private InstrumentIpAddress insturment;
	private boolean activeStatus = true;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public TestCodeProfile getTestCodeProfile() {
		return testCodeProfile;
	}
	public void setTestCodeProfile(TestCodeProfile testCodeProfile) {
		this.testCodeProfile = testCodeProfile;
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

}
