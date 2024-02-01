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
@Table(name = "work_flow_Review_Stages")
public class WorkFlowReviewStages extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="WorkFlowReviewStages_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name = "fromRole_id")
	private RoleMaster fromRole;
	@ManyToOne
	@JoinColumn(name = "toRole_id")
	private RoleMaster toRole;
	@ManyToOne
	@JoinColumn(name = "workFlow_id")
	private WorkFlow workFlow;
	@Column(name="action_perform")
	private String action = "";
	@Column(name="active_status")
	private boolean activeStatus = true;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public WorkFlowReviewStages() {
		super();
		// TODO Auto-generated constructor stub
	}
	public WorkFlowReviewStages(RoleMaster fromRole, RoleMaster toRole, WorkFlow workFlow, String action,
			boolean activeStatus) {
		super();
		this.fromRole = fromRole;
		this.toRole = toRole;
		this.workFlow = workFlow;
		this.action = action;
		this.activeStatus = activeStatus;
	}
	public RoleMaster getFromRole() {
		return fromRole;
	}
	public void setFromRole(RoleMaster fromRole) {
		this.fromRole = fromRole;
	}
	public RoleMaster getToRole() {
		return toRole;
	}
	public void setToRole(RoleMaster toRole) {
		this.toRole = toRole;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public WorkFlow getWorkFlow() {
		return workFlow;
	}
	public void setWorkFlow(WorkFlow workFlow) {
		this.workFlow = workFlow;
	}
	public boolean isActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(boolean activeStatus) {
		this.activeStatus = activeStatus;
	}
	
}
