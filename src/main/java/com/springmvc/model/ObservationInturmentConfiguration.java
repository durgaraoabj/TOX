package com.springmvc.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="Observation_Inturment_Configuration")
public class ObservationInturmentConfiguration extends CommonMaster implements Serializable{

	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="ObservationInturmentConfiguration_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name="studyId")
	private StudyMaster study;
	@ManyToOne
	@JoinColumn(name="studyAcclamatizationDatesId")
	private StudyAcclamatizationDates studyAcclamatizationDates;
	
	@ManyToOne
	@JoinColumn(name = "studyTreatmentDataDatesId")
	private StudyTreatmentDataDates studyTreatmentDataDates;
	
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
	public StudyAcclamatizationDates getStudyAcclamatizationDates() {
		return studyAcclamatizationDates;
	}
	public void setStudyAcclamatizationDates(StudyAcclamatizationDates studyAcclamatizationDates) {
		this.studyAcclamatizationDates = studyAcclamatizationDates;
	}
	public StudyTreatmentDataDates getStudyTreatmentDataDates() {
		return studyTreatmentDataDates;
	}
	public void setStudyTreatmentDataDates(StudyTreatmentDataDates studyTreatmentDataDates) {
		this.studyTreatmentDataDates = studyTreatmentDataDates;
	}
	
}
