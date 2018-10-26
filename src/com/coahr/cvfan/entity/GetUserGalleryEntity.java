package com.coahr.cvfan.entity;

public class GetUserGalleryEntity {
    private String statementId;
    private RequestGalleryEntity parameters;
    public String getStatementId() {
        return statementId;
    }
    public void setStatementId(String statementId) {
        this.statementId = statementId;
    }
    public RequestGalleryEntity getParameters() {
        return parameters;
    }
    public void setParameters(RequestGalleryEntity parameters) {
        this.parameters = parameters;
    }
    
    
}
