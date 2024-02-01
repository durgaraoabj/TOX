package com.covide.crf.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import com.covide.template.dto.PdfHeadderDto;
import com.covide.template.dto.VitrosTestCodeDataDto;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyTestCodes;

/**
 * @author swami.tammireddi
 *
 */
public class VistrosDataDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3546761538197796632L;
	private String testDate;
	private String sampleType;
	private PdfHeadderDto header = new PdfHeadderDto();
	private SortedMap<Integer, StudyTestCodes> heading;
	private Map<String, SortedMap<Integer, List<VitrosTestCodeDataDto>>> animalTcDataListMap = new HashMap<>();
	private StudyMaster study;
	private String animalId = null;
	
	private Map<String, StudyAnimal> studyAnimals = new HashMap<>();//<animalno, StudyAnimal>
	private Map<String, String> studyAnimalsGroup = new HashMap<>();//<animalno, StudyAnimal>
	private Map<String, String> animalTestDoneDate;
	private Map<String, SortedMap<Integer, List<VitrosTestCodeDataDto>>> animalTcData;
	
	
	public String getSampleType() {
		return sampleType;
	}
	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}
	public Map<String, SortedMap<Integer, List<VitrosTestCodeDataDto>>> getAnimalTcDataListMap() {
		return animalTcDataListMap;
	}
	public void setAnimalTcDataListMap(Map<String, SortedMap<Integer, List<VitrosTestCodeDataDto>>> animalTcDataListMap) {
		this.animalTcDataListMap = animalTcDataListMap;
	}
	public PdfHeadderDto getHeader() {
		return header;
	}
	public void setHeader(PdfHeadderDto header) {
		this.header = header;
	}
	public Map<String, String> getStudyAnimalsGroup() {
		return studyAnimalsGroup;
	}
	public void setStudyAnimalsGroup(Map<String, String> studyAnimalsGroup) {
		this.studyAnimalsGroup = studyAnimalsGroup;
	}
	public Map<String, StudyAnimal> getStudyAnimals() {
		return studyAnimals;
	}
	public void setStudyAnimals(Map<String, StudyAnimal> studyAnimals) {
		this.studyAnimals = studyAnimals;
	}
	public Map<String, String> getAnimalTestDoneDate() {
		return animalTestDoneDate;
	}
	public void setAnimalTestDoneDate(Map<String, String> animalTestDoneDate) {
		this.animalTestDoneDate = animalTestDoneDate;
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
	
	
	public Map<String, SortedMap<Integer, List<VitrosTestCodeDataDto>>> getAnimalTcData() {
		return animalTcData;
	}
	public void setAnimalTcData(Map<String, SortedMap<Integer, List<VitrosTestCodeDataDto>>> animalTcData) {
		this.animalTcData = animalTcData;
	}
	public String getTestDate() {
		return testDate;
	}
	public void setTestDate(String testDate) {
		this.testDate = testDate;
	}
	
//	private Long id;
//	private String animalNo;
//	private String testDate;
//	private String machineName;
//	private SortedMap<Integer, String> testNameResult;
	
}
