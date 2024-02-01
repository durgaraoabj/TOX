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
import javax.persistence.Transient;

@Entity
@Table(name="STAGO_DATA_LOG")
public class StagoDataLog extends CommonMaster implements Serializable{
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="StagoDataLog_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name="stoaGoDataId")
	private StagoData stoaGoData;
	@ManyToOne
	@JoinColumn(name="studyId")
	private StudyMaster study;
	@ManyToOne
	@JoinColumn(name="studyAnimalId")
	private StudyAnimal studyAnimal;
	private String studyNumber;
	private String sampleType = "Animal";
	private String lotNo = "";
	private String testName;
	
	@Transient
	private String stdId;
	@ManyToOne
	@JoinColumn(name="testCodeId")
	private StudyTestCodes testCode;
	private String animalNo;
	private String data;
	private String testResult;
	private String instrument;
	private String tar;
	private String duriation;
	private String afterDuriation;
	private String noOfAnimals;
	@ManyToOne
	@JoinColumn(name="activityDoneBy")
	private LoginUsers activityDoneBy;
	private Date receivedTime;
	private boolean selectedResult = true;
	private LoginUsers selectedBy;
	private Date selectedOn;
	private LoginUsers deSelectedBy;
	private Date deSelectedOn;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public StagoData getStoaGoData() {
		return stoaGoData;
	}
	public void setStoaGoData(StagoData stoaGoData) {
		this.stoaGoData = stoaGoData;
	}
	public StudyMaster getStudy() {
		return study;
	}
	public void setStudy(StudyMaster study) {
		this.study = study;
	}
	public StudyAnimal getStudyAnimal() {
		return studyAnimal;
	}
	public void setStudyAnimal(StudyAnimal studyAnimal) {
		this.studyAnimal = studyAnimal;
	}
	public String getStudyNumber() {
		return studyNumber;
	}
	public void setStudyNumber(String studyNumber) {
		this.studyNumber = studyNumber;
	}
	public String getSampleType() {
		return sampleType;
	}
	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}
	public String getLotNo() {
		return lotNo;
	}
	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
	public String getTestName() {
		return testName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	public String getStdId() {
		return stdId;
	}
	public void setStdId(String stdId) {
		this.stdId = stdId;
	}
	public StudyTestCodes getTestCode() {
		return testCode;
	}
	public void setTestCode(StudyTestCodes testCode) {
		this.testCode = testCode;
	}
	public String getAnimalNo() {
		return animalNo;
	}
	public void setAnimalNo(String animalNo) {
		this.animalNo = animalNo;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getTestResult() {
		return testResult;
	}
	public void setTestResult(String testResult) {
		this.testResult = testResult;
	}
	public String getInstrument() {
		return instrument;
	}
	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}
	public String getTar() {
		return tar;
	}
	public void setTar(String tar) {
		this.tar = tar;
	}
	public String getDuriation() {
		return duriation;
	}
	public void setDuriation(String duriation) {
		this.duriation = duriation;
	}
	public String getAfterDuriation() {
		return afterDuriation;
	}
	public void setAfterDuriation(String afterDuriation) {
		this.afterDuriation = afterDuriation;
	}
	public String getNoOfAnimals() {
		return noOfAnimals;
	}
	public void setNoOfAnimals(String noOfAnimals) {
		this.noOfAnimals = noOfAnimals;
	}
	public LoginUsers getActivityDoneBy() {
		return activityDoneBy;
	}
	public void setActivityDoneBy(LoginUsers activityDoneBy) {
		this.activityDoneBy = activityDoneBy;
	}
	public Date getReceivedTime() {
		return receivedTime;
	}
	public void setReceivedTime(Date receivedTime) {
		this.receivedTime = receivedTime;
	}
	public boolean isSelectedResult() {
		return selectedResult;
	}
	public void setSelectedResult(boolean selectedResult) {
		this.selectedResult = selectedResult;
	}
	public LoginUsers getSelectedBy() {
		return selectedBy;
	}
	public void setSelectedBy(LoginUsers selectedBy) {
		this.selectedBy = selectedBy;
	}
	public Date getSelectedOn() {
		return selectedOn;
	}
	public void setSelectedOn(Date selectedOn) {
		this.selectedOn = selectedOn;
	}
	public LoginUsers getDeSelectedBy() {
		return deSelectedBy;
	}
	public void setDeSelectedBy(LoginUsers deSelectedBy) {
		this.deSelectedBy = deSelectedBy;
	}
	public Date getDeSelectedOn() {
		return deSelectedOn;
	}
	public void setDeSelectedOn(Date deSelectedOn) {
		this.deSelectedOn = deSelectedOn;
	}
	
	
	
	
}
