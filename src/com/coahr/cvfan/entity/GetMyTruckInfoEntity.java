package com.coahr.cvfan.entity;

public class GetMyTruckInfoEntity {
    private String statementId;
    private MyTruckInfoParameterEntity parameters;
    
    public String getStatementId() {
        return statementId;
    }
    public void setStatementId(String statementId) {
        this.statementId = statementId;
    }
    public MyTruckInfoParameterEntity getParameters() {
        return parameters;
    }
    public void setParameters(MyTruckInfoParameterEntity parameters) {
        this.parameters = parameters;
    }
    
}
