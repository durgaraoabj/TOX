package com.springmvc.model;

import java.io.Serializable;
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
@Table(name="sub_group_info")
public class SubGroupInfo extends CommonMaster implements Serializable,Comparable<SubGroupInfo> {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="SubGroupInfo_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="study_id")
	private StudyMaster study;
	@ManyToOne()
	@JoinColumn(name="group_info_id")
	private GroupInfo group;
	private int subGroupNo;
	private String name;
	private String dose;
	private String conc;
	@Column(name="status")
	private String status="active";
	@Transient
	private List<SubGroupAnimalsInfo> animalInfo;
	@Transient
	private List<SubGroupAnimalsInfoAll> animalInfoAll;
	private boolean crfConfiguation   = false;
	@Transient
	private int noOfSubGroupsNeedToReview = 0;
	@Transient
	private String reviewStatus = "";
	@Transient
	private List<StdSubGroupObservationCrfs> ssgocList;
	
	@Transient
	private boolean dataEntry= false;;
	private String gender = "Male";
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getSubGroupNo() {
		return subGroupNo;
	}
	public void setSubGroupNo(int subGroupNo) {
		this.subGroupNo = subGroupNo;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public boolean isDataEntry() {
		return dataEntry;
	}
	public void setDataEntry(boolean dataEntry) {
		this.dataEntry = dataEntry;
	}
	public GroupInfo getGroup() {
		return group;
	}
	public void setGroup(GroupInfo group) {
		this.group = group;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDose() {
		return dose;
	}
	public void setDose(String dose) {
		this.dose = dose;
	}
	public String getConc() {
		return conc;
	}
	public void setConc(String conc) {
		this.conc = conc;
	}
	public List<SubGroupAnimalsInfo> getAnimalInfo() {
		return animalInfo;
	}
	public void setAnimalInfo(List<SubGroupAnimalsInfo> animalInfo) {
		this.animalInfo = animalInfo;
	}
	public StudyMaster getStudy() {
		return study;
	}
	public void setStudy(StudyMaster study) {
		this.study = study;
	}
	public List<SubGroupAnimalsInfoAll> getAnimalInfoAll() {
		return animalInfoAll;
	}
	public void setAnimalInfoAll(List<SubGroupAnimalsInfoAll> animalInfoAll) {
		this.animalInfoAll = animalInfoAll;
	}
	public boolean isCrfConfiguation() {
		return crfConfiguation;
	}
	public void setCrfConfiguation(boolean crfConfiguation) {
		this.crfConfiguation = crfConfiguation;
	}
	public int getNoOfSubGroupsNeedToReview() {
		return noOfSubGroupsNeedToReview;
	}
	public void setNoOfSubGroupsNeedToReview(int noOfSubGroupsNeedToReview) {
		this.noOfSubGroupsNeedToReview = noOfSubGroupsNeedToReview;
	}
	public String getReviewStatus() {
		return reviewStatus;
	}
	public void setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
	}
	public List<StdSubGroupObservationCrfs> getSsgocList() {
		return ssgocList;
	}
	public void setSsgocList(List<StdSubGroupObservationCrfs> ssgocList) {
		this.ssgocList = ssgocList;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public int compareTo(SubGroupInfo arg0) {
		// TODO Auto-generated method stub
		return this.getSubGroupNo()- arg0.getSubGroupNo();
	}

	
}
