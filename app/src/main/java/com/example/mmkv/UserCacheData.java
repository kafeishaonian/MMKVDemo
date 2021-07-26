package com.example.mmkv;

public class UserCacheData {
    //=========数据类型====
    private final String KEY_ACTION_USER = "key_action_user";
    //====================

    private MMKVWriter writer;
    private volatile static UserCacheData instance;

    private UserCacheData(){
        writer = MMKVWriter.getInstance();
        writer.setEncrypt(true, "12345678");
    }

    public static UserCacheData getInstance() {
        if (instance == null) {
            synchronized (UserCacheData.class) {
                if (instance == null) {
                    instance = new UserCacheData();
                }
            }
        }
        return instance;
    }

    public String getActionUser(){
        return writer.getString(KEY_ACTION_USER);
    }

    public void setActionUser(String value){
        writer.putString(KEY_ACTION_USER, value);
    }


}
