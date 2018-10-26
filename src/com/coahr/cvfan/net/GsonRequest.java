package com.coahr.cvfan.net;

public class GsonRequest {
    
    //user login request entity.
    public static class UserLogin{
        public String userName;
        public String password;
        public String code;//企业账户-company | 个人账户-person
    }
    
}
