package com.covide.template.dto;

import java.io.Serializable;

public class TestCodeProfileParametersDto implements Serializable{
	private Long testCodeId;
	private int orderNo;
	
	public TestCodeProfileParametersDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TestCodeProfileParametersDto(Long testCodeId, int orderNo) {
		super();
		this.testCodeId = testCodeId;
		this.orderNo = orderNo;
	}
	public Long getTestCodeId() {
		return testCodeId;
	}
	public void setTestCodeId(Long testCodeId) {
		this.testCodeId = testCodeId;
	}
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	
	
}
