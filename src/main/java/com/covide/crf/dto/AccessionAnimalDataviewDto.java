package com.covide.crf.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import com.springmvc.model.AccessionAnimalDataDto;
import com.springmvc.model.CommonMaster;
import com.springmvc.model.StudyMaster;

@SuppressWarnings("serial")
public class AccessionAnimalDataviewDto extends CommonMaster implements Serializable {
	
	private Long Id;
	private int totoalDescNotDone = 0;
	private StudyMaster study;
	private Crf crf;
	private Map<Long, CrfSectionElement> elements;
	private List<AccessionAnimalDataDto> animalData;
	
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
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
	public List<AccessionAnimalDataDto> getAnimalData() {
		return animalData;
	}
	public void setAnimalData(List<AccessionAnimalDataDto> animalData) {
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
