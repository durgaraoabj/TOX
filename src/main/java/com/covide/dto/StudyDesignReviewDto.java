package com.covide.dto;

import java.io.Serializable;
import java.util.List;

import com.springmvc.model.GroupInfo;
import com.springmvc.model.StudyMaster;

@SuppressWarnings("serial")
public class StudyDesignReviewDto implements Serializable {
	
	private StudyMaster sm;
	private List<GroupInfo> gi;
	public StudyMaster getSm() {
		return sm;
	}
	public void setSm(StudyMaster sm) {
		this.sm = sm;
	}
	public List<GroupInfo> getGi() {
		return gi;
	}
	public void setGi(List<GroupInfo> gi) {
		this.gi = gi;
	}
	
	
	

}
