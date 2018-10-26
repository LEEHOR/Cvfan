package com.coahr.cvfan.entity;

public class ModifyPasswordEntity {
    private String userId;
    private String oldPwd;
    private String newPwd;
    private String code;
    
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getOldPwd() {
        return oldPwd;
    }
    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }
    public String getNewPwd() {
        return newPwd;
    }
    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    
    
}
