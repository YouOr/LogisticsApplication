package com.yifeng.logistics.bean;

/**
 * Created by Administrator on 2017/3/10.
 */
public class User {

    private String userName;
    private String vID;
    private String longitude;
    private String Latitude;

    public User(String userName, String vID, String longitude,String Latitude) {
        this.userName = userName;
        this.vID = vID;
        this.longitude = longitude;
        this.Latitude = Latitude;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setvID(String vID) {
        this.vID = vID;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }


    public String getUserName() {
        return userName;
    }

    public String getvID() {
        return vID;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

}
