package com.coahr.cvfan.entity;

public class GetStationCommentListEntity {
    private String statementId;
    private GetStationCommentListData parameters;
    
    public String getStatementId() {
        return statementId;
    }
    public void setStatementId(String statementId) {
        this.statementId = statementId;
    }
    public GetStationCommentListData getParameters() {
        return parameters;
    }
    public void setParameters(GetStationCommentListData parameters) {
        this.parameters = parameters;
    }
    
}
