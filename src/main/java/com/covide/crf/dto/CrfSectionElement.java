package com.covide.crf.dto;

import java.io.Serializable;
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

import com.springmvc.model.CommonMaster;

@Entity
@Table(name = "CRF_SECTION_ELEMENT")
public class CrfSectionElement extends CommonMaster implements Serializable{
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="CrfSectionElement_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@Column(columnDefinition="TEXT")
	private String name = "";
	@Column(columnDefinition="TEXT")
	private String leftDesc = "";
	@Column(columnDefinition="TEXT")
	private String rigtDesc = "";
	@Column(columnDefinition="TEXT")
	private String middeDesc = "";
	@Column(columnDefinition="TEXT")
	private String totalDesc = ""; //  left+middle+right
	@Column(columnDefinition="TEXT")
	private String topDesc = "";
	@Column(columnDefinition="TEXT")
	private String bottemDesc = "";
	private int rowNo;
	private int columnNo;
	@Column(name="ele_type")
	private String type= "";// text/textArea/radio/select/checkBox/date/dateAndTime/non/selectTable
	private String dataType = "String"; // Number/String
	private String responceType = "";
	@Column(name="ele_display")
	private String display="horizantal";// vertical
	@Column(name="ele_values")
	private String values = "";
	private String pattren = "";
	private boolean required;
	private String typeOfTime = "";
	@Transient
	private Crf crf;
	@OneToMany(cascade=CascadeType.PERSIST, mappedBy="sectionElement")
	private List<CrfSectionElementValue> elementValues; 
	@ManyToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name="section")
	private CrfSection section;
	@Column(name="formula")
	private String formula;
	private String clinicalCode = "";

	
	public String getClinicalCode() {
		return clinicalCode;
	}
	public void setClinicalCode(String clinicalCode) {
		this.clinicalCode = clinicalCode;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLeftDesc() {
		return leftDesc;
	}
	public void setLeftDesc(String leftDesc) {
		this.leftDesc = leftDesc;
	}
	public String getRigtDesc() {
		return rigtDesc;
	}
	public void setRigtDesc(String rigtDesc) {
		this.rigtDesc = rigtDesc;
	}
	public String getMiddeDesc() {
		return middeDesc;
	}
	public void setMiddeDesc(String middeDesc) {
		this.middeDesc = middeDesc;
	}
	public String getTotalDesc() {
		return totalDesc;
	}
	public void setTotalDesc(String totalDesc) {
		this.totalDesc = totalDesc;
	}
	public String getTopDesc() {
		return topDesc;
	}
	public void setTopDesc(String topDesc) {
		this.topDesc = topDesc;
	}
	public String getBottemDesc() {
		return bottemDesc;
	}
	public void setBottemDesc(String bottemDesc) {
		this.bottemDesc = bottemDesc;
	}
	public int getRowNo() {
		return rowNo;
	}
	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}
	public int getColumnNo() {
		return columnNo;
	}
	public void setColumnNo(int columnNo) {
		this.columnNo = columnNo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getResponceType() {
		return responceType;
	}
	public void setResponceType(String responceType) {
		this.responceType = responceType;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public String getValues() {
		return values;
	}
	public void setValues(String values) {
		this.values = values;
	}
	public String getPattren() {
		return pattren;
	}
	public void setPattren(String pattren) {
		this.pattren = pattren;
	}
	public boolean isRequired() {
		return required;
	}
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public List<CrfSectionElementValue> getElementValues() {
		return elementValues;
	}
	public void setElementValues(List<CrfSectionElementValue> elementValues) {
		this.elementValues = elementValues;
	}
	public CrfSection getSection() {
		return section;
	}
	public void setSection(CrfSection section) {
		this.section = section;
	}
	public Crf getCrf() {
		return crf;
	}
	public void setCrf(Crf crf) {
		this.crf = crf;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getTypeOfTime() {
		return typeOfTime;
	}
	public void setTypeOfTime(String typeOfTime) {
		this.typeOfTime = typeOfTime;
	}
	
	
}
