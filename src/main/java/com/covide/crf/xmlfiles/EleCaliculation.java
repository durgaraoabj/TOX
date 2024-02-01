package com.covide.crf.xmlfiles;

public class EleCaliculation {
	
	private String resultField;
    private String rule;
	public String getResultField() {
		return resultField;
	}
	public void setResultField(String resultField) {
		this.resultField = resultField;
	}
	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	@Override
	public String toString() {
		return "EleCaliculation [resultField=" + resultField + ", rule=" + rule + "]";
	}
    
	
}
