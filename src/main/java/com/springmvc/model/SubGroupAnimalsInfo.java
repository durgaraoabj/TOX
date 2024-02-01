package com.springmvc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.covide.crf.dto.Crf;

@Entity
@Table(name="Sub_Group_Animals_Info")
public class SubGroupAnimalsInfo extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="SubGroupAnimalsInfo_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="study_id")
	private StudyMaster study;
	@ManyToOne()
	@JoinColumn(name="group_info_id")
	private GroupInfo group;
	@ManyToOne()
	@JoinColumn(name="sub_group_info_id")
	private SubGroupInfo subGroup;
	private int sequenceNo;
	@Column(name="no_of_animals")
	private int count;
	private String gender = "";
	@Column(name="no_of_animals_from")
	private String from = "";
	@Column(name="no_of_animals_to")
	private String to = "";
	private String formId = "";
	private String toId = "";
	private Double doseVolume = 0d;
	private Double concentration = 0d;
	@Transient
	List<Integer> numbers = new ArrayList<>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<Integer> getNumbers() {
		return numbers;
	}
	public void setNumbers(List<Integer> numbers) {
		this.numbers = numbers;
	}
	@Transient
	private List<Crf> observation= new ArrayList<>();
	
	@Transient
	private boolean dataEntry= false;;
	
	public Double getDoseVolume() {
		return doseVolume;
	}
	public void setDoseVolume(Double doseVolume) {
		this.doseVolume = doseVolume;
	}
	public Double getConcentration() {
		return concentration;
	}
	public void setConcentration(Double concentration) {
		this.concentration = concentration;
	}
	public boolean isDataEntry() {
		return dataEntry;
	}
	public void setDataEntry(boolean dataEntry) {
		this.dataEntry = dataEntry;
	}
	public int getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(int sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	public GroupInfo getGroup() {
		return group;
	}
	public void setGroup(GroupInfo group) {
		this.group = group;
	}
	public SubGroupInfo getSubGroup() {
		return subGroup;
	}
	public void setSubGroup(SubGroupInfo subGroup) {
		this.subGroup = subGroup;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	public String getToId() {
		return toId;
	}
	public void setToId(String toId) {
		this.toId = toId;
	}
	public StudyMaster getStudy() {
		return study;
	}
	public void setStudy(StudyMaster study) {
		this.study = study;
	}
	public List<Crf> getObservation() {
		return observation;
	}
	public void setObservation(List<Crf> observation) {
		this.observation = observation;
	}
	
	
	
}
