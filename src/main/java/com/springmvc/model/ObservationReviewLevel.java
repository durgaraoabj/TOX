package com.springmvc.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.covide.crf.dto.Crf;

@Entity
@Table(name = "Observation_Review_Level")
public class ObservationReviewLevel extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="ObservationReviewLevel_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name="sub_Group_Observation_Crf")
	private StdSubGroupObservationCrfs subGroupObservationCrf;
	@ManyToOne
	@JoinColumn(name="sub_Group_AnimalsInfoCrfDataCount")
	private SubGroupAnimalsInfoCrfDataCount subGroupAnimalsInfoCrfDataCount;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="study_id")
	private StudyMaster study;
	@ManyToOne
	@JoinColumn(name="crf_id")
	private Crf crf;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="group_info_id")
	private GroupInfo group;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="sub_group_info_id")
	private SubGroupInfo subGroup;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="subGroupAnimalsInfo_id")
	private SubGroupAnimalsInfo subGroupAnimalsInfo;
	@ManyToOne
	@JoinColumn(name="subGroupAnimalsInfoAll_id")
	private SubGroupAnimalsInfoAll subGroupAnimalsInfoAll;
	@ManyToOne
	@JoinColumn(name="user_id")
	private LoginUsers user;
	private int userLevel = 0;
	private boolean reviewFlag = true;
	
	public SubGroupAnimalsInfoCrfDataCount getSubGroupAnimalsInfoCrfDataCount() {
		return subGroupAnimalsInfoCrfDataCount;
	}
	public void setSubGroupAnimalsInfoCrfDataCount(SubGroupAnimalsInfoCrfDataCount subGroupAnimalsInfoCrfDataCount) {
		this.subGroupAnimalsInfoCrfDataCount = subGroupAnimalsInfoCrfDataCount;
	}
	public boolean isReviewFlag() {
		return reviewFlag;
	}
	public void setReviewFlag(boolean reviewFlag) {
		this.reviewFlag = reviewFlag;
	}
	public int getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}
	public StdSubGroupObservationCrfs getSubGroupObservationCrf() {
		return subGroupObservationCrf;
	}
	public void setSubGroupObservationCrf(StdSubGroupObservationCrfs subGroupObservationCrf) {
		this.subGroupObservationCrf = subGroupObservationCrf;
	}
	public StudyMaster getStudy() {
		return study;
	}
	public void setStudy(StudyMaster study) {
		this.study = study;
	}
	public Crf getCrf() {
		return crf;
	}
	public void setCrf(Crf crf) {
		this.crf = crf;
	}
	public GroupInfo getGroup() {
		return group;
	}
	public void setGroup(GroupInfo group) {
		this.group = group;
	}
	public SubGroupInfo getSubGroup() {
		return subGroup;
	}
	public void setSubGroup(SubGroupInfo subGroup) {
		this.subGroup = subGroup;
	}
	public SubGroupAnimalsInfo getSubGroupAnimalsInfo() {
		return subGroupAnimalsInfo;
	}
	public void setSubGroupAnimalsInfo(SubGroupAnimalsInfo subGroupAnimalsInfo) {
		this.subGroupAnimalsInfo = subGroupAnimalsInfo;
	}
	public SubGroupAnimalsInfoAll getSubGroupAnimalsInfoAll() {
		return subGroupAnimalsInfoAll;
	}
	public void setSubGroupAnimalsInfoAll(SubGroupAnimalsInfoAll subGroupAnimalsInfoAll) {
		this.subGroupAnimalsInfoAll = subGroupAnimalsInfoAll;
	}
	public LoginUsers getUser() {
		return user;
	}
	public void setUser(LoginUsers user) {
		this.user = user;
	}
	
	
}
