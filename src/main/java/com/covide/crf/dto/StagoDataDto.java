package com.covide.crf.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyTestCodes;

public class StagoDataDto implements Serializable, Comparable<StagoDataDto>{
	private String groupName = "";
	private int groupNumber = 0;
	private int animalSeqno = 0;
	private String rowId = "";
	private String id = "";
	private String instrumentName = "";
	private StudyMaster study;
	private String animalNo = "";
	private Long animalId;
	private String ptResult = "";
	private String ptResultDoneBy = "";
	private String ptResultTime = "";
	private String apttResult = "";
	private String apttResultDoneBy = "";
	private String apttResultTime = "";
	private String fibrinogenResult = "";
	private String fibrinogenResultDoneBy = "";
	private String fibrinogenResultTime = "";
	private boolean multipleResult = false;
	private StudyAnimal animal;
	private String lotNo = "";
	private Set<String> tcs = new HashSet<>();
	private SortedMap<Integer, StudyTestCodes> selecteTestCodes = new TreeMap<>();
	
	
	public String getInstrumentName() {
		return instrumentName;
	}
	public void setInstrumentName(String instrumentName) {
		this.instrumentName = instrumentName;
	}
	public Long getAnimalId() {
		return animalId;
	}
	public void setAnimalId(Long animalId) {
		this.animalId = animalId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public int getGroupNumber() {
		return groupNumber;
	}
	public void setGroupNumber(int groupNumber) {
		this.groupNumber = groupNumber;
	}
	public int getAnimalSeqno() {
		return animalSeqno;
	}
	public void setAnimalSeqno(int animalSeqno) {
		this.animalSeqno = animalSeqno;
	}
	public Set<String> getTcs() {
		return tcs;
	}
	public void setTcs(Set<String> tcs) {
		this.tcs = tcs;
	}

	public SortedMap<Integer, StudyTestCodes> getSelecteTestCodes() {
		return selecteTestCodes;
	}
	public void setSelecteTestCodes(SortedMap<Integer, StudyTestCodes> selecteTestCodes) {
		this.selecteTestCodes = selecteTestCodes;
	}
	public String getRowId() {
		return rowId;
	}
	public void setRowId(String rowId) {
		this.rowId = rowId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public StudyMaster getStudy() {
		return study;
	}
	public void setStudy(StudyMaster study) {
		this.study = study;
	}
	public String getAnimalNo() {
		return animalNo;
	}
	public void setAnimalNo(String animalNo) {
		this.animalNo = animalNo;
	}
	public String getPtResult() {
		return ptResult;
	}
	public void setPtResult(String ptResult) {
		this.ptResult = ptResult;
	}
	public String getPtResultDoneBy() {
		return ptResultDoneBy;
	}
	public void setPtResultDoneBy(String ptResultDoneBy) {
		this.ptResultDoneBy = ptResultDoneBy;
	}
	public String getPtResultTime() {
		return ptResultTime;
	}
	public void setPtResultTime(String ptResultTime) {
		this.ptResultTime = ptResultTime;
	}
	public String getApttResult() {
		return apttResult;
	}
	public void setApttResult(String apttResult) {
		this.apttResult = apttResult;
	}
	public String getApttResultDoneBy() {
		return apttResultDoneBy;
	}
	public void setApttResultDoneBy(String apttResultDoneBy) {
		this.apttResultDoneBy = apttResultDoneBy;
	}
	public String getApttResultTime() {
		return apttResultTime;
	}
	public void setApttResultTime(String apttResultTime) {
		this.apttResultTime = apttResultTime;
	}
	public String getFibrinogenResult() {
		return fibrinogenResult;
	}
	public void setFibrinogenResult(String fibrinogenResult) {
		this.fibrinogenResult = fibrinogenResult;
	}
	public String getFibrinogenResultDoneBy() {
		return fibrinogenResultDoneBy;
	}
	public void setFibrinogenResultDoneBy(String fibrinogenResultDoneBy) {
		this.fibrinogenResultDoneBy = fibrinogenResultDoneBy;
	}
	public String getFibrinogenResultTime() {
		return fibrinogenResultTime;
	}
	public void setFibrinogenResultTime(String fibrinogenResultTime) {
		this.fibrinogenResultTime = fibrinogenResultTime;
	}
	public boolean isMultipleResult() {
		return multipleResult;
	}
	public void setMultipleResult(boolean multipleResult) {
		this.multipleResult = multipleResult;
	}
	public StudyAnimal getAnimal() {
		return animal;
	}
	public void setAnimal(StudyAnimal animal) {
		this.animal = animal;
	}
	public String getLotNo() {
		return lotNo;
	}
	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}
	@Override
	public int compareTo(StagoDataDto o) {
		// TODO Auto-generated method stub
		return (Integer.parseInt(this.getAnimalNo())-Integer.parseInt(o.getAnimalNo()));
	}
	
}
