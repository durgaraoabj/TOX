package com.covide.crf.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.springmvc.model.CommonMaster;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.SubGroupAnimalsInfoAll;
@Entity
@Table(name = "Crf_Section_Element_Instrument_Value")
public class CrfSectionElementInstrumentValue extends CommonMaster implements Serializable{
	/**
	 * 
	 */
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="CrfSectionElementInstrumentValue_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	private boolean weightStatus = true;
	@ManyToOne
	@JoinColumn(name="animalId")
	private StudyAnimal animal;
//	@ManyToOne
//	@JoinColumn(name="subGroupAnimalsInfoAll_id")
//	private SubGroupAnimalsInfoAll animal;
	@ManyToOne
	@JoinColumn(name="crf_id")
	private Crf crf;
	@ManyToOne
	@JoinColumn(name="sec_element_id")
	private CrfSectionElement element;
	@ManyToOne
	@JoinColumn(name="study_id")
	private StudyMaster studyid;
	private String weight = "";
	@Column(name="user_name")
	private String userName = "";
	@Column(name="ip_address")
	private String ipAddress = "";
	@Column(name="mac_address")
	private String macAddress = "";
	
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
	public boolean isWeightStatus() {
		return weightStatus;
	}
	public void setWeightStatus(boolean weightStatus) {
		this.weightStatus = weightStatus;
	}
	
	public StudyAnimal getAnimal() {
		return animal;
	}
	public void setAnimal(StudyAnimal animal) {
		this.animal = animal;
	}
	public Crf getCrf() {
		return crf;
	}
	public void setCrf(Crf crf) {
		this.crf = crf;
	}
	public CrfSectionElement getElement() {
		return element;
	}
	public void setElement(CrfSectionElement element) {
		this.element = element;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public StudyMaster getStudyid() {
		return studyid;
	}
	public void setStudyid(StudyMaster studyid) {
		this.studyid = studyid;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	
}
