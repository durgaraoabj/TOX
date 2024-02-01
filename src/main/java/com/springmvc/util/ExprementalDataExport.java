package com.springmvc.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.covide.crf.dto.CrfSectionElement;
import com.covide.crf.dto.CrfSectionElementData;

public class ExprementalDataExport {
	private int max = 0;
	private Long groupId;
	private String groupName;
	private Long subgroupId;
	private String subGroupName;
	private Long obserVationId;
	private String obserVationName;
	private String gender;
	private String animalNo;
	private String dayOrWeek;
	private String date;
	private boolean hedding;
	private String reviewStatus = "";
	private String createdBy = "";
	private String createdOn = "";
	private Map<String, String> reviews = new HashMap<>();
	private Map<Long, CrfSectionElement> elements = new HashMap<>();
	private Map<Long, CrfSectionElementData> elementData = new HashMap<>();
	
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public Long getSubgroupId() {
		return subgroupId;
	}
	public void setSubgroupId(Long subgroupId) {
		this.subgroupId = subgroupId;
	}
	public String getSubGroupName() {
		return subGroupName;
	}
	public void setSubGroupName(String subGroupName) {
		this.subGroupName = subGroupName;
	}
	public Long getObserVationId() {
		return obserVationId;
	}
	public void setObserVationId(Long obserVationId) {
		this.obserVationId = obserVationId;
	}
	public String getObserVationName() {
		return obserVationName;
	}
	public void setObserVationName(String obserVationName) {
		this.obserVationName = obserVationName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAnimalNo() {
		return animalNo;
	}
	public void setAnimalNo(String animalNo) {
		this.animalNo = animalNo;
	}
	public String getDayOrWeek() {
		return dayOrWeek;
	}
	public void setDayOrWeek(String dayOrWeek) {
		this.dayOrWeek = dayOrWeek;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Map<Long, CrfSectionElementData> getElementData() {
		return elementData;
	}
	public void setElementData(Map<Long, CrfSectionElementData> elementData) {
		this.elementData = elementData;
	}
	public boolean isHedding() {
		return hedding;
	}
	public void setHedding(boolean hedding) {
		this.hedding = hedding;
	}
	public Map<Long, CrfSectionElement> getElements() {
		return elements;
	}
	public void setElements(Map<Long, CrfSectionElement> elements) {
		this.elements = elements;
	}
	public String getReviewStatus() {
		return reviewStatus;
	}
	public void setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
	}
	public Map<String, String> getReviews() {
		return reviews;
	}
	public void setReviews(Map<String, String> reviews) {
		this.reviews = reviews;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	
	
}
