package com.springmvc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="RandamizationGroupAnimalDto")
public class RandamizationGroupAnimalDto extends CommonMaster implements Comparable<RandamizationGroupAnimalDto>{

	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="RandamizationGroupAnimalDto_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name="randamizationGroupDtoId")
	private RandamizationGroupDto randamizationGroupDto;
	@ManyToOne
	@JoinColumn(name="animalId")
	private StudyAnimal animal;
	private String animalNo;
	@Column(name="animalOrderNumber")
	private int order;
	@ManyToOne
	@JoinColumn(name="subGroupId")
	private SubGroupInfo subGroup;
	
	
	public String getAnimalNo() {
		return animalNo;
	}
	public void setAnimalNo(String animalNo) {
		this.animalNo = animalNo;
	}
	public SubGroupInfo getSubGroup() {
		return subGroup;
	}
	public void setSubGroup(SubGroupInfo subGroup) {
		this.subGroup = subGroup;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public RandamizationGroupDto getRandamizationGroupDto() {
		return randamizationGroupDto;
	}
	public void setRandamizationGroupDto(RandamizationGroupDto randamizationGroupDto) {
		this.randamizationGroupDto = randamizationGroupDto;
	}
	public StudyAnimal getAnimal() {
		return animal;
	}
	public void setAnimal(StudyAnimal animal) {
		this.animal = animal;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	@Override
	public int compareTo(RandamizationGroupAnimalDto arg0) {
		// TODO Auto-generated method stub
		return this.order - arg0.getOrder();
	}

}
