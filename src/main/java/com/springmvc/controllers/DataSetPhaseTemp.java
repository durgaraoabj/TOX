package com.springmvc.controllers;

import java.util.Map;
import java.util.Set;

public class DataSetPhaseTemp {

	private Long phaseid;
	private Map<Long, Set<Long>> sectionEle;
	private Map<Long, Set<Long>> groupEle;
	public Long getPhaseid() {
		return phaseid;
	}
	public void setPhaseid(Long phaseid) {
		this.phaseid = phaseid;
	}
	public Map<Long, Set<Long>> getSectionEle() {
		return sectionEle;
	}
	public void setSectionEle(Map<Long, Set<Long>> sectionEle) {
		this.sectionEle = sectionEle;
	}
	public Map<Long, Set<Long>> getGroupEle() {
		return groupEle;
	}
	public void setGroupEle(Map<Long, Set<Long>> groupEle) {
		this.groupEle = groupEle;
	}
	
	
	
}
