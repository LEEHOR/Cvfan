package com.coahr.cvfan.net;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import cn.zipper.framwork.core.ZLog;

import com.coahr.cvfan.entity.AppraiseStationEntity;
import com.coahr.cvfan.entity.CitysRequestData;
import com.coahr.cvfan.entity.BindAndChangePhoneEntity;
import com.coahr.cvfan.entity.BrandID;
import com.coahr.cvfan.entity.DriverCertificateEntity;
import com.coahr.cvfan.entity.DriverInfoDataEntity;
import com.coahr.cvfan.entity.FeedBackEntity;
import com.coahr.cvfan.entity.GainVerificationCodeEntity;
import com.coahr.cvfan.entity.GetCitysListEntity;
import com.coahr.cvfan.entity.GetBrandListEntity;
import com.coahr.cvfan.entity.GetDriverInfoEntity;
import com.coahr.cvfan.entity.GetMyTruckInfoEntity;
import com.coahr.cvfan.entity.GetPersonalInfoEntity;
import com.coahr.cvfan.entity.GetProvincesListEntity;
import com.coahr.cvfan.entity.GetSalesPromotionAndDisscountInfoEntity;
import com.coahr.cvfan.entity.GetStationCommentListData;
import com.coahr.cvfan.entity.GetStationCommentListEntity;
import com.coahr.cvfan.entity.GetStationListEntity;
import com.coahr.cvfan.entity.GetTruckTypeEntity;
import com.coahr.cvfan.entity.GetUserGalleryEntity;
import com.coahr.cvfan.entity.MaintainPaperData;
import com.coahr.cvfan.entity.MaintainPaperDetailData;
import com.coahr.cvfan.entity.MaintainPaperDetailRequestEntity;
import com.coahr.cvfan.entity.MaintainPaperListRequestEntity;
import com.coahr.cvfan.entity.ModifyMyTruckEntity;
import com.coahr.cvfan.entity.ModifyPasswordEntity;
import com.coahr.cvfan.entity.ModifyPersonalInfoEntity;
import com.coahr.cvfan.entity.MyTruckInfoParameterEntity;
import com.coahr.cvfan.entity.ParameterEntity;
import com.coahr.cvfan.entity.PersonalInfoDataEntity;
import com.coahr.cvfan.entity.RegisterMyTruckEntity;
import com.coahr.cvfan.entity.RequestGalleryEntity;
import com.coahr.cvfan.entity.RequestStationAssuranceData;
import com.coahr.cvfan.entity.ResetPasswordEntity;
import com.coahr.cvfan.entity.SalesPromotionAndDisscountData;
import com.coahr.cvfan.entity.TruckInfoData;
import com.coahr.cvfan.entity.TruckInfoDataEntity;
import com.coahr.cvfan.entity.UserLoginEntity;
import com.coahr.cvfan.entity.UserRegisterEntity;
import com.coahr.cvfan.entity.ValidateCodeEntity;
import com.coahr.cvfan.entity.VersionUpdateEntity;
import com.coahr.cvfan.net.GsonResponse.UserAccountResponse;
import com.coahr.cvfan.util.Config;
import com.coahr.cvfan.util.NetworkStateManager;
import com.coahr.cvfan.util.UserInfoPersist;
import com.google.gson.Gson;

public class ClientRequest {

	// tag
	private static final String TAG = "CILENTREQUEST";
	private static String JSESSIONID;

	@Deprecated
	/**
	 * 
	 * @param params
	 * @return request json string
	 */
	private static String jsonStringHandle(List<HashMap<String, String>> params) {

		String returnString = null;

		Gson gson = new Gson();
		returnString = gson.toJson(params).trim();

		if (returnString.indexOf("[") == 0) {
			returnString = returnString
					.substring(returnString.indexOf("[") + 1);
			if (returnString.indexOf("]") == returnString.length() - 1) {
				returnString = returnString.substring(0,
						returnString.lastIndexOf("]"));
			}
		}

		return returnString;
	}

	/*
	 * 登录
	 */
	public static void userLogin(Handler handler, String username,
			String password) {

		// 使用NameValuePair来保存要传递的 参数
		// List<GsonRequest.UserLogin> params = new
		// ArrayList<GsonRequest.UserLogin>();
		// 添加要传递的参数
		/*
		 * Map<String, String> mapUserName = new HashMap<String, String>();
		 * mapUserName.put("userName", username); Map<String, String>
		 * mapPassword = new HashMap<String, String>();
		 * mapPassword.put("password", password); Map<String, String> mapCode =
		 * new HashMap<String, String>(); mapCode.put("code", "");
		 * params.add((HashMap<String, String>) mapUserName);
		 * params.add((HashMap<String, String>) mapPassword);
		 * params.add((HashMap<String, String>) mapCode);
		 * 
		 * String jsonString = jsonStringHandle(params); String jsonStringSpell
		 * = "{" + "\"" + "userName" + "\"" + ":" + "\"" + "admin" + "\"" + ","
		 * + "\"" + "password" + "\"" + ":" + "\"" + "123" + "\"" + "," + "\"" +
		 * "code" + "\"" + ":" + "\"" + "\"" +"}" ;
		 */

		UserLoginEntity userLoginEntity = new UserLoginEntity();
		userLoginEntity.setUserName(username);
		userLoginEntity.setPassword(password);
		userLoginEntity.setCode("");

		Gson gson = new Gson();
		String jsonString = gson.toJson(userLoginEntity).trim();

		ZLog.e("<< Request (" + "userLogin" + "): " + jsonString);

		PostRequestWork rWork = new PostRequestWork(handler,
				Config.LOGIN_RESPONSE_TYPE, UserAccountResponse.class);
		rWork.execute(Config.LOGIN_REQUEST, jsonString);
	}

	/*
	 * 获取验证码
	 */
	public static void gainVerificationCode(Handler handler, String mobileNo) {
		GainVerificationCodeEntity gainVerificationCodeEntity = new GainVerificationCodeEntity();
		gainVerificationCodeEntity.setMobileNo(mobileNo);

		Gson gson = new Gson();
		String jsonString = gson.toJson(gainVerificationCodeEntity).trim();

		ZLog.e("<< Request (" + "userLogin" + "): " + jsonString);

		PostRequestWork rWork = new PostRequestWork(handler,
				Config.GAIN_VERIFITCATION_CODE_RESPONSE_TYPE,
				UserAccountResponse.class);
		rWork.execute(Config.GAIN_VERIFITCATION_CODE, jsonString);
	}

	/*
	 * 获取服务站列表
	 */
	public static void getStationList(Handler handler, String iDisplayStart,
			String iDisplayLength, String brand, String distance,
			String posLat, String posLong, String provinceName, String cityName, String name) {

		GetStationListEntity getStationListEntity = new GetStationListEntity();
		ParameterEntity parameterEntity = new ParameterEntity();

		parameterEntity.setiDisplayStart(iDisplayStart);
		parameterEntity.setiDisplayLength(iDisplayLength);
		parameterEntity.setBrand(brand);
		parameterEntity.setDistance(distance);
		parameterEntity.setPosLat(posLat);
		parameterEntity.setPosLong(posLong);
		parameterEntity.setProvinceName(provinceName);
		parameterEntity.setCityName(cityName);
		parameterEntity.setName(name);
		getStationListEntity.setEntity("cvfans.queryStationInfos");
		getStationListEntity.setParameter(parameterEntity);

		Gson gson = new Gson();
		String jsonString = gson.toJson(getStationListEntity).trim();

		ZLog.e("<< Request (" + "getStationList" + "): " + jsonString);

		PostRequestWork rWork = new PostRequestWork(handler,
				Config.GET_STATION_lIST_RESPONSE_TYPE,
				GsonResponse.GetStationListResponse.class);
		rWork.execute(Config.GET_STATION_lIST_REQUEST, jsonString);
	}

	/*
	 * 获取个人信息
	 */
	public static void getPersonalInfo(Handler handler) {

		PersonalInfoDataEntity driverInfo = new PersonalInfoDataEntity();
		driverInfo.setDriverId(UserInfoPersist.driverId);
		GetPersonalInfoEntity personalInfoEntity = new GetPersonalInfoEntity();
		personalInfoEntity.setStatementId("cvfans.queryDriverInfo");
		personalInfoEntity.setParameters(driverInfo);

		Gson gson = new Gson();
		String jsonString = gson.toJson(personalInfoEntity).trim();
		
		ZLog.e("<< Request (" + "getPersonalInfo" + "): " + jsonString);

		PostRequestWork rWork = new PostRequestWork(handler,
				Config.GET_PERSONAL_INFO_RESPONSE_TYPE,
				GsonResponse.GetPersonalInfoResponse.class);
		rWork.execute(Config.GET_PERSONAL_INFO, jsonString);
	}
	
	/*
	 * 修改个人信息
	 */
	public static void modifyPersonalInfo(Handler handler, String headUrl, String nickName, String bornDate, String provinceName,
			String cityName, String areaName, String address, String licenseDate){
		ModifyPersonalInfoEntity modifyPersonalInfoEntity = new ModifyPersonalInfoEntity();
		if(address!=null&&address!="")
		{
			modifyPersonalInfoEntity.setAddress(address);
		}
		if(areaName!=null&&areaName!="")
		{
			modifyPersonalInfoEntity.setAreaName(areaName);
		}
		if(bornDate!=null&&bornDate!="")
		{
			modifyPersonalInfoEntity.setBornDate(bornDate);
		}
		if(cityName!=null&&cityName!="")
		{
			modifyPersonalInfoEntity.setCityName(cityName);
		}
		if(licenseDate!=null&&licenseDate!="")
		{
			modifyPersonalInfoEntity.setLicenseDate(licenseDate);
		}
		if(nickName!=null&&nickName!="")
		{
			modifyPersonalInfoEntity.setNickName(nickName);
		}
		if(provinceName!=null&&provinceName!="")
		{
			modifyPersonalInfoEntity.setProvinceName(provinceName);
		}
		modifyPersonalInfoEntity.setDriverId(UserInfoPersist.driverId);
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(modifyPersonalInfoEntity).trim();

		ZLog.e("<< Request (" + "modifyPersonalInfo" + "): " + jsonString);

		FilePostRequestWork rWork = new FilePostRequestWork(handler,
				Config.MODIFY_PERSONAL_INFO_RESPONSE_TYPE,
				GsonResponse.GetPersonalInfoResponse.class);
		rWork.execute(Config.MODIFY_PERSONAL_INFO, jsonString, headUrl, "logoFile");
	}

	/*
	 * 获取个人信息
	 */
	@Deprecated
	public static void getDriverInfo(Handler handler) {

		DriverInfoDataEntity driverInfo = new DriverInfoDataEntity();
		driverInfo.setDriverId(UserInfoPersist.driverId);
		driverInfo.setDeleted("0");
		GetDriverInfoEntity driverInfoEntity = new GetDriverInfoEntity();
		driverInfoEntity.setEntity("Driver");
		driverInfoEntity.setParameters(driverInfo);

		Gson gson = new Gson();
		String jsonString = gson.toJson(driverInfoEntity).trim();

		ZLog.e("<< Request (" + "getDriverInfo" + "): " + jsonString);

		PostRequestWork rWork = new PostRequestWork(handler,
				Config.GET_DRIVER_INFO_RESPONSE_TYPE,
				GsonResponse.GetDriverInfoResponse.class);
		rWork.execute(Config.GET_DRIVER_INFO, jsonString);
	}

	/*
	 * 获取卡车详细信息
	 */
	public static void getTruckInfo(Handler handler, String autoId,
			String ownerRole) {

		TruckInfoData truckInfo = new TruckInfoData();
		truckInfo.setAutoId(autoId);
		truckInfo.setDriverId(UserInfoPersist.driverId);
		truckInfo.setOwnerRole(ownerRole);
		TruckInfoDataEntity truckInfoDataEntity = new TruckInfoDataEntity();
		truckInfoDataEntity.setStatementId("cvfans.queryAutoInfo_p");
		truckInfoDataEntity.setParameters(truckInfo);

		Gson gson = new Gson();
		String jsonString = gson.toJson(truckInfoDataEntity).trim();

		ZLog.e("<< Request (" + "getTruckInfo" + "): " + jsonString);

		PostRequestWork rWork = new PostRequestWork(handler,
				Config.GET_TRUCK_INFO_RESPONSE_TYPE,
				GsonResponse.GetTruckInfoResponse.class);
		rWork.execute(Config.GET_TRUCK_INFO, jsonString);
	}

	/*
	 * 获取我的卡车信息
	 */
	public static void getMyTruckInfo(Handler handler, String driverId) {

		MyTruckInfoParameterEntity myTruckInfoParameterEntity = new MyTruckInfoParameterEntity();
		myTruckInfoParameterEntity.setDriverId(driverId);
		GetMyTruckInfoEntity myTruckInfo = new GetMyTruckInfoEntity();
		myTruckInfo.setStatementId("cvfans.queryAutoInfoList_p");
		myTruckInfo.setParameters(myTruckInfoParameterEntity);

		Gson gson = new Gson();
		String jsonString = gson.toJson(myTruckInfo).trim();

		ZLog.e("<< Request (" + "getMyTruckInfo" + "): " + jsonString);

		PostRequestWork rWork = new PostRequestWork(handler,
				Config.GET_DRIVER_TRUCK_INFO_RESPONSE_TYPE,
				GsonResponse.MyTrucksInfoResponse.class);
		rWork.execute(Config.GET_DRIVER_TRUCK_INFO, jsonString);
	}

	/*
	 * 修改密码
	 */
	public static void modifyPassword(Handler handler, String userId,
			String oldPwd, String newPwd) {

		ModifyPasswordEntity modifyPasswordEntity = new ModifyPasswordEntity();
		modifyPasswordEntity.setNewPwd(newPwd);
		modifyPasswordEntity.setOldPwd(oldPwd);
		modifyPasswordEntity.setUserId(userId);
		modifyPasswordEntity.setCode("");

		Gson gson = new Gson();
		String jsonString = gson.toJson(modifyPasswordEntity).trim();

		ZLog.e("<< Request (" + "modifyPassword" + "): " + jsonString);

		PostRequestWork rWork = new PostRequestWork(handler,
				Config.MODIFY_PASSWORD_RESPONSE_TYPE,
				GsonResponse.ModifyPasswordResponse.class);
		rWork.execute(Config.MODIFY_PASSWORD, jsonString);
	}

	/*
	 * 司机身份认证
	 */
	public static void driverCertification(Handler handler, String driverId,
			String name, String licenseNo, String licenseFile) {

		DriverCertificateEntity driverCertificateEntity = new DriverCertificateEntity();
		driverCertificateEntity.setDriverId(driverId);
		driverCertificateEntity.setName(name);
		driverCertificateEntity.setLicenseNo(licenseNo);

		Gson gson = new Gson();
		String jsonString = gson.toJson(driverCertificateEntity).trim();

		FilePostRequestWork rWork = new FilePostRequestWork(handler,
				Config.IDENTIFY_DRIVER_LICENSE_RESPONSE_TYPE,
				GsonResponse.CertificateDriverLicenseResponse.class);
		rWork.execute(Config.IDENTIFY_DRIVER_LICENSE, jsonString, licenseFile,
				"licenseFile");

	}

	/*
	 * 点评服务站
	 */
	public static void appraiseStation(Handler handler, float quality_rating,
			float price_rating, float speed_rating, float general_rating,
			String comment, String stationID) {
		AppraiseStationEntity appraiseStationEntity = new AppraiseStationEntity();
		appraiseStationEntity.setQualityScore(quality_rating + "");
		appraiseStationEntity.setDriverId(UserInfoPersist.userID);
		appraiseStationEntity.setPriceScore(price_rating + "");
		appraiseStationEntity.setTimeScore(speed_rating + "");
		appraiseStationEntity.setGeneralScore(general_rating + "");
		appraiseStationEntity.setStationId(stationID);
		appraiseStationEntity.setComment(comment);

		Gson gson = new Gson();
		String jsonString = gson.toJson(appraiseStationEntity).trim();

		ZLog.e("<< Request (" + "appraiseStation" + "): " + jsonString);

		PostRequestWork rWork = new PostRequestWork(handler,
				Config.APPRAISE_STATION_RESPONSE_TYPE,
				GsonResponse.AppraiseStationResponse.class);
		rWork.execute(Config.APPRAISE_STATION, jsonString);
	}

	/*
	 * 查询服务站评论列表
	 */
	public static void getStationCommentList(Handler handler, String stationID,
			String stratPageNo, String pageLength) {

		GetStationCommentListData getStationCommentListData = new GetStationCommentListData();
		getStationCommentListData.setStationId(stationID);
		getStationCommentListData.setiDisplayStart(stratPageNo);
		getStationCommentListData.setiDisplayLength(pageLength);

		GetStationCommentListEntity getStationCommentListEntity = new GetStationCommentListEntity();
		getStationCommentListEntity
				.setStatementId("cvfans.queryStationCommentInfo");
		getStationCommentListEntity.setParameters(getStationCommentListData);

		Gson gson = new Gson();
		String jsonString = gson.toJson(getStationCommentListEntity).trim();

		ZLog.e("<< Request (" + "getStationCommentList" + "): " + jsonString);

		PostRequestWork rWork = new PostRequestWork(handler,
				Config.GET_STATION_COMMENT_LIST_RESPONSE_TYPE,
				GsonResponse.GetStationCommentListResponse.class);
		rWork.execute(Config.GET_STATION_COMMENT_LIST, jsonString);
	}

	/*
	 * 查询我的维修单列表
	 */
	public static void getMaintainPaperList(Handler handler, String driverId) {
		MaintainPaperData maintainPaperData = new MaintainPaperData();
		maintainPaperData.setDriverId(driverId);
		maintainPaperData.setiDisplayStart("0");
		maintainPaperData.setiDisplayLength("10");
		MaintainPaperListRequestEntity maintainPaperListRequestEntity = new MaintainPaperListRequestEntity();
		maintainPaperListRequestEntity
				.setEntity("cvfans.queryServiceInfo_Driver");
		maintainPaperListRequestEntity.setParameters(maintainPaperData);

		Gson gson = new Gson();
		String jsonString = gson.toJson(maintainPaperListRequestEntity).trim();

		ZLog.e("<< Request (" + "getMaintainPaperList" + "): " + jsonString);

		PostRequestWork rWork = new PostRequestWork(handler,
				Config.GET_MAINTAIN_PAPER_LIST_RESPONSE_TYPE,
				GsonResponse.MaintainPaperList.class);
		rWork.execute(Config.GET_MAINTAIN_PAPER_LIST, jsonString);
	}

	/*
	 * 查询维修单详情
	 */
	public static void getMaintainPaperDetails(Handler handler, String serviceid) {
		MaintainPaperDetailData maintainPaperDetailData = new MaintainPaperDetailData();
		maintainPaperDetailData.setServiceId(serviceid);
		MaintainPaperDetailRequestEntity maintainPaperDetailRequestEntity = new MaintainPaperDetailRequestEntity();
		maintainPaperDetailRequestEntity
				.setEntity("cvfans.serviceDetailInfoe_p");
		maintainPaperDetailRequestEntity.setParameters(maintainPaperDetailData);

		Gson gson = new Gson();
		String jsonString = gson.toJson(maintainPaperDetailRequestEntity)
				.trim();

		ZLog.e("<< Request (" + "getMaintainPaperDetails" + "): " + jsonString);

		PostRequestWork rWork = new PostRequestWork(handler,
				Config.GET_MAINTAIN_PAPER_DETAIL_RESPONSE_TYPE,
				GsonResponse.GetMaintainPaperDetailResponse.class);
		rWork.execute(Config.GET_MAINTAIN_PAPER_DETAIL, jsonString);
	}

	
	/*
	 * 验证码验证
	 */
	public static void validateSendCode(Handler handler,String mobileNo, String vcode)
	{
		ValidateCodeEntity validateCodeEntity=new ValidateCodeEntity();
		validateCodeEntity.setMobileNo(mobileNo);
		validateCodeEntity.setVcode(vcode);
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(validateCodeEntity).trim();

		ZLog.e("<< Request (" + "validateSendCode" + "): " + jsonString);

		PostRequestWork rWork = new PostRequestWork(handler,
				Config.CDOE_VALIDATION_RESPONSE_TYPE,
				GsonResponse.HeadResponse.class);
		rWork.execute(Config.CDOE_VALIDATION, jsonString);
	}
	
	/*
	 * 绑定手机号/更改绑定手机号
	 */
	public static void bindAndChangePhoneNumber(Handler handler,
			String mobileNo, String vcode) {
		BindAndChangePhoneEntity bindAndChangePhoneEntity = new BindAndChangePhoneEntity();
		bindAndChangePhoneEntity.setDriverId(UserInfoPersist.driverId);
		bindAndChangePhoneEntity.setMobileNo(mobileNo);
		bindAndChangePhoneEntity.setVcode(vcode);

		Gson gson = new Gson();
		String jsonString = gson.toJson(bindAndChangePhoneEntity).trim();

		ZLog.e("<< Request (" + "bindAndChangePhoneNumber" + "): " + jsonString);

		PostRequestWork rWork = new PostRequestWork(handler,
				Config.BIND_PHONE_RESPONSE_TYPE,
				GsonResponse.HeadResponse.class);
		rWork.execute(Config.BIND_PHONE, jsonString);
	}

	/*
	 * 用户注册
	 */
	public static void userRegister(Handler handler, String username,
			String password, String phoneNum) {

		UserRegisterEntity userRegisterEntity = new UserRegisterEntity();
		userRegisterEntity.setMobileNo(phoneNum);
		userRegisterEntity.setOwnerRole("1");
		userRegisterEntity.setUserName(username);
		userRegisterEntity.setPassword(password);

		Gson gson = new Gson();
		String jsonString = gson.toJson(userRegisterEntity).trim();

		ZLog.e("<< Request (" + "userRegister" + "): " + jsonString);

		PostRequestWork rWork = new PostRequestWork(handler,
				Config.USER_REGISTER_RESPONSE_TYPE, UserAccountResponse.class);
		rWork.execute(Config.USER_REGISTER, jsonString);
	}

	/*
	 * 获取用户相册
	 */
	public static void getUserGallery(Handler handler, String driverId) {

		RequestGalleryEntity requestGalleryEntity = new RequestGalleryEntity();
		requestGalleryEntity.setDriverId(driverId);
		GetUserGalleryEntity getUserGalleryEntity = new GetUserGalleryEntity();
		getUserGalleryEntity.setParameters(requestGalleryEntity);
		getUserGalleryEntity.setStatementId("cvfans.driverAlbum_p");

		Gson gson = new Gson();
		String jsonString = gson.toJson(getUserGalleryEntity).trim();

		ZLog.e("<< Request (" + "getUserGallery" + "): " + jsonString);

		PostRequestWork rWork = new PostRequestWork(handler,
				Config.GET_GALLERY_LIST_RESPONSE_TYPE,
				GsonResponse.HeadResponse.class);
		rWork.execute(Config.GET_GALLERY_LIST, jsonString);
	}

	/*
	 * 获取汽车品牌列表
	 */
	public static void getTruckBrandList(Handler handler) {
		GetBrandListEntity getBrandListEntity = new GetBrandListEntity();
		getBrandListEntity.setStatementId("cvfans.queryAutoBrandInfoes");

		Gson gson = new Gson();
		String jsonString = gson.toJson(getBrandListEntity).trim();

		ZLog.e("<< Request (" + "getTruckBrandList" + "): " + jsonString);

		PostRequestWork rWork = new PostRequestWork(handler,
				Config.GET_TRUCK_BRAND_LIST_RESPONSE_TYPE,
				GsonResponse.BrandListResponse.class);
		rWork.execute(Config.GET_TRUCK_BRAND_LIST, jsonString);
	}

	/*
	 * 获取选定品牌下车型列表
	 */
	public static void getTruckTypeList(Handler handler, String id) {
		BrandID brandID = new BrandID();
		brandID.setId(id);

		GetTruckTypeEntity getTruckTypeEntity = new GetTruckTypeEntity();
		getTruckTypeEntity.setStatementId("cvfans.queryAutoModelInfoes");
		getTruckTypeEntity.setParameters(brandID);

		Gson gson = new Gson();
		String jsonString = gson.toJson(getTruckTypeEntity).trim();

		ZLog.e("<< Request (" + "getTruckTypeList" + "): " + jsonString);

		PostRequestWork rWork = new PostRequestWork(handler,
				Config.GET_TRUCK_TYPE_LIST_RESPONSE_TYPE,
				GsonResponse.TruckTypeListResponse.class);
		rWork.execute(Config.GET_TRUCK_TYPE_LIST, jsonString);
	}

	/*
	 * 车辆注册
	 */
	public static void registerTruck(Handler handler, String licenseFileUri,
			String plateNo, String engineNo, String frameNo) {

		RegisterMyTruckEntity registerMyTruckEntity = new RegisterMyTruckEntity();
		registerMyTruckEntity.setPlateNo(plateNo);
		registerMyTruckEntity.setOwnerId(UserInfoPersist.ownerID);
		registerMyTruckEntity.setBrand(UserInfoPersist.choseBrandData.id);
		registerMyTruckEntity
				.setBrandName(UserInfoPersist.choseBrandData.label);
		registerMyTruckEntity.setModel(UserInfoPersist.choseTruckTypeData.ID);
		registerMyTruckEntity
				.setModelName(UserInfoPersist.choseTruckTypeData.LABEL);
		registerMyTruckEntity.setEngineNo(engineNo);
		registerMyTruckEntity.setFrameNo(frameNo);

		Gson gson = new Gson();
		String jsonString = gson.toJson(registerMyTruckEntity).trim();

		ZLog.e("<< Request (" + "registerTruck" + "): " + jsonString);

		FilePostRequestWork rWork = new FilePostRequestWork(handler,
				Config.REGISTER_MY_TRUCK_RESPONSE_TYPE,
				GsonResponse.HeadResponse.class);
		rWork.execute(Config.REGISTER_MY_TRUCK, jsonString, licenseFileUri,
				"licenseFileUri");
	}
	
	/*
	 * 车辆资料修改
	 */
	public static void modifyTruckInfo(Handler handler, String licenseFileUri,
			String plateNo, String engineNo, String frameNo, String autoId) {

		ModifyMyTruckEntity modifyMyTruckEntity = new ModifyMyTruckEntity();
		modifyMyTruckEntity.setPlateNo(plateNo);
		modifyMyTruckEntity.setOwnerId(UserInfoPersist.ownerID);
		//modifyMyTruckEntity.setAddress(address);
		modifyMyTruckEntity.setBrand(UserInfoPersist.choseBrandData.id);
		modifyMyTruckEntity
				.setBrandName(UserInfoPersist.choseBrandData.label);
		if(UserInfoPersist.choseTruckTypeData!=null)
		{
			if(UserInfoPersist.choseTruckTypeData.LABEL!=null&&!"".equals(UserInfoPersist.choseTruckTypeData.LABEL))
			{
				modifyMyTruckEntity.setModel(UserInfoPersist.choseTruckTypeData.ID);
				modifyMyTruckEntity.setModelName(UserInfoPersist.choseTruckTypeData.LABEL);
			}
			else
			{
				modifyMyTruckEntity.setModel("");
				modifyMyTruckEntity.setModelName("");
			}
		}
		modifyMyTruckEntity.setEngineNo(engineNo);
		//modifyMyTruckEntity.setOwnerRole(UserInfoPersist.ownerRole);
		modifyMyTruckEntity.setFrameNo(frameNo);
		modifyMyTruckEntity.setAutoId(autoId);

		Gson gson = new Gson();
		String jsonString = gson.toJson(modifyMyTruckEntity).trim();

		ZLog.e("<< Request (" + "modifyTruckInfo" + "): " + jsonString);

		FilePostRequestWork rWork = new FilePostRequestWork(handler,
				Config.MODIFY_MY_TRUCK_INFO_RESPONSE_TYPE,
				GsonResponse.ModifyTruckInfoResponse.class);
		rWork.execute(Config.MODIFY_MY_TRUCK_INFO, jsonString, licenseFileUri,
				"licenseFileUri");
	}
	
	/*
	 * 全国区域省级数据查询
	 */
	public static void getProvinceList(Handler handler){
		
		GetProvincesListEntity getProvincesListEntity = new GetProvincesListEntity();
		getProvincesListEntity.setStatementId("cvfans.queryProvinceInfoes");
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(getProvincesListEntity).trim();

		ZLog.e("<< Request (" + "getProvincesList" + "): " + jsonString);

		PostRequestWork rWork = new PostRequestWork(handler,
				Config.GET_PROVINCE_LIST_RESPONSE_TYPE,
				GsonResponse.AreaListResponse.class);
		rWork.execute(Config.GET_AREA_LIST, jsonString);
	}
	
	/*
	 * 对应省级区域下市级数据查询/市级下地区列表查询
	 */
	public static void getCityList(Handler handler, String id){
		
		CitysRequestData areaRequestData = new CitysRequestData();
		areaRequestData.setId(id);
		GetCitysListEntity getAreaListEntity = new GetCitysListEntity();
		getAreaListEntity.setParameters(areaRequestData);
		getAreaListEntity.setStatementId("cvfans.queryCityAreaInfoes");
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(getAreaListEntity).trim();

		ZLog.e("<< Request (" + "getCitysList" + "): " + jsonString);

		PostRequestWork rWork = new PostRequestWork(handler,
				Config.GET_CITY_LIST_RESPONSE_TYPE,
				GsonResponse.AreaListResponse.class);
		rWork.execute(Config.GET_AREA_LIST, jsonString);
	}
	
	/*
	 *  申请服务站担保
	 */
	public static void requestStationAssurance(Handler handler, String autoId, String stationId){
		RequestStationAssuranceData requestStationAssuranceData = new RequestStationAssuranceData();
		requestStationAssuranceData.setAutoId(autoId);
		requestStationAssuranceData.setStationId(stationId);
		/*RequestStationAssurance requestStationAssurance = new RequestStationAssurance();
		requestStationAssurance.setEntity("AutoGuarantee");
		requestStationAssurance.setParameters(requestStationAssuranceData);*/
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(requestStationAssuranceData).trim();

		ZLog.e("<< Request (" + "requestStationAssurance" + "): " + jsonString);

		PostRequestWork rWork = new PostRequestWork(handler,
				Config.REQUEST_STATION_ASSURANCE_RESPONSE_TPE,
				GsonResponse.StationAssuranceResponse.class);
		rWork.execute(Config.REQUEST_STATION_ASSURANCE, jsonString);
	}
	
	/*
	 * 服务站折扣信息查询
	 */
	public static void getDissountInfo(Handler handler, String stationId){
		SalesPromotionAndDisscountData data = new SalesPromotionAndDisscountData();
		data.setStationId(stationId);
		
		GetSalesPromotionAndDisscountInfoEntity entity = new GetSalesPromotionAndDisscountInfoEntity();
		entity.setStatementId("queryStationDiscountInfos_f");
		entity.setParameters(data);
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(entity).trim();

		ZLog.e("<< Request (" + "getDissountInfo" + "): " + jsonString);

		PostRequestWork rWork = new PostRequestWork(handler,
				Config.REQUEST_DISSCOUNT_RESPONSE_TYPE,
				GsonResponse.StationAssuranceResponse.class);
		rWork.execute(Config.REQUEST_DISSCOUNT, jsonString);
	}
	
	/*
	 * 意见反馈
	 */
	public static void feedBack(Handler handler, String content, String contact){
		FeedBackEntity feedBackEntity = new FeedBackEntity();
		feedBackEntity.setContent(content);
		feedBackEntity.setContact(contact);
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(feedBackEntity).trim();

		ZLog.e("<< Request (" + "feedBack" + "): " + jsonString);

		PostRequestWork rWork = new PostRequestWork(handler,
				Config.FEED_BACK_RESPONSE_TYPE,
				GsonResponse.FeedBackResponse.class);
		rWork.execute(Config.FEED_BACK, jsonString);
	}
	
	/*
	 * 服务站促销信息查询
	 */
	public static void getSalesPromotionInfo(Handler handler, String stationId){
		SalesPromotionAndDisscountData data = new SalesPromotionAndDisscountData();
		data.setStationId(stationId);
		
		GetSalesPromotionAndDisscountInfoEntity entity = new GetSalesPromotionAndDisscountInfoEntity();
		entity.setStatementId("queryStationPromotionInfos_f");
		entity.setParameters(data);
		Gson gson = new Gson();
		String jsonString = gson.toJson(entity).trim();

		ZLog.e("<< Request (" + "getSalesPromotionInfo" + "): " + jsonString);

		PostRequestWork rWork = new PostRequestWork(handler,
				Config.REQUEST_SALES_PROMOTION_RESPONSE_TYPE,
				GsonResponse.StationAssuranceResponse.class);
		rWork.execute(Config.REQUEST_SALES_PROMOTION, jsonString);
	}
	
	public static void versionUpdate(Handler handler, String versionNo){
		VersionUpdateEntity versionUpdateEntity = new VersionUpdateEntity();
		versionUpdateEntity.setDeviceType(Config.CLIENT_DEVICE_TYPE);
		versionUpdateEntity.setVersionNo(versionNo);
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(versionUpdateEntity).trim();

		ZLog.e("<< Request (" + "versionUpdate" + "): " + jsonString);

		PostRequestWork rWork = new PostRequestWork(handler,
				Config.VERSION_UPDATE_RESPONSE_TYP,
				GsonResponse.StationAssuranceResponse.class);
		rWork.execute(Config.VERSION_UPDATE, jsonString);
	}
	
	public static void  resetPassword(Handler handler, String userId,String password){
		ResetPasswordEntity resetPasswordEntity = new ResetPasswordEntity();
		resetPasswordEntity.setUserId(userId);
		resetPasswordEntity.setPassword(password);
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(resetPasswordEntity).trim();

		ZLog.e("<< Request (" + "versionUpdate" + "): " + jsonString);

		PostRequestWork rWork = new PostRequestWork(handler,
				Config.RESET_PASSWORD_RESPONSE_TYP,
				GsonResponse.StationAssuranceResponse.class);
		rWork.execute(Config.RESET_PASSWORD, jsonString);
	}

	protected static class PostRequestWork extends Thread {
		private Handler handler;
		private Class<?> cls;
		private int responseType;
		private String ria_command_id;
		private String jsonString;

		/**
		 * 
		 * @param handler
		 * @param responseType
		 *            : 响应类型;
		 * @param cls
		 *            : 响应对象的class类型;
		 */
		public PostRequestWork(Handler handler, int responseType, Class<?> cls) {
			this.handler = handler;
			this.responseType = responseType;
			this.cls = cls;
		}

		public void execute(String ria_command_id, String jsonString) {
			// TODO Auto-generated method stub
			this.ria_command_id = ria_command_id;
			this.jsonString = jsonString;
			this.start();

		}
		
		
		@Override
		public void run() {

			HttpPost httpRequest = null;

			if(!NetworkStateManager.instance().isNetworkConnected())
			{
				Message msg = handler.obtainMessage();
				msg.what = Config.NET_CONNECT_EXCEPTION;
				handler.sendMessage(msg);
			}
			else
			{
				try {
					
					String httpUrl = Config.REQUEST_URL + ria_command_id;
					
					httpRequest = new HttpPost(httpUrl);
					
					// String encoderJson = URLEncoder.encode(jsonString,
					// HTTP.UTF_8);
					
					if(JSESSIONID!=null)
					{
						httpRequest.addHeader("Cookie","JSESSIONID="+JSESSIONID);
					}
					httpRequest.addHeader(HTTP.CONTENT_TYPE,
							Config.APPLICATION_JSON);
					httpRequest.addHeader("uuid", UserInfoPersist.uuid);
					if ((!ria_command_id.equals(Config.LOGIN_REQUEST))
							&& (UserInfoPersist.userID != null)) {
						httpRequest.addHeader("userId", UserInfoPersist.userID);
					}
					
					StringEntity se = new StringEntity(jsonString, HTTP.UTF_8);
					se.setContentType(Config.CONTENT_TYPE_TEXT_JSON);
					se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
							Config.APPLICATION_JSON));
					// httpRequest.setEntity(se);
					
					// HttpEntity httpentity = new UrlEncodedFormEntity(
					// (ArrayList<NameValuePair>) request, "utf-8");
					// 请求httpRequest
					httpRequest.setEntity(se);
					
					// ZLog.e("<< Request (" + cls.getSimpleName() + "): " + se);
					
					// MultipartEntity multiEntity = new
					// MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
					// StringEntity sEntity =
					// 取得默认的HttpClient
					HttpClient httpclient = new DefaultHttpClient();
					// 取得HttpResponse
					HttpResponse httpResponse = httpclient.execute(httpRequest);
					// HttpStatus.SC_OK表示连接成功
					if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 取得返回的字符串
						String strResult = EntityUtils.toString(
								httpResponse.getEntity(), "utf-8");
						// Log.e("strResult = ", strResult + "");
						
						strResult = strResult.substring(strResult.indexOf("{"));
						/* 获取cookieStore */  
						List<Cookie> cookies = ((AbstractHttpClient) httpclient).getCookieStore().getCookies();  
						for(int i=0;i<cookies.size();i++){  
							if("JSESSIONID".equals(cookies.get(i).getName())){  
								JSESSIONID = cookies.get(i).getValue();  
								break;  
							}  
						}  
						
						String status_code = strResult.substring(
								strResult.indexOf("status_code"),
								strResult.indexOf("status_code") + 15);
						status_code = status_code
								.substring(status_code.length() - 1);
						
						Message msg = handler.obtainMessage();
						if (status_code.equals("0")) {
							msg.what = responseType;
						} else {
							msg.what = Config.RESPONSE_TYPE_ERROR;
						}
						msg.obj = strResult;
						handler.sendMessage(msg);
						ZLog.e("<< Response (" + cls.getSimpleName() + "): "
								+ strResult);
					}
				}
				catch (Exception e) 
				{
					e.printStackTrace();
					Log.e(TAG, "ria_command_id:" + ria_command_id);
				}
			}
		}
	}

	protected static class FilePostRequestWork extends Thread {
		private Handler handler;
		private Class<?> cls;
		private int responseType;
		private String ria_command_id;
		private String jsonString;
		private String fileURL;
		private String tag;

		/**
		 * 
		 * @param handler
		 * @param responseType
		 *            : 响应类型;
		 * @param cls
		 *            : 响应对象的class类型;
		 */
		public FilePostRequestWork(Handler handler, int responseType,
				Class<?> cls) {
			this.handler = handler;
			this.responseType = responseType;
			this.cls = cls;
		}

		public void execute(String ria_command_id, String jsonString,
				String fileURL, String tag) {
			// TODO Auto-generated method stub
			this.ria_command_id = ria_command_id;
			this.jsonString = jsonString;
			this.fileURL = fileURL;
			this.tag = tag;
			this.start();
		}

		@Override
		public void run() {

			HttpPost httpRequest = null;
			String httpUrl = Config.REQUEST_URL + ria_command_id;
			HttpClient client = new DefaultHttpClient();
			try {
				// 使用HttpPost对象设置发送的URL路径
				httpRequest = new HttpPost(httpUrl);
				/*
				 * httpRequest.addHeader(HTTP.CONTENT_TYPE,
				 * Config.APPLICATION_MULTI);
				 */
				httpRequest.addHeader("uuid", UserInfoPersist.uuid);
				if ((!ria_command_id.equals(Config.LOGIN_REQUEST))
						&& (UserInfoPersist.userID != null)) {
					httpRequest.addHeader("userId", UserInfoPersist.userID);
				}

				MultipartEntity mpEntity = new MultipartEntity();
				StringBody par = new StringBody(jsonString,
						Charset.forName("UTF-8"));
				// mpEntity.getContentType();
				mpEntity.addPart("data", par);

				// 图片
				if ((fileURL != null) && !fileURL.equals("")) {
					FileBody file = new FileBody(new File(fileURL),
							Config.UPLOAD_IMAGE_MIME_TYPE);
					mpEntity.addPart(tag, file);
				}

				// 发送请求体
				httpRequest.setEntity(mpEntity);
				// httpRequest.setEntity(se);

				// 创建一个浏览器对象，以把POST对象向服务器发送，并返回响应消息

				// HttpClient client = new DefaultHttpClient();

				// client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
				// 20000);

				HttpResponse httpResponse = client.execute(httpRequest);

				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 取得返回的字符串
					String strResult = EntityUtils.toString(
							httpResponse.getEntity(), "utf-8");
					// Log.e("strResult = ", strResult + "");

					ZLog.e("<< Response (" + cls.getSimpleName() + "): "
							+ strResult);

					strResult = strResult.substring(strResult.indexOf("{"));

					String status_code = strResult.substring(
							strResult.indexOf("status_code"),
							strResult.indexOf("status_code") + 15);
					status_code = status_code
							.substring(status_code.length() - 1);

					Message msg = handler.obtainMessage();

					if (status_code.equals("0")) {
						msg.what = responseType;
					} else {
						msg.what = Config.RESPONSE_TYPE_ERROR;
					}

					msg.obj = strResult;
					handler.sendMessage(msg);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// 释放资源
				client.getConnectionManager().shutdown();
			}
		}
	}

	protected static class GetRequestWork extends Thread {
		private Handler handler;
		private Class<?> cls;
		private int responseType;
		private String ria_command_id;
		private String request;

		/**
		 * 
		 * @param handler
		 * @param responseType
		 *            : 响应类型;
		 * @param cls
		 *            : 响应对象的class类型;
		 */
		public GetRequestWork(Handler handler, int responseType, Class<?> cls) {
			this.handler = handler;
			this.responseType = responseType;
			this.cls = cls;
		}

		public void execute(String ria_command_id, String request) {
			// TODO Auto-generated method stub
			this.ria_command_id = ria_command_id;
			this.request = request;
			this.start();
		}

		@Override
		public void run() {

			HttpGet httpRequest = null;

			try {
				String httpUrl = Config.REQUEST_URL + ria_command_id;

				httpRequest = new HttpGet(httpUrl);

				// 取得默认的HttpClient
				HttpClient httpclient = new DefaultHttpClient();
				// 取得HttpResponse
				HttpResponse httpResponse = httpclient.execute(httpRequest);
				// HttpStatus.SC_OK表示连接成功
				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 取得返回的字符串
					String strResult = EntityUtils.toString(
							httpResponse.getEntity(), "utf-8");
					Log.e("strResult = ", strResult + "");

					strResult = strResult.substring(strResult.indexOf("{"));

					Message msg = handler.obtainMessage(responseType);
					msg.obj = strResult;
					handler.sendMessage(msg);

					ZLog.e("<< Response (" + cls.getSimpleName() + "): "
							+ strResult);
				}

			} catch (Exception e) {
				e.printStackTrace();
				Log.e(TAG, "ria_command_id:" + ria_command_id);
			}
		}
	}
	
	//退出登录后清除JSESSIONID
	public static  void clearJseeionID()
	{
		ClientRequest.JSESSIONID=null;
	}
}
