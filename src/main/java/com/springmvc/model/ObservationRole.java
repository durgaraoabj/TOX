package com.springmvc.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.covide.crf.dto.Crf;
@Entity
@Table(name = "Observation_Role")
public class ObservationRole extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="ObservationRole_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name="crfId")
	private Crf crf;
	@ManyToOne
	@JoinColumn(name="roleMasterId")
	private RoleMaster roleMaster;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Crf getCrf() {
		return crf;
	}
	public void setCrf(Crf crf) {
		this.crf = crf;
	}
	public RoleMaster getRoleMaster() {
		return roleMaster;
	}
	public void setRoleMaster(RoleMaster roleMaster) {
		this.roleMaster = roleMaster;
	}
	public ObservationRole(Crf crf, RoleMaster roleMaster, String usrName) {
		super();
		this.crf = crf;
		this.roleMaster = roleMaster;
		this.setCreatedBy(usrName);
	}
	public ObservationRole() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
