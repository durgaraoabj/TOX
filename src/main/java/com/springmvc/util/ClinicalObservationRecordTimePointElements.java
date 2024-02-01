package com.springmvc.util;

public class ClinicalObservationRecordTimePointElements {
	private String observation = "";
	private String timeInHr = "";
	public String getObservation() {
		return observation;
	}
	public void setObservation(String observation) {
		this.observation = observation;
	}
	public String getTimeInHr() {
		return timeInHr;
	}
	public void setTimeInHr(String timeInHr) {
		this.timeInHr = timeInHr;
	}
	public ClinicalObservationRecordTimePointElements(String observation, String timeInHr) {
		super();
		this.observation = observation;
		this.timeInHr = timeInHr;
	}
	public ClinicalObservationRecordTimePointElements() {
		super();
		// TODO Auto-generated constructor stub
	}

	
}
