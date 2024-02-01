package com.springmvc.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="Group_Info")
public class GroupInfo extends CommonMaster implements Serializable, Comparable<GroupInfo>{
	private static final long serialVersionUID = 5849738255480079090L;
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="ObservationAnimalDataDto_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name="study_id")
	private StudyMaster study;
	private String groupName = "";
	private int groupNo;
	private int groupTest = 1;
	private int noOfAnimals = 0;;
	private String gender = "Both"; //Male, Female, Both
	
	@Transient
	private List<SubGroupInfo> subGroupInfo;
	@Transient
	private int noOfGroupsNeedToReview = 0;
	@Transient
	private String reviewStatus = "";
	
	@Transient
	private boolean dataEntry= false;;
	
	public boolean isDataEntry() {
		return dataEntry;
	}
	public void setDataEntry(boolean dataEntry) {
		this.dataEntry = dataEntry;
	}
	public int getNoOfAnimals() {
		return noOfAnimals;
	}
	public void setNoOfAnimals(int noOfAnimals) {
		this.noOfAnimals = noOfAnimals;
	}
	public int getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(int groupNo) {
		this.groupNo = groupNo;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public int getGroupTest() {
		return groupTest;
	}
	public void setGroupTest(int groupTest) {
		this.groupTest = groupTest;
	}
	public StudyMaster getStudy() {
		return study;
	}
	public void setStudy(StudyMaster study) {
		this.study = study;
	}
	public List<SubGroupInfo> getSubGroupInfo() {
		return subGroupInfo;
	}
	public void setSubGroupInfo(List<SubGroupInfo> subGroupInfo) {
		this.subGroupInfo = subGroupInfo;
	}
	public int getNoOfGroupsNeedToReview() {
		return noOfGroupsNeedToReview;
	}
	public void setNoOfGroupsNeedToReview(int noOfGroupsNeedToReview) {
		this.noOfGroupsNeedToReview = noOfGroupsNeedToReview;
	}
	public String getReviewStatus() {
		return reviewStatus;
	}
	public void setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public int compareTo(GroupInfo o) {
		// TODO Auto-generated method stub
		return this.getGroupNo() - o.getGroupNo();
	}
	
	
}
