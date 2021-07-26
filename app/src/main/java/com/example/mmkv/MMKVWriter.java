package com.example.mmkv;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.mmkv.MMKV;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MMKVWriter {

    private volatile static MMKVWriter instance;
    private WeakReference<Context> reference;

    /**
     * 是否加密
     */
    private boolean encrypt;
    /**
     * 秘钥
     */
    private String cryptKey;

    /**
     * 是否迁移SharedPreferences旧数据
     */
    private boolean migrate;


    private MMKVWriter() {

    }

    public static MMKVWriter getInstance() {
        if (instance == null) {
            synchronized (MMKVWriter.class) {
                if (instance == null) {
                    instance = new MMKVWriter();
                }
            }
        }
        return instance;
    }

    /**
     * 是否开启加密解密
     *
     * @param encrypt  是否开启
     * @param cryptKey 秘钥
     */
    public void setEncrypt(boolean encrypt, String cryptKey) {
        this.encrypt = encrypt;
        this.cryptKey = cryptKey;
    }

    /**
     * 是否迁移旧数据
     */
    public void setMigrate(boolean migrate) {
        this.migrate = migrate;
    }

    public MMKV getMMKV() {
        return getMMKV(null);
    }

    public MMKV getMMKV(String name) {
        MMKV mmkv;
        if (TextUtils.isEmpty(name)) {
            mmkv = encrypt ? MMKV.defaultMMKV(MMKV.MULTI_PROCESS_MODE, cryptKey) : MMKV.defaultMMKV(MMKV.MULTI_PROCESS_MODE, null);
        } else {
            mmkv = encrypt ? MMKV.mmkvWithID(name, MMKV.MULTI_PROCESS_MODE, cryptKey) : MMKV.mmkvWithID(name, MMKV.MULTI_PROCESS_MODE);
        }
        if (migrate) {
            SharedPreferences sharedPreferences;
            if (TextUtils.isEmpty(name)) {
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(reference.get());
            } else {
                sharedPreferences = reference.get().getSharedPreferences(name, Context.MODE_PRIVATE);
            }
            importFromSharedPreferences(sharedPreferences);
//            sharedPreferences.edit().clear().apply();
        }
        return mmkv;
    }

    private int importFromSharedPreferences(SharedPreferences preferences) {
        Map<String, ?> kvs = preferences.getAll();
        if (kvs != null && kvs.size() > 0) {
            Iterator var3 = kvs.entrySet().iterator();

            while(var3.hasNext()) {
                Map.Entry<String, ?> entry = (Map.Entry)var3.next();
                String key = (String)entry.getKey();
                Object value = entry.getValue();
                if (key != null && value != null) {
                    if (value instanceof Boolean) {
                        putBoolean(getTypeKey(Boolean.class, key), (Boolean) value);
                    } else if (value instanceof Integer) {
                        putInt(getTypeKey(Integer.class, key), (Integer) value);
                    } else if (value instanceof Long) {
                        putLong(getTypeKey(Long.class, key), (Long) value);
                    } else if (value instanceof Float) {
                        putFloat(getTypeKey(Float.class, key), (Float) value);
                    } else if (value instanceof Double) {
                        putDouble(getTypeKey(Double.class, key), (Double) value);
                    } else if (value instanceof String) {
                        putString(getTypeKey(String.class, key), (String) value);
                    } else if (value instanceof Set) {
                        putStringSet(getTypeKey(Set.class, key), (Set<String>) value);
                    } else {
                        Log.e("错误数据", "错误的数据类型: " + value.getClass());
                    }
                }
            }

            return kvs.size();
        } else {
            return 0;
        }
    }

    //============Set<String>=========

    public Set<String> getStringSet(String key) {
        return getMMKV().getStringSet(getTypeKey(Set.class, key), new HashSet<>());
    }

    public Set<String> getStringSet(String key, Set<String> defValues) {
        return getMMKV().getStringSet(getTypeKey(Set.class, key), defValues);
    }

    public Set<String> getStringSet(String name, String key, Set<String> defValues) {
        return getMMKV(name).getStringSet(getTypeKey(Set.class, key), defValues);
    }

    public void putStringSet(String key, Set<String> values) {
        getMMKV().putStringSet(getTypeKey(Set.class, key), values);
    }

    public void putStringSet(String name, String key, Set<String> values) {
        getMMKV(name).putStringSet(getTypeKey(Set.class, key), values);
    }

    //==========Double===========
    public void putDouble(String key, double value) {
        getMMKV().encode(getTypeKey(Double.class, key), value);
    }

    public double getDouble(String key) {
        return getMMKV().decodeDouble(getTypeKey(Double.class, key));
    }

    public double getDouble(String key, double defValue) {
        return getMMKV().decodeDouble(getTypeKey(Double.class, key), defValue);
    }

    public double getDouble(String name, String key, double defValue) {
        return getMMKV(name).decodeDouble(getTypeKey(Double.class, key), defValue);
    }

    //=========Byte===============
    public void putByte(String key, byte[] value) {
        getMMKV().encode(getTypeKey(Byte.class, key), value);
    }

    public byte[] getBytes(String key) {
        return getMMKV().decodeBytes(getTypeKey(Byte.class, key));
    }

    public byte[] getBytes(String key, byte[] defValue) {
        return getMMKV().decodeBytes(getTypeKey(Byte.class, key), defValue);
    }

    public byte[] getBytes(String name, String key, byte[] defValue) {
        return getMMKV(name).decodeBytes(getTypeKey(Byte.class, key), defValue);
    }

    //==================String===============
    public String getString(String key) {
        return getMMKV().getString(getTypeKey(String.class, key), "");
    }

    public String getString(String key, String defValue) {
        return getMMKV().getString(getTypeKey(String.class, key), defValue);
    }

    public String getString(String name, String key, String defValue) {
        return getMMKV(name).getString(getTypeKey(String.class, key), defValue);
    }

    public void putString(String key, String value) {
        getMMKV().putString(getTypeKey(String.class, key), value);
    }

    public void putString(String name, String key, String value) {
        getMMKV(name).putString(getTypeKey(String.class, key), value);
    }

    //=======Boolean=========

    public boolean getBoolean(String key) {
        return getMMKV().getBoolean(getTypeKey(Boolean.class, key), false);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return getMMKV().getBoolean(getTypeKey(Boolean.class, key), defValue);
    }

    public boolean getBoolean(String name, String key, boolean defValue) {
        return getMMKV(name).getBoolean(getTypeKey(Boolean.class, key), defValue);
    }

    public void putBoolean(String key, boolean value) {
        getMMKV().putBoolean(getTypeKey(Boolean.class, key), value);
    }

    public void putBoolean(String name, String key, boolean value) {
        getMMKV(name).putBoolean(getTypeKey(Boolean.class, key), value);
    }

    //=========Int=========

    public void putInt(String key, int value) {
        getMMKV().putInt(getTypeKey(Integer.class, key), value);
    }

    public void putInt(String name, String key, int value) {
        getMMKV(name).putInt(getTypeKey(Integer.class, key), value);
    }

    public int getInt(String key) {
        return getMMKV().getInt(getTypeKey(Integer.class, key), 0);
    }

    public int getInt(String key, int defValue) {
        return getMMKV().getInt(getTypeKey(Integer.class, key), defValue);
    }

    public int getInt(String name, String key, int defValue) {
        return getMMKV(name).getInt(getTypeKey(Integer.class, key), defValue);
    }

    //==========Float========

    public void putFloat(String key, float value) {
        getMMKV().putFloat(getTypeKey(Float.class, key), value);
    }

    public void putFloat(String name, String key, float value) {
        getMMKV(name).putFloat(getTypeKey(Float.class, key), value);
    }

    public float getFloat(String key) {
        return getMMKV().getFloat(getTypeKey(Float.class, key), 0f);
    }

    public float getFloat(String key, float defValue) {
        return getMMKV().getFloat(getTypeKey(Float.class, key), defValue);
    }

    public float getFloat(String name, String key, float defValue) {
        return getMMKV(name).getFloat(getTypeKey(Float.class, key), defValue);
    }

    //=======Long===========

    public void putLong(String key, long value) {
        getMMKV().putLong(getTypeKey(Long.class, key), value);
    }

    public void putLong(String name, String key, long value) {
        getMMKV(name).putLong(getTypeKey(Long.class, key), value);
    }

    public long getLong(String key) {
        return getMMKV().getLong(getTypeKey(Long.class, key), 0L);
    }

    public long getLong(String key, long defValue) {
        return getMMKV().getLong(getTypeKey(Long.class, key), defValue);
    }

    public long getLong(String name, String key, long defValue) {
        return getMMKV(name).getLong(getTypeKey(Long.class, key), defValue);
    }



    public void remove(String key) {
        getMMKV().remove(getRealKey(key));
    }

    public void remove(String name, String key) {
        getMMKV(name).remove(getRealKey(key));
    }

    public void clear() {
        getMMKV().clear();
    }

    public void clear(String name) {
        getMMKV(name).clear();
    }

    public boolean contains(String key) {
        getMMKV().getAll();
        return getMMKV().contains(getRealKey(key));
    }

    public boolean contains(String name, String key) {
        return getMMKV(name).contains(getRealKey(key));
    }

    private <T extends Object> String getTypeKey(Class<T> clazz, String key){
        String type = "@" + clazz.getSimpleName();
        if (key.contains(type)) {
            return type;
        } else {
            return key + type;
        }
    }

    private String getRealKey(String key){
        List<String> typeKys = new ArrayList<String>(){{
            add(getTypeKey(String.class, key));
            add(getTypeKey(Long.class, key));
            add(getTypeKey(Float.class, key));
            add(getTypeKey(Integer.class, key));
            add(getTypeKey(Boolean.class, key));
        }};

        for (String typeKey: typeKys){
            if (getMMKV().contains(typeKey)){
                return typeKey;
            }
        }
        return "";
    }


}
