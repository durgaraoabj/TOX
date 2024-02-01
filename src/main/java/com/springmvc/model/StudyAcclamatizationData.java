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

import org.springframework.format.annotation.DateTimeFormat;

import com.covide.crf.dto.Crf;

@SuppressWarnings("serial")
@Entity
@Table(name = "study_acclamatization_data")
public class StudyAcclamatizationData extends CommonMaster implements Serializable {

	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="StudyAcclamatizationData_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name = "study_id")
	private StudyMaster study;
	@Column(name = "type")
	private String type;
	@Column(name = "entry_values")
	private String entryValues;
	private String deviationSign = "+/-";
	private int deviation;
	@ManyToOne
	@JoinColumn(name = "crf_id")
	private Crf crf;
	@Column(name = "status")
	private String status = "active"; // inactive
	
	private boolean allowDataEntry = false;
	@Transient
	private String color = "#3788d8";
	@Transient
	private List<StudyAcclamatizationDates> acclamatizationDates;
	
	private boolean amendment;
	private boolean amendmentApproveStaus;
	private String amendmentCreatedBy;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date amendmentCreatedOn;
	
	
	public boolean isAmendment() {
		return amendment;
	}

	public void setAmendment(boolean amendment) {
		this.amendment = amendment;
	}

	public boolean isAmendmentApproveStaus() {
		return amendmentApproveStaus;
	}

	public void setAmendmentApproveStaus(boolean amendmentApproveStaus) {
		this.amendmentApproveStaus = amendmentApproveStaus;
	}

	public String getAmendmentCreatedBy() {
		return amendmentCreatedBy;
	}

	public void setAmendmentCreatedBy(String amendmentCreatedBy) {
		this.amendmentCreatedBy = amendmentCreatedBy;
	}

	public Date getAmendmentCreatedOn() {
		return amendmentCreatedOn;
	}

	public void setAmendmentCreatedOn(Date amendmentCreatedOn) {
		this.amendmentCreatedOn = amendmentCreatedOn;
	}

	public List<StudyAcclamatizationDates> getAcclamatizationDates() {
		return acclamatizationDates;
	}

	public void setAcclamatizationDates(List<StudyAcclamatizationDates> acclamatizationDates) {
		this.acclamatizationDates = acclamatizationDates;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	
	public boolean isAllowDataEntry() {
		return allowDataEntry;
	}

	public void setAllowDataEntry(boolean allowDataEntry) {
		this.allowDataEntry = allowDataEntry;
	}

	public String getDeviationSign() {
		return deviationSign;
	}

	public void setDeviationSign(String deviationSign) {
		this.deviationSign = deviationSign;
	}

	public int getDeviation() {
		return deviation;
	}

	public void setDeviation(int deviation) {
		this.deviation = deviation;
	}

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEntryValues() {
		return entryValues;
	}

	public void setEntryValues(String entryValues) {
		this.entryValues = entryValues;
	}

	
	public Crf getCrf() {
		return crf;
	}

	public void setCrf(Crf crf) {
		this.crf = crf;
	}

	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
