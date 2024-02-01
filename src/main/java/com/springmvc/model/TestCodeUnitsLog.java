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

@Entity
@Table(name="TEST_CODE_UNITS_LOG")
public class TestCodeUnitsLog extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="TestCodeUnitsLog_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name="testCodeUntisId")
	private TestCodeUnits testCodeUnits;
	private String instumentUnit;
	private String displayUnit;
	private boolean activeStatus = true;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public TestCodeUnits getTestCodeUnits() {
		return testCodeUnits;
	}
	public void setTestCodeUnits(TestCodeUnits testCodeUnits) {
		this.testCodeUnits = testCodeUnits;
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
	
	
}
