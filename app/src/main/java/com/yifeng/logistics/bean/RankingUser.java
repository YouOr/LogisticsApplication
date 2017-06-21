package com.yifeng.logistics.bean;

/**
 * Created by Administrator on 2017/2/28.
 */
public class RankingUser {
    private String UserDisplayName;
    private int NUM;
    private String HeadPic;

    public RankingUser(String userName, int num, String headPic) {
        this.UserDisplayName = userName;
        this.NUM = num;
        this.HeadPic = headPic;
    }

    public void setUserName(String userName) {
        this.UserDisplayName = userName;
    }

    public void setNum(int num) {
        this.NUM = num;
    }

    public void setHeadPic(String headPic) {
        this.HeadPic = headPic;
    }


    public String getUserName() {
        return UserDisplayName;
    }

    public int getNum() {
        return NUM;
    }

    public String getHeadPic() {
        return HeadPic;
    }


}
