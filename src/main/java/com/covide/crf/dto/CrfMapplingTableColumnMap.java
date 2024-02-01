package com.covide.crf.dto;

import java.io.Serializable;

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
@Table(name="CRF_Mapping_Table_column_map")
public class CrfMapplingTableColumnMap extends CommonMaster implements Serializable{
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="CrfMapplingTableColumnMap_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name="crf_id")
	private Crf crf;
	@ManyToOne
	@JoinColumn(name="crfSectionElement_id")
	private CrfSectionElement element;
	@ManyToOne
	@JoinColumn(name="crfMapplingTable_id")
	private CrfMapplingTable mappingTable;
	@ManyToOne
	@JoinColumn(name="crfMapplingTableColumn_id")
	private CrfMapplingTableColumn mappingColumn;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public CrfMapplingTable getMappingTable() {
		return mappingTable;
	}
	public void setMappingTable(CrfMapplingTable mappingTable) {
		this.mappingTable = mappingTable;
	}
	public CrfMapplingTableColumn getMappingColumn() {
		return mappingColumn;
	}
	public void setMappingColumn(CrfMapplingTableColumn mappingColumn) {
		this.mappingColumn = mappingColumn;
	}
	public Crf getCrf() {
		return crf;
	}
	public void setCrf(Crf crf) {
		this.crf = crf;
	}
	public CrfSectionElement getElement() {
		return element;
	}
	public void setElement(CrfSectionElement element) {
		this.element = element;
	}
	
}
