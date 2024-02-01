package com.covide.crf.dto;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.springmvc.model.CommonMaster;
import com.springmvc.model.SubGroupAnimalsInfoAll;

@Entity
@Table(name = "CRF_SECTION_ELEMENT_WeightData_log")
public class CrfSectionElementWeightDataLog  extends CommonMaster implements Serializable{
	
	/**
	 * 
	 */
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="CrfSectionElementWeightDataLog_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name="section_element")
	private CrfSectionElement sectionElement;
	@ManyToOne
	@JoinColumn(name="animal_Info_All")
	private SubGroupAnimalsInfoAll animalInfoAll;
	private String userName = "";
	private String weight = "";
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public SubGroupAnimalsInfoAll getAnimalInfoAll() {
		return animalInfoAll;
	}
	public void setAnimalInfoAll(SubGroupAnimalsInfoAll animalInfoAll) {
		this.animalInfoAll = animalInfoAll;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public CrfSectionElement getSectionElement() {
		return sectionElement;
	}
	public void setSectionElement(CrfSectionElement sectionElement) {
		this.sectionElement = sectionElement;
	}
	
}
