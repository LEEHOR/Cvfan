package com.coahr.cvfan.entity;

public class GetStationCommentListData {
    private String stationId;
    private String iDisplayStart;
    private String iDisplayLength;
    
    public String getStationId() {
        return stationId;
    }
    public void setStationId(String stationId) {
        this.stationId = stationId;
    }
    public String getiDisplayStart() {
        return iDisplayStart;
    }
    public void setiDisplayStart(String iDisplayStart) {
        this.iDisplayStart = iDisplayStart;
    }
    public String getiDisplayLength() {
        return iDisplayLength;
    }
    public void setiDisplayLength(String iDisplayLength) {
        this.iDisplayLength = iDisplayLength;
    }
    
}
