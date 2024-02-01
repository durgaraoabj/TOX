package com.covide.crf.dto;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
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
import com.springmvc.model.StudyPeriodMaster;

@Entity
@Table(name = "dataset_phase")
public class DataSetPhase extends CommonMaster implements Serializable{
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="DataSetPhase_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name="dataSetId")
	private DataSet dataSetId;
//	@ManyToOne
//	@JoinColumn(name="phase")
	@Transient
	private StudyPeriodMaster phase;
	
	@OneToMany(cascade=CascadeType.PERSIST,  mappedBy="phasesId")
	private List<DataSetPhasewiseCrfs> phases;
	
	public DataSet getDataSetId() {
		return dataSetId;
	}
	public void setDataSetId(DataSet dataSetId) {
		this.dataSetId = dataSetId;
	}
	public List<DataSetPhasewiseCrfs> getPhases() {
		return phases;
	}
	public void setPhases(List<DataSetPhasewiseCrfs> phases) {
		this.phases = phases;
	}
	
	public StudyPeriodMaster getPhase() {
		return phase;
	}
	public void setPhase(StudyPeriodMaster phase) {
		this.phase = phase;
	}

	
}
