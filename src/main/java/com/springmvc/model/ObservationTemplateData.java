package com.springmvc.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="observation_template_data")
public class ObservationTemplateData extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="ObservationTemplateData_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@Column(name="obser_name")
	private String obserVationName;
	@Column(name="obser_desc")
	private String obserVationDesc;
	@Column(name="fileName")
	private String fileName;
	@Column(name = "type")
	private String type;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getObserVationName() {
		return obserVationName;
	}
	public void setObserVationName(String obserVationName) {
		this.obserVationName = obserVationName;
	}
	public String getObserVationDesc() {
		return obserVationDesc;
	}
	public void setObserVationDesc(String obserVationDesc) {
		this.obserVationDesc = obserVationDesc;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
	
	

}
