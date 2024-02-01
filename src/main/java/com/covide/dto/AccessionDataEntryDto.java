package com.covide.dto;

import java.io.Serializable;
import java.util.List;

import com.covide.crf.dto.Crf;
import com.springmvc.model.AccessionAnimalsDataEntryDetails;
import com.springmvc.model.StudyAccessionAnimals;
import com.springmvc.model.StudyAcclamatizationData;
import com.springmvc.model.StudyMaster;

@SuppressWarnings("serial")
public class AccessionDataEntryDto implements Serializable {
	
	private StudyAcclamatizationData sad;
	private Crf crf;
	private StudyMaster sm;
	private AccessionAnimalsDataEntryDetails aaded;
	private int entrySize;
	private StudyAccessionAnimals saaPojo;
	private int genderSize;
	private List<StudyAccessionAnimals> saaList;
	public StudyAcclamatizationData getSad() {
		return sad;
	}
	public void setSad(StudyAcclamatizationData sad) {
		this.sad = sad;
	}
	public Crf getCrf() {
		return crf;
	}
	public void setCrf(Crf crf) {
		this.crf = crf;
	}
	public StudyMaster getSm() {
		return sm;
	}
	public void setSm(StudyMaster sm) {
		this.sm = sm;
	}
	public AccessionAnimalsDataEntryDetails getAaded() {
		return aaded;
	}
	public void setAaded(AccessionAnimalsDataEntryDetails aaded) {
		this.aaded = aaded;
	}
	public int getEntrySize() {
		return entrySize;
	}
	public void setEntrySize(int entrySize) {
		this.entrySize = entrySize;
	}
	public StudyAccessionAnimals getSaaPojo() {
		return saaPojo;
	}
	public void setSaaPojo(StudyAccessionAnimals saaPojo) {
		this.saaPojo = saaPojo;
	}
	public int getGenderSize() {
		return genderSize;
	}
	public void setGenderSize(int genderSize) {
		this.genderSize = genderSize;
	}
	public List<StudyAccessionAnimals> getSaaList() {
		return saaList;
	}
	public void setSaaList(List<StudyAccessionAnimals> saaList) {
		this.saaList = saaList;
	}
	
	

}
