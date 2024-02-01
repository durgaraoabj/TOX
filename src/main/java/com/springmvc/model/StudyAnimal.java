package com.springmvc.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "STUDY_animal")
public class StudyAnimal extends CommonMaster implements Serializable, Comparable<StudyAnimal> {
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="StudyAnimal_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name = "studyId")
	private StudyMaster study;
	private String animalNo;
	private String permanentNo;
	private int animalId;
	private String gender;
	private String genderCode;
	private int sequnceNo;
	@ManyToOne
	@JoinColumn(name = "animalStatus")
	private StatusMaster animalStatus;

	private boolean accessionDataEntryStatus;
	@ManyToOne
	@JoinColumn(name = "accessionDataEntryDoneBy")
	private LoginUsers accessionDataEntryDoneBy;
	private Date accessionDataEntryDoneDate;
	@ManyToOne
	@JoinColumn(name = "animalDataMeataDataId")
	private AccessionAnimalsDataEntryDetails animalDataMeataData;
	private Double weight;
	@ManyToOne
	@JoinColumn(name = "activityDataId")
	private StudyAccessionCrfSectionElementData activityData;// for weight
	@ManyToOne
	@JoinColumn(name = "groupId")
	private GroupInfo groupInfo;
	@ManyToOne
	@JoinColumn(name = "subGropId")
	private SubGroupAnimalsInfo subGrop;
	@ManyToOne
	@JoinColumn(name = "subGropInfoId")
	private SubGroupInfo subGropInfo;
	private Double doseAmount;
	private int groupCount = 0;
	
	private StudyCageAnimal d;

	private boolean caged;
	@Transient
	private int index;
	@Transient
	private int noOfEntry;
	
	
	public SubGroupInfo getSubGropInfo() {
		return subGropInfo;
	}


	public void setSubGropInfo(SubGroupInfo subGropInfo) {
		this.subGropInfo = subGropInfo;
	}


	public int getNoOfEntry() {
		return noOfEntry;
	}


	public void setNoOfEntry(int noOfEntry) {
		this.noOfEntry = noOfEntry;
	}


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
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


	public String getPermanentNo() {
		return permanentNo;
	}


	public void setPermanentNo(String permanentNo) {
		this.permanentNo = permanentNo;
	}


	public int getAnimalId() {
		return animalId;
	}


	public void setAnimalId(int animalId) {
		this.animalId = animalId;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	public String getGenderCode() {
		return genderCode;
	}


	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}


	public int getSequnceNo() {
		return sequnceNo;
	}


	public void setSequnceNo(int sequnceNo) {
		this.sequnceNo = sequnceNo;
	}


	public StatusMaster getAnimalStatus() {
		return animalStatus;
	}


	public void setAnimalStatus(StatusMaster animalStatus) {
		this.animalStatus = animalStatus;
	}


	


	public boolean isAccessionDataEntryStatus() {
		return accessionDataEntryStatus;
	}


	public void setAccessionDataEntryStatus(boolean accessionDataEntryStatus) {
		this.accessionDataEntryStatus = accessionDataEntryStatus;
	}


	public LoginUsers getAccessionDataEntryDoneBy() {
		return accessionDataEntryDoneBy;
	}


	public void setAccessionDataEntryDoneBy(LoginUsers accessionDataEntryDoneBy) {
		this.accessionDataEntryDoneBy = accessionDataEntryDoneBy;
	}


	public Date getAccessionDataEntryDoneDate() {
		return accessionDataEntryDoneDate;
	}


	public void setAccessionDataEntryDoneDate(Date accessionDataEntryDoneDate) {
		this.accessionDataEntryDoneDate = accessionDataEntryDoneDate;
	}


	public AccessionAnimalsDataEntryDetails getAnimalDataMeataData() {
		return animalDataMeataData;
	}


	public void setAnimalDataMeataData(AccessionAnimalsDataEntryDetails animalDataMeataData) {
		this.animalDataMeataData = animalDataMeataData;
	}


	public Double getWeight() {
		return weight;
	}


	public void setWeight(Double weight) {
		this.weight = weight;
	}


	public StudyAccessionCrfSectionElementData getActivityData() {
		return activityData;
	}


	public void setActivityData(StudyAccessionCrfSectionElementData activityData) {
		this.activityData = activityData;
	}


	public GroupInfo getGroupInfo() {
		return groupInfo;
	}


	public void setGroupInfo(GroupInfo groupInfo) {
		this.groupInfo = groupInfo;
	}


	public SubGroupAnimalsInfo getSubGrop() {
		return subGrop;
	}


	public void setSubGrop(SubGroupAnimalsInfo subGrop) {
		this.subGrop = subGrop;
	}


	public Double getDoseAmount() {
		return doseAmount;
	}


	public void setDoseAmount(Double doseAmount) {
		this.doseAmount = doseAmount;
	}


	public int getGroupCount() {
		return groupCount;
	}


	public void setGroupCount(int groupCount) {
		this.groupCount = groupCount;
	}


	public StudyCageAnimal getD() {
		return d;
	}


	public void setD(StudyCageAnimal d) {
		this.d = d;
	}


	public boolean isCaged() {
		return caged;
	}


	public void setCaged(boolean caged) {
		this.caged = caged;
	}


	@Override
	public int compareTo(StudyAnimal o) {
		// TODO Auto-generated method stub
		return this.getWeight().compareTo(o.getWeight());
	}


	
}
