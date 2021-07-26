package com.example.mmkv;

public class CacheDataTools {


    public static String getActionUser(){
        UserCacheData data = UserCacheData.getInstance();
        return data.getActionUser();
    }

    public static void setActionUser(String value){
        UserCacheData data = UserCacheData.getInstance();
        data.setActionUser(value);
    }


}
