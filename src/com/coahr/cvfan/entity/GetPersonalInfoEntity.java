package com.coahr.cvfan.entity;

public class GetPersonalInfoEntity {
    private String statementId;
    private PersonalInfoDataEntity parameters;
	public String getStatementId() {
		return statementId;
	}
	public void setStatementId(String statementId) {
		this.statementId = statementId;
	}
	public PersonalInfoDataEntity getParameters() {
		return parameters;
	}
	public void setParameters(PersonalInfoDataEntity parameters) {
		this.parameters = parameters;
	}
    
}
