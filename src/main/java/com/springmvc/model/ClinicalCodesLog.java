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

@Entity
@Table(name="CLINI_CALCODES_log")
public class ClinicalCodesLog extends CommonMaster implements Serializable{

	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="ClinicalCodes_log_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	
	private String headding;
	private String subHeadding ="";
	private String clinicalCode ="";
	private String clinicalSign ="";
	private String description ="";
	private String rank ="";
	@Column(name="activestatus")
	private boolean activeStatus = true;
	@ManyToOne
	@JoinColumn(name="clinicalCodesId")
	private ClinicalCodes clinicalCodes;
	
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
	public String getHeadding() {
		return headding;
	}
	public void setHeadding(String headding) {
		this.headding = headding;
	}
	public String getSubHeadding() {
		return subHeadding;
	}
	public void setSubHeadding(String subHeadding) {
		this.subHeadding = subHeadding;
	}
	public String getClinicalCode() {
		return clinicalCode;
	}
	public void setClinicalCode(String clinicalCode) {
		this.clinicalCode = clinicalCode;
	}
	public String getClinicalSign() {
		return clinicalSign;
	}
	public void setClinicalSign(String clinicalSign) {
		this.clinicalSign = clinicalSign;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public boolean isActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(boolean activeStatus) {
		this.activeStatus = activeStatus;
	}
	
}
