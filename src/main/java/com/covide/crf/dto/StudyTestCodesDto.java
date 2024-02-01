package com.covide.crf.dto;

import java.io.Serializable;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.springmvc.model.InstrumentIpAddress;
import com.springmvc.model.TestCode;

public class StudyTestCodesDto implements Serializable{

	private TestCode testCode;
	private int orderNo;
	private InstrumentIpAddress instrument;
	public TestCode getTestCode() {
		return testCode;
	}
	public void setTestCode(TestCode testCode) {
		this.testCode = testCode;
	}
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	public InstrumentIpAddress getInstrument() {
		return instrument;
	}
	public void setInstrument(InstrumentIpAddress instrument) {
		this.instrument = instrument;
	}
	public StudyTestCodesDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public StudyTestCodesDto(TestCode testCode, int orderNo, InstrumentIpAddress instrument) {
		super();
		this.testCode = testCode;
		this.orderNo = orderNo;
		this.instrument = instrument;
	}
	
}
