package com.springmvc.model;

import java.io.Serializable;
import java.util.List;

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
@Table(name="STUDY_SITE_Subject")
public class StudySiteSubject  extends CommonMaster implements Comparable<StudySiteSubject>, Serializable{
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="StudySiteSubject_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn
	private StudyMaster studyMaster;
	@ManyToOne
	@JoinColumn
	private StudySite site;
	@ManyToOne
	@JoinColumn
	private StudyGroup studyGroup;
	@ManyToOne
	@JoinColumn
	StudyGroupClass groupClass;
	@Transient
	List<StudySiteSubjectGroup> groups;
	
	@ManyToOne
	@JoinColumn
	Volunteer vol;
	private int subjectno;
	private String subjectId = "";
	private String personId = "";
	private String enrollmentDate = "";
	private String dateOfBirth = "";
	private String name = "";
	private String gender = "";
	private String startDate = "";
	
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
	public int getSubjectno() {
		return subjectno;
	}
	public void setSubjectno(int subjectno) {
		this.subjectno = subjectno;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getEnrollmentDate() {
		return enrollmentDate;
	}
	public void setEnrollmentDate(String enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	@Override
	public int compareTo(StudySiteSubject o) {
		return ((Integer)this.getSubjectno()).compareTo((Integer)o.getSubjectno());
	}
	public List<StudySiteSubjectGroup> getGroups() {
		return groups;
	}
	public void setGroups(List<StudySiteSubjectGroup> groups) {
		this.groups = groups;
	}
	public Volunteer getVol() {
		return vol;
	}
	public void setVol(Volunteer vol) {
		this.vol = vol;
	}
	
}
