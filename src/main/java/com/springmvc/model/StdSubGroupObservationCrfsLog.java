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

import com.covide.crf.dto.Crf;

@Entity
@Table(name="Std_SubGroup_ObservationCrfs_Log")
public class StdSubGroupObservationCrfsLog extends CommonMaster implements Serializable {

	/**
	 * 
	 */
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="StdSubGroupObservationCrfsLog_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	private String days = "";
	private String dayType = "Day";
	private String windowSign;
	private int window;
	@Column(name = "is_active")	
	private boolean active = true;//status
	private boolean reviewStatus;
	@ManyToOne
	@JoinColumn(name="deptId")
	private DepartmentMaster deptId;
	@ManyToOne
	@JoinColumn(name="ssgoc_id")
	private StdSubGroupObservationCrfs ssgoc;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	public String getDayType() {
		return dayType;
	}
	public void setDayType(String dayType) {
		this.dayType = dayType;
	}
	public String getWindowSign() {
		return windowSign;
	}
	public void setWindowSign(String windowSign) {
		this.windowSign = windowSign;
	}
	public int getWindow() {
		return window;
	}
	public void setWindow(int window) {
		this.window = window;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public boolean isReviewStatus() {
		return reviewStatus;
	}
	public void setReviewStatus(boolean reviewStatus) {
		this.reviewStatus = reviewStatus;
	}
	public DepartmentMaster getDeptId() {
		return deptId;
	}
	public void setDeptId(DepartmentMaster deptId) {
		this.deptId = deptId;
	}
	public StdSubGroupObservationCrfs getSsgoc() {
		return ssgoc;
	}
	public void setSsgoc(StdSubGroupObservationCrfs ssgoc) {
		this.ssgoc = ssgoc;
	}
		
}
