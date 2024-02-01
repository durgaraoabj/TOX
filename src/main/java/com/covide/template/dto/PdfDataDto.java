package com.covide.template.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import com.springmvc.model.SysmexTestCodeData;

public class PdfDataDto implements Serializable{
	private String instumentName = "";
	private String reporType = "";
	private Integer groupNo = null;
	private String  groupName = "";
	private String species = "";
	private int animalNo = 0;
	private String animalName = "";
	private String studyNumber = "";
	private String testRunTime = "";
	private String printDate = "";
	private String anaysedBy = "";
	private List<String> rowHeadin = new ArrayList<>();
	private  SortedMap<Integer, SysmexTestCodePdfData> rowData;
	
	public String getSpecies() {
		return species;
	}
	public void setSpecies(String species) {
		this.species = species;
	}
	public Integer getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(Integer groupNo) {
		this.groupNo = groupNo;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getInstumentName() {
		return instumentName;
	}
	public void setInstumentName(String instumentName) {
		this.instumentName = instumentName;
	}
	public String getReporType() {
		return reporType;
	}
	public void setReporType(String reporType) {
		this.reporType = reporType;
	}
	
	public int getAnimalNo() {
		return animalNo;
	}
	public void setAnimalNo(int animalNo) {
		this.animalNo = animalNo;
	}
	public String getAnimalName() {
		return animalName;
	}
	public void setAnimalName(String animalName) {
		this.animalName = animalName;
	}
	public String getStudyNumber() {
		return studyNumber;
	}
	public void setStudyNumber(String studyNumber) {
		this.studyNumber = studyNumber;
	}
	public String getTestRunTime() {
		return testRunTime;
	}
	public void setTestRunTime(String testRunTime) {
		this.testRunTime = testRunTime;
	}
	public String getPrintDate() {
		return printDate;
	}
	public void setPrintDate(String printDate) {
		this.printDate = printDate;
	}
	public String getAnaysedBy() {
		return anaysedBy;
	}
	public void setAnaysedBy(String anaysedBy) {
		this.anaysedBy = anaysedBy;
	}
	public List<String> getRowHeadin() {
		return rowHeadin;
	}
	public void setRowHeadin(List<String> rowHeadin) {
		this.rowHeadin = rowHeadin;
	}
	public SortedMap<Integer, SysmexTestCodePdfData> getRowData() {
		return rowData;
	}
	public void setRowData(SortedMap<Integer, SysmexTestCodePdfData> rowData) {
		this.rowData = rowData;
	}
	
	
}
