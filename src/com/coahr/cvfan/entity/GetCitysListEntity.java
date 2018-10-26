package com.coahr.cvfan.entity;

public class GetCitysListEntity {
    private String statementId;
    private CitysRequestData parameters;

    public String getStatementId() {
        return statementId;
    }

    public void setStatementId(String statementId) {
        this.statementId = statementId;
    }

	public CitysRequestData getParameters() {
		return parameters;
	}

	public void setParameters(CitysRequestData parameters) {
		this.parameters = parameters;
	}
    
    
}
