package com.springmvc.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.covide.crf.dto.Crf;

@Entity
@Table(name = "study_treatment_Data_dates")
public class StudyTreatmentDataDates extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="StudyTreatmentDataDates_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name = "studyId")
	private StudyMaster study;
	@ManyToOne
	@JoinColumn(name = "stdSubGroupObservationCrfsId")
	private StdSubGroupObservationCrfs stdSubGroupObservationCrfs;
	@ManyToOne
	@JoinColumn(name = "crfId")
	private Crf crf;
	private boolean genderBased;
	private String gender = "";
	private int dayNo;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date accDate;
	@Transient
	private String dateString;
	private int noOfEntry;
	private boolean activeStatus;
	
	private String comment;
	private int noOfEntrysDone;
	@Transient
	private String displayGender;
	
	public String getDisplayGender() {
		return displayGender;
	}
	public void setDisplayGender(String displayGender) {
		this.displayGender = displayGender;
	}
	public boolean isGenderBased() {
		return genderBased;
	}
	public void setGenderBased(boolean genderBased) {
		this.genderBased = genderBased;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getDateString() {
		return dateString;
	}
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public StudyMaster getStudy() {
		return study;
	}
	public void setStudy(StudyMaster study) {
		this.study = study;
	}
	public StdSubGroupObservationCrfs getStdSubGroupObservationCrfs() {
		return stdSubGroupObservationCrfs;
	}
	public void setStdSubGroupObservationCrfs(StdSubGroupObservationCrfs stdSubGroupObservationCrfs) {
		this.stdSubGroupObservationCrfs = stdSubGroupObservationCrfs;
	}
	public Crf getCrf() {
		return crf;
	}
	public void setCrf(Crf crf) {
		this.crf = crf;
	}
	public int getDayNo() {
		return dayNo;
	}
	public void setDayNo(int dayNo) {
		this.dayNo = dayNo;
	}
	public Date getAccDate() {
		return accDate;
	}
	public void setAccDate(Date accDate) {
		this.accDate = accDate;
	}
	public int getNoOfEntry() {
		return noOfEntry;
	}
	public void setNoOfEntry(int noOfEntry) {
		this.noOfEntry = noOfEntry;
	}
	public boolean isActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(boolean activeStatus) {
		this.activeStatus = activeStatus;
	}
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getNoOfEntrysDone() {
		return noOfEntrysDone;
	}
	public void setNoOfEntrysDone(int noOfEntrysDone) {
		this.noOfEntrysDone = noOfEntrysDone;
	}
	
	
}
