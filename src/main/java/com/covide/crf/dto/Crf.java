package com.covide.crf.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import com.covide.crf.service.CrfExcelSheet;
import com.springmvc.model.CommonMaster;
import com.springmvc.model.ObservationRole;
import com.springmvc.model.ObservationTemplateData;

@Entity
@Table(name = "CRF")
public class Crf extends CommonMaster implements Serializable{
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long Id;
	private String prefix = "";
	private String observationName;
	private String observationDesc;
	@Column(columnDefinition = "TEXT")
	private String name = "";
	private String title = "";
	private String type = ""; //
	private String subType = ""; 
	private String gender = ""; // MALE FEMALE
	private String version = "";
	@Column(name = "is_active")
	private boolean active = true;// status
	@OneToMany(cascade = CascadeType.PERSIST, mappedBy = "crf")
	private List<CrfSection> sections;
	private DataSet d;
	@Transient
	private String days = "";
	private String dayType = "";
	@ManyToOne
	@JoinColumn(name="templete_id")
	private ObservationTemplateData templete;
	@Column(name="crf_code")
	private String crfCode;
	@Column(name="configuration_For")
	private String configurationFor; //Acclimatization Or Treatmentor Bouth
	@Transient
	private List<ObservationRole> observationRoles = new ArrayList<>();
	
	
	public Crf(String prefix, String observationName, String type, String subType, String configurationFor) {
		super();
		this.prefix = prefix;
		this.observationName = observationName;
		this.type = type;
		this.subType = subType;
		this.configurationFor = configurationFor;
	}

	public Crf() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public List<ObservationRole> getObservationRoles() {
		return observationRoles;
	}

	public void setObservationRoles(List<ObservationRole> observationRoles) {
		this.observationRoles = observationRoles;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getDayType() {
		return dayType;
	}

	public void setDayType(String dayType) {
		this.dayType = dayType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<CrfSection> getSections() {
		return sections;
	}

	public void setSections(List<CrfSection> sections) {
		this.sections = sections;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Transient
	private MultipartFile file;

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	@Transient
	private String message = "";
	@Transient
	private boolean flag;
	@Transient
	private boolean configure = false;
	@Transient
	private CrfExcelSheet exclData;

	public CrfExcelSheet getExclData() {
		return exclData;
	}

	public void setExclData(CrfExcelSheet exclData) {
		this.exclData = exclData;
	}

	public boolean isConfigure() {
		return configure;
	}

	public void setConfigure(boolean configure) {
		this.configure = configure;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public DataSet getD() {
		return d;
	}

	public void setD(DataSet d) {
		this.d = d;
	}

	public String getObservationName() {
		return observationName;
	}

	public void setObservationName(String observationName) {
		this.observationName = observationName;
	}

	public String getObservationDesc() {
		return observationDesc;
	}

	public void setObservationDesc(String observationDesc) {
		this.observationDesc = observationDesc;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public ObservationTemplateData getTemplete() {
		return templete;
	}

	public void setTemplete(ObservationTemplateData templete) {
		this.templete = templete;
	}

	public String getCrfCode() {
		return crfCode;
	}

	public void setCrfCode(String crfCode) {
		this.crfCode = crfCode;
	}

	public String getConfigurationFor() {
		return configurationFor;
	}

	public void setConfigurationFor(String configurationFor) {
		this.configurationFor = configurationFor;
	}
	
	
	
}
