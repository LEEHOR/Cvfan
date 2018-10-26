package com.coahr.cvfan.entity;

public class MaintainPaperDetailRequestEntity {
    private String statementId;
    private MaintainPaperDetailData parameters;
    
    public String getEntity() {
        return statementId;
    }
    public void setEntity(String statementId) {
        this.statementId = statementId;
    }
    public MaintainPaperDetailData getParameters() {
        return parameters;
    }
    public void setParameters(MaintainPaperDetailData parameters) {
        this.parameters = parameters;
    }
    
}
