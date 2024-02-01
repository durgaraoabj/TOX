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
@Table(name = "CRF_GROUP_ELEMENT")
public class CrfGroupElement extends CommonMaster implements Serializable{
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="CrfGroupElement_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@Column(name="ele_name")
	private String name;
	@Column(name="ele_title", columnDefinition="TEXT")
	private String title;
	@Column(name="ele_rowNo")
	private int rowNo;
	@Column(name="ele_columnNo")
	private int columnNo;
	@Column(name="ele_type")
	private String type= "";// text/textArea/radio/select/checkBox/date/dateAndTime/non
	private String dataType = "String"; // Number/String
	private String responceType;
	@Column(name="ele_display")
	private String display="horizantal";// vertical
	@Column(name="ele_values")
	private String values;
	@Column(name="ele_pattren")
	private String pattren;
	@Column(name="ele_required")
	private boolean required;
	@Transient
	private Crf crf;
	@Transient
	private CrfSection section;
	@ManyToOne
	@JoinColumn(name="crf_group")
	private CrfGroup group;
	@OneToMany(cascade=CascadeType.PERSIST, mappedBy="groupElement")
	private List<CrfGroupElementValue> elementValues;
	@Column(name="formula")
	private String formula;
	
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
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public void setRequired(boolean required) {
		this.required = required;
	}
	public CrfSection getSection() {
		return section;
	}
	public void setSection(CrfSection section) {
		this.section = section;
	}
	public CrfGroup getGroup() {
		return group;
	}
	public void setGroup(CrfGroup group) {
		this.group = group;
	}
	public List<CrfGroupElementValue> getElementValues() {
		return elementValues;
	}
	public void setElementValues(List<CrfGroupElementValue> elementValues) {
		this.elementValues = elementValues;
	}
	public Crf getCrf() {
		return crf;
	}
	public void setCrf(Crf crf) {
		this.crf = crf;
	}
	@Override
	public String toString() {
		return "CrfGroupElement [name=" + name + ", title=" + title + ", rowNo=" + rowNo + ", columnNo=" + columnNo
				+ ", type=" + type + ", responceType=" + responceType + ", display=" + display + ", values=" + values
				+ ", pattren=" + pattren + ", required=" + required + ", crf=" + crf + ", section=" + section
				+ ", group=" + group + ", elementValues=" + elementValues + "]";
	}
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	} 
	
	
}
