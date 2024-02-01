package com.covide.dto;

import java.io.Serializable;

import com.springmvc.model.StatusMaster;
import com.springmvc.model.StudyDesignStatus;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.WorkFlowStatusDetails;

@SuppressWarnings("serial")
public class SatusAndWorkFlowDetailsDto implements Serializable {
	
	private StudyMaster study;
	private StatusMaster sm;
	private WorkFlowStatusDetails wfsd;
	private StudyDesignStatus sds;
	public StatusMaster getSm() {
		return sm;
	}
	public void setSm(StatusMaster sm) {
		this.sm = sm;
	}
	public WorkFlowStatusDetails getWfsd() {
		return wfsd;
	}
	public void setWfsd(WorkFlowStatusDetails wfsd) {
		this.wfsd = wfsd;
	}
	public StudyMaster getStudy() {
		return study;
	}
	public void setStudy(StudyMaster study) {
		this.study = study;
	}
	public StudyDesignStatus getSds() {
		return sds;
	}
	public void setSds(StudyDesignStatus sds) {
		this.sds = sds;
	}
	
	

}
