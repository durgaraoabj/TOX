package com.covide.crf.dto;

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

import com.springmvc.model.CommonMaster;

@Entity
@Table(name = "CRF_RULE_WITH_OTHER")
public class CrfRuleWithOther extends CommonMaster implements Serializable{
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="CrfRuleWithOther_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name="crf_rule")
	private CrfRule crfRule;
	@ManyToOne
	@JoinColumn(name="crf")
	private Crf crf;
	@ManyToOne
	@JoinColumn(name="section_ele")
	private CrfSectionElement secEle;
	@ManyToOne
	@JoinColumn(name="group_ele")
	private CrfGroupElement groupEle;
	private int rowNo;
	private String compareWith;
	private String condtion;
	private String ncondtion;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Crf getCrf() {
		return crf;
	}
	public void setCrf(Crf crf) {
		this.crf = crf;
	}
	public CrfSectionElement getSecEle() {
		return secEle;
	}
	public void setSecEle(CrfSectionElement secEle) {
		this.secEle = secEle;
	}
	public CrfGroupElement getGroupEle() {
		return groupEle;
	}
	public void setGroupEle(CrfGroupElement groupEle) {
		this.groupEle = groupEle;
	}
	public String getCompareWith() {
		return compareWith;
	}
	public void setCompareWith(String compareWith) {
		this.compareWith = compareWith;
	}
	public String getCondtion() {
		return condtion;
	}
	public void setCondtion(String condtion) {
		this.condtion = condtion;
	}
	public String getNcondtion() {
		return ncondtion;
	}
	public void setNcondtion(String ncondtion) {
		this.ncondtion = ncondtion;
	}
	public CrfRule getCrfRule() {
		return crfRule;
	}
	public void setCrfRule(CrfRule crfRule) {
		this.crfRule = crfRule;
	}
	public int getRowNo() {
		return rowNo;
	}
	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}
	
	
}
