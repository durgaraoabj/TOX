package com.covide.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.covide.template.dto.PdfHeadderDto;
import com.covide.template.dto.SysmexRowiseDto;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyTestCodes;
import com.springmvc.model.SysmexTestCodeData;

@SuppressWarnings("serial")
public class SysmexDataDto implements Serializable{

	private PdfHeadderDto header;
	private SortedMap<Integer, StudyTestCodes> heading;
	//key = animalNo, value = SortedMap<Integer, List<SysmexTestCodeData>>
						// key = order no,  value = List<SysmexTestCodeData>
	private SortedMap<String, SortedMap<Integer, List<SysmexTestCodeData>>> sysmexDataListMap = new TreeMap<>();
		
	private StudyMaster study;
	private String animalId = null;
	private String testDate;
	private Map<String, String> studyAnimalsGroup = new HashMap<>();//<animalno, StudyAnimal>
	private Map<String, String> animalTestDoneDate = new HashMap<>();
	private SortedMap<String, SortedMap<Integer, List<SysmexTestCodeData>>> sysmexDataList = new TreeMap<>();
	
	private List<StudyTestCodes> testCodes = new ArrayList<>();
	private List<SysmexRowiseDto> sysmexRowiseDtos;
	
	
	public SortedMap<String, SortedMap<Integer, List<SysmexTestCodeData>>> getSysmexDataListMap() {
		return sysmexDataListMap;
	}
	public void setSysmexDataListMap(SortedMap<String, SortedMap<Integer, List<SysmexTestCodeData>>> sysmexDataListMap) {
		this.sysmexDataListMap = sysmexDataListMap;
	}
	public List<StudyTestCodes> getTestCodes() {
		return testCodes;
	}
	public void setTestCodes(List<StudyTestCodes> testCodes) {
		this.testCodes = testCodes;
	}
	public List<SysmexRowiseDto> getSysmexRowiseDtos() {
		return sysmexRowiseDtos;
	}
	public void setSysmexRowiseDtos(List<SysmexRowiseDto> sysmexRowiseDtos) {
		this.sysmexRowiseDtos = sysmexRowiseDtos;
	}
	public PdfHeadderDto getHeader() {
		return header;
	}
	public void setHeader(PdfHeadderDto header) {
		this.header = header;
	}
	public SortedMap<Integer, StudyTestCodes> getHeading() {
		return heading;
	}
	public void setHeading(SortedMap<Integer, StudyTestCodes> heading) {
		this.heading = heading;
	}
	public StudyMaster getStudy() {
		return study;
	}
	public void setStudy(StudyMaster study) {
		this.study = study;
	}
	public String getAnimalId() {
		return animalId;
	}
	public void setAnimalId(String animalId) {
		this.animalId = animalId;
	}
	public String getTestDate() {
		return testDate;
	}
	public void setTestDate(String testDate) {
		this.testDate = testDate;
	}
	public Map<String, String> getStudyAnimalsGroup() {
		return studyAnimalsGroup;
	}
	public void setStudyAnimalsGroup(Map<String, String> studyAnimalsGroup) {
		this.studyAnimalsGroup = studyAnimalsGroup;
	}
	public Map<String, String> getAnimalTestDoneDate() {
		return animalTestDoneDate;
	}
	public void setAnimalTestDoneDate(Map<String, String> animalTestDoneDate) {
		this.animalTestDoneDate = animalTestDoneDate;
	}
	public SortedMap<String, SortedMap<Integer, List<SysmexTestCodeData>>> getSysmexDataList() {
		return sysmexDataList;
	}
	public void setSysmexDataList(SortedMap<String, SortedMap<Integer, List<SysmexTestCodeData>>> sysmexDataList) {
		this.sysmexDataList = sysmexDataList;
	}
	public SysmexDataDto(List<SysmexRowiseDto> sysmexRowiseDtos) {
		super();
		this.sysmexRowiseDtos = sysmexRowiseDtos;
	}
	public SysmexDataDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
