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

import com.covide.crf.dto.CrfGroupElement;
import com.covide.crf.dto.CrfRule;
import com.covide.crf.dto.CrfSectionElement;

@SuppressWarnings("serial")
@Entity
@Table(name="accession_crf_descrpency_audit")
public class AccessionCrfDescrpencyAudit extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="AccessionCrfDescrpencyAudit_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@Column(name="study_id")
	private Long studyId;
	@ManyToOne
	@JoinColumn(name="acc_animal_data_details")
	private AccessionAnimalsDataEntryDetails accAnimalDataEntryDetails;
	@ManyToOne
	@JoinColumn(name="std_descrepency")
	private StudyAccessionCrfDescrpency desc;
	@Column(name="crf_id")
	private Long crfId;
	@Column(name="key_name")
	private String kayName;
	@ManyToOne
	@JoinColumn(name="crf_Section_element")
	private CrfSectionElement secElement;
	@ManyToOne
	@JoinColumn(name="study_acc_crf_ele_data")
	private StudyAccessionCrfSectionElementData studyAccCrfEleData;
	@ManyToOne
	@JoinColumn(name="crf_Group_element")
	private CrfGroupElement groupElement;
	@ManyToOne
	@JoinColumn(name="study_acc_crf_group_ele_data")
	private StudyAccessionCrfGroupElementData studyAccCrfGroupEleData;
	private int rowNo;
	@ManyToOne
	@JoinColumn(name="crf_rule")
	private CrfRule crfRule;
	private String status  = "open"; //open, closed, onHold
	private String assingnedTo = "all"; // username or all
	
	private String risedBy = "user"; //user , reviewer
	
	private String oldValue = "";
	private String oldStatus = "";
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
	public AccessionAnimalsDataEntryDetails getAccAnimalDataEntryDetails() {
		return accAnimalDataEntryDetails;
	}
	public void setAccAnimalDataEntryDetails(AccessionAnimalsDataEntryDetails accAnimalDataEntryDetails) {
		this.accAnimalDataEntryDetails = accAnimalDataEntryDetails;
	}
	public StudyAccessionCrfDescrpency getDesc() {
		return desc;
	}
	public void setDesc(StudyAccessionCrfDescrpency desc) {
		this.desc = desc;
	}
	public Long getCrfId() {
		return crfId;
	}
	public void setCrfId(Long crfId) {
		this.crfId = crfId;
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
	public StudyAccessionCrfSectionElementData getStudyAccCrfEleData() {
		return studyAccCrfEleData;
	}
	public void setStudyAccCrfEleData(StudyAccessionCrfSectionElementData studyAccCrfEleData) {
		this.studyAccCrfEleData = studyAccCrfEleData;
	}
	public CrfGroupElement getGroupElement() {
		return groupElement;
	}
	public void setGroupElement(CrfGroupElement groupElement) {
		this.groupElement = groupElement;
	}
	public StudyAccessionCrfGroupElementData getStudyAccCrfGroupEleData() {
		return studyAccCrfGroupEleData;
	}
	public void setStudyAccCrfGroupEleData(StudyAccessionCrfGroupElementData studyAccCrfGroupEleData) {
		this.studyAccCrfGroupEleData = studyAccCrfGroupEleData;
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
	
	
	

}
