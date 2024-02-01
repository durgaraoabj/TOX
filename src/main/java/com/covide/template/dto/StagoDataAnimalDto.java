package com.covide.template.dto;

import java.io.Serializable;
import java.util.SortedMap;
import java.util.TreeMap;

import com.springmvc.model.StagoData;
import com.springmvc.model.StudyTestCodes;

public class StagoDataAnimalDto implements Serializable{
	private int count;
	private String groupName;
	private int groupNo;
	private String animalName;
	private int animalNo;
	private String lotNumber = "";
	
	SortedMap<Integer, String> tcdataDispay = new TreeMap<>();
	private SortedMap<Integer, String> receivedTimes;
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public int getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(int groupNo) {
		this.groupNo = groupNo;
	}
	public String getAnimalName() {
		return animalName;
	}
	public void setAnimalName(String animalName) {
		this.animalName = animalName;
	}
	public int getAnimalNo() {
		return animalNo;
	}
	public void setAnimalNo(int animalNo) {
		this.animalNo = animalNo;
	}
	public String getLotNumber() {
		return lotNumber;
	}
	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}
	public SortedMap<Integer, String> getTcdataDispay() {
		return tcdataDispay;
	}
	public void setTcdataDispay(SortedMap<Integer, String> tcdataDispay) {
		this.tcdataDispay = tcdataDispay;
	}
	public SortedMap<Integer, String> getReceivedTimes() {
		return receivedTimes;
	}
	public void setReceivedTimes(SortedMap<Integer, String> receivedTimes) {
		this.receivedTimes = receivedTimes;
	}
	
	
	
}
