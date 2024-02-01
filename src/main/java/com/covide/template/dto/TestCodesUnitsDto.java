package com.covide.template.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.springmvc.model.StudyTestCodes;

public class TestCodesUnitsDto {
	private String message = "Connection Failed";
	private String instrumentName = "";
//	public Map<Long, SortedMap<String, StudyTestCodes>> testCodes = new TreeMap<>();
	public Map<Long, SortedMap<Integer, String>> testCodesMap = new TreeMap<>();
	public Map<Long, SortedMap<Integer, String>> testCodesUnitsMap = new TreeMap<>();
	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getInstrumentName() {
		return instrumentName;
	}
	public void setInstrumentName(String instrumentName) {
		this.instrumentName = instrumentName;
	}
//	public Map<Long, SortedMap<String, StudyTestCodes>> getTestCodes() {
//		return testCodes;
//	}
//	public void setTestCodes(Map<Long, SortedMap<String, StudyTestCodes>> testCodes) {
//		this.testCodes = testCodes;
//	}
	public Map<Long, SortedMap<Integer, String>> getTestCodesMap() {
		return testCodesMap;
	}
	public void setTestCodesMap(Map<Long, SortedMap<Integer, String>> testCodesMap) {
		this.testCodesMap = testCodesMap;
	}
	public Map<Long, SortedMap<Integer, String>> getTestCodesUnitsMap() {
		return testCodesUnitsMap;
	}
	public void setTestCodesUnitsMap(Map<Long, SortedMap<Integer, String>> testCodesUnitsMap) {
		this.testCodesUnitsMap = testCodesUnitsMap;
	}

	
}
