package com.springmvc.model.dummy;

import java.util.List;

import com.springmvc.model.GroupInfo;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyMaster;

public class RadamizationAllData {
	private StudyMaster study;
	private String gender;
	private List<StudyAnimal> animals;
	private Double mean;
	private Double start;
	private Double end;
	private Double minimum;
	private Double maximum;
	private GroupInfo group;
	private int gorupNo;
	private Double groupMean;
	private Double grouoSD;
	
	public GroupInfo getGroup() {
		return group;
	}
	public void setGroup(GroupInfo group) {
		this.group = group;
	}
	public int getGorupNo() {
		return gorupNo;
	}
	public void setGorupNo(int gorupNo) {
		this.gorupNo = gorupNo;
	}
	public Double getGroupMean() {
		return groupMean;
	}
	public void setGroupMean(Double groupMean) {
		this.groupMean = groupMean;
	}
	public Double getGrouoSD() {
		return grouoSD;
	}
	public void setGrouoSD(Double grouoSD) {
		this.grouoSD = grouoSD;
	}
	public Double getMean() {
		return mean;
	}
	public void setMean(Double mean) {
		this.mean = mean;
	}
	public Double getStart() {
		return start;
	}
	public void setStart(Double start) {
		this.start = start;
	}
	public Double getEnd() {
		return end;
	}
	public void setEnd(Double end) {
		this.end = end;
	}
	public Double getMinimum() {
		return minimum;
	}
	public void setMinimum(Double minimum) {
		this.minimum = minimum;
	}
	public Double getMaximum() {
		return maximum;
	}
	public void setMaximum(Double maximum) {
		this.maximum = maximum;
	}
	public StudyMaster getStudy() {
		return study;
	}
	public void setStudy(StudyMaster study) {
		this.study = study;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public List<StudyAnimal> getAnimals() {
		return animals;
	}
	public void setAnimals(List<StudyAnimal> animals) {
		this.animals = animals;
	}
	public RadamizationAllData(StudyMaster study, String gender, List<StudyAnimal> animals) {
		super();
		this.study = study;
		this.gender = gender;
		this.animals = animals;
	}
	public RadamizationAllData() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
