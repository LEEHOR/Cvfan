package com.coahr.cvfan.entity;

public class GetStationListEntity {
    private String statementId;
    private ParameterEntity parameters;
    
    public String getEntity() {
        return statementId;
    }
    public void setEntity(String statementId) {
        this.statementId = statementId;
    }
    
    public ParameterEntity getParameter() {
        return parameters;
    }
    public void setParameter(ParameterEntity parameters) {
        this.parameters = parameters;
    }
    
    
}
