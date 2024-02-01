package com.springmvc.util;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyMaster;

public class DailyClinicalObservation implements Serializable{
	private StudyMaster study;
	private Set<Integer> allDays = new HashSet<>(); 
	private Map<Integer, Long> animalOderMap = new TreeMap<>();
	private Map<Long, Map<Integer, String>> eachAnimalData = new TreeMap<>();
	private Map<Long, StudyAnimal> animalMap = new TreeMap<>();
	public Map<Integer, Long> getAnimalOderMap() {
		return animalOderMap;
	}
	public void setAnimalOderMap(Map<Integer, Long> animalOderMap) {
		this.animalOderMap = animalOderMap;
	}
	public StudyMaster getStudy() {
		return study;
	}
	public void setStudy(StudyMaster study) {
		this.study = study;
	}
	public Set<Integer> getAllDays() {
		return allDays;
	}
	public void setAllDays(Set<Integer> allDays) {
		this.allDays = allDays;
	}
	public Map<Long, Map<Integer, String>> getEachAnimalData() {
		return eachAnimalData;
	}
	public void setEachAnimalData(Map<Long, Map<Integer, String>> eachAnimalData) {
		this.eachAnimalData = eachAnimalData;
	}
	public Map<Long, StudyAnimal> getAnimalMap() {
		return animalMap;
	}
	public void setAnimalMap(Map<Long, StudyAnimal> animalMap) {
		this.animalMap = animalMap;
	}
	
}
