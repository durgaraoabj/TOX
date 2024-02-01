package com.springmvc.util;

public class CEvents {
	 public int id;
	    public String title = "";
	    public String start = "";
	    public String end = "";
	    public String color = "";
	    public String url = "";
	    public String divId = "";
	    public String groupId = "";
	    public String subGroupId = "";
	    public String gender = "Both";	// Both/Male/Female
	    public String navigation = "Yes"; //	Yes/No
	    public String crfId = "";
	    public String studyId = "";
	    public String action = "";	// Acclamatization/Treatment
	    public int windowPre;
	    public int windowPost;
	    public Long primaryKey;
	    public Long studyAcclamatizationDatesId;
	    public Long studyTreatmentDataDatesId;
	    public String schduleType = "scheduled";  // unscheduled/scheduled
	    public boolean clinPaht = false;
	    
	    
		public boolean isClinPaht() {
			return clinPaht;
		}
		public void setClinPaht(boolean clinPaht) {
			this.clinPaht = clinPaht;
		}
		public String getSchduleType() {
			return schduleType;
		}
		public void setSchduleType(String schduleType) {
			this.schduleType = schduleType;
		}
		public Long getStudyTreatmentDataDatesId() {
			return studyTreatmentDataDatesId;
		}
		public void setStudyTreatmentDataDatesId(Long studyTreatmentDataDatesId) {
			this.studyTreatmentDataDatesId = studyTreatmentDataDatesId;
		}
		public Long getStudyAcclamatizationDatesId() {
				return studyAcclamatizationDatesId;
			}
			public void setStudyAcclamatizationDatesId(Long studyAcclamatizationDatesId) {
				this.studyAcclamatizationDatesId = studyAcclamatizationDatesId;
			}
	public Long getPrimaryKey() {
			return primaryKey;
		}
		public void setPrimaryKey(Long primaryKey) {
			this.primaryKey = primaryKey;
		}
	public int getWindowPost() {
			return windowPost;
		}
		public void setWindowPost(int windowPost) {
			this.windowPost = windowPost;
		}
	public int getWindowPre() {
			return windowPre;
		}
		public void setWindowPre(int windowPre) {
			this.windowPre = windowPre;
		}
	public String getSubGroupId() {
			return subGroupId;
		}
		public void setSubGroupId(String subGroupId) {
			this.subGroupId = subGroupId;
		}
	public String getGroupId() {
			return groupId;
		}
		public void setGroupId(String groupId) {
			this.groupId = groupId;
		}
	public String getCrfId() {
			return crfId;
		}
		public void setCrfId(String crfId) {
			this.crfId = crfId;
		}
		public String getStudyId() {
			return studyId;
		}
		public void setStudyId(String studyId) {
			this.studyId = studyId;
		}
		public String getAction() {
			return action;
		}
		public void setAction(String action) {
			this.action = action;
		}
	public String getDivId() {
			return divId;
		}
		public void setDivId(String divId) {
			this.divId = divId;
		}
	public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
//	public String getEnd() {
//		return end;
//	}
//	public void setEnd(String end) {
//		this.end = end;
//	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public CEvents(int id) {
		super();
		this.id = id;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getNavigation() {
		return navigation;
	}
	public void setNavigation(String navigation) {
		this.navigation = navigation;
	}
	
}
