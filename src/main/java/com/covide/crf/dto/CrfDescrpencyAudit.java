package com.covide.crf.dto;

import java.io.Serializable;

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

import com.springmvc.model.CommonMaster;
import com.springmvc.model.GroupInfo;
import com.springmvc.model.StdSubGroupObservationCrfs;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.SubGroupAnimalsInfo;
import com.springmvc.model.SubGroupAnimalsInfoAll;
import com.springmvc.model.SubGroupAnimalsInfoCrfDataCount;
import com.springmvc.model.SubGroupInfo;
import com.springmvc.model.Volunteer;
import com.springmvc.model.VolunteerPeriodCrf;

@Entity
@Table(name = "CRF_DESCREPENCY_Audit")
public class CrfDescrpencyAudit extends CommonMaster implements Serializable{
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="CrfDescrpencyAudit_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long Id;
	@ManyToOne
	@JoinColumn(name="stdSubGroupObservationCrfsId")
	private StdSubGroupObservationCrfs stdSubGroupObservationCrfs;
	@ManyToOne
	@JoinColumn(name="subGroupAnimalsInfo_id")
	private SubGroupAnimalsInfo subGroupAnimalsInfo;
	@ManyToOne
	@JoinColumn(name="dataCount")
	private SubGroupAnimalsInfoCrfDataCount dataCount;
	@ManyToOne
	@JoinColumn(name="std_descrepency")
	private CrfDescrpency desc;
	@ManyToOne
	@JoinColumn(name="vol")
	private Volunteer vol;
	@ManyToOne
	@JoinColumn(name="std_crf")
	private Crf crf;
	@ManyToOne
	@JoinColumn(name="vol_period_crf")
	private VolunteerPeriodCrf volPeriodCrf;
	@Column(name="key_name")
	private String kayName;
	@ManyToOne
	@JoinColumn(name="crf_Section_element")
	private CrfSectionElement secElement;
	@ManyToOne
	@JoinColumn(name="sec_ele_data")
	private CrfSectionElementData secEleData;
	@ManyToOne
	@JoinColumn(name="crf_Group_element")
	private CrfGroupElement groupElement;
	@ManyToOne
	@JoinColumn(name="group_ele_data")
	private CrfGroupElementData groupEleData;
	private int rowNo;
	@ManyToOne
	@JoinColumn(name="crf_rule")
	private CrfRule crfRule;
	private String status  = "open"; //open, closed, onHold
	private String assingnedTo = "all"; // username or all
	
	private String risedBy = "user"; //user , reviewer
	
	private String oldValue = "";
	private String oldStatus = "";
	private String newValue;
	
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
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
	public CrfDescrpency getDesc() {
		return desc;
	}
	public void setDesc(CrfDescrpency desc) {
		this.desc = desc;
	}
	public Volunteer getVol() {
		return vol;
	}
	public void setVol(Volunteer vol) {
		this.vol = vol;
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
	
		
}
