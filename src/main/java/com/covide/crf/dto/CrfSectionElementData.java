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
import javax.persistence.Transient;

import com.springmvc.model.ClinicalCodes;
import com.springmvc.model.CommonMaster;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.SubjectDataEntryDetails;

/**
 * @author swami.tammireddi
 *
 */
/**
 * @author swami.tammireddi
 *
 */
@Entity
@Table(name = "Crf_Section_Element_Data")
public class CrfSectionElementData extends CommonMaster implements Serializable{
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="CrfSectionElementData_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	
	@Column(name="study_id")
	private Long studyId;
	@ManyToOne
	@JoinColumn(name="animal_id")
	private StudyAnimal animal;
	@ManyToOne
	@JoinColumn(name="crf_id")
	private Crf crf;
	
	@ManyToOne
	@JoinColumn(name="subjectDataEntryDetailsId")
	private SubjectDataEntryDetails subjectDataEntryDetails;
	@ManyToOne
	@JoinColumn(name="clinicalCodesId")
	private ClinicalCodes clinicalCodes;
	@ManyToOne
	@JoinColumn(name="crf_Section_element")
	private CrfSectionElement element;
	@Column(name="key_name")
	private String kayName;
	@Column(name="ele_value")
	private String value;
	@Column(name="entry_type")
	private String entryType;
	@Transient
	private int discripencyClose = 0;
	@Transient
	private int discripency = 0;
	@ManyToOne
	@JoinColumn(name="entredBy")
	private LoginUsers entredBy;
	private Date entredOn;
	private String updateBy;
	private Date updateOn;
	private boolean dataUpdate ;
	
	@Transient
	private String creationDate = "";
	@Transient
	private String creationTime = "";
	
	
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}
	public ClinicalCodes getClinicalCodes() {
		return clinicalCodes;
	}
	public void setClinicalCodes(ClinicalCodes clinicalCodes) {
		this.clinicalCodes = clinicalCodes;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdateOn() {
		return updateOn;
	}
	public void setUpdateOn(Date updateOn) {
		this.updateOn = updateOn;
	}
	public boolean isDataUpdate() {
		return dataUpdate;
	}
	public void setDataUpdate(boolean dataUpdate) {
		this.dataUpdate = dataUpdate;
	}
	public LoginUsers getEntredBy() {
		return entredBy;
	}
	public void setEntredBy(LoginUsers entredBy) {
		this.entredBy = entredBy;
	}
	public Date getEntredOn() {
		return entredOn;
	}
	public void setEntredOn(Date entredOn) {
		this.entredOn = entredOn;
	}
	public SubjectDataEntryDetails getSubjectDataEntryDetails() {
		return subjectDataEntryDetails;
	}
	public void setSubjectDataEntryDetails(SubjectDataEntryDetails subjectDataEntryDetails) {
		this.subjectDataEntryDetails = subjectDataEntryDetails;
	}
	public CrfSectionElement getElement() {
		return element;
	}
	public void setElement(CrfSectionElement element) {
		this.element = element;
	}
	public String getKayName() {
		return kayName;
	}
	public void setKayName(String kayName) {
		this.kayName = kayName;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getEntryType() {
		return entryType;
	}
	public void setEntryType(String entryType) {
		this.entryType = entryType;
	}
	public int getDiscripencyClose() {
		return discripencyClose;
	}
	public void setDiscripencyClose(int discripencyClose) {
		this.discripencyClose = discripencyClose;
	}
	public int getDiscripency() {
		return discripency;
	}
	public void setDiscripency(int discripency) {
		this.discripency = discripency;
	}
	public StudyAnimal getAnimal() {
		return animal;
	}
	public void setAnimal(StudyAnimal animal) {
		this.animal = animal;
	}
	public Crf getCrf() {
		return crf;
	}
	public void setCrf(Crf crf) {
		this.crf = crf;
	}
	public Long getStudyId() {
		return studyId;
	}
	public void setStudyId(Long studyId) {
		this.studyId = studyId;
	}
	@Override
	public String toString() {
		return "CrfSectionElementData [id=" + id + ", element=" + element.getName() + ", value=" + value + ", creationTime="
				+ creationTime + "]";
	}
	
	
}
