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
@Entity
@Table(name = "CRF_GROUP_ELEMENT_VALUE")
public class CrfGroupElementValue extends CommonMaster implements Serializable{
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="CrfGroupElementValue_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	private String value = "";
	@ManyToOne
	@JoinColumn(name="groupElement")
	private CrfGroupElement groupElement;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public CrfGroupElement getGroupElement() {
		return groupElement;
	}
	public void setGroupElement(CrfGroupElement groupElement) {
		this.groupElement = groupElement;
	}
	
}
