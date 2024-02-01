package com.springmvc.reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RulesInfoTemp {
	private String key = "";
	private String value = "";
	private List<String> oeles = new ArrayList<>();
	private Map<String, String> eleType = new HashMap<>();
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public List<String> getOeles() {
		return oeles;
	}
	public void setOeles(List<String> oeles) {
		this.oeles = oeles;
	}
	public Map<String, String> getEleType() {
		return eleType;
	}
	public void setEleType(Map<String, String> eleType) {
		this.eleType = eleType;
	}
	
}
