package com.springmvc.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.HashedMap;

import com.springmvc.model.StudyMaster;

public class NeurobehavioralObservationsofIndividual {
	private String type = "";
	private StudyMaster study;
	private int groupNumber;
	private String gender;
	private Set<Integer> animalNumbers;
	Map<Integer, NeurobehavioralObservationsofIndividualAnimal> homeCageObservations = null;
	Map<Integer, NeurobehavioralObservationsofIndividualAnimal> handlingObservations = null;
	Map<Integer, NeurobehavioralObservationsofIndividualAnimal> openFiledObservations = null;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public StudyMaster getStudy() {
		return study;
	}
	public void setStudy(StudyMaster study) {
		this.study = study;
	}
	public int getGroupNumber() {
		return groupNumber;
	}
	public void setGroupNumber(int groupNumber) {
		this.groupNumber = groupNumber;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Set<Integer> getAnimalNumbers() {
		return animalNumbers;
	}
	public void setAnimalNumbers(Set<Integer> animalNumbers) {
		this.animalNumbers = animalNumbers;
	}
	public Map<Integer, NeurobehavioralObservationsofIndividualAnimal> getHomeCageObservations() {
		return homeCageObservations;
	}
	public void setHomeCageObservations(Map<Integer, NeurobehavioralObservationsofIndividualAnimal> homeCageObservations) {
		this.homeCageObservations = homeCageObservations;
	}
	public Map<Integer, NeurobehavioralObservationsofIndividualAnimal> getHandlingObservations() {
		return handlingObservations;
	}
	public void setHandlingObservations(Map<Integer, NeurobehavioralObservationsofIndividualAnimal> handlingObservations) {
		this.handlingObservations = handlingObservations;
	}
	public Map<Integer, NeurobehavioralObservationsofIndividualAnimal> getOpenFiledObservations() {
		return openFiledObservations;
	}
	public void setOpenFiledObservations(
			Map<Integer, NeurobehavioralObservationsofIndividualAnimal> openFiledObservations) {
		this.openFiledObservations = openFiledObservations;
	}
	
	
	
}
