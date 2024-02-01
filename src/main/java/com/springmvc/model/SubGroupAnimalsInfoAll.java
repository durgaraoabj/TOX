package com.springmvc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
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
@Table(name="Sub_Group_Animals_Info_All")
public class SubGroupAnimalsInfoAll extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="SubGroupAnimalsInfoAll_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name="sub_group_info_id")
	private SubGroupInfo subGroup;
	@ManyToOne
	@JoinColumn(name="subGroupAnimalsInfo_id")
	private SubGroupAnimalsInfo subGroupAnimalsInfo;
	private String animalNo;
	private int no;
	@Column(name="actual_dayys")
	private String actalDays;
	@ManyToOne
	@JoinColumn(name="radomization_id")
	private RandomizationDetails razdId;
	@Transient
	private List<StdSubGroupObservationCrfs> observation= new ArrayList<>();
	@Transient
	private int noOfObservationsNeedToReview = 0;
	@Transient
	private String reviewStatus = "Not Done";
	@Transient
	private List<SubGroupAnimalsInfoCrfDataCountReviewLevel> livels = null;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<SubGroupAnimalsInfoCrfDataCountReviewLevel> getLivels() {
		return livels;
	}
	public void setLivels(List<SubGroupAnimalsInfoCrfDataCountReviewLevel> livels) {
		this.livels = livels;
	}
	public String getReviewStatus() {
		return reviewStatus;
	}
	public void setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
	}
	public int getNoOfObservationsNeedToReview() {
		return noOfObservationsNeedToReview;
	}
	public void setNoOfObservationsNeedToReview(int noOfObservationsNeedToReview) {
		this.noOfObservationsNeedToReview = noOfObservationsNeedToReview;
	}
	public SubGroupAnimalsInfo getSubGroupAnimalsInfo() {
		return subGroupAnimalsInfo;
	}
	public void setSubGroupAnimalsInfo(SubGroupAnimalsInfo subGroupAnimalsInfo) {
		this.subGroupAnimalsInfo = subGroupAnimalsInfo;
	}
	public String getAnimalNo() {
		return animalNo;
	}
	public void setAnimalNo(String animalNo) {
		this.animalNo = animalNo;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public SubGroupInfo getSubGroup() {
		return subGroup;
	}
	public void setSubGroup(SubGroupInfo subGroup) {
		this.subGroup = subGroup;
	}
	public List<StdSubGroupObservationCrfs> getObservation() {
		return observation;
	}
	public void setObservation(List<StdSubGroupObservationCrfs> observation) {
		this.observation = observation;
	}
	public String getActalDays() {
		return actalDays;
	}
	public void setActalDays(String actalDays) {
		this.actalDays = actalDays;
	}
	public RandomizationDetails getRazdId() {
		return razdId;
	}
	public void setRazdId(RandomizationDetails razdId) {
		this.razdId = razdId;
	}
	
	
	
}
