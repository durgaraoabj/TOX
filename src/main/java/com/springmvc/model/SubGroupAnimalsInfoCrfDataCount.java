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
import javax.persistence.Transient;

@Entity
@Table(name="Sub_Group_Animals_InfoCrfData_Count")
public class SubGroupAnimalsInfoCrfDataCount extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="SubGroupAnimalsInfoCrfDataCount_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	private String entryDate;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="group_info_id")
	private GroupInfo group;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="sub_group_info_id")
	private SubGroupInfo subGroup;
	
	@ManyToOne
	@JoinColumn(name="subGroupAnimalsInfo_id")
	private SubGroupAnimalsInfo subGroupAnimalsInfo;
	@ManyToOne
	@JoinColumn(name="subGroupAnimalsInfoAll_id")
	private SubGroupAnimalsInfoAll subGroupAnimalsInfoAll;
	@ManyToOne
	@JoinColumn(name="std_crf_id")
	private StdSubGroupObservationCrfs crf;
	private int dataEntryCount = 0;
	private String deviationMessage ="";
	private String frequntlyMessage = "";
	private int dayWeekCount;
	private String crfStatus = "IN REVIEW";  // IN REVIEW, REVIEWED
	
	private Long studyid;
	
	@Transient
	private boolean userReview;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public boolean isUserReview() {
		return userReview;
	}
	public void setUserReview(boolean userReview) {
		this.userReview = userReview;
	}
	public String getCrfStatus() {
		return crfStatus;
	}
	public void setCrfStatus(String crfStatus) {
		this.crfStatus = crfStatus;
	}
	public String getDeviationMessage() {
		return deviationMessage;
	}
	public void setDeviationMessage(String deviationMessage) {
		this.deviationMessage = deviationMessage;
	}
	public SubGroupAnimalsInfo getSubGroupAnimalsInfo() {
		return subGroupAnimalsInfo;
	}
	public void setSubGroupAnimalsInfo(SubGroupAnimalsInfo subGroupAnimalsInfo) {
		this.subGroupAnimalsInfo = subGroupAnimalsInfo;
	}
	
	public StdSubGroupObservationCrfs getCrf() {
		return crf;
	}
	public void setCrf(StdSubGroupObservationCrfs crf) {
		this.crf = crf;
	}
	public int getDataEntryCount() {
		return dataEntryCount;
	}
	public void setDataEntryCount(int dataEntryCount) {
		this.dataEntryCount = dataEntryCount;
	}
	public SubGroupAnimalsInfoAll getSubGroupAnimalsInfoAll() {
		return subGroupAnimalsInfoAll;
	}
	public void setSubGroupAnimalsInfoAll(SubGroupAnimalsInfoAll subGroupAnimalsInfoAll) {
		this.subGroupAnimalsInfoAll = subGroupAnimalsInfoAll;
	}
	public String getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}
	public int getDayWeekCount() {
		return dayWeekCount;
	}
	public void setDayWeekCount(int dayWeekCount) {
		this.dayWeekCount = dayWeekCount;
	}
	public String getFrequntlyMessage() {
		return frequntlyMessage;
	}
	public void setFrequntlyMessage(String frequntlyMessage) {
		this.frequntlyMessage = frequntlyMessage;
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
	public Long getStudyid() {
		return studyid;
	}
	public void setStudyid(Long studyid) {
		this.studyid = studyid;
	}
	
}
