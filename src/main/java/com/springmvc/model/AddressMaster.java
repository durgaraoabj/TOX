package com.springmvc.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ADDRESS_MASTER")
public class AddressMaster extends CommonMaster implements Serializable {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="AddressMaster_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@Column(nullable = false)
	private String presentStreet1 ="";
	private String presentStreet2 ="";
	private String presentCity ="";
	private String presentState ="";
	private String presentCountry ="";
	private String presentZipCode ="";	
	private boolean bothSame = false;	
	private String permanentStreet1 ="";
	private String permanentStreet2 ="";
	private String permanentCity ="";
	private String permanentState ="";
	private String permanentCountry ="";
	private String permanentZipCode ="";
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPresentStreet1() {
		return presentStreet1;
	}
	public void setPresentStreet1(String presentStreet1) {
		this.presentStreet1 = presentStreet1;
	}
	public String getPresentStreet2() {
		return presentStreet2;
	}
	public void setPresentStreet2(String presentStreet2) {
		this.presentStreet2 = presentStreet2;
	}
	public String getPresentCity() {
		return presentCity;
	}
	public void setPresentCity(String presentCity) {
		this.presentCity = presentCity;
	}
	public String getPresentState() {
		return presentState;
	}
	public void setPresentState(String presentState) {
		this.presentState = presentState;
	}
	public String getPresentCountry() {
		return presentCountry;
	}
	public void setPresentCountry(String presentCountry) {
		this.presentCountry = presentCountry;
	}
	public String getPresentZipCode() {
		return presentZipCode;
	}
	public void setPresentZipCode(String presentZipCode) {
		this.presentZipCode = presentZipCode;
	}
	public boolean isBothSame() {
		return bothSame;
	}
	public void setBothSame(boolean bothSame) {
		this.bothSame = bothSame;
	}
	public String getPermanentStreet1() {
		return permanentStreet1;
	}
	public void setPermanentStreet1(String permanentStreet1) {
		this.permanentStreet1 = permanentStreet1;
	}
	public String getPermanentStreet2() {
		return permanentStreet2;
	}
	public void setPermanentStreet2(String permanentStreet2) {
		this.permanentStreet2 = permanentStreet2;
	}
	public String getPermanentCity() {
		return permanentCity;
	}
	public void setPermanentCity(String permanentCity) {
		this.permanentCity = permanentCity;
	}
	public String getPermanentState() {
		return permanentState;
	}
	public void setPermanentState(String permanentState) {
		this.permanentState = permanentState;
	}
	public String getPermanentCountry() {
		return permanentCountry;
	}
	public void setPermanentCountry(String permanentCountry) {
		this.permanentCountry = permanentCountry;
	}
	public String getPermanentZipCode() {
		return permanentZipCode;
	}
	public void setPermanentZipCode(String permanentZipCode) {
		this.permanentZipCode = permanentZipCode;
	}
	
}
