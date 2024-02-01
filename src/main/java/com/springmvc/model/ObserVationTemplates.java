package com.springmvc.model;

import java.io.Serializable;
import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="observation_templates")
public class ObserVationTemplates extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="ObserVationTemplates_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@Column(name="file")
	private Blob blob;
	@ManyToOne
	@JoinColumn(name="obstd")
	private ObservationTemplateData obstd;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Blob getBlob() {
		return blob;
	}
	public void setBlob(Blob blob) {
		this.blob = blob;
	}
	public ObservationTemplateData getObstd() {
		return obstd;
	}
	public void setObstd(ObservationTemplateData obstd) {
		this.obstd = obstd;
	}
	
	

}
