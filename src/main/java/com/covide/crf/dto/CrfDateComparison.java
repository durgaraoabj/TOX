package com.covide.crf.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.springmvc.model.CommonMaster;

@Entity
@Table(name = "CRF_data_comparison")
public class CrfDateComparison extends CommonMaster implements Serializable {
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="CrfDateComparison_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long Id;
	private String name;
	@Column(columnDefinition="TEXT")
	private String descreption;
	@ManyToOne
	@JoinColumn(name="sourcrf")
	private Crf sourcrf;
	@ManyToOne
	@JoinColumn(name="crf_section_ele")
	private CrfSectionElement secEle;
	@ManyToOne
	@JoinColumn(name="crf_group_ele")
	private CrfGroupElement groupEle;
	private int rowNo;
	@Column(columnDefinition="TEXT")
	private String message = "";
	private String compare = "";
	private String status = "active";

	@ManyToOne
	@JoinColumn(name="crf1")
	private Crf crf1;
	@ManyToOne
	@JoinColumn(name="crf_section_ele1")
	private CrfSectionElement secEle1;
	@ManyToOne
	@JoinColumn(name="crf_group_ele1")
	private CrfGroupElement groupEle1;
	private int rowNo1;
	@ManyToOne
	@JoinColumn(name="crf2")
	private Crf crf2;
	@ManyToOne
	@JoinColumn(name="crf_section_ele2")
	private CrfSectionElement secEle2;
	@ManyToOne
	@JoinColumn(name="crf_group_ele2")
	private CrfGroupElement groupEle2;
	private int rowNo2;
	@ManyToOne
	@JoinColumn(name="crf3")
	private Crf crf3;
	@ManyToOne
	@JoinColumn(name="crf_section_ele3")
	private CrfSectionElement secEle3;
	@ManyToOne
	@JoinColumn(name="crf_group_ele3")
	private CrfGroupElement groupEle3;
	private int rowNo3;
	@ManyToOne
	@JoinColumn(name="crf4")
	private Crf crf4;
	@ManyToOne
	@JoinColumn(name="crf_section_ele4")
	private CrfSectionElement secEle4;
	@ManyToOne
	@JoinColumn(name="crf_group_ele4")
	private CrfGroupElement groupEle4;
	private int rowNo4;
	
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescreption() {
		return descreption;
	}
	public void setDescreption(String descreption) {
		this.descreption = descreption;
	}
	public Crf getSourcrf() {
		return sourcrf;
	}
	public void setSourcrf(Crf sourcrf) {
		this.sourcrf = sourcrf;
	}
	public CrfSectionElement getSecEle() {
		return secEle;
	}
	public void setSecEle(CrfSectionElement secEle) {
		this.secEle = secEle;
	}
	public CrfGroupElement getGroupEle() {
		return groupEle;
	}
	public void setGroupEle(CrfGroupElement groupEle) {
		this.groupEle = groupEle;
	}
	public int getRowNo() {
		return rowNo;
	}
	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCompare() {
		return compare;
	}
	public void setCompare(String compare) {
		this.compare = compare;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Crf getCrf1() {
		return crf1;
	}
	public void setCrf1(Crf crf1) {
		this.crf1 = crf1;
	}
	public CrfSectionElement getSecEle1() {
		return secEle1;
	}
	public void setSecEle1(CrfSectionElement secEle1) {
		this.secEle1 = secEle1;
	}
	public CrfGroupElement getGroupEle1() {
		return groupEle1;
	}
	public void setGroupEle1(CrfGroupElement groupEle1) {
		this.groupEle1 = groupEle1;
	}
	public int getRowNo1() {
		return rowNo1;
	}
	public void setRowNo1(int rowNo1) {
		this.rowNo1 = rowNo1;
	}
	public Crf getCrf2() {
		return crf2;
	}
	public void setCrf2(Crf crf2) {
		this.crf2 = crf2;
	}
	public CrfSectionElement getSecEle2() {
		return secEle2;
	}
	public void setSecEle2(CrfSectionElement secEle2) {
		this.secEle2 = secEle2;
	}
	public CrfGroupElement getGroupEle2() {
		return groupEle2;
	}
	public void setGroupEle2(CrfGroupElement groupEle2) {
		this.groupEle2 = groupEle2;
	}
	public int getRowNo2() {
		return rowNo2;
	}
	public void setRowNo2(int rowNo2) {
		this.rowNo2 = rowNo2;
	}
	public Crf getCrf3() {
		return crf3;
	}
	public void setCrf3(Crf crf3) {
		this.crf3 = crf3;
	}
	public CrfSectionElement getSecEle3() {
		return secEle3;
	}
	public void setSecEle3(CrfSectionElement secEle3) {
		this.secEle3 = secEle3;
	}
	public CrfGroupElement getGroupEle3() {
		return groupEle3;
	}
	public void setGroupEle3(CrfGroupElement groupEle3) {
		this.groupEle3 = groupEle3;
	}
	public int getRowNo3() {
		return rowNo3;
	}
	public void setRowNo3(int rowNo3) {
		this.rowNo3 = rowNo3;
	}
	public Crf getCrf4() {
		return crf4;
	}
	public void setCrf4(Crf crf4) {
		this.crf4 = crf4;
	}
	public CrfSectionElement getSecEle4() {
		return secEle4;
	}
	public void setSecEle4(CrfSectionElement secEle4) {
		this.secEle4 = secEle4;
	}
	public CrfGroupElement getGroupEle4() {
		return groupEle4;
	}
	public void setGroupEle4(CrfGroupElement groupEle4) {
		this.groupEle4 = groupEle4;
	}
	public int getRowNo4() {
		return rowNo4;
	}
	public void setRowNo4(int rowNo4) {
		this.rowNo4 = rowNo4;
	}

	
}
