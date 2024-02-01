package com.springmvc.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="SYSMEX_DATA")
public class SysmexData extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="SysmexData_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="observationInturmentConfigurationId")
	private ObservationInturmentConfiguration observationInturmentConfiguration;
	private String typeOfActivity = "Acclamatization";
	private String instumentModelNo = "";
	private String instumentYear = "";
	private String instrumentSno = "";
	private String instrumentReferenceNo = "";
	private String instrumentRemainNo = "";
	private String sampleNo = "";
	private String gender = "";
	private String animalNumber = "";
	private String animalCode = ""; 	
	private String studyNumber = "";
	private String timePoint = "";
	private Date startTime;
	private Date endTime;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="animalId")
	private StudyAnimal animal;
	@ManyToOne
	@JoinColumn(name="studyId")
	private StudyMaster study;
	@ManyToOne
	@JoinColumn(name="sysmexAnimalCodeId")
	private SysmexAnimalCode sysmexAnimalCode;
	private boolean finalValue;
	private String testRunType = "Animal"; // Animal/QC/BACKGROUNDCHECK
	
	
	
	@Transient
	private SysmexRawData sysmexRawData;
	@Transient
	List<SysmexTestCodeData> sysmexTestCodeData;
	
	
	public String getTypeOfActivity() {
		return typeOfActivity;
	}
	public void setTypeOfActivity(String typeOfActivity) {
		this.typeOfActivity = typeOfActivity;
	}
	public boolean isFinalValue() {
		return finalValue;
	}
	public void setFinalValue(boolean finalValue) {
		this.finalValue = finalValue;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public SysmexAnimalCode getSysmexAnimalCode() {
		return sysmexAnimalCode;
	}
	public void setSysmexAnimalCode(SysmexAnimalCode sysmexAnimalCode) {
		this.sysmexAnimalCode = sysmexAnimalCode;
	}
	public String getTestRunType() {
		return testRunType;
	}
	public void setTestRunType(String testRunType) {
		this.testRunType = testRunType;
	}
	public String getStudyNumber() {
		return studyNumber;
	}
	public void setStudyNumber(String studyNumber) {
		this.studyNumber = studyNumber;
	}
	public String getSampleNo() {
		return sampleNo;
	}
	public void setSampleNo(String sampleNo) {
		this.sampleNo = sampleNo;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAnimalCode() {
		return animalCode;
	}
	public void setAnimalCode(String animalCode) {
		this.animalCode = animalCode;
	}
	public StudyAnimal getAnimal() {
		return animal;
	}
	public void setAnimal(StudyAnimal animal) {
		this.animal = animal;
	}
	public StudyMaster getStudy() {
		return study;
	}
	public void setStudy(StudyMaster study) {
		this.study = study;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public List<SysmexTestCodeData> getSysmexTestCodeData() {
		return sysmexTestCodeData;
	}
	public void setSysmexTestCodeData(List<SysmexTestCodeData> sysmexTestCodeData) {
		this.sysmexTestCodeData = sysmexTestCodeData;
	}
	public SysmexRawData getSysmexRawData() {
		return sysmexRawData;
	}
	public void setSysmexRawData(SysmexRawData sysmexRawData) {
		this.sysmexRawData = sysmexRawData;
	}
	public String getInstumentModelNo() {
		return instumentModelNo;
	}
	public void setInstumentModelNo(String instumentModelNo) {
		this.instumentModelNo = instumentModelNo;
	}
	public String getInstumentYear() {
		return instumentYear;
	}
	public void setInstumentYear(String instumentYear) {
		this.instumentYear = instumentYear;
	}
	public String getInstrumentSno() {
		return instrumentSno;
	}
	public void setInstrumentSno(String instrumentSno) {
		this.instrumentSno = instrumentSno;
	}
	public String getInstrumentReferenceNo() {
		return instrumentReferenceNo;
	}
	public void setInstrumentReferenceNo(String instrumentReferenceNo) {
		this.instrumentReferenceNo = instrumentReferenceNo;
	}
	public String getInstrumentRemainNo() {
		return instrumentRemainNo;
	}
	public void setInstrumentRemainNo(String instrumentRemainNo) {
		this.instrumentRemainNo = instrumentRemainNo;
	}
	public String getAnimalNumber() {
		return animalNumber;
	}
	public void setAnimalNumber(String animalNumber) {
		this.animalNumber = animalNumber;
	}
	public String getTimePoint() {
		return timePoint;
	}
	public void setTimePoint(String timePoint) {
		this.timePoint = timePoint;
	}
	public ObservationInturmentConfiguration getObservationInturmentConfiguration() {
		return observationInturmentConfiguration;
	}
	public void setObservationInturmentConfiguration(ObservationInturmentConfiguration observationInturmentConfiguration) {
		this.observationInturmentConfiguration = observationInturmentConfiguration;
	}
	
	
}
