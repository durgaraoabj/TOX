package com.springmvc.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="species")
public class Species extends CommonMaster implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -185029460382613591L;
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="Species_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@Column(name="animal_code")
	private String code;
	@Column(name="animal_name")
	private String name;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Species() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Species(String code, String name) {
		super();
		this.code = code;
		this.name = name;
	}
	
}
