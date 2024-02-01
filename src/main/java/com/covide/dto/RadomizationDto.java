package com.covide.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.SubGroupAnimalsInfo;
import com.springmvc.model.dummy.RadamizationAllData;

@SuppressWarnings("serial")
public class RadomizationDto implements Serializable {
	private int errorCode = 0;
	private String errorMessage = "";
	private StudyMaster study;
	private String gender = "";
	private List<StudyAnimal> maleAnimals = new ArrayList<>();
	private List<StudyAnimal> femaleAnimals = new ArrayList<>();
	private Map<String, Map<Integer, SubGroupAnimalsInfo>> genderWiseGroupsWithOrder = new HashMap<String, Map<Integer,SubGroupAnimalsInfo>>();
	
	private RadamizationAllData randamizaitonSheetMale;
	private RadamizationAllData randamizaitonSheetFemale;
	private RadamizationAllData randamizaitonSheetMaleAscedning;
	private RadamizationAllData randamizaitonSheetFemaleAscedning;
	
	
	private List<RadamizationAllData> randamizaitonSheetMaleGruoup;
	private List<RadamizationAllData> randamizaitonSheetFemaleGruoup;
	
	
	// validation result;
	private RadamizationAllData randamizaitonSheetMaleGruoupAll;
	private RadamizationAllData randamizaitonSheetFemaleGruoupAll;
	
	private RadamizationAllData randamizaitonSheetMaleGruoupAvialble;
	private RadamizationAllData randamizaitonSheetFemaleGruoupAvialble;
	

	
	
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Map<String, Map<Integer, SubGroupAnimalsInfo>> getGenderWiseGroupsWithOrder() {
		return genderWiseGroupsWithOrder;
	}
	public void setGenderWiseGroupsWithOrder(Map<String, Map<Integer, SubGroupAnimalsInfo>> genderWiseGroupsWithOrder) {
		this.genderWiseGroupsWithOrder = genderWiseGroupsWithOrder;
	}
	public List<StudyAnimal> getMaleAnimals() {
		return maleAnimals;
	}
	public void setMaleAnimals(List<StudyAnimal> maleAnimals) {
		this.maleAnimals = maleAnimals;
	}
	public List<StudyAnimal> getFemaleAnimals() {
		return femaleAnimals;
	}
	public void setFemaleAnimals(List<StudyAnimal> femaleAnimals) {
		this.femaleAnimals = femaleAnimals;
	}
	public RadamizationAllData getRandamizaitonSheetMaleGruoupAll() {
		return randamizaitonSheetMaleGruoupAll;
	}
	public void setRandamizaitonSheetMaleGruoupAll(RadamizationAllData randamizaitonSheetMaleGruoupAll) {
		this.randamizaitonSheetMaleGruoupAll = randamizaitonSheetMaleGruoupAll;
	}
	public RadamizationAllData getRandamizaitonSheetFemaleGruoupAll() {
		return randamizaitonSheetFemaleGruoupAll;
	}
	public void setRandamizaitonSheetFemaleGruoupAll(RadamizationAllData randamizaitonSheetFemaleGruoupAll) {
		this.randamizaitonSheetFemaleGruoupAll = randamizaitonSheetFemaleGruoupAll;
	}
	public RadamizationAllData getRandamizaitonSheetMaleGruoupAvialble() {
		return randamizaitonSheetMaleGruoupAvialble;
	}
	public void setRandamizaitonSheetMaleGruoupAvialble(RadamizationAllData randamizaitonSheetMaleGruoupAvialble) {
		this.randamizaitonSheetMaleGruoupAvialble = randamizaitonSheetMaleGruoupAvialble;
	}
	public RadamizationAllData getRandamizaitonSheetFemaleGruoupAvialble() {
		return randamizaitonSheetFemaleGruoupAvialble;
	}
	public void setRandamizaitonSheetFemaleGruoupAvialble(RadamizationAllData randamizaitonSheetFemaleGruoupAvialble) {
		this.randamizaitonSheetFemaleGruoupAvialble = randamizaitonSheetFemaleGruoupAvialble;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public RadamizationAllData getRandamizaitonSheetMale() {
		return randamizaitonSheetMale;
	}
	public void setRandamizaitonSheetMale(RadamizationAllData randamizaitonSheetMale) {
		this.randamizaitonSheetMale = randamizaitonSheetMale;
	}
	public RadamizationAllData getRandamizaitonSheetFemale() {
		return randamizaitonSheetFemale;
	}
	public void setRandamizaitonSheetFemale(RadamizationAllData randamizaitonSheetFemale) {
		this.randamizaitonSheetFemale = randamizaitonSheetFemale;
	}
	public RadamizationAllData getRandamizaitonSheetMaleAscedning() {
		return randamizaitonSheetMaleAscedning;
	}
	public void setRandamizaitonSheetMaleAscedning(RadamizationAllData randamizaitonSheetMaleAscedning) {
		this.randamizaitonSheetMaleAscedning = randamizaitonSheetMaleAscedning;
	}
	public RadamizationAllData getRandamizaitonSheetFemaleAscedning() {
		return randamizaitonSheetFemaleAscedning;
	}
	public void setRandamizaitonSheetFemaleAscedning(RadamizationAllData randamizaitonSheetFemaleAscedning) {
		this.randamizaitonSheetFemaleAscedning = randamizaitonSheetFemaleAscedning;
	}
	public List<RadamizationAllData> getRandamizaitonSheetMaleGruoup() {
		return randamizaitonSheetMaleGruoup;
	}
	public void setRandamizaitonSheetMaleGruoup(List<RadamizationAllData> randamizaitonSheetMaleGruoup) {
		this.randamizaitonSheetMaleGruoup = randamizaitonSheetMaleGruoup;
	}
	public List<RadamizationAllData> getRandamizaitonSheetFemaleGruoup() {
		return randamizaitonSheetFemaleGruoup;
	}
	public void setRandamizaitonSheetFemaleGruoup(List<RadamizationAllData> randamizaitonSheetFemaleGruoup) {
		this.randamizaitonSheetFemaleGruoup = randamizaitonSheetFemaleGruoup;
	}
	public StudyMaster getStudy() {
		return study;
	}
	public void setStudy(StudyMaster study) {
		this.study = study;
	}
	
	

}
