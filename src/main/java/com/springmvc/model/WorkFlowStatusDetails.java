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
@Table(name="work_flow_status_details")
public class WorkFlowStatusDetails extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="WorkFlowStatusDetails_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name="from_status")
	private StatusMaster fromStatus;
	@ManyToOne
	@JoinColumn(name="to_status")
	private StatusMaster toStatus;
	@Column(name="module")
	private String module="";
	@ManyToOne
	@JoinColumn(name="role")
	private RoleMaster role;
	@Column(name="action")
	private String action;
	
	public Long getId() {
		return id;
	}
	public StatusMaster getFromStatus() {
		return fromStatus;
	}
	public StatusMaster getToStatus() {
		return toStatus;
	}
	public String getModule() {
		return module;
	}
	public RoleMaster getRole() {
		return role;
	}
	public String getAction() {
		return action;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setFromStatus(StatusMaster fromStatus) {
		this.fromStatus = fromStatus;
	}
	public void setToStatus(StatusMaster toStatus) {
		this.toStatus = toStatus;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public void setRole(RoleMaster role) {
		this.role = role;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	
	
	
	

}
