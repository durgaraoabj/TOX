package com.covide.crf.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.springmvc.model.AccessionAnimalDataDto;
import com.springmvc.model.ObservationAnimalDataDto;
import com.springmvc.model.StudyMaster;

@SuppressWarnings("serial")
public class ObservationAnimalDataviewDto implements Serializable {
	private int totoalDescNotDone = 0;
	private StudyMaster study;
	private Crf crf;
	private Map<Long, CrfSectionElement> elements;
	private List<ObservationAnimalDataDto> animalData;
	public Crf getCrf() {
		return crf;
	}
	public void setCrf(Crf crf) {
		this.crf = crf;
	}
	public Map<Long, CrfSectionElement> getElements() {
		return elements;
	}
	public void setElements(Map<Long, CrfSectionElement> elements) {
		this.elements = elements;
	}
	
	public List<ObservationAnimalDataDto> getAnimalData() {
		return animalData;
	}
	public void setAnimalData(List<ObservationAnimalDataDto> animalData) {
		this.animalData = animalData;
	}
	public int getTotoalDescNotDone() {
		return totoalDescNotDone;
	}
	public void setTotoalDescNotDone(int totoalDescNotDone) {
		this.totoalDescNotDone = totoalDescNotDone;
	}
	public StudyMaster getStudy() {
		return study;
	}
	public void setStudy(StudyMaster study) {
		this.study = study;
	}
	
	
	
	
	
	

}
