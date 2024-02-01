package com.covide.crf.dto;

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

import com.springmvc.model.CommonMaster;
import com.springmvc.model.VolunteerPeriodCrf;

@Entity
@Table(name = "Crf_Group_Element_Data")
public class CrfGroupElementData extends CommonMaster implements Serializable{
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="CrfGroupElementData_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name="vol_period_crf")
	private VolunteerPeriodCrf volPeriodCrf;
	@ManyToOne
	@JoinColumn(name="crf_Group_element")
	private CrfGroupElement element;
	@ManyToOne
	@JoinColumn(name="study_gorup")
	CrfGroup group;
	@Column(name="key_name")
	private String kayName;
	@Column(name="ele_value")
	private String value;
	private int dataRows;
	
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
	public CrfGroupElement getElement() {
		return element;
	}
	public void setElement(CrfGroupElement element) {
		this.element = element;
	}
	public CrfGroup getGroup() {
		return group;
	}
	public void setGroup(CrfGroup group) {
		this.group = group;
	}
	public String getKayName() {
		return kayName;
	}
	public void setKayName(String kayName) {
		this.kayName = kayName;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getDataRows() {
		return dataRows;
	}
	public void setDataRows(int dataRows) {
		this.dataRows = dataRows;
	}
	
	
}
