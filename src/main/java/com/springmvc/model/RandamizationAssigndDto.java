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
@Table(name="RandamizationAssigndDto")
public class RandamizationAssigndDto extends CommonMaster implements Comparable<RandamizationAssigndDto>{

	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="RandamizationAssigndDto_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name="randamizationDtoId")
	private RandamizationDto randamizationDto;
	@ManyToOne
	@JoinColumn(name="animalId")
	private StudyAnimal animal;
	@Column(name="animalOrderNumber")
	private int order;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public RandamizationDto getRandamizationDto() {
		return randamizationDto;
	}
	public void setRandamizationDto(RandamizationDto randamizationDto) {
		this.randamizationDto = randamizationDto;
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
	public int compareTo(RandamizationAssigndDto arg0) {
		// TODO Auto-generated method stub
		return this.order - arg0.getOrder();
	}
	
	
}
