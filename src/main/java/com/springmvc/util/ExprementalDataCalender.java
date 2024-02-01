package com.springmvc.util;

import java.util.List;

import com.springmvc.model.StdSubGroupObservationCrfs;
import com.springmvc.model.SubGroupAnimalsInfo;

public class ExprementalDataCalender {
	private String expermentDate = "";
	private List<StdSubGroupObservationCrfs> observation;
	private SubGroupAnimalsInfo animals;
	public String getExpermentDate() {
		return expermentDate;
	}
	public void setExpermentDate(String expermentDate) {
		this.expermentDate = expermentDate;
	}
	public List<StdSubGroupObservationCrfs> getObservation() {
		return observation;
	}
	public void setObservation(List<StdSubGroupObservationCrfs> observation) {
		this.observation = observation;
	}
	public SubGroupAnimalsInfo getAnimals() {
		return animals;
	}
	public void setAnimals(SubGroupAnimalsInfo animals) {
		this.animals = animals;
	}
	
}
