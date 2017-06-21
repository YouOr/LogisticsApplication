package com.yifeng.logistics.bean;

/**
 * Created by Administrator on 2017/3/15.
 */
public class Vehicle {

    private String UserName;
    private String VID;
    private String Longitude;
    private String Latitude;
    private int ID;
    private String Time;
    private String PassWord;

    public Vehicle(String userName, String vID, int ID, String longitude, String latitude, String Time, String PassWord) {
        this.UserName = userName;
        this.VID = vID;
        this.ID = ID;
        this.Longitude = longitude;
        this.Latitude = latitude;
        this.Time = Time;
        this.PassWord = PassWord;
    }

    public String getUserName() {
        return UserName;
    }

    public String getvID() {
        return VID;
    }

    public String getLongitude() {
        return Longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String passWord) {
        PassWord = passWord;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        this.Time = time;
    }

    public void setUserName(String userName) {
        this.UserName = userName;
    }

    public void setvID(String vID) {
        this.VID = vID;
    }

    public void setLongitude(String longitude) {
        this.Longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.Latitude = latitude;
    }

}
