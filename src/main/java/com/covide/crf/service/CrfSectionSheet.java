package com.covide.crf.service;

public class CrfSectionSheet {
	private int lineNo =  1;
	private String name ="NAME";
	private String desc = "DESC";
	private String hedding = "HEDDING";
	private String subHedding = "SUB HEDDING";
	private String maxRows = "MAX ROWS";
	private String maxColumns = "MAX COLUMNS";
	private String order = "ORDER";
	private String gender = "GENDER";
	private String role = "ROLE";
	private String containsGroup="CONTAINS GROUP";
	
	public CrfSectionSheet() {
		super();
	}
	
	
	public CrfSectionSheet(int lineNo) {
		super();
		this.lineNo = lineNo;
		this.name = "<font color='red'>Required</font>";
		this.desc = "";
		this.hedding = "";
		this.maxRows = "";
		this.maxColumns = "";
		this.order = "<font color='red'>Required</font>";
		this.gender = "<font color='red'>Required</font>";
		this.role = "<font color='red'>Required</font>";
		this.containsGroup = "<font color='red'>Required</font>";
	}


	public int getLineNo() {
		return lineNo;
	}


	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDesc() {
		return desc;
	}


	public void setDesc(String desc) {
		this.desc = desc;
	}


	public String getHedding() {
		return hedding;
	}


	public void setHedding(String hedding) {
		this.hedding = hedding;
	}


	public String getSubHedding() {
		return subHedding;
	}


	public void setSubHedding(String subHedding) {
		this.subHedding = subHedding;
	}


	public String getMaxRows() {
		return maxRows;
	}


	public void setMaxRows(String maxRows) {
		this.maxRows = maxRows;
	}


	public String getMaxColumns() {
		return maxColumns;
	}


	public void setMaxColumns(String maxColumns) {
		this.maxColumns = maxColumns;
	}


	public String getOrder() {
		return order;
	}


	public void setOrder(String order) {
		this.order = order;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}


	public String getContainsGroup() {
		return containsGroup;
	}


	public void setContainsGroup(String containsGroup) {
		this.containsGroup = containsGroup;
	}

	
	
}
