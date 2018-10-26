package com.coahr.cvfan.entity;

public class GetSalesPromotionAndDisscountInfoEntity {
    private String statementId;
    private SalesPromotionAndDisscountData parameters;
    
	public String getStatementId() {
		return statementId;
	}
	public void setStatementId(String statementId) {
		this.statementId = statementId;
	}
	public SalesPromotionAndDisscountData getParameters() {
		return parameters;
	}
	public void setParameters(SalesPromotionAndDisscountData parameters) {
		this.parameters = parameters;
	}
}
