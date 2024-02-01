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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.springmvc.model.CommonMaster;

@Entity
@Table(name = "CRF_GROUP")
public class CrfGroup extends CommonMaster implements Serializable{
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="CrfGroup_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@Column(columnDefinition="TEXT")
	private String name = "";
	@Column(columnDefinition="TEXT")
	private String title = "";
	@Column(columnDefinition="TEXT")
	private String hedding = "";
	@Column(columnDefinition="TEXT")
	private String subHedding = "";
	private int minRows;
	private int maxRows;
	private int maxColumns;
	private int sectionId;
	@Transient
	private CrfSection section;
	@Transient
	private int displayedRows;
	@Transient
	private Crf crf;
	@OneToMany(cascade=CascadeType.PERSIST, mappedBy="group")
	private List<CrfGroupElement> element;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	public int getSectionId() {
		return sectionId;
	}

	public void setSectionId(int sectionId) {
		this.sectionId = sectionId;
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
	public List<CrfGroupElement> getElement() {
		return element;
	}
	public void setElement(List<CrfGroupElement> element) {
		this.element = element;
	}
	public String getHedding() {
		return hedding;
	}
	public void setHedding(String hedding) {
		this.hedding = hedding;
	}
	public String getSubHedding() {
		return subHedding;
	}
	public void setSubHedding(String subHedding) {
		this.subHedding = subHedding;
	}
	public int getMinRows() {
		return minRows;
	}
	public void setMinRows(int minRows) {
		this.minRows = minRows;
	}
	public int getMaxRows() {
		return maxRows;
	}
	public void setMaxRows(int maxRows) {
		this.maxRows = maxRows;
	}
	public int getMaxColumns() {
		return maxColumns;
	}
	public void setMaxColumns(int maxColumns) {
		this.maxColumns = maxColumns;
	}
	public int getDisplayedRows() {
		return displayedRows;
	}
	public void setDisplayedRows(int displayedRows) {
		this.displayedRows = displayedRows;
	}
	public Crf getCrf() {
		return crf;
	}
	public void setCrf(Crf crf) {
		this.crf = crf;
	}
	@Override
	public String toString() {
		return "CrfGroup [name=" + name + ", title=" + title + ", hedding=" + hedding + ", subHedding=" + subHedding
				+ ", minRows=" + minRows + ", maxRows=" + maxRows + ", maxColumns=" + maxColumns + ", displayedRows="
				+ displayedRows + ", crf=" + crf + ", element=" + element + "]";
	}
	public CrfSection getSection() {
		return section;
	}
	public void setSection(CrfSection section) {
		this.section = section;
	}
	
}
