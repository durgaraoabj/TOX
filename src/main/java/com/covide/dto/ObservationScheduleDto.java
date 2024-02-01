package com.covide.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.springmvc.model.StdSubGroupObservationCrfs;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.SubGroupAnimalsInfoAll;
import com.springmvc.model.SubGroupAnimalsInfoCrfDataCount;

@SuppressWarnings("serial")
public class ObservationScheduleDto implements Serializable {
	private StudyMaster sm;
	private List<StdSubGroupObservationCrfs> obvList;
	private List<SubGroupAnimalsInfoAll> sgaList;
	private List<SubGroupAnimalsInfoCrfDataCount> sgaicdcList;
	private Map<Long, List<SubGroupAnimalsInfoAll>> animalMap;
	private Map<Long, List<SubGroupAnimalsInfoCrfDataCount>> map2;
	private Map<Long, StdSubGroupObservationCrfs> obsvMap;
	public StudyMaster getSm() {
		return sm;
	}
	public List<StdSubGroupObservationCrfs> getObvList() {
		return obvList;
	}
	public List<SubGroupAnimalsInfoAll> getSgaList() {
		return sgaList;
	}
	public List<SubGroupAnimalsInfoCrfDataCount> getSgaicdcList() {
		return sgaicdcList;
	}
	public void setSm(StudyMaster sm) {
		this.sm = sm;
	}
	public void setObvList(List<StdSubGroupObservationCrfs> obvList) {
		this.obvList = obvList;
	}
	public void setSgaList(List<SubGroupAnimalsInfoAll> sgaList) {
		this.sgaList = sgaList;
	}
	public void setSgaicdcList(List<SubGroupAnimalsInfoCrfDataCount> sgaicdcList) {
		this.sgaicdcList = sgaicdcList;
	}
	public Map<Long, List<SubGroupAnimalsInfoAll>> getAnimalMap() {
		return animalMap;
	}
	public Map<Long, List<SubGroupAnimalsInfoCrfDataCount>> getMap2() {
		return map2;
	}
	public void setAnimalMap(Map<Long, List<SubGroupAnimalsInfoAll>> animalMap) {
		this.animalMap = animalMap;
	}
	public void setMap2(Map<Long, List<SubGroupAnimalsInfoCrfDataCount>> map2) {
		this.map2 = map2;
	}
	public Map<Long, StdSubGroupObservationCrfs> getObsvMap() {
		return obsvMap;
	}
	public void setObsvMap(Map<Long, StdSubGroupObservationCrfs> obsvMap) {
		this.obsvMap = obsvMap;
	}
	
	
	
	
	

}
