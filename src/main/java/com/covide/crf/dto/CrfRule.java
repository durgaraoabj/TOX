package com.covide.crf.dto;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.springmvc.model.CommonMaster;

@Entity
@Table(name = "CRF_RULE")
public class CrfRule extends CommonMaster implements Serializable{
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="CrfRule_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@Column(columnDefinition="TEXT")
	private String name;
	@Column(columnDefinition="TEXT")
	private String ruleDesc;
	@Column(columnDefinition="TEXT")
	private String message;
	@Column(name = "is_active")	
	private boolean active = true;//status
	@ManyToOne
	@JoinColumn(name="crf")
	private Crf crf;
	@ManyToOne
	@JoinColumn(name="section_ele")
	private CrfSectionElement secEle;
	@ManyToOne
	@JoinColumn(name="group_ele")
	private CrfGroupElement groupEle;
	private String rowNumber = "0";
	private String compareWith;
	private String value1;
	private String condtion1;
	private String ncondtion1;
	private String value2;
	private String condtion2;
	private String ncondtion2;
	@OneToMany(cascade=CascadeType.PERSIST, mappedBy="crfRule")
	List<CrfRuleWithOther> otherCrf;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRuleDesc() {
		return ruleDesc;
	}
	public void setRuleDesc(String ruleDesc) {
		this.ruleDesc = ruleDesc;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
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
	public String getValue1() {
		return value1;
	}
	public void setValue1(String value1) {
		this.value1 = value1;
	}
	public String getCondtion1() {
		return condtion1;
	}
	public void setCondtion1(String condtion1) {
		this.condtion1 = condtion1;
	}
	public String getNcondtion1() {
		return ncondtion1;
	}
	public void setNcondtion1(String ncondtion1) {
		this.ncondtion1 = ncondtion1;
	}
	public String getValue2() {
		return value2;
	}
	public void setValue2(String value2) {
		this.value2 = value2;
	}
	public String getCondtion2() {
		return condtion2;
	}
	public void setCondtion2(String condtion2) {
		this.condtion2 = condtion2;
	}
	public String getNcondtion2() {
		return ncondtion2;
	}
	public void setNcondtion2(String ncondtion2) {
		this.ncondtion2 = ncondtion2;
	}
	public List<CrfRuleWithOther> getOtherCrf() {
		return otherCrf;
	}
	public void setOtherCrf(List<CrfRuleWithOther> otherCrf) {
		this.otherCrf = otherCrf;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getRowNumber() {
		return rowNumber;
	}
	public void setRowNumber(String rowNumber) {
		this.rowNumber = rowNumber;
	}
	
	
}
