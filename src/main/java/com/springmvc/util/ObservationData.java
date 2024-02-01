package com.springmvc.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.covide.crf.dto.Crf;
import com.covide.crf.dto.CrfSectionElement;
import com.springmvc.model.GroupInfo;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.SubGroupAnimalsInfo;
import com.springmvc.model.SubGroupAnimalsInfoAll;
import com.springmvc.model.SubGroupInfo;

public class ObservationData {
	private int totoalDescNotDone = 0;
	private StudyMaster study;
	private GroupInfo group;
	private SubGroupInfo subGroup;
	private SubGroupAnimalsInfo animal;
	private SubGroupAnimalsInfoAll animalAll;
	private Crf crfOrObservation;
	private Map<Long, CrfSectionElement> elements = new HashMap<>();
	private List<AnimalObservationData> animalData = new ArrayList<>();
	private String message = "";
	private boolean allowDataEntry = true;
	
	public boolean isAllowDataEntry() {
		return allowDataEntry;
	}
	public void setAllowDataEntry(boolean allowDataEntry) {
		this.allowDataEntry = allowDataEntry;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public StudyMaster getStudy() {
		return study;
	}
	public void setStudy(StudyMaster study) {
		this.study = study;
	}
	public GroupInfo getGroup() {
		return group;
	}
	public void setGroup(GroupInfo group) {
		this.group = group;
	}
	public SubGroupInfo getSubGroup() {
		return subGroup;
	}
	public void setSubGroup(SubGroupInfo subGroup) {
		this.subGroup = subGroup;
	}
	public SubGroupAnimalsInfo getAnimal() {
		return animal;
	}
	public void setAnimal(SubGroupAnimalsInfo animal) {
		this.animal = animal;
	}
	public SubGroupAnimalsInfoAll getAnimalAll() {
		return animalAll;
	}
	public void setAnimalAll(SubGroupAnimalsInfoAll animalAll) {
		this.animalAll = animalAll;
	}
	public Crf getCrfOrObservation() {
		return crfOrObservation;
	}
	public void setCrfOrObservation(Crf crfOrObservation) {
		this.crfOrObservation = crfOrObservation;
	}
	public Map<Long, CrfSectionElement> getElements() {
		return elements;
	}
	public void setElements(Map<Long, CrfSectionElement> elements) {
		this.elements = elements;
	}
	public List<AnimalObservationData> getAnimalData() {
		return animalData;
	}
	public void setAnimalData(List<AnimalObservationData> animalData) {
		this.animalData = animalData;
	}
	public int getTotoalDescNotDone() {
		return totoalDescNotDone;
	}
	public void setTotoalDescNotDone(int totoalDescNotDone) {
		this.totoalDescNotDone = totoalDescNotDone;
	}
	
}
