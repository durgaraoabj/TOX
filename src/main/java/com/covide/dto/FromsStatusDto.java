package com.covide.dto;

import java.io.Serializable;
import java.util.Map;

@SuppressWarnings("serial")
public class FromsStatusDto implements Serializable {
	
	private Map<Long, String> crfMap;
	private Map<Long, Map<String, String>> animalsMap;
	public Map<Long, String> getCrfMap() {
		return crfMap;
	}
	public void setCrfMap(Map<Long, String> crfMap) {
		this.crfMap = crfMap;
	}
	public Map<Long, Map<String, String>> getAnimalsMap() {
		return animalsMap;
	}
	public void setAnimalsMap(Map<Long, Map<String, String>> animalsMap) {
		this.animalsMap = animalsMap;
	}
	
	

}
