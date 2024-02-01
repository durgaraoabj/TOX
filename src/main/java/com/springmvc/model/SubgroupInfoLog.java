package com.springmvc.model;

import java.io.Serializable;
import java.util.Date;

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

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="sub_group_info_log")
public class SubgroupInfoLog extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="SubgroupInfoLog_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="study_id")
	private StudyMaster study;
	@ManyToOne
	@JoinColumn(name="group_info_id")
	private GroupInfo group;
	private String name;
	private String dose;
	private String conc;
	@Column(name="status")
	private String status;
	@ManyToOne
	@JoinColumn(name="sgi_id")
	private SubGroupInfo sgi;
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
	public GroupInfo getGroup() {
		return group;
	}
	public void setGroup(GroupInfo group) {
		this.group = group;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDose() {
		return dose;
	}
	public void setDose(String dose) {
		this.dose = dose;
	}
	public String getConc() {
		return conc;
	}
	public void setConc(String conc) {
		this.conc = conc;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public SubGroupInfo getSgi() {
		return sgi;
	}
	public void setSgi(SubGroupInfo sgi) {
		this.sgi = sgi;
	}
	
	
	

}
