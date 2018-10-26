package com.coahr.cvfan.entity;

public class ParameterEntity {
    private String iDisplayStart;
    private String iDisplayLength;
    private String brand;
    private String distance;
    private String posLat;
    private String posLong;
    private String provinceName;
    private String cityName;
    private String name;
    
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
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public String getDistance() {
        return distance;
    }
    public void setDistance(String distance) {
        this.distance = distance;
    }
    public String getPosLat() {
        return posLat;
    }
    public void setPosLat(String posLat) {
        this.posLat = posLat;
    }
    public String getPosLong() {
        return posLong;
    }
    public void setPosLong(String posLong) {
        this.posLong = posLong;
    }
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
