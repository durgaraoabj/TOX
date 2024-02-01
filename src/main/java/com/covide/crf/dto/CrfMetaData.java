package com.covide.crf.dto;



import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.springmvc.model.CommonMaster;

@Component
@Entity
@Table(name="crf_meta_data")
public class CrfMetaData extends CommonMaster implements Serializable {

	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="CrfMetaData_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	private String code;
	private String crfName = "";
	private String crfDesc = "";
	private String type = "";// All,Screening,Clinical,Bio-Analysis,Tox
	private String gender = "";
	@Column(name = "is_active")	
	private boolean active = true;//status
	private int version  = 1;
	private int orderNo = 0;
	private String stringCellValue;
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="section_id")
	private List<CRFSections> sections;
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name = "group_id")
	private List<CRFGroupItem> groups;
	
	@Transient
	private boolean configure = false;
	
	public void setId(Long id) {
		this.id = id;
	}

	public boolean isConfigure() {
		return configure;
	}

	public void setConfigure(boolean configure) {
		this.configure = configure;
	}

	public String getCrfName() {
		return crfName;
	}

	public void setCrfName(String crfName) {
		this.crfName = crfName;
	}

	public String getCrfDesc() {
		return crfDesc;
	}

	public void setCrfDesc(String crfDesc) {
		this.crfDesc = crfDesc;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}


	@Transient
	private MultipartFile file;
			
	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}


	// default constructor
	public CrfMetaData() {
		
		this.code = "PRD" + UUID.randomUUID().toString().substring(26).toUpperCase();
		
	}
	
	
	
	public Long getId() {
		return id;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}

	public String getStringCellValue() {
		return stringCellValue;
	}

	public void setStringCellValue(String stringCellValue) {
		this.stringCellValue = stringCellValue;
	}

	
	
	public List<CRFSections> getSections() {
		return sections;
	}
	public void setSections(List<CRFSections> sections) {
		this.sections = sections;
	}
	
	public List<CRFGroupItem> getGroups() {
		return groups;
	}
	public void setGroups(List<CRFGroupItem> groups) {
		this.groups = groups;
	}

	// toString for debugging
	@Override
	public String toString() {
		return "CrfMetaData [id=" + id + ", code=" + code + ", crfName=" + crfName + ", crfDesc=" + crfDesc + ", type="
				+ type + ", gender=" + gender + ", active=" + active + ", version=" + version + ", orderNo=" + orderNo
				+ ", stringCellValue=" + stringCellValue + ", sections=" + sections + ", groups=" + groups + ", file="
				+ file + "]";
	}
	
	
	
}
