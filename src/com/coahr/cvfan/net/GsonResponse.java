package com.coahr.cvfan.net;

import java.io.Serializable;

public class GsonResponse {
	// login response status
	public static class ForgetPasswordResponse {
		public String status_code;
		public String status_text;
		public UserDatas data;
	}
	
	// login response user data
	public static class UserDatas {
		public String createdUserId;
		public String createdDate;
		public String updatedUserId;
		public String updatedDate;
		public String deleted;
		public String userId;
		public String userName;
		public String userStatus;
		public String ownerId;
		public String ownerRole;
		public String loginCount;
		public String code;
		public String owner;
	}
	
	// login response status
	public static class UserAccountResponse {
		public String status_code;
		public String status_text;
		public UserData data;
	}

	// login response user data
	public static class UserData {
		public String createdUserId;
		public String createdDate;
		public String updatedUserId;
		public String updatedDate;
		public String deleted;
		public String userId;
		public String userName;
		public String userStatus;
		public String ownerId;
		public String ownerRole;
		public String loginCount;
		public String code;
		public OwnerData owner;
	}

	// login response owner data
	public static class OwnerData {
		public String createdUserId;
		public String createdDate;
		public String updatedUserId;
		public String updatedDate;
		public String deleted;
		public String driverId;
		public String nickName;
		public String name;
		public String province;
		public String city;
		public String area;
		public String address;
		public String mobileNo;
		public String bornDate;
		public String bgLogoFile;
		public String logoFile;
		public String licenseNo;
		public String licenseFile;
		public String licenseDate;
		public String licenseAuthenticated;
		public String memberLevel;
		public String memberScore;
		public String mobileBinded;
	}

	// query station list response status
	public static class GetStationListResponse {
		public String status_code;
		public String status_text;
		public String sEcho;
		public String iTotalRecords;
		public String iTotalDisplayRecords;
		public StationDetail[] data;
	}

	// response status
	public static class HeadResponse {
		public String status_text;
		public String status_code;
	}

	// station detail data
	public static class StationDetail implements Serializable {
		private static final long serialVersionUID = 1L;
		public String STATION_ID;
		public String PARENT_ID;
		public String NAME;
		public String PROVINCE;
		public String CITY;
		public String AREA;
		public String ADDRESS;
		public String QUALIFICATION;
		public String LOGO_FILE;
		public String LICENSE_FILE;
		public String IS_MEMBER;
		public String BRAND;
		public String CONTACTER;
		public String CONTACT_TEL;
		public String FAX;
		public String SERVICE_MEMO;
		public String BRIEF_INTRO;
		public String ACCOUNT_BANK;
		public String ACCOUNT_NO;
		public String PRAISE_RATE;
		public String COMMENT_COUNT;
		public String GENERAL_SCORE;
		public String PRICE_SCORE;
		public String QUALITY_SCORE;
		public String TIME_SCORE;
		public String POS_LONG;
		public String POS_LAT;
		public String DISCOUNT_FLAG;
		public String PROMOTION_FLAG;
		public String CREATED_USER_ID;
		public String CREATED_DATE;
		public String UPDATED_USER_ID;
		public String UPDATED_DATE;
		public String IS_DELETED;
	}

	// query driver information response status
	public static class GetDriverInfoResponse {
		public String status_text;
		public String status_code;
		public DriverInfo data;
	}

	// query driver information response data
	public static class DriverInfo {
		public String licenseDate;
		public String memberScore;
		public String updatedDate;
		public String deleted;
		public String city;
		public String createdUserId;
		public String licenseNo;
		public String area;
		public String bornDate;
		public String updatedUserId;
		public String nickName;
		public String address;
		public String driverId;
		public String name;
		public String province;
		public String licenseAuthenticated;
		public String licenseFile;
		public String mobileBinded;
		public String logoFile;
		public String createdDate;
		public String bgLogoFile;
		public String mobileNo;
		public String memberLevel;
	}

	// query personal information response status
	public static class GetPersonalInfoResponse {
		public String status_text;
		public String status_code;
		public PersonalInfo data;
	}

	// query driver information response data
	public static class PersonalInfo {
		public String AGE;
		public String ADDRESS;
		public String LICENSE_AGE; // 驾龄
		public String LICENSE_AUTHENTICATED;
		public String MOBILE_BINDED;
		public String MEMBER_SCORE;
		public String DRIVER_ID;
		public String LOGO_FILE;
		public String MOBILE_NO;
		public String BORN_DATE;
		public String NICK_NAME;
		public String NAME;
		public String LICENSE_NO;
		public String LICENSE_FILE;
		public String MEMBER_LEVEL;
		public String PLATE_NO;
		public String LICENSE_DATE;
	}

	// query turck information response status
	public static class GetTruckInfoResponse {
		public String status_text;
		public String status_code;
		public TruckInfoData data;
	}

	// query truck information response data
	public static class TruckInfoData {
		public String PLATE_NO;
		public String LICENSE_NO;
		public String NAME;
		public String ADDRESS;
		public String BRAND_NAME;
		public String MODEL_NAME;
		public String ENGINE_NO;
		public String FRAME_NO;
		public String LICENSE_FILE;
		public String AUTO_LEVEL;
		public String GUARANTEE;
		public String BRAND;
	}

	// query my trucks response status
	public static class MyTrucksInfoResponse {
		public String status_text;
		public String status_code;
		public String sEcho;
		public String iTotalRecords;
		public String iTotalDisplayRecords;
		public TruckItem[] data;
	}

	// my truck information data
	public static class TruckItem {
		public String GUARANTEE;
		public String PLATE_NO;
		public String AUTO_ID;
	}

	// modify password response status
	public static class ModifyPasswordResponse {
		public String status_text;
		public String status_code;
	}

	// query station comments response status
	public static class GetStationCommentListResponse {
		public String status_code;
		public String status_text;
		public String sEcho;
		public String iTotalRecords;
		public String iTotalDisplayRecords;
		public StationCommentItem[] data;
	}

	// query station comment response data
	public static class StationCommentItem {
		public String COMMENT_ID;
		public String DRIVER_ID;
		public String STATION_ID;
		public String GENERAL_SCORE;
		public String PRICE_SCORE;
		public String QUALITY_SCORE;
		public String TIME_SCORE;
		public String COMMENT;
		public String CREATED_USER_ID;
		public String CREATED_DATE;
		public String UPDATED_USER_ID;
		public String UPDATED_DATE;
		public String IS_DELETED;
		public String DRIVER_NAME;
		public String MOBILE_NO;
	}

	// get my maintain paper list response status
	public static class MaintainPaperList {
		public String status_code;
		public String status_text;
		public String sEcho;
		public String iTotalRecords;
		public String iTotalDisplayRecords;
		public MaintainPaperItem[] data;
	}

	// my maintain paper detail data
	public static class MaintainPaperItem {
		public String SERVICE_ID;
		public String PAY_MODE;
		public String AUTO_ID;
		public String ENTER_DATE;
		public String NAME;
		public String PAYMENT_STATUS;
	}

	// user register response status
	/*
	 * public static class RegisterResponse { public String status_code; public
	 * String status_text; public UserData data; }
	 */

	/*
	 * public static class RegisterData { public String createdUserId; public
	 * String createdDate; public String updatedUserId; public String
	 * updatedDate; public String deleted; public String userId; public String
	 * userName; public String userStatus; public String ownerId; public String
	 * ownerRole; public String loginCount; public String code; public OwnerData
	 * owner; }
	 */

	// modify truck info response status
	public static class ModifyTruckInfoResponse {
		public String status_text;
		public String status_code;
		public String data;
	}

	// modify truck info response status
	public static class FeedBackResponse {
		public String status_text;
		public String status_code;
		public String data;
	}

	public static class TruckTypeListResponse {
		public String status_code;
		public String status_text;
		public String sEcho;
		public String iTotalRecords;
		public String iTotalDisplayRecords;
		public TruckType[] data;
	}

	public static class TruckType {
		public String ID;
		public String LABEL;
		public String VALUE;
	}

	public static class BrandListResponse {
		public String status_code;
		public String status_text;
		public String sEcho;
		public String iTotalRecords;
		public String iTotalDisplayRecords;
		public BrandType[] data;
	}

	public static class BrandType {
		public String label;
		public String value;
		public String id;
	}

	// modify password response status
	public static class CertificateDriverLicenseResponse {
		public String status_text;
		public String status_code;
	}

	public static class AreaListResponse {
		public String status_code;
		public String status_text;
		public String sEcho;
		public String iTotalRecords;
		public String iTotalDisplayRecords;
		public AreaData[] data;
	}

	public static class AreaData {
		public String ID;
		public String LABEL;
		public String VALUE;
	}

	/**
	 * job details
	 */
	public static class Infonub implements Serializable {
		private static final long serialVersionUID = 1L;
		public String Cid;
		public String Companyname;
		public String Industry;
		public String Status;
		public String Properity;
		public String Employers;
		public String Suozaidi;
		public String Jid;
		public String Jobname;
		public String Job;
		public String Deal;
		public String Jobcommend;
		public String Number;
		public String Works;
		public String Province;
		public String City;
		public String Edus;
		public String Contactperson;
		public String Phone;
		public String Addtime;
		public String Enddate;
		public String Hits;
		public String Uptime;
		public String Audit;
	}

	public static class AppraiseStationResponse {
		public String status_code;
		public String status_text;
		public String data;
	}

	public static class GetMaintainPaperDetailResponse {
		public String status_code;
		public String status_text;
		public MainTainPaperData data;
	}

	public static class MainTainPaperData {
		public String STATION_ID;
		public String SERVICE_CODE;
		public String AUTO_PLATE_NO;
		public String ENTER_DATE;
		public String LEAVE_DATE;
		public String OWNER_NAME;
		public String CONTACTER;
		public String CONTACT_TEL;
		public String SERVICE_STATION_NAME;
		public String GURANTEE_STATION_NAME;
		public String SERVICE_AMOUNT;
		public String PAYMENT_AMOUNT;
		public String AUTO_LEVEl;
		public String SERVICE_ITEMS;
		public String PAYMENT_STATUS;
	}

	// query driver information response status
	public static class StationAssuranceResponse {
		public String status_text;
		public String status_code;
		public String data;
	}

	public static class Location {
		public String controller; // 接口类型
		public String type; // error -> 失败； success -> 成功.
		public String infonub; // -1 -> 查询出错； 0 -> type类型错误 ； 1 -> 查询成功.
	}
	
	public static class ModifyPersonalInfoResponse {
		public String status_text;
		public String status_code;
		public String data;
	}
	
	public static class VersionUpdateResponse{
		public String status_code;
		public String status_text;
		public VersionUpdateData data;
	}
	
	public static class VersionUpdateData {
		public String createdUserId;
		public String createdDate;
		public String updatedUserId;
		public String updatedDate;
		public String deleted;
		public String versionId;
		public String devType;
		public String versionNo;
		public String versionPath;
		public String optionalVersionNo;
		public String requiredVersionNo;
		public String required;
		public String message;
	}
}
