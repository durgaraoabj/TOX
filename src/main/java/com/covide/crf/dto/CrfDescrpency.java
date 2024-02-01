package com.covide.crf.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.springmvc.model.AccessionAnimalsDataEntryDetails;
import com.springmvc.model.CommonMaster;
import com.springmvc.model.GroupInfo;
import com.springmvc.model.StdSubGroupObservationCrfs;
import com.springmvc.model.StudyAccessionCrfGroupElementData;
import com.springmvc.model.StudyAccessionCrfSectionElementData;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.SubGroupAnimalsInfo;
import com.springmvc.model.SubGroupAnimalsInfoAll;
import com.springmvc.model.SubGroupAnimalsInfoCrfDataCount;
import com.springmvc.model.SubGroupInfo;
import com.springmvc.model.VolunteerPeriodCrf;

@Entity
@Table(name = "STUDY_CRF_DESCREPENCY")
public class CrfDescrpency extends CommonMaster{
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="CrfDescrpency_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long Id;
	@ManyToOne
	@JoinColumn(name="study_id")
	private StudyMaster study;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="stdSubGroupObservationCrfsId")
	private StdSubGroupObservationCrfs stdSubGroupObservationCrfs;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="subGroupAnimalsInfo_id")
	private SubGroupAnimalsInfo subGroupAnimalsInfo;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="dataCount")
	private SubGroupAnimalsInfoCrfDataCount dataCount;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="std_crf")
	private Crf crf;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="vol_period_crf")
	private VolunteerPeriodCrf volPeriodCrf;
	@Column(name="key_name")
	private String kayName;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="crf_Section_element")
	private CrfSectionElement secElement;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="sec_ele_data")
	private CrfSectionElementData secEleData;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="study_crf_Group_element")
	private CrfGroupElement groupElement;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="group_ele_data")
	private CrfGroupElementData groupEleData;
	private int rowNo;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="crf_rule")
	private CrfRule crfRule;
	private String status  = "open"; //open, closed, onHold
	private String assingnedTo = "all"; // username or all
	
	private String risedBy = "user"; //user , reviewer
	
	private String oldValue = "";
	private String oldStatus = "";
	private String comment = "";
	@Transient
	private String typeOfElemet = "";

	private String value;
	@Transient
	private boolean saveFlag;
	
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	public boolean isSaveFlag() {
		return saveFlag;
	}
	public void setSaveFlag(boolean saveFlag) {
		this.saveFlag = saveFlag;
	}
	public StudyMaster getStudy() {
		return study;
	}
	public void setStudy(StudyMaster study) {
		this.study = study;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getTypeOfElemet() {
		return typeOfElemet;
	}
	public void setTypeOfElemet(String typeOfElemet) {
		this.typeOfElemet = typeOfElemet;
	}
	public StdSubGroupObservationCrfs getStdSubGroupObservationCrfs() {
		return stdSubGroupObservationCrfs;
	}
	public void setStdSubGroupObservationCrfs(StdSubGroupObservationCrfs stdSubGroupObservationCrfs) {
		this.stdSubGroupObservationCrfs = stdSubGroupObservationCrfs;
	}
	public SubGroupAnimalsInfo getSubGroupAnimalsInfo() {
		return subGroupAnimalsInfo;
	}
	public void setSubGroupAnimalsInfo(SubGroupAnimalsInfo subGroupAnimalsInfo) {
		this.subGroupAnimalsInfo = subGroupAnimalsInfo;
	}
	public SubGroupAnimalsInfoCrfDataCount getDataCount() {
		return dataCount;
	}
	public void setDataCount(SubGroupAnimalsInfoCrfDataCount dataCount) {
		this.dataCount = dataCount;
	}
	public Crf getCrf() {
		return crf;
	}
	public void setCrf(Crf crf) {
		this.crf = crf;
	}
	public VolunteerPeriodCrf getVolPeriodCrf() {
		return volPeriodCrf;
	}
	public void setVolPeriodCrf(VolunteerPeriodCrf volPeriodCrf) {
		this.volPeriodCrf = volPeriodCrf;
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
	public CrfSectionElementData getSecEleData() {
		return secEleData;
	}
	public void setSecEleData(CrfSectionElementData secEleData) {
		this.secEleData = secEleData;
	}
	public CrfGroupElement getGroupElement() {
		return groupElement;
	}
	public void setGroupElement(CrfGroupElement groupElement) {
		this.groupElement = groupElement;
	}
	public CrfGroupElementData getGroupEleData() {
		return groupEleData;
	}
	public void setGroupEleData(CrfGroupElementData groupEleData) {
		this.groupEleData = groupEleData;
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
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
	
}
