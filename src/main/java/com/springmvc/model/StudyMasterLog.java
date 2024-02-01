package com.springmvc.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
@Entity
@Table(name = "STUDY_MASTER_Log")
public class StudyMasterLog extends CommonMaster implements Serializable{
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="StudyMasterLog_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name = "study_id")
	private StudyMaster sm;
	private String studyNo;
	private String studyDesc;
	private int subjects = 0;
	private Date startDate;
	private String protocalNo;
	private String molecule;
	private String studyType;
	@ManyToOne
	@JoinColumn
	private StatusMaster globalStatus;
	private String studyStatus = "Design"; // Available, Frozen, Locked
	private String experimentalDesign = "No Done";// Not Done, Done
	private String edDoneBy = "";
	private Date edDoneDate = null;
	private boolean crfConfiguation = false;
	private boolean crfPeriodConfiguation = false;
	private boolean volConfiguation = false;
	@Column(name = "sd_user")
	private Long sdUser;
	@Column(name = "asd_user")
	private Long asdUser;
	

	private boolean calculationBased;
	private Double doseVolume;
	private Double concentration;
	@ManyToOne
	@JoinColumn(name = "weightUnits")
	private StaticData weightUnits;

	@ManyToOne
	@JoinColumn(name="radamizationStatus")
	private StatusMaster radamizationStatus;
	@Column(name="radamizationDoneBy")
	private String radamizationDoneBy;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date radamizationDate;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public StatusMaster getRadamizationStatus() {
		return radamizationStatus;
	}

	public void setRadamizationStatus(StatusMaster radamizationStatus) {
		this.radamizationStatus = radamizationStatus;
	}

	public String getRadamizationDoneBy() {
		return radamizationDoneBy;
	}

	public void setRadamizationDoneBy(String radamizationDoneBy) {
		this.radamizationDoneBy = radamizationDoneBy;
	}

	public Date getRadamizationDate() {
		return radamizationDate;
	}

	public void setRadamizationDate(Date radamizationDate) {
		this.radamizationDate = radamizationDate;
	}

	public boolean isCalculationBased() {
		return calculationBased;
	}

	public void setCalculationBased(boolean calculationBased) {
		this.calculationBased = calculationBased;
	}

	public Double getDoseVolume() {
		return doseVolume;
	}

	public void setDoseVolume(Double doseVolume) {
		this.doseVolume = doseVolume;
	}

	public Double getConcentration() {
		return concentration;
	}

	public void setConcentration(Double concentration) {
		this.concentration = concentration;
	}

	public StaticData getWeightUnits() {
		return weightUnits;
	}

	public void setWeightUnits(StaticData weightUnits) {
		this.weightUnits = weightUnits;
	}

	public StudyMaster getSm() {
		return sm;
	}

	public void setSm(StudyMaster sm) {
		this.sm = sm;
	}

	public String getStudyNo() {
		return studyNo;
	}

	public void setStudyNo(String studyNo) {
		this.studyNo = studyNo;
	}

	public String getStudyDesc() {
		return studyDesc;
	}

	public void setStudyDesc(String studyDesc) {
		this.studyDesc = studyDesc;
	}

	public int getSubjects() {
		return subjects;
	}

	public void setSubjects(int subjects) {
		this.subjects = subjects;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getProtocalNo() {
		return protocalNo;
	}

	public void setProtocalNo(String protocalNo) {
		this.protocalNo = protocalNo;
	}

	public String getMolecule() {
		return molecule;
	}

	public void setMolecule(String molecule) {
		this.molecule = molecule;
	}

	public String getStudyType() {
		return studyType;
	}

	public void setStudyType(String studyType) {
		this.studyType = studyType;
	}

	public StatusMaster getGlobalStatus() {
		return globalStatus;
	}

	public void setGlobalStatus(StatusMaster globalStatus) {
		this.globalStatus = globalStatus;
	}

	public String getStudyStatus() {
		return studyStatus;
	}

	public void setStudyStatus(String studyStatus) {
		this.studyStatus = studyStatus;
	}

	public String getExperimentalDesign() {
		return experimentalDesign;
	}

	public void setExperimentalDesign(String experimentalDesign) {
		this.experimentalDesign = experimentalDesign;
	}

	public String getEdDoneBy() {
		return edDoneBy;
	}

	public void setEdDoneBy(String edDoneBy) {
		this.edDoneBy = edDoneBy;
	}

	public Date getEdDoneDate() {
		return edDoneDate;
	}

	public void setEdDoneDate(Date edDoneDate) {
		this.edDoneDate = edDoneDate;
	}

	public boolean isCrfConfiguation() {
		return crfConfiguation;
	}

	public void setCrfConfiguation(boolean crfConfiguation) {
		this.crfConfiguation = crfConfiguation;
	}

	public boolean isCrfPeriodConfiguation() {
		return crfPeriodConfiguation;
	}

	public void setCrfPeriodConfiguation(boolean crfPeriodConfiguation) {
		this.crfPeriodConfiguation = crfPeriodConfiguation;
	}

	public boolean isVolConfiguation() {
		return volConfiguation;
	}

	public void setVolConfiguation(boolean volConfiguation) {
		this.volConfiguation = volConfiguation;
	}

	public Long getSdUser() {
		return sdUser;
	}

	public void setSdUser(Long sdUser) {
		this.sdUser = sdUser;
	}

	public Long getAsdUser() {
		return asdUser;
	}

	public void setAsdUser(Long asdUser) {
		this.asdUser = asdUser;
	}

}
