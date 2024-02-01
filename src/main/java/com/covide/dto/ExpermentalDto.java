package com.covide.dto;

import java.io.Serializable;

import com.springmvc.model.StudyDesignStatus;
import com.springmvc.model.StudyMaster;

@SuppressWarnings("serial")
public class ExpermentalDto implements Serializable{
	
	private StudyMaster sm;
	private StudyDesignStatus sds;
	
	public StudyMaster getSm() {
		return sm;
	}
	public void setSm(StudyMaster sm) {
		this.sm = sm;
	}
	public StudyDesignStatus getSds() {
		return sds;
	}
	public void setSds(StudyDesignStatus sds) {
		this.sds = sds;
	}

}
