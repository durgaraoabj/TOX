package com.covide.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.springmvc.model.StudyMaster;

@SuppressWarnings("serial")
public class FinalRandomizationDto implements Serializable {
	
	private StudyMaster sm;
	private Map<Long, Double> meanMap;
	private Map<Long, Double> sdMap;
	private List<StudyGroupSubGroupDetails> sgsgList;
	private Map<Long, List<StudyGroupSubGroupDetails>> sgsgdMap;
	private boolean submitFlag = true;
	public List<StudyGroupSubGroupDetails> getSgsgList() {
		return sgsgList;
	}
	public void setSgsgList(List<StudyGroupSubGroupDetails> sgsgList) {
		this.sgsgList = sgsgList;
	}
	public Map<Long, Double> getMeanMap() {
		return meanMap;
	}
	public void setMeanMap(Map<Long, Double> meanMap) {
		this.meanMap = meanMap;
	}
	public Map<Long, Double> getSdMap() {
		return sdMap;
	}
	public void setSdMap(Map<Long, Double> sdMap) {
		this.sdMap = sdMap;
	}
	public Map<Long, List<StudyGroupSubGroupDetails>> getSgsgdMap() {
		return sgsgdMap;
	}
	public void setSgsgdMap(Map<Long, List<StudyGroupSubGroupDetails>> sgsgdMap) {
		this.sgsgdMap = sgsgdMap;
	}
	public StudyMaster getSm() {
		return sm;
	}
	public void setSm(StudyMaster sm) {
		this.sm = sm;
	}
	public boolean isSubmitFlag() {
		return submitFlag;
	}
	public void setSubmitFlag(boolean submitFlag) {
		this.submitFlag = submitFlag;
	}
	

}
