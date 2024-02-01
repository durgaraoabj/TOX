package com.springmvc.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import com.covide.crf.dto.CrfSectionElementData;

@SuppressWarnings("serial")
public class ObservationAnimalDataDto extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="ObservationAnimalDataDto_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	private String userName;
	private Date date;
	private int count;
	private String deviationMessage="";
	private String frequntlyMessage = "";
	private int dayOrWeek = 0;
	private int discrepencyClosed = 0;
	private int discrepencyOpend = 0;
	private boolean reviewed;
	private String animalno;
	private String animalId;
	private String entryType;
	private String gender;
	private String genderCode;
	private Map<Long, CrfSectionElementData> elementData = new HashMap<>();
	private boolean allowToReview = false;
	private String statusCode;
	private String status;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getDeviationMessage() {
		return deviationMessage;
	}
	public void setDeviationMessage(String deviationMessage) {
		this.deviationMessage = deviationMessage;
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
	public int getDiscrepencyClosed() {
		return discrepencyClosed;
	}
	public void setDiscrepencyClosed(int discrepencyClosed) {
		this.discrepencyClosed = discrepencyClosed;
	}
	public int getDiscrepencyOpend() {
		return discrepencyOpend;
	}
	public void setDiscrepencyOpend(int discrepencyOpend) {
		this.discrepencyOpend = discrepencyOpend;
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
	public String getAnimalId() {
		return animalId;
	}
	public void setAnimalId(String animalId) {
		this.animalId = animalId;
	}
	public String getEntryType() {
		return entryType;
	}
	public void setEntryType(String entryType) {
		this.entryType = entryType;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getGenderCode() {
		return genderCode;
	}
	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}
	public Map<Long, CrfSectionElementData> getElementData() {
		return elementData;
	}
	public void setElementData(Map<Long, CrfSectionElementData> elementData) {
		this.elementData = elementData;
	}
	public boolean isAllowToReview() {
		return allowToReview;
	}
	public void setAllowToReview(boolean allowToReview) {
		this.allowToReview = allowToReview;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
