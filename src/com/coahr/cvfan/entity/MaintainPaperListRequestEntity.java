package com.coahr.cvfan.entity;

public class MaintainPaperListRequestEntity {
    private String statementId;
    private MaintainPaperData parameters;
    
    public String getEntity() {
        return statementId;
    }
    public void setEntity(String statementId) {
        this.statementId = statementId;
    }
    public MaintainPaperData getParameters() {
        return parameters;
    }
    public void setParameters(MaintainPaperData parameters) {
        this.parameters = parameters;
    }
    
}
