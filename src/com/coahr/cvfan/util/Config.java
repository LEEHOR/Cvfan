package com.coahr.cvfan.util;

import java.util.ArrayList;

import android.os.Environment;

import com.coahr.cvfan.net.GsonResponse;

public class Config {
    //server url
    public static final String REQUEST_URL = "http://121.41.36.244/auto";
    public static final String CLIENT_DEVICE_TYPE = "1";
    
    public static final String UPDATE_SAVENAME = "cvfans.apk";
    public static final String EXTERNAL_STORAGE_PATH = Environment.getExternalStorageDirectory().getPath() + "/com.coahr.cvfan";

    public static String latitude;
    public static String longitude;

    //server data interface url;
    public static final String LOGIN_REQUEST         = "/User/Login.action";
    public static final String REGISTER_REQUEST      = "/Index-Api-PersonRegister";
    public static final String JOBSEARCH_REQUEST     = "/Index-Api-Jobs";
    public static final String LOCATIONQUERY_REQUEST = "/Index-Api-getName";
    
    public static final String GET_STATION_lIST_REQUEST  = "/Query/Station/Pagination.action";
    public static final String GET_DRIVER_INFO           = "/Query/Driver/Load.action";
    public static final String GET_TRUCK_INFO            = "/Query/AutoInfo/Load.action";
    public static final String MODIFY_PASSWORD           = "/User/Password/Update.action";
    public static final String GET_DRIVER_TRUCK_INFO     = "/Query/AutoInfo/List.action";
    public static final String IDENTIFY_DRIVER_LICENSE   = "/Driver/Authentication/Insert.action";
    public static final String APPRAISE_STATION          = "/Persist/Comment/Insert.action";
    public static final String GET_STATION_COMMENT_LIST  = "/Query/Comment/Pagination.action";
    public static final String GET_MAINTAIN_PAPER_LIST   = "/Query/AutoService/Pagination.action";
    public static final String BIND_PHONE                = "/Driver/Bind/Phone.action";
    public static final String USER_REGISTER             = "/User/Register.action";
    public static final String GET_GALLERY_LIST          = "/Query/DriverAlbum/List.action";
    public static final String GET_TRUCK_BRAND_LIST      = "/Query/DataInfo/List.action";
    public static final String GET_MAINTAIN_PAPER_DETAIL = "/Query/AutoService/Load.action";
    public static final String REGISTER_TRUCK            = "/MultipartPersist/AutoInfo/Insert.action";
    public static final String GET_PERSONAL_INFO         = "/Query/Driver/Load.action";
    public static final String REGISTER_MY_TRUCK         = "/MultipartPersist/AutoInfo/Insert.action";
    public static final String GET_TRUCK_TYPE_LIST       = "/Query/DataInfo/List.action";
    public static final String GAIN_VERIFITCATION_CODE   = "/Code/Send.action";
    public static final String MODIFY_MY_TRUCK_INFO      = "/MultipartPersist/AutoInfo/Update.action";
    public static final String GET_AREA_LIST             = "/Query/DataInfo/List.action";
    public static final String REQUEST_STATION_ASSURANCE = "/Persist/AutoGuarantee/Insert.action";    
    public static final String REQUEST_DISSCOUNT         = "/Query/Discount/Pagination.action";
    public static final String REQUEST_SALES_PROMOTION   = "/Query/Promotion/Pagination.action";
    public static final String CDOE_VALIDATION			 = "/Driver/Phone/Validation.action";	
    public static final String FEED_BACK     			 = "/Persist/Opinion/Insert.action";
    public static final String MODIFY_PERSONAL_INFO      = "/MultipartPersist/Driver/Update.action";
    public static final String VERSION_UPDATE            = "/device/version/Update.action";
    public static final String RESET_PASSWORD            = "/User/Password/Reset.action";
    //data identity
    public static final String BUNDLE_KEY              = "BUNDLER_KEY";
    public static final String ACCOUNT_DATA_PREFERENCE = "account_data";
    public static final String ACCOUNT_USERNAME        = "username";
    public static final String ACCOUNT_PASSWORD        = "password";
    public static final String ACCOUNT_OWNEROLE        = "ownerRole";
    public static final String ACCOUNT_MOBILENO        = "mobileNo";
    
    public static final String ACCOUNT_USERID          = "userid";
    public static final	String LOGIN_FLAG			   = "loginFlag";
    public static final	String FIRST_LOGIN			   = "firstLogin";


    public static boolean isLogIn = false;
    public static boolean UPDATE_WINDOW_SHOW = true;

    public static ArrayList<GsonResponse.Infonub> jobListData;

    public static final String STATION_DETAIL_INFO    = "station_detail";
    public static final String APPLICATION_JSON       = "application/json";
    public static final String APPLICATION_MULTI      = "multipart/form-data";
    public static final String CONTENT_TYPE_TEXT_JSON = "text/json";
    public static final String UPLOAD_IMAGE_MIME_TYPE = "image/*";
    
    
    public static final String QUERY_PROVINCE_AREA_URL = "cvfans.queryProvinceInfoes";
    public static final String QUERY_CITY_AREA_URL     = "cvfans.queryCityAreaInfoes";
    
    public static final String LOCAL_IMAGE_URL = Environment.getExternalStorageDirectory() + "/com.coahr.cvfan";

    /*
     * private Config(){ jobListData = new ArrayList<GsonResponse.Infonub>(); }
     * 
     * public static void initConfig(){ new Config(); }
     */
    
    //eixt app flag
    public static final int EXIT_APP_FLAG = 0x100001;

    // response type
    public static final int RESPONSE_TYPE_ERROR                     = 0x100000;
	public static final int APP_MSG_ERROR							= 0x100024;//msg_erro
    public static final int LOGIN_RESPONSE_TYPE                     = 0x000001;// login response
    public static final int GET_STATION_lIST_RESPONSE_TYPE          = 0x000002;
    public static final int GET_DRIVER_INFO_RESPONSE_TYPE           = 0x000003;
    public static final int GET_TRUCK_INFO_RESPONSE_TYPE            = 0x000004;
    public static final int MODIFY_PASSWORD_RESPONSE_TYPE           = 0x000005;
    public static final int GET_DRIVER_TRUCK_INFO_RESPONSE_TYPE     = 0x000006;
    public static final int IDENTIFY_DRIVER_LICENSE_RESPONSE_TYPE   = 0x000007;
    public static final int APPRAISE_STATION_RESPONSE_TYPE          = 0x000008;
    public static final int GET_STATION_COMMENT_LIST_RESPONSE_TYPE  = 0x000009;
    public static final int GET_MAINTAIN_PAPER_LIST_RESPONSE_TYPE   = 0x000010;
    public static final int BIND_PHONE_RESPONSE_TYPE                = 0x000011;
    public static final int USER_REGISTER_RESPONSE_TYPE             = 0x000012;
    public static final int GET_GALLERY_LIST_RESPONSE_TYPE          = 0x000013;
    public static final int GET_TRUCK_BRAND_LIST_RESPONSE_TYPE      = 0x000014;
    public static final int GET_MAINTAIN_PAPER_DETAIL_RESPONSE_TYPE = 0x000015;
    public static final int REGISTER_TRUCK_RESPONSE_TYPE            = 0x000016;
    public static final int GET_PERSONAL_INFO_RESPONSE_TYPE         = 0x000017;
    public static final int REGISTER_MY_TRUCK_RESPONSE_TYPE         = 0x000018;
    public static final int GET_TRUCK_TYPE_LIST_RESPONSE_TYPE       = 0x000018;
    public static final int GAIN_VERIFITCATION_CODE_RESPONSE_TYPE   = 0x000019;
    public static final int MODIFY_MY_TRUCK_INFO_RESPONSE_TYPE      = 0x000020;
    public static final int GET_PROVINCE_LIST_RESPONSE_TYPE         = 0x000021;
    public static final int GET_CITY_LIST_RESPONSE_TYPE             = 0x000022;
    public static final int REQUEST_STATION_ASSURANCE_RESPONSE_TPE  = 0x000023;
    public static final int REQUEST_DISSCOUNT_RESPONSE_TYPE         = 0x000024;
    public static final int REQUEST_SALES_PROMOTION_RESPONSE_TYPE   = 0x000025;
    public static final int CDOE_VALIDATION_RESPONSE_TYPE 			= 0x000026;
    public static final int FEED_BACK_RESPONSE_TYPE                 = 0x000027;
    public static final int MODIFY_PERSONAL_INFO_RESPONSE_TYPE      = 0x000028;
    public static final int NET_CONNECT_EXCEPTION				    = 0x000029;
    public static final int VERSION_UPDATE_RESPONSE_TYP             = 0x000030;
    public static final int TIMER_COUNT_RESPONSE_TYP                = 0x000031;
    public static final int RESET_PASSWORD_RESPONSE_TYP             = 0x000032;
}
