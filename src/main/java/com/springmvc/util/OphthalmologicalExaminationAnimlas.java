package com.springmvc.util;

import com.springmvc.model.StudyAnimal;

public class OphthalmologicalExaminationAnimlas implements Comparable<OphthalmologicalExaminationAnimlas> {
	private String type = "";
	private int sno;
	private StudyAnimal animal;
	private String leftEye = "";
	private String RightEye = "";
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getSno() {
		return sno;
	}
	public void setSno(int sno) {
		this.sno = sno;
	}
	public StudyAnimal getAnimal() {
		return animal;
	}
	public void setAnimal(StudyAnimal animal) {
		this.animal = animal;
	}
	public String getLeftEye() {
		return leftEye;
	}
	public void setLeftEye(String leftEye) {
		this.leftEye = leftEye;
	}
	public String getRightEye() {
		return RightEye;
	}
	public void setRightEye(String rightEye) {
		RightEye = rightEye;
	}
	
	@Override
	public int compareTo(OphthalmologicalExaminationAnimlas o) {
		// TODO Auto-generated method stub
		if(type.equals("Treatment"))
			return this.getAnimal().getAnimalId() - o.getAnimal().getAnimalId();
		else
			return this.getAnimal().getSequnceNo() - o.getAnimal().getSequnceNo();
	}	
}
