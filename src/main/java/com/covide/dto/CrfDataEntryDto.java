package com.covide.dto;

import java.io.Serializable;
import java.util.List;

import com.springmvc.model.StdSubGroupObservationCrfs;
import com.springmvc.model.StudyDesignStatus;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.SubGroupAnimalsInfoAll;
import com.springmvc.model.SubjectDataEntryDetails;

@SuppressWarnings("serial")
public class CrfDataEntryDto implements Serializable {
	
	private StudyMaster sm;
	private SubGroupAnimalsInfoAll animal;
	private StdSubGroupObservationCrfs stdCrf;
	private StudyDesignStatus sds;
	private List<SubjectDataEntryDetails> sdedList;
//	private int sgcdataEntryCount;
	public StudyMaster getSm() {
		return sm;
	}
	public void setSm(StudyMaster sm) {
		this.sm = sm;
	}
	
	public SubGroupAnimalsInfoAll getAnimal() {
		return animal;
	}
	public void setAnimal(SubGroupAnimalsInfoAll animal) {
		this.animal = animal;
	}
	public StdSubGroupObservationCrfs getStdCrf() {
		return stdCrf;
	}
	public void setStdCrf(StdSubGroupObservationCrfs stdCrf) {
		this.stdCrf = stdCrf;
	}
	public StudyDesignStatus getSds() {
		return sds;
	}
	public void setSds(StudyDesignStatus sds) {
		this.sds = sds;
	}
	public List<SubjectDataEntryDetails> getSdedList() {
		return sdedList;
	}
	public void setSdedList(List<SubjectDataEntryDetails> sdedList) {
		this.sdedList = sdedList;
	}
	
	
	

}
