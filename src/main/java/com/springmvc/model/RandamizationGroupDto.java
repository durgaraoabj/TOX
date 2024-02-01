package com.springmvc.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity
@Table(name="RandamizationGroupDto")
public class RandamizationGroupDto  extends CommonMaster implements Comparable<RandamizationGroupDto>{

	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="RandamizationGroupDto_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name="randamizationDtoId")
	private RandamizationDto randamizationDto;
	@ManyToOne
	@JoinColumn(name="subGroupInfoId")
	private SubGroupInfo subGroupInfo;
	private int subGroupAnimalCount = 0;
	private Double mean;
	private Double sd;
	@Transient
	private List<RandamizationGroupAnimalDto> randamizationGroupAnimalDto;
	
	public int getSubGroupAnimalCount() {
		return subGroupAnimalCount;
	}
	public void setSubGroupAnimalCount(int subGroupAnimalCount) {
		this.subGroupAnimalCount = subGroupAnimalCount;
	}
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
	public SubGroupInfo getSubGroupInfo() {
		return subGroupInfo;
	}
	public void setSubGroupInfo(SubGroupInfo subGroupInfo) {
		this.subGroupInfo = subGroupInfo;
	}
	public Double getMean() {
		return mean;
	}
	public void setMean(Double mean) {
		this.mean = mean;
	}
	public Double getSd() {
		return sd;
	}
	public void setSd(Double sd) {
		this.sd = sd;
	}
	
	public List<RandamizationGroupAnimalDto> getRandamizationGroupAnimalDto() {
		return randamizationGroupAnimalDto;
	}
	public void setRandamizationGroupAnimalDto(List<RandamizationGroupAnimalDto> randamizationGroupAnimalDto) {
		this.randamizationGroupAnimalDto = randamizationGroupAnimalDto;
	}
	@Override
	public int compareTo(RandamizationGroupDto arg0) {
		// TODO Auto-generated method stub
		return this.subGroupInfo.getSubGroupNo() - arg0.getSubGroupInfo().getSubGroupNo();
	}
	
}
