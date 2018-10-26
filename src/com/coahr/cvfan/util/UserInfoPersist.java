package com.coahr.cvfan.util;

import java.util.ArrayList;

import com.coahr.cvfan.net.GsonResponse;
import com.coahr.cvfan.net.GsonResponse.AreaData;
import com.coahr.cvfan.net.GsonResponse.BrandType;
import com.coahr.cvfan.net.GsonResponse.TruckType;

public class UserInfoPersist {

    public static String uuid;
    public static String userID;
    public static String ownerID;
    public static String driverId;
    public static String ownerRole;
    public static String phoneNum;

    
    public static String headIconUrl;
    public static String nickName;
    public static String birthday;
    
    public static GsonResponse.BrandType choseBrandData = new BrandType();
    public static GsonResponse.TruckType choseTruckTypeData = new TruckType();
    public static GsonResponse.AreaData choseProvince = new AreaData();
    public static GsonResponse.AreaData choseCity = new AreaData();
    public static GsonResponse.AreaData choseArea = new AreaData();
    public static GsonResponse.StationDetail choseStation = new GsonResponse.StationDetail();
    
    public static GsonResponse.PersonalInfo personalInfo;
    public static ArrayList<GsonResponse.BrandType> brands = new ArrayList<GsonResponse.BrandType>();
    public static ArrayList<GsonResponse.TruckType> truckTypes = new ArrayList<GsonResponse.TruckType>();
    
    
    public static UserInfoPersist getInstance(){
    	return new UserInfoPersist();
    }
}
