package com.springmvc.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.covide.crf.dto.CrfSectionElementData;

public class AnimalObservationData {
	private Long id;
	private String userName;
	private Date date;
	private int count;
	private String deviationMessage="";
	private String frequntlyMessage = "";
	private int dayOrWeek = 0;
	private int discrepencyClosed = 0;
	private boolean reviewed;
	private String animalno;
	private Long animalId;
	private Map<Long, CrfSectionElementData> elementData = new HashMap<>();
	
	public int getDiscrepencyClosed() {
		return discrepencyClosed;
	}
	public void setDiscrepencyClosed(int discrepencyClosed) {
		this.discrepencyClosed = discrepencyClosed;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDeviationMessage() {
		return deviationMessage;
	}
	public void setDeviationMessage(String deviationMessage) {
		this.deviationMessage = deviationMessage;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	public Map<Long, CrfSectionElementData> getElementData() {
		return elementData;
	}
	public void setElementData(Map<Long, CrfSectionElementData> elementData) {
		this.elementData = elementData;
	}
	public String getFrequntlyMessage() {
		return frequntlyMessage;
	}
	public void setFrequntlyMessage(String frequntlyMessage) {
		this.frequntlyMessage = frequntlyMessage;
	}
	public int getDayOrWeek() {
		return dayOrWeek;
	}
	public void setDayOrWeek(int dayOrWeek) {
		this.dayOrWeek = dayOrWeek;
	}
	public boolean isReviewed() {
		return reviewed;
	}
	public void setReviewed(boolean reviewed) {
		this.reviewed = reviewed;
	}
	public String getAnimalno() {
		return animalno;
	}
	public void setAnimalno(String animalno) {
		this.animalno = animalno;
	}
	public Long getAnimalId() {
		return animalId;
	}
	public void setAnimalId(Long animalId) {
		this.animalId = animalId;
	}
	
}
