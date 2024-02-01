package com.covide.crf.service;

public class CrfGroupSheet {
	private int lineNo =  1;
	private String name ="NAME";
	private String desc = "DESC";
	private String hedding = "HEDDING";
	private String subHedding = "SUB HEDDING";
	private String minRows = "MIN ROWS";
	private String maxRows = "MAX ROWS";
	private String maxColumns = "MAX COLUMNS";
	private String sectionName="SECTION NAME";
	public CrfGroupSheet() {
		super();
	}
	
	
	public CrfGroupSheet(int lineNo) {
		super();
		this.lineNo = lineNo;
		this.name = "<font color='red'>Required</font>";
		this.desc = "";
		this.hedding = "";
		this.minRows = "<font color='red'>Required</font>";
		this.maxRows = "<font color='red'>Required</font>";
		this.maxColumns = "<font color='red'>Required</font>";
		this.sectionName = "<font color='red'>Required</font>";
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


	public String getMinRows() {
		return minRows;
	}


	public void setMinRows(String minRows) {
		this.minRows = minRows;
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


	public String getSectionName() {
		return sectionName;
	}


	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	
}
