package com.covide.crf.dto;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.stereotype.Component;

import com.springmvc.model.CommonMaster;
@Component
@Entity
@Table(name="crf_item_value")
public class CRFItemValues extends CommonMaster implements Serializable{
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="CRFItemValues_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	private String elemenstValue;
	@ManyToOne
	@JoinColumn(name="crf_item_id")
	private CrfItems crfItem;
	
	public CRFItemValues() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CRFItemValues(String elemenstValue, CrfItems crfItem) {
		super();
		this.elemenstValue = elemenstValue;
		this.crfItem = crfItem;
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
	
	public CrfItems getCrfItem() {
		return crfItem;
	}
	public void setCrfItem(CrfItems crfItem) {
		this.crfItem = crfItem;
	}
	
}