package com.coahr.cvfan.entity;

public class DriverCertificateEntity {
    private String driverId;
    private String name;
    private String licenseNo;
    private String licenseFile;
	public String getDriverId() {
		return driverId;
	}
	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public String getLicenseFile() {
		return licenseFile;
	}
	public void setLicenseFile(String licenseFile) {
		this.licenseFile = licenseFile;
	}
    
}
