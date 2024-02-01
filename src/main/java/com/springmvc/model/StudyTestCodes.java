package com.springmvc.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.mysql.jdbc.log.Log;

@Entity
@Table(name="STUDY_TEST_CODES")
public class StudyTestCodes extends CommonMaster implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7013316168702384736L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@ManyToOne
	@JoinColumn(name="studyId")
	private StudyMaster study;
	@ManyToOne
	@JoinColumn(name="observationInturmentConfigurationId")
	private ObservationInturmentConfiguration observationInturmentConfiguration;
//	@ManyToOne
//	@JoinColumn(name="insturmentIpAddressId")
//	private InstrumentIpAddress insturmentIpAddress;
	@ManyToOne
	@JoinColumn(name="testCodeId")
	private TestCode testCode;
	private int orderNo;
	@ManyToOne
	@JoinColumn(name="instrumentId")
	private InstrumentIpAddress instrument;
	private boolean activeStatus = true;
	
	
//	public InstrumentIpAddress getInsturmentIpAddress() {
//		return insturmentIpAddress;
//	}
//	public void setInsturmentIpAddress(InstrumentIpAddress insturmentIpAddress) {
//		this.insturmentIpAddress = insturmentIpAddress;
//	}
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public StudyMaster getStudy() {
		return study;
	}
	public void setStudy(StudyMaster study) {
		this.study = study;
	}
	public TestCode getTestCode() {
		return testCode;
	}
	public void setTestCode(TestCode testCode) {
		this.testCode = testCode;
	}
	public InstrumentIpAddress getInstrument() {
		return instrument;
	}
	public void setInstrument(InstrumentIpAddress instrument) {
		this.instrument = instrument;
	}
	public boolean isActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(boolean activeStatus) {
		this.activeStatus = activeStatus;
	}
	public ObservationInturmentConfiguration getObservationInturmentConfiguration() {
		return observationInturmentConfiguration;
	}
	public void setObservationInturmentConfiguration(ObservationInturmentConfiguration observationInturmentConfiguration) {
		this.observationInturmentConfiguration = observationInturmentConfiguration;
	}
	
}
