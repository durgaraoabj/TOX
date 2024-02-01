package com.springmvc.dto;

import java.io.Serializable;

public class StdSubGroupObservationCrfsDto implements Serializable{
	
	private Long id;
	private String type;
	private String subType;
	private String prefix;
	private String observationName;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSubType() {
		return subType;
	}
	public void setSubType(String subType) {
		this.subType = subType;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getObservationName() {
		return observationName;
	}
	public void setObservationName(String observationName) {
		this.observationName = observationName;
	}
	
}
