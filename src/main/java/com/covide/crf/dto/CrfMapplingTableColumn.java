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
@Table(name="CRF_Mapping_Table_Column")
public class CrfMapplingTableColumn extends CommonMaster implements Serializable{
	/**
	 * 
	 */
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="CrfMapplingTableColumn_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@ManyToOne
	@JoinColumn(name="crf_mapping_table_id")
	private CrfMapplingTable mappingTable;
	private String cloumnName = "";
	private String cloumnType = "";

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
	public String getCloumnName() {
		return cloumnName;
	}
	public void setCloumnName(String cloumnName) {
		this.cloumnName = cloumnName;
	}
	public String getCloumnType() {
		return cloumnType;
	}
	public void setCloumnType(String cloumnType) {
		this.cloumnType = cloumnType;
	}
	
}
