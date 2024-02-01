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
@Table(name="STUDY_SITE_Subject_group")
public class StudySiteSubjectGroup extends CommonMaster implements Serializable {
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="StudySiteSubjectGroup_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn
	private StudySite site;
	@ManyToOne
	@JoinColumn
	private StudyGroup studyGroup;
	@ManyToOne
	@JoinColumn
	StudyGroupClass groupClass;
	
	@ManyToOne
	@JoinColumn
	private StudySiteSubject subject;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public StudySiteSubject getSubject() {
		return subject;
	}
	public void setSubject(StudySiteSubject subject) {
		this.subject = subject;
	}
	public StudySite getSite() {
		return site;
	}
	public void setSite(StudySite site) {
		this.site = site;
	}
	public StudyGroup getStudyGroup() {
		return studyGroup;
	}
	public void setStudyGroup(StudyGroup studyGroup) {
		this.studyGroup = studyGroup;
	}
	public StudyGroupClass getGroupClass() {
		return groupClass;
	}
	public void setGroupClass(StudyGroupClass groupClass) {
		this.groupClass = groupClass;
	}
	
	
}
