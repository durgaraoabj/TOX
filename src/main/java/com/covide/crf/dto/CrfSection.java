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
@Table(name = "CRF_SECTION")
public class CrfSection extends CommonMaster  implements Comparable<CrfSection>, Serializable  {
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="CrfSection_id_seq", allocationSize=1)
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
	private int maxRows;
	private int maxColumns;
	@Column(name="sec_order")
	private int order;
	private boolean containsGroup;
	@OneToOne(cascade=CascadeType.ALL)
	private CrfGroup group;
	@OneToMany(cascade=CascadeType.PERSIST, mappedBy="section")
	private List<CrfSectionElement> element;
	private String gender = ""; //MALE FEMALE 
	@Column(name = "is_active")	
	private boolean active = true;//status
	private String roles = "";
	@ManyToOne
	@JoinColumn(name="crf")
	private Crf crf;
	
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
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public boolean isContainsGroup() {
		return containsGroup;
	}
	public void setContainsGroup(boolean containsGroup) {
		this.containsGroup = containsGroup;
	}
	public CrfGroup getGroup() {
		return group;
	}
	public void setGroup(CrfGroup group) {
		this.group = group;
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
	public String getRoles() {
		return roles;
	}
	public void setRoles(String roles) {
		this.roles = roles;
	}
	public Crf getCrf() {
		return crf;
	}
	public void setCrf(Crf crf) {
		this.crf = crf;
	}
	public List<CrfSectionElement> getElement() {
		return element;
	}
	public void setElement(List<CrfSectionElement> element) {
		this.element = element;
	}
	@Override
	public int compareTo(CrfSection o) {
		return ((Integer)this.getOrder()).compareTo((Integer)o.getOrder());
	}
	
	@Transient
	private boolean allowDataEntry = true;
	public boolean isAllowDataEntry() {
		return allowDataEntry;
	}
	public void setAllowDataEntry(boolean allowDataEntry) {
		this.allowDataEntry = allowDataEntry;
	}
}
