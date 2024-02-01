package com.springmvc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="STUDY_MASTER")
public class StudyMaster extends CommonMaster implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3413482887346473703L;
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="StudyMaster_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	private Long id;
	@Column(unique=true, nullable=false)
	private String studyNo;
	@Column(nullable=false)
	private String studyDesc;
	private int noOfGroups;
	private String gender = "Male";
	private boolean splitStudyByGender;
	private int subjects = 0;
	private Date startDate;
	private String protocalNo;
	private String molecule;
	private String studyType;
	private boolean requiredSubGroup = true;
	private String profileIds = "";
	@ManyToOne
	@JoinColumn
	private StatusMaster globalStatus;
	private String experimentalDesign = "No Done";// Not Done, Done
	private String edDoneBy = "";
	private Date edDoneDate = null;
	private boolean crfConfiguation   = false;
	private boolean crfPeriodConfiguation   = false;
	private boolean volConfiguation   = false;
	@Column(name="sd_user")
	private Long sdUser;
	@Column(name="asd_user")
	private Long asdUser;
	@ManyToOne
	@JoinColumn(name="status")
	private StatusMaster status;
	private boolean intrumentConfuguraiton;
	private Long intrumentConfuguredBy;
	private Date intrumentConfuguredOn;
	@Transient
	private String statusCode;
	@Transient
	private boolean dirctore = false;
	
	
//	@Transient
	private String principalInvestigator = "";
	@Transient
	private String officialTitle = "";
	@Transient
	private String secondarIDs = "";
	@Transient
	private String protocolType = "";
	@Transient
	private String briefSummary = "";
	@Transient
	private String detailedDescription = "";
	@Transient
	private String sponsor = "";
	@Transient
	private String collaborators = "";
	@Transient
	private Long userId;
	
	
	
//	@OneToMany(mappedBy="studyMaster")
	@Transient
	private List<StudyAnalyteMaster> studyAnalytes = new ArrayList<>();
	@Transient
	private List<StudyPeriodMaster> studyPeriods = new ArrayList<>();
	
//	@ManyToOne
//	@JoinColumn(name="groupInfo_id")
	@Transient
	private List<GroupInfo> groupInfo = new ArrayList<>();
	
	
	@Column(name="sd_userlable")
	private String sdUserLable;
	@Column(name="asd_userLable")
	private String asdUserLable;
	
	/*@Column(name="sponsorname")
	private String sponsorname;*/
	@Column(name="metaDataBy")
	private String metaDataBy;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date metaDataOn;
	
	private boolean calculationBased;
	private Double doseVolume = 0d;
	private Double concentration = 0d;
	@ManyToOne
	@JoinColumn(name="weightUnits")
	private StaticData weightUnits;
	@ManyToOne
	@JoinColumn(name="radamizationStatus")
	private StatusMaster radamizationStatus;
	@Column(name="radamizationDoneBy")
	private String radamizationDoneBy;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date radamizationDate;
	
	private String testItemCode;
	@ManyToOne
	@JoinColumn(name="sponsorMasterId")
	private SponsorMaster sponsorMasterId;
	private String glpNonGlp;
	
	private String doseMgKg;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date acclimatizationStarDate;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date acclimatizationEndDate;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date treatmentStarDate;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date treatmentEndDate;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date acclimatizationStarDateFemale;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date acclimatizationEndDateFemale;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date treatmentStarDateFemale;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date treatmentEndDateFemale;
	
	private String acceptComments;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date acceptedDate;
	private String acceptStatus = "New"; //New,Accept,On-Hold,Change Required,Reject
	
	@ManyToOne
	@JoinColumn(name="speciesId")
	private Species species;
	private boolean clinpathPerameters;
	
	private String randamizationDoneNow;
	private boolean randamizattionStatus;
	private boolean maleRandamizationStatus;
	private boolean femaleRandamizationStatus;
	private boolean maleRandamizationReviewStatus;
	private boolean femaleRandamizationReviewStatus;
	
	
	private boolean cpAcclamatization = false;
	private boolean cpTreatment = false;

	
	private String studyStatus = "New Study"; // New Study/ Selected By Director/ In-Design/ In-Review/ Comments Required/ Reviewed/ In-Progress/ Frozen/ Locked/ Cancel
	private Date sentToReviewOn;
	private String sentToReviewComment;
	private String reviewedBy = "";
	private Date reviewedOn;
	private String reviewComment = "";
	@Column(name="study_approval_status")
	private boolean approvalStatus = false;
	
	

	public String getSentToReviewComment() {
		return sentToReviewComment;
	}


	public void setSentToReviewComment(String sentToReviewComment) {
		this.sentToReviewComment = sentToReviewComment;
	}


	public Date getSentToReviewOn() {
		return sentToReviewOn;
	}


	public void setSentToReviewOn(Date sentToReviewOn) {
		this.sentToReviewOn = sentToReviewOn;
	}


	public String getReviewedBy() {
		return reviewedBy;
	}


	public void setReviewedBy(String reviewedBy) {
		this.reviewedBy = reviewedBy;
	}


	public Date getReviewedOn() {
		return reviewedOn;
	}


	public void setReviewedOn(Date reviewedOn) {
		this.reviewedOn = reviewedOn;
	}


	public String getReviewComment() {
		return reviewComment;
	}


	public void setReviewComment(String reviewComment) {
		this.reviewComment = reviewComment;
	}


	public boolean isApprovalStatus() {
		return approvalStatus;
	}


	public void setApprovalStatus(boolean approvalStatus) {
		this.approvalStatus = approvalStatus;
	}


	public boolean isCpAcclamatization() {
		return cpAcclamatization;
	}


	public void setCpAcclamatization(boolean cpAcclamatization) {
		this.cpAcclamatization = cpAcclamatization;
	}


	public boolean isCpTreatment() {
		return cpTreatment;
	}


	public void setCpTreatment(boolean cpTreatment) {
		this.cpTreatment = cpTreatment;
	}


	public String getRandamizationDoneNow() {
		return randamizationDoneNow;
	}


	public void setRandamizationDoneNow(String randamizationDoneNow) {
		this.randamizationDoneNow = randamizationDoneNow;
	}


	public boolean isMaleRandamizationReviewStatus() {
		return maleRandamizationReviewStatus;
	}


	public void setMaleRandamizationReviewStatus(boolean maleRandamizationReviewStatus) {
		this.maleRandamizationReviewStatus = maleRandamizationReviewStatus;
	}


	public boolean isFemaleRandamizationReviewStatus() {
		return femaleRandamizationReviewStatus;
	}


	public void setFemaleRandamizationReviewStatus(boolean femaleRandamizationReviewStatus) {
		this.femaleRandamizationReviewStatus = femaleRandamizationReviewStatus;
	}


	public boolean isRandamizattionStatus() {
		return randamizattionStatus;
	}


	public void setRandamizattionStatus(boolean randamizattionStatus) {
		this.randamizattionStatus = randamizattionStatus;
	}


	public boolean isMaleRandamizationStatus() {
		return maleRandamizationStatus;
	}


	public void setMaleRandamizationStatus(boolean maleRandamizationStatus) {
		this.maleRandamizationStatus = maleRandamizationStatus;
	}


	public boolean isFemaleRandamizationStatus() {
		return femaleRandamizationStatus;
	}


	public void setFemaleRandamizationStatus(boolean femaleRandamizationStatus) {
		this.femaleRandamizationStatus = femaleRandamizationStatus;
	}


	public boolean isSplitStudyByGender() {
		return splitStudyByGender;
	}


	public void setSplitStudyByGender(boolean splitStudyByGender) {
		this.splitStudyByGender = splitStudyByGender;
	}


	public boolean isClinpathPerameters() {
		return clinpathPerameters;
	}


	public void setClinpathPerameters(boolean clinpathPerameters) {
		this.clinpathPerameters = clinpathPerameters;
	}


	public Species getSpecies() {
		return species;
	}


	public void setSpecies(Species species) {
		this.species = species;
	}


	public String getRadamizationDoneBy() {
		return radamizationDoneBy;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	public void setRadamizationDoneBy(String radamizationDoneBy) {
		this.radamizationDoneBy = radamizationDoneBy;
	}


	public Date getRadamizationDate() {
		return radamizationDate;
	}


	public void setRadamizationDate(Date radamizationDate) {
		this.radamizationDate = radamizationDate;
	}


	public StatusMaster getRadamizationStatus() {
		return radamizationStatus;
	}


	public void setRadamizationStatus(StatusMaster radamizationStatus) {
		this.radamizationStatus = radamizationStatus;
	}


	public Double getConcentration() {
		return concentration;
	}


	public void setConcentration(Double concentration) {
		this.concentration = concentration;
	}


	public void setWeightUnits(StaticData weightUnits) {
		this.weightUnits = weightUnits;
	}


	public boolean isCalculationBased() {
		return calculationBased;
	}


	public void setCalculationBased(boolean calculationBased) {
		this.calculationBased = calculationBased;
	}


	public Double getDoseVolume() {
		return doseVolume;
	}

	public void setDoseVolume(Double doseVolume) {
		this.doseVolume = doseVolume;
	}
	
	public StaticData getWeightUnits() {
		return weightUnits;
	}

	
	public boolean isRequiredSubGroup() {
		return requiredSubGroup;
	}

	public void setRequiredSubGroup(boolean requiredSubGroup) {
		this.requiredSubGroup = requiredSubGroup;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStudyNo() {
		return studyNo;
	}

	public void setStudyNo(String studyNo) {
		this.studyNo = studyNo;
	}

	public String getStudyDesc() {
		return studyDesc;
	}

	public void setStudyDesc(String studyDesc) {
		this.studyDesc = studyDesc;
	}

	public int getNoOfGroups() {
		return noOfGroups;
	}

	public StatusMaster getStatus() {
		return status;
	}

	public void setStatus(StatusMaster status) {
		this.status = status;
	}

	public void setNoOfGroups(int noOfGroups) {
		this.noOfGroups = noOfGroups;
	}

	public int getSubjects() {
		return subjects;
	}

	public void setSubjects(int subjects) {
		this.subjects = subjects;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getProtocalNo() {
		return protocalNo;
	}

	public void setProtocalNo(String protocalNo) {
		this.protocalNo = protocalNo;
	}

	public String getMolecule() {
		return molecule;
	}

	public void setMolecule(String molecule) {
		this.molecule = molecule;
	}

	public String getStudyType() {
		return studyType;
	}

	public void setStudyType(String studyType) {
		this.studyType = studyType;
	}

	public StatusMaster getGlobalStatus() {
		return globalStatus;
	}

	public void setGlobalStatus(StatusMaster globalStatus) {
		this.globalStatus = globalStatus;
	}

	public String getStudyStatus() {
		return studyStatus;
	}

	public void setStudyStatus(String studyStatus) {
		this.studyStatus = studyStatus;
	}

	public String getExperimentalDesign() {
		return experimentalDesign;
	}

	public void setExperimentalDesign(String experimentalDesign) {
		this.experimentalDesign = experimentalDesign;
	}

	public String getEdDoneBy() {
		return edDoneBy;
	}

	public void setEdDoneBy(String edDoneBy) {
		this.edDoneBy = edDoneBy;
	}

	public Date getEdDoneDate() {
		return edDoneDate;
	}

	public void setEdDoneDate(Date edDoneDate) {
		this.edDoneDate = edDoneDate;
	}

	public boolean isCrfConfiguation() {
		return crfConfiguation;
	}

	public void setCrfConfiguation(boolean crfConfiguation) {
		this.crfConfiguation = crfConfiguation;
	}

	public boolean isCrfPeriodConfiguation() {
		return crfPeriodConfiguation;
	}

	public void setCrfPeriodConfiguation(boolean crfPeriodConfiguation) {
		this.crfPeriodConfiguation = crfPeriodConfiguation;
	}

	public boolean isVolConfiguation() {
		return volConfiguation;
	}

	public void setVolConfiguation(boolean volConfiguation) {
		this.volConfiguation = volConfiguation;
	}

	public Long getSdUser() {
		return sdUser;
	}

	public void setSdUser(Long sdUser) {
		this.sdUser = sdUser;
	}

	public Long getAsdUser() {
		return asdUser;
	}

	public void setAsdUser(Long asdUser) {
		this.asdUser = asdUser;
	}

	

	public String getPrincipalInvestigator() {
		return principalInvestigator;
	}

	public void setPrincipalInvestigator(String principalInvestigator) {
		this.principalInvestigator = principalInvestigator;
	}

	public String getOfficialTitle() {
		return officialTitle;
	}

	public void setOfficialTitle(String officialTitle) {
		this.officialTitle = officialTitle;
	}

	public String getSecondarIDs() {
		return secondarIDs;
	}

	public void setSecondarIDs(String secondarIDs) {
		this.secondarIDs = secondarIDs;
	}

	public String getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}

	public String getBriefSummary() {
		return briefSummary;
	}

	public void setBriefSummary(String briefSummary) {
		this.briefSummary = briefSummary;
	}

	public String getDetailedDescription() {
		return detailedDescription;
	}

	public void setDetailedDescription(String detailedDescription) {
		this.detailedDescription = detailedDescription;
	}

	public String getSponsor() {
		return sponsor;
	}

	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}

	public String getCollaborators() {
		return collaborators;
	}

	public void setCollaborators(String collaborators) {
		this.collaborators = collaborators;
	}

	public List<StudyAnalyteMaster> getStudyAnalytes() {
		return studyAnalytes;
	}

	public void setStudyAnalytes(List<StudyAnalyteMaster> studyAnalytes) {
		this.studyAnalytes = studyAnalytes;
	}

	public List<StudyPeriodMaster> getStudyPeriods() {
		return studyPeriods;
	}

	public void setStudyPeriods(List<StudyPeriodMaster> studyPeriods) {
		this.studyPeriods = studyPeriods;
	}

	public List<GroupInfo> getGroupInfo() {
		return groupInfo;
	}

	public void setGroupInfo(List<GroupInfo> groupInfo) {
		this.groupInfo = groupInfo;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getSdUserLable() {
		return sdUserLable;
	}

	public void setSdUserLable(String sdUserLable) {
		this.sdUserLable = sdUserLable;
	}

	public String getAsdUserLable() {
		return asdUserLable;
	}

	public void setAsdUserLable(String asdUserLable) {
		this.asdUserLable = asdUserLable;
	}


	public String getMetaDataBy() {
		return metaDataBy;
	}

	public void setMetaDataBy(String metaDataBy) {
		this.metaDataBy = metaDataBy;
	}

	public Date getMetaDataOn() {
		return metaDataOn;
	}

	public void setMetaDataOn(Date metaDataOn) {
		this.metaDataOn = metaDataOn;
	}


	public String getStatusCode() {
		return statusCode;
	}


	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}


	public boolean isDirctore() {
		return dirctore;
	}


	public void setDirctore(boolean dirctore) {
		this.dirctore = dirctore;
	}


	public String getProfileIds() {
		return profileIds;
	}


	public void setProfileIds(String profileIds) {
		this.profileIds = profileIds;
	}


	public boolean isIntrumentConfuguraiton() {
		return intrumentConfuguraiton;
	}


	public void setIntrumentConfuguraiton(boolean intrumentConfuguraiton) {
		this.intrumentConfuguraiton = intrumentConfuguraiton;
	}



	public Long getIntrumentConfuguredBy() {
		return intrumentConfuguredBy;
	}


	public void setIntrumentConfuguredBy(Long intrumentConfuguredBy) {
		this.intrumentConfuguredBy = intrumentConfuguredBy;
	}


	public Date getIntrumentConfuguredOn() {
		return intrumentConfuguredOn;
	}


	public void setIntrumentConfuguredOn(Date intrumentConfuguredOn) {
		this.intrumentConfuguredOn = intrumentConfuguredOn;
	}


	public String getTestItemCode() {
		return testItemCode;
	}


	public void setTestItemCode(String testItemCode) {
		this.testItemCode = testItemCode;
	}


	public SponsorMaster getSponsorMasterId() {
		return sponsorMasterId;
	}


	public void setSponsorMasterId(SponsorMaster sponsorMasterId) {
		this.sponsorMasterId = sponsorMasterId;
	}


	public String getGlpNonGlp() {
		return glpNonGlp;
	}


	public void setGlpNonGlp(String glpNonGlp) {
		this.glpNonGlp = glpNonGlp;
	}


	public String getDoseMgKg() {
		return doseMgKg;
	}


	public void setDoseMgKg(String doseMgKg) {
		this.doseMgKg = doseMgKg;
	}


	public Date getAcclimatizationStarDate() {
		return acclimatizationStarDate;
	}


	public void setAcclimatizationStarDate(Date acclimatizationStarDate) {
		this.acclimatizationStarDate = acclimatizationStarDate;
	}


	public Date getAcclimatizationEndDate() {
		return acclimatizationEndDate;
	}


	public void setAcclimatizationEndDate(Date acclimatizationEndDate) {
		this.acclimatizationEndDate = acclimatizationEndDate;
	}


	public Date getTreatmentStarDate() {
		return treatmentStarDate;
	}


	public void setTreatmentStarDate(Date treatmentStarDate) {
		this.treatmentStarDate = treatmentStarDate;
	}


	public Date getTreatmentEndDate() {
		return treatmentEndDate;
	}


	public void setTreatmentEndDate(Date treatmentEndDate) {
		this.treatmentEndDate = treatmentEndDate;
	}


	public String getAcceptComments() {
		return acceptComments;
	}


	public void setAcceptComments(String acceptComments) {
		this.acceptComments = acceptComments;
	}


	public Date getAcceptedDate() {
		return acceptedDate;
	}


	public void setAcceptedDate(Date acceptedDate) {
		this.acceptedDate = acceptedDate;
	}


	public String getAcceptStatus() {
		return acceptStatus;
	}


	public void setAcceptStatus(String acceptStatus) {
		this.acceptStatus = acceptStatus;
	}


	public Date getAcclimatizationStarDateFemale() {
		return acclimatizationStarDateFemale;
	}


	public void setAcclimatizationStarDateFemale(Date acclimatizationStarDateFemale) {
		this.acclimatizationStarDateFemale = acclimatizationStarDateFemale;
	}


	public Date getAcclimatizationEndDateFemale() {
		return acclimatizationEndDateFemale;
	}


	public void setAcclimatizationEndDateFemale(Date acclimatizationEndDateFemale) {
		this.acclimatizationEndDateFemale = acclimatizationEndDateFemale;
	}


	public Date getTreatmentStarDateFemale() {
		return treatmentStarDateFemale;
	}


	public void setTreatmentStarDateFemale(Date treatmentStarDateFemale) {
		this.treatmentStarDateFemale = treatmentStarDateFemale;
	}


	public Date getTreatmentEndDateFemale() {
		return treatmentEndDateFemale;
	}


	public void setTreatmentEndDateFemale(Date treatmentEndDateFemale) {
		this.treatmentEndDateFemale = treatmentEndDateFemale;
	}
	  
	
	
}
