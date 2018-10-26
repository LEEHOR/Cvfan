package com.coahr.cvfan.entity;

public class GetTruckTypeEntity {
    private String statementId;
    private BrandID parameters;

    public String getStatementId() {
        return statementId;
    }

    public void setStatementId(String statementId) {
        this.statementId = statementId;
    }

	public BrandID getParameters() {
		return parameters;
	}

	public void setParameters(BrandID parameters) {
		this.parameters = parameters;
	}
    
    
    
}
