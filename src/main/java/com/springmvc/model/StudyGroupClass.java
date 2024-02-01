package com.springmvc.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="STUDY_Group_class")
public class StudyGroupClass  extends CommonMaster implements Comparable<StudyGroupClass>,Serializable{
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="StudyGroupClass_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn
	private StudyMaster studyMaster;
	private int groupNo;
	private String groupName = "";
	private String type = "";
	private String subjectAssignment = ""; // Required, Optional
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public StudyMaster getStudyMaster() {
		return studyMaster;
	}
	public void setStudyMaster(StudyMaster studyMaster) {
		this.studyMaster = studyMaster;
	}
	public int getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(int groupNo) {
		this.groupNo = groupNo;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSubjectAssignment() {
		return subjectAssignment;
	}
	public void setSubjectAssignment(String subjectAssignment) {
		this.subjectAssignment = subjectAssignment;
	}
	
	@Override
	public int compareTo(StudyGroupClass o) {
		return ((Integer)this.getGroupNo()).compareTo((Integer)o.getGroupNo());
	}
}
