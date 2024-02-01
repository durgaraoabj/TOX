package com.covide.dto;

import java.io.Serializable;

import com.springmvc.model.GroupInfo;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.SubGroupAnimalsInfo;
import com.springmvc.model.SubGroupInfo;

@SuppressWarnings("serial")
public class StudyGroupSubGroupDetails implements Serializable {
	
	private SubGroupAnimalsInfo subgaInfo;
	private SubGroupInfo subgroupId;
	private StudyMaster studyId;
	private GroupInfo groupId;
	private String tempAnimalId;
	private String gender;
	private String perminentNo;
	private String toId;
	private double weight;
	public StudyMaster getStudyId() {
		return studyId;
	}
	public void setStudyId(StudyMaster studyId) {
		this.studyId = studyId;
	}
	public GroupInfo getGroupId() {
		return groupId;
	}
	public void setGroupId(GroupInfo groupId) {
		this.groupId = groupId;
	}
	
	public SubGroupInfo getSubgroupId() {
		return subgroupId;
	}
	public String getTempAnimalId() {
		return tempAnimalId;
	}
	public void setTempAnimalId(String tempAnimalId) {
		this.tempAnimalId = tempAnimalId;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getPerminentNo() {
		return perminentNo;
	}
	public void setPerminentNo(String perminentNo) {
		this.perminentNo = perminentNo;
	}
	public String getToId() {
		return toId;
	}
	public void setToId(String toId) {
		this.toId = toId;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public SubGroupAnimalsInfo getSubgaInfo() {
		return subgaInfo;
	}
	public void setSubgaInfo(SubGroupAnimalsInfo subgaInfo) {
		this.subgaInfo = subgaInfo;
	}
	public void setSubgroupId(SubGroupInfo subgroupId) {
		this.subgroupId = subgroupId;
	}
	
	
	
	
	

}
