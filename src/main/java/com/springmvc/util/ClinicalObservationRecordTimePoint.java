package com.springmvc.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyMaster;

public class ClinicalObservationRecordTimePoint {
	private StudyMaster study; 
	
	private Set<String> allTimePoitns = new HashSet<>(); 
	private Map<Integer, Long> animalOderMap = new TreeMap<>();
	private Map<Long, Map<String, ClinicalObservationRecordTimePointElements>> eachAnimalData = new TreeMap<>();
	private Map<Long, StudyAnimal> animalMap = new TreeMap<>();
	
	
	public StudyMaster getStudy() {
		return study;
	}
	public void setStudy(StudyMaster study) {
		this.study = study;
	}
	public Set<String> getAllTimePoitns() {
		return allTimePoitns;
	}
	public void setAllTimePoitns(Set<String> allTimePoitns) {
		this.allTimePoitns = allTimePoitns;
	}
	public Map<Integer, Long> getAnimalOderMap() {
		return animalOderMap;
	}
	public void setAnimalOderMap(Map<Integer, Long> animalOderMap) {
		this.animalOderMap = animalOderMap;
	}
	
	public Map<Long, Map<String, ClinicalObservationRecordTimePointElements>> getEachAnimalData() {
		return eachAnimalData;
	}
	public void setEachAnimalData(Map<Long, Map<String, ClinicalObservationRecordTimePointElements>> eachAnimalData) {
		this.eachAnimalData = eachAnimalData;
	}
	public Map<Long, StudyAnimal> getAnimalMap() {
		return animalMap;
	}
	public void setAnimalMap(Map<Long, StudyAnimal> animalMap) {
		this.animalMap = animalMap;
	}

	
}
