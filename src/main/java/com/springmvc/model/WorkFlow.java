package com.springmvc.model;

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

@Entity
@Table(name = "work_flow")
public class WorkFlow extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="WorkFlow_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@Column(name="work_flow_name")
	private String name = "";
	@Column(name="work_flow_code")
	private String workFlowCode;
	@ManyToOne
	@JoinColumn(name="activeStatus")
	private StatusMaster activeStatus;
	
	
	
	public void setId(Long id) {
		this.id = id;
	}
	public WorkFlow(String workFlowCode, String name,  StatusMaster activeStatus) {
		super();
		this.name = name;
		this.workFlowCode = workFlowCode;
		this.activeStatus = activeStatus;
	}
	public WorkFlow() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getWorkFlowCode() {
		return workFlowCode;
	}
	public void setWorkFlowCode(String workFlowCode) {
		this.workFlowCode = workFlowCode;
	}
	public StatusMaster getActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(StatusMaster activeStatus) {
		this.activeStatus = activeStatus;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
