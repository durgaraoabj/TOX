package com.springmvc.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="randomization_details")
public class RandomizationDetails extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="RandomizationDetails_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name="study_id")
	private StudyMaster studyId;
	@ManyToOne
	@JoinColumn(name="group_id")
	private GroupInfo groupId;
	@ManyToOne
	@JoinColumn(name="sub_gropid")
	private SubGroupInfo subgroupId;
	@ManyToOne
	@JoinColumn(name="sub_animal_info")
	private SubGroupAnimalsInfo subanimalInfo;
	@Column(name="temp_id")
	private String animalTempId;
	@Column(name="permanent_id")
	private String permanentId;
	@Column(name="mean")
	private double mean;
	@Column(name="standard_deviation")
	private double standeredDeviation;
	@Column(name="weight")
	private double weight;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public StudyMaster getStudyId() {
		return studyId;
	}
	public void setStudyId(StudyMaster studyId) {
		this.studyId = studyId;
	}
	public GroupInfo getGroupId() {
		return groupId;
	}
	public void setGroupId(GroupInfo groupId) {
		this.groupId = groupId;
	}
	public String getPermanentId() {
		return permanentId;
	}
	public void setPermanentId(String permanentId) {
		this.permanentId = permanentId;
	}
	public double getMean() {
		return mean;
	}
	public void setMean(double mean) {
		this.mean = mean;
	}
	public double getStanderedDeviation() {
		return standeredDeviation;
	}
	public void setStanderedDeviation(double standeredDeviation) {
		this.standeredDeviation = standeredDeviation;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	
	public SubGroupInfo getSubgroupId() {
		return subgroupId;
	}
	public void setSubgroupId(SubGroupInfo subgroupId) {
		this.subgroupId = subgroupId;
	}
	public SubGroupAnimalsInfo getSubanimalInfo() {
		return subanimalInfo;
	}
	public void setSubanimalInfo(SubGroupAnimalsInfo subanimalInfo) {
		this.subanimalInfo = subanimalInfo;
	}
	public String getAnimalTempId() {
		return animalTempId;
	}
	public void setAnimalTempId(String animalTempId) {
		this.animalTempId = animalTempId;
	}
	
	

}
