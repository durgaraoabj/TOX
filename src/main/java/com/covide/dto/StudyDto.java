package com.covide.dto;

import java.io.Serializable;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyTestCodes;

/**
 * @author swami.tammireddi
 *
 */
public class StudyDto implements Serializable{
	private List<StudyMaster> studys;
//	private SortedMap<String, StudyTestCodes> testCodes = new TreeMap<>();
	private SortedMap<Integer, String> stagoTestCodesMap = new TreeMap<>();
	private SortedMap<Integer, String> stagoTestCodesUnitsMap = new TreeMap<>();
	public List<StudyMaster> getStudys() {
		return studys;
	}

	public void setStudys(List<StudyMaster> studys) {
		this.studys = studys;
	}

//	public SortedMap<String, StudyTestCodes> getTestCodes() {
//		return testCodes;
//	}
//
//	public void setTestCodes(SortedMap<String, StudyTestCodes> testCodes) {
//		this.testCodes = testCodes;
//	}

	public SortedMap<Integer, String> getStagoTestCodesMap() {
		return stagoTestCodesMap;
	}

	public void setStagoTestCodesMap(SortedMap<Integer, String> stagoTestCodesMap) {
		this.stagoTestCodesMap = stagoTestCodesMap;
	}

	public SortedMap<Integer, String> getStagoTestCodesUnitsMap() {
		return stagoTestCodesUnitsMap;
	}

	public void setStagoTestCodesUnitsMap(SortedMap<Integer, String> stagoTestCodesUnitsMap) {
		this.stagoTestCodesUnitsMap = stagoTestCodesUnitsMap;
	}
	
	
}
