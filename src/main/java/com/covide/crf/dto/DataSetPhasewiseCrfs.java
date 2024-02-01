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
@Table(name = "dataset_Phasewise_Crfs")
public class DataSetPhasewiseCrfs  extends CommonMaster implements Serializable{
	
	private static final long serialVersionUID = 5944799587711782448L;
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="DataSetPhasewiseCrfs_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name="phaseId")
	private DataSetPhase phasesId;
	@ManyToOne
	@JoinColumn(name="crf")
	private Crf crf;
	
	@Column(name="secEleIds", length=10000)
	private String secEleIds;
	@Column(name="groupEleIds", length=10000)
	private String groupEleIds;
	public DataSetPhase getPhasesId() {
		return phasesId;
	}
	public void setPhasesId(DataSetPhase phasesId) {
		this.phasesId = phasesId;
	}
	
	public Crf getCrf() {
		return crf;
	}
	public void setCrf(Crf crf) {
		this.crf = crf;
	}
	public String getSecEleIds() {
		return secEleIds;
	}
	public void setSecEleIds(String secEleIds) {
		this.secEleIds = secEleIds;
	}
	public String getGroupEleIds() {
		return groupEleIds;
	}
	public void setGroupEleIds(String groupEleIds) {
		this.groupEleIds = groupEleIds;
	}
	
	
}
