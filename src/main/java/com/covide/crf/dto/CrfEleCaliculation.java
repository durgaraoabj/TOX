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
@Table(name = "Crf_Ele_Caliculation")
public class CrfEleCaliculation extends CommonMaster implements Serializable{
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="CrfEleCaliculation_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name="crf")
	private Crf crf;
	private String resultField;
    private String caliculation;
    private String status = "active";
    
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Crf getCrf() {
		return crf;
	}
	public void setCrf(Crf crf) {
		this.crf = crf;
	}
	public String getResultField() {
		return resultField;
	}
	public void setResultField(String resultField) {
		this.resultField = resultField;
	}
	public String getCaliculation() {
		return caliculation;
	}
	public void setCaliculation(String caliculation) {
		this.caliculation = caliculation;
	}
	public CrfEleCaliculation(Crf crf, String resultField, String caliculation, String status) {
		super();
		this.crf = crf;
		this.resultField = resultField;
		this.caliculation = caliculation;
		this.status = status;
	}
	public CrfEleCaliculation() {
		super();
		// TODO Auto-generated constructor stub
	}

	
}
