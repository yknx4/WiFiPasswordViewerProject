package com.yknx.wifipasswordviewer;

/**
 * Created by Yknx on 4/01/14.
 */
public class wifiNetwork {

    private String Name;
    private String Key;
    private security Type;

    public wifiNetwork(String name, String key, security type) {
        Name = name;
        Key = key;
        Type = type;
    }

    public String getName() {
        return Name;
    }

    public String getKey() {
        return Key;
    }

    public security getType() {
        return Type;
    }


    public enum security{
        wep,wpa,open
    }
}
