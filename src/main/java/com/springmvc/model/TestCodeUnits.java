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

@Entity
@Table(name = "TEST_CODE_UNITS")
public class TestCodeUnits extends CommonMaster  implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="TestCodeUnits_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
//	@Column(unique=true, nullable=false)
	private String instumentUnit;
	private String displayUnit;
	private boolean activeStatus = true;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getInstumentUnit() {
		return instumentUnit;
	}
	public void setInstumentUnit(String instumentUnit) {
		this.instumentUnit = instumentUnit;
	}
	
	public String getDisplayUnit() {
		return displayUnit;
	}
	public void setDisplayUnit(String displayUnit) {
		this.displayUnit = displayUnit;
	}
	public boolean isActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(boolean activeStatus) {
		this.activeStatus = activeStatus;
	}
	
	@Override
	public String toString() {
		return "TestCodeUnits [id=" + id + ", instumentUnit=" + instumentUnit + ", displayUnit=" + displayUnit
				+ ", activeStatus=" + activeStatus + "]";
	}
	
}
