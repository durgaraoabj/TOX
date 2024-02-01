package com.springmvc.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="SYSMEX_TESTCODE_DATA")
public class SysmexTestCodeData extends CommonMaster implements Comparable<SysmexTestCodeData> , Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="SysmexTestCodeData_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="sysmexDataId")
	private SysmexData sysmexData;
	private String code;
	private int seqNo;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="studyTestCodeId")
	private StudyTestCodes studyTestCode;
	private int orderNo = 0;
	private String testCode;
	private String insturmentTestCode;
	private String value;
	private String units;
	private String code1;
	private String code2;
	private Date runTime;
	private String displayRunTime = "";
	private boolean finalValue = true;
	private String rerunCommnet = "";
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDisplayRunTime() {
		return displayRunTime;
	}
	public void setDisplayRunTime(String displayRunTime) {
		this.displayRunTime = displayRunTime;
	}
	public String getRerunCommnet() {
		return rerunCommnet;
	}
	public void setRerunCommnet(String rerunCommnet) {
		this.rerunCommnet = rerunCommnet;
	}
	public boolean isFinalValue() {
		return finalValue;
	}
	public void setFinalValue(boolean finalValue) {
		this.finalValue = finalValue;
	}
	public String getInsturmentTestCode() {
		return insturmentTestCode;
	}
	public void setInsturmentTestCode(String insturmentTestCode) {
		this.insturmentTestCode = insturmentTestCode;
	}
	public StudyTestCodes getStudyTestCode() {
		return studyTestCode;
	}
	public void setStudyTestCode(StudyTestCodes studyTestCode) {
		this.studyTestCode = studyTestCode;
	}
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	public SysmexData getSysmexData() {
		return sysmexData;
	}
	public void setSysmexData(SysmexData sysmexData) {
		this.sysmexData = sysmexData;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
	public String getTestCode() {
		return testCode;
	}
	public void setTestCode(String testCode) {
		this.testCode = testCode;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
	public String getCode1() {
		return code1;
	}
	public void setCode1(String code1) {
		this.code1 = code1;
	}
	public String getCode2() {
		return code2;
	}
	public void setCode2(String code2) {
		this.code2 = code2;
	}
	public Date getRunTime() {
		return runTime;
	}
	public void setRunTime(Date runTime) {
		this.runTime = runTime;
	}
	@Override
	public int compareTo(SysmexTestCodeData o) {
		// TODO Auto-generated method stub
		return this.orderNo - o.getOrderNo();
	}
	
	
}
