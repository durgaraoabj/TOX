package com.covide.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.springmvc.model.StudyAccessionAnimals;
import com.springmvc.model.StudyMaster;

@SuppressWarnings("serial")
public class RandomizationGenerationDto implements Serializable {
	
	private StudyMaster sm;
	private List<StudyAccessionAnimals> saaList;
	private int totalAnimals=0;
	private Long seceleId;
	private Map<String, Double> actMap;
	private Double mean;
	private Double plustwentyVal;
	private Double minusTewentyVal;
	private Double minVal;
	private Double maxVal;
	private String groupingMsg;
	private String result;
	private List<StudyGroupSubGroupDetails> sgsgList;
	public StudyMaster getSm() {
		return sm;
	}
	public void setSm(StudyMaster sm) {
		this.sm = sm;
	}
	public List<StudyAccessionAnimals> getSaaList() {
		return saaList;
	}
	public void setSaaList(List<StudyAccessionAnimals> saaList) {
		this.saaList = saaList;
	}
	public int getTotalAnimals() {
		return totalAnimals;
	}
	public void setTotalAnimals(int totalAnimals) {
		this.totalAnimals = totalAnimals;
	}
	public Long getSeceleId() {
		return seceleId;
	}
	public void setSeceleId(Long seceleId) {
		this.seceleId = seceleId;
	}
	public Map<String, Double> getActMap() {
		return actMap;
	}
	public void setActMap(Map<String, Double> actMap) {
		this.actMap = actMap;
	}
	public Double getMean() {
		return mean;
	}
	public void setMean(Double mean) {
		this.mean = mean;
	}
	public Double getPlustwentyVal() {
		return plustwentyVal;
	}
	public void setPlustwentyVal(Double plustwentyVal) {
		this.plustwentyVal = plustwentyVal;
	}
	public Double getMinusTewentyVal() {
		return minusTewentyVal;
	}
	public void setMinusTewentyVal(Double minusTewentyVal) {
		this.minusTewentyVal = minusTewentyVal;
	}
	public Double getMinVal() {
		return minVal;
	}
	public void setMinVal(Double minVal) {
		this.minVal = minVal;
	}
	public Double getMaxVal() {
		return maxVal;
	}
	public void setMaxVal(Double maxVal) {
		this.maxVal = maxVal;
	}
	public String getGroupingMsg() {
		return groupingMsg;
	}
	public void setGroupingMsg(String groupingMsg) {
		this.groupingMsg = groupingMsg;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public List<StudyGroupSubGroupDetails> getSgsgList() {
		return sgsgList;
	}
	public void setSgsgList(List<StudyGroupSubGroupDetails> sgsgList) {
		this.sgsgList = sgsgList;
	}
	
	

}
