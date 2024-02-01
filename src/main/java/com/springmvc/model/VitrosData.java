package com.springmvc.model;

import java.io.Serializable;
import java.util.Date;

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
@Table(name="vitros_data")
public class VitrosData extends CommonMaster  implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="VitrosData_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="observationInturmentConfigurationId")
	private ObservationInturmentConfiguration observationInturmentConfiguration;
	@ManyToOne
	@JoinColumn(name="studyId")
	private StudyMaster study;
	@Column(name="patientId")
	private String animalNo;
	@Column(name="testDate")
	private Date testDate;
	@Column(name="machineName")
	private String machineName;
	@Column(name="TestName")
	private String testName;
	@Column(name="Result")
	private Float result;
	private boolean finalize = false;
	private String rerunCommnet;
	@ManyToOne
	@JoinColumn(name="animalId")
	private StudyAnimal animal;

	@ManyToOne
	@JoinColumn(name="vitrosDataFromExeId")
	private VitrosDataFromExe vitrosDataFromExe;
	private String startDateTime;
	private String endDateTime;
	

	public ObservationInturmentConfiguration getObservationInturmentConfiguration() {
		return observationInturmentConfiguration;
	}
	public void setObservationInturmentConfiguration(ObservationInturmentConfiguration observationInturmentConfiguration) {
		this.observationInturmentConfiguration = observationInturmentConfiguration;
	}
	public VitrosDataFromExe getVitrosDataFromExe() {
		return vitrosDataFromExe;
	}
	public void setVitrosDataFromExe(VitrosDataFromExe vitrosDataFromExe) {
		this.vitrosDataFromExe = vitrosDataFromExe;
	}
	
	public String getStartDateTime() {
		return startDateTime;
	}
	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}
	public String getEndDateTime() {
		return endDateTime;
	}
	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}
	public StudyAnimal getAnimal() {
		return animal;
	}
	public void setAnimal(StudyAnimal animal) {
		this.animal = animal;
	}
	public String getRerunCommnet() {
		return rerunCommnet;
	}
	public void setRerunCommnet(String rerunCommnet) {
		this.rerunCommnet = rerunCommnet;
	}
	
	public boolean isFinalize() {
		return finalize;
	}
	public void setFinalize(boolean finalize) {
		this.finalize = finalize;
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
	public String getAnimalNo() {
		return animalNo;
	}
	public void setAnimalNo(String animalNo) {
		this.animalNo = animalNo;
	}
	public Date getTestDate() {
		return testDate;
	}
	public void setTestDate(Date testDate) {
		this.testDate = testDate;
	}
	public String getMachineName() {
		return machineName;
	}
	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}
	public String getTestName() {
		return testName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	public Float getResult() {
		return result;
	}
	public void setResult(Float result) {
		this.result = result;
	}
	
}
