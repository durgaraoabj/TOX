package com.covide.crf.dto;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.springmvc.model.CommonMaster;
import com.springmvc.model.VolunteerPeriodCrf;
@Entity
@Table(name = "Crf_Group_Data_Rows")
public class CrfGroupDataRows extends CommonMaster implements Serializable{
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="CrfGroupDataRows_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name="vol_period_crf")
	private VolunteerPeriodCrf volPeriodCrf;
	@ManyToOne
	@JoinColumn(name="crf_group")
	private CrfGroup group;
	private int noOfRows;  // data contained  current crf
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public VolunteerPeriodCrf getVolPeriodCrf() {
		return volPeriodCrf;
	}
	public void setVolPeriodCrf(VolunteerPeriodCrf volPeriodCrf) {
		this.volPeriodCrf = volPeriodCrf;
	}
	
	public CrfGroup getGroup() {
		return group;
	}
	public void setGroup(CrfGroup group) {
		this.group = group;
	}
	public int getNoOfRows() {
		return noOfRows;
	}
	public void setNoOfRows(int noOfRows) {
		this.noOfRows = noOfRows;
	}
	
	
}
