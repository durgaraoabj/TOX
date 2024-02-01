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

import org.springframework.stereotype.Component;

import com.springmvc.model.CommonMaster;

@Component
@Entity
@Table(name="crf_item_value_std")
public class CRFItemValuesStd extends CommonMaster implements Serializable{
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="CRFItemValuesStd_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id; 
	@ManyToOne
	private CRFItemValues crfItemValueId;
	private String elemenstValue;
	@ManyToOne
	@JoinColumn(name="crf_item_id")
	private CrfItemsStd crfItem;
	
	public CRFItemValuesStd() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CRFItemValuesStd(String elemenstValue, CrfItemsStd crfItem) {
		super();
		this.elemenstValue = elemenstValue;
		this.crfItem = crfItem;
	}
	
	
	public CRFItemValues getCrfItemValueId() {
		return crfItemValueId;
	}
	public void setCrfItemValueId(CRFItemValues crfItemValueId) {
		this.crfItemValueId = crfItemValueId;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getElemenstValue() {
		return elemenstValue;
	}
	public void setElemenstValue(String elemenstValue) {
		this.elemenstValue = elemenstValue;
	}
	
	public CrfItemsStd getCrfItem() {
		return crfItem;
	}
	public void setCrfItem(CrfItemsStd crfItem) {
		this.crfItem = crfItem;
	}
}
