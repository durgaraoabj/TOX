package com.springmvc.model;

import java.io.Serializable;
import java.util.Date;
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

import com.covide.crf.dto.Crf;
import com.covide.crf.dto.CrfSectionElement;
import com.covide.crf.dto.CrfSectionElementDataAudit;

@SuppressWarnings("serial")
@Entity
@Table(name="study_accession_crf_section_element_data")
public class StudyAccessionCrfSectionElementData extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="StudyAccessionCrfSectionElementData_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@Column(name="study_id")
	private Long studyId;
	@ManyToOne
	@JoinColumn(name="animal_id")
	private StudyAnimal animal;
	@ManyToOne
	@JoinColumn(name="study_acces_animal_entry")
	private AccessionAnimalsDataEntryDetails studyAccAnimal;
	@ManyToOne
	@JoinColumn(name="clinicalCodesId")
	private ClinicalCodes clinicalCodes;
	@ManyToOne
	@JoinColumn(name="crf_id")
	private Crf crf;
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
	private boolean dataUpdate ;

	
	
	
	public ClinicalCodes getClinicalCodes() {
		return clinicalCodes;
	}
	public void setClinicalCodes(ClinicalCodes clinicalCodes) {
		this.clinicalCodes = clinicalCodes;
	}
	public boolean isDataUpdate() {
		return dataUpdate;
	}
	public void setDataUpdate(boolean dataUpdate) {
		this.dataUpdate = dataUpdate;
	}
	
	@Transient
	private List<CrfSectionElementDataAudit> audit;
	
	
	public List<CrfSectionElementDataAudit> getAudit() {
		return audit;
	}
	public void setAudit(List<CrfSectionElementDataAudit> audit) {
		this.audit = audit;
	}
	public StudyAnimal getAnimal() {
		return animal;
	}
	public void setAnimal(StudyAnimal animal) {
		this.animal = animal;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getStudyId() {
		return studyId;
	}
	public void setStudyId(Long studyId) {
		this.studyId = studyId;
	}
	public AccessionAnimalsDataEntryDetails getStudyAccAnimal() {
		return studyAccAnimal;
	}
	public void setStudyAccAnimal(AccessionAnimalsDataEntryDetails studyAccAnimal) {
		this.studyAccAnimal = studyAccAnimal;
	}
	
	public Crf getCrf() {
		return crf;
	}
	public void setCrf(Crf crf) {
		this.crf = crf;
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
	
	
	
	
	

}
