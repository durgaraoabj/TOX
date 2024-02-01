package com.springmvc.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="STUDY_Group")
public class StudyGroup  extends CommonMaster implements Comparable<StudyGroup>{
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="StudyGroup_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn
	private StudyMaster studyMaster;
	@ManyToOne
	@JoinColumn
	StudyGroupClass groupClass;
	private int groupNo;
	private String groupName = "";
	private String description = "";
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public StudyGroupClass getGroupClass() {
		return groupClass;
	}
	public void setGroupClass(StudyGroupClass groupClass) {
		this.groupClass = groupClass;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public int compareTo(StudyGroup o) {
		return ((Integer)this.getGroupNo()).compareTo((Integer)o.getGroupNo());
	}
	
	
}
