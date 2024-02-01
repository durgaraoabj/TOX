package com.springmvc.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.covide.crf.dto.Crf;
import com.covide.crf.dto.CrfGroupElement;
import com.covide.crf.dto.CrfRule;
import com.covide.crf.dto.CrfSectionElement;

@Entity
@Table(name="study_accession_crf_descrpency_log")
public class StudyAccessionCrfDescrpencyLog extends CommonMaster implements Serializable {
	
	
	/**
	 * 
	 */
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="StudyAccessionCrfDescrpencyLog_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name="study_id")
	private StudyMaster study;
	@ManyToOne
	@JoinColumn(name="acc_animal_data_entry_details")
	private AccessionAnimalsDataEntryDetails accAnimalDataEntryDetails;
	@ManyToOne
	@JoinColumn(name="study_acc_crf_sec_ele_data")
	private StudyAccessionCrfSectionElementData stydyAccCrfSecEleData;
	@ManyToOne
	@JoinColumn(name="study_acc_crf_group_ele_data")
	private StudyAccessionCrfGroupElementData studyAccCrfGroupEleData;
	@ManyToOne
	@JoinColumn(name="crf_id")
	private Crf crf;
	
	@Column(name="key_name")
	private String kayName;
	@ManyToOne
	@JoinColumn(name="crf_Section_element")
	private CrfSectionElement secElement;
	@ManyToOne
	@JoinColumn(name="study_crf_Group_element")
	private CrfGroupElement groupElement;
	private int rowNo;
	@ManyToOne
	@JoinColumn(name="crf_rule")
	private CrfRule crfRule;
	private String status  = "open"; //open, closed, onHold
	private String assingnedTo = "all"; // username or all
	
	private String risedBy = "user"; //user , reviewer
	private String value = "";
	private String oldValue = "";
	private String oldStatus = "";
	private String comment = "";
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
	public AccessionAnimalsDataEntryDetails getAccAnimalDataEntryDetails() {
		return accAnimalDataEntryDetails;
	}
	public void setAccAnimalDataEntryDetails(AccessionAnimalsDataEntryDetails accAnimalDataEntryDetails) {
		this.accAnimalDataEntryDetails = accAnimalDataEntryDetails;
	}
	public StudyAccessionCrfSectionElementData getStydyAccCrfSecEleData() {
		return stydyAccCrfSecEleData;
	}
	public void setStydyAccCrfSecEleData(StudyAccessionCrfSectionElementData stydyAccCrfSecEleData) {
		this.stydyAccCrfSecEleData = stydyAccCrfSecEleData;
	}
	public StudyAccessionCrfGroupElementData getStudyAccCrfGroupEleData() {
		return studyAccCrfGroupEleData;
	}
	public void setStudyAccCrfGroupEleData(StudyAccessionCrfGroupElementData studyAccCrfGroupEleData) {
		this.studyAccCrfGroupEleData = studyAccCrfGroupEleData;
	}
	public Crf getCrf() {
		return crf;
	}
	public void setCrf(Crf crf) {
		this.crf = crf;
	}
	public String getKayName() {
		return kayName;
	}
	public void setKayName(String kayName) {
		this.kayName = kayName;
	}
	public CrfSectionElement getSecElement() {
		return secElement;
	}
	public void setSecElement(CrfSectionElement secElement) {
		this.secElement = secElement;
	}
	public CrfGroupElement getGroupElement() {
		return groupElement;
	}
	public void setGroupElement(CrfGroupElement groupElement) {
		this.groupElement = groupElement;
	}
	public int getRowNo() {
		return rowNo;
	}
	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}
	public CrfRule getCrfRule() {
		return crfRule;
	}
	public void setCrfRule(CrfRule crfRule) {
		this.crfRule = crfRule;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAssingnedTo() {
		return assingnedTo;
	}
	public void setAssingnedTo(String assingnedTo) {
		this.assingnedTo = assingnedTo;
	}
	public String getRisedBy() {
		return risedBy;
	}
	public void setRisedBy(String risedBy) {
		this.risedBy = risedBy;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getOldValue() {
		return oldValue;
	}
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
	public String getOldStatus() {
		return oldStatus;
	}
	public void setOldStatus(String oldStatus) {
		this.oldStatus = oldStatus;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
	
	

}
