package com.covide.crf.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.springmvc.model.CommonMaster;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.StudyAccessionCrfSectionElementData;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.SubjectDataEntryDetails;

@Entity
@Table(name = "Crf_Section_Element_Data_Audit")
public class CrfSectionElementDataAudit extends CommonMaster implements Serializable{
	/**
	 * 
	 */
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="CrfSectionElementDataAudit_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name = "study_id")
	private StudyMaster study;
	@ManyToOne
	@JoinColumn(name="subjectDataEntryDetailsId")
	private SubjectDataEntryDetails subjectDataEntryDetails;
	@ManyToOne
	@JoinColumn(name="accessionCrfSectionElementDataId")
	private StudyAccessionCrfSectionElementData data;
	
	@ManyToOne
	@JoinColumn(name="crfSectionElementDataId")
	private CrfSectionElementData animalObsercationElementData;
	@ManyToOne
	@JoinColumn(name="crf_Section_element")
	private CrfSectionElement element;

	@ManyToOne
	@JoinColumn(name="group_data")
	private CrfGroupElementData gdata;
	
	@ManyToOne
	@JoinColumn(name="crf_Group_element")
	private CrfGroupElement gelement;
	@Column(name="key_name")
	private String kayName = "";
	@Column(name="old_value")
	private String oldValue = "";
	@Column(name="new_value")
	private String newValue = "";
	
	@ManyToOne
	@JoinColumn(name = "userId")
	private LoginUsers loginUsers;
	private String userComment = "";
	private String dataEntryType = "New";// New,Update
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public StudyMaster getStudy() {
		return study;
	}
	public void setStudy(StudyMaster study) {
		this.study = study;
	}
	public SubjectDataEntryDetails getSubjectDataEntryDetails() {
		return subjectDataEntryDetails;
	}
	public void setSubjectDataEntryDetails(SubjectDataEntryDetails subjectDataEntryDetails) {
		this.subjectDataEntryDetails = subjectDataEntryDetails;
	}
	public StudyAccessionCrfSectionElementData getData() {
		return data;
	}
	public void setData(StudyAccessionCrfSectionElementData data) {
		this.data = data;
	}
	public CrfSectionElementData getAnimalObsercationElementData() {
		return animalObsercationElementData;
	}
	public void setAnimalObsercationElementData(CrfSectionElementData animalObsercationElementData) {
		this.animalObsercationElementData = animalObsercationElementData;
	}
	public CrfSectionElement getElement() {
		return element;
	}
	public void setElement(CrfSectionElement element) {
		this.element = element;
	}
	public CrfGroupElementData getGdata() {
		return gdata;
	}
	public void setGdata(CrfGroupElementData gdata) {
		this.gdata = gdata;
	}
	public CrfGroupElement getGelement() {
		return gelement;
	}
	public void setGelement(CrfGroupElement gelement) {
		this.gelement = gelement;
	}
	public String getKayName() {
		return kayName;
	}
	public void setKayName(String kayName) {
		this.kayName = kayName;
	}
	public String getOldValue() {
		return oldValue;
	}
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	public LoginUsers getLoginUsers() {
		return loginUsers;
	}
	public void setLoginUsers(LoginUsers loginUsers) {
		this.loginUsers = loginUsers;
	}
	
	public String getUserComment() {
		return userComment;
	}
	public void setUserComment(String userComment) {
		this.userComment = userComment;
	}
	public String getDataEntryType() {
		return dataEntryType;
	}
	public void setDataEntryType(String dataEntryType) {
		this.dataEntryType = dataEntryType;
	}
	
	
}
