package com.springmvc.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="USER_WISE_ASIGN_Sites")
public class UserWiseSitesAsignMaster extends CommonMaster implements Comparable<UserWiseStudiesAsignMaster>, Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="USER_WISE_ASIGN_Sites_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn
	private StudyMaster studyMaster;
	@ManyToOne
	@JoinColumn
	private StudySite site;
	@ManyToOne
	@JoinColumn
	private LoginUsers userId;
	private Character status='T';
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public StudySite getSite() {
		return site;
	}
	public void setSite(StudySite site) {
		this.site = site;
	}
	public StudyMaster getStudyMaster() {
		return studyMaster;
	}
	public void setStudyMaster(StudyMaster studyMaster) {
		this.studyMaster = studyMaster;
	}
	public LoginUsers getUserId() {
		return userId;
	}
	public void setUserId(LoginUsers userId) {
		this.userId = userId;
	}
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
	}
	@Override
	public int compareTo(UserWiseStudiesAsignMaster o) {
		// TODO Auto-generated method stub
		return (int) (this.getId()-o.getId());
	}
	
}
