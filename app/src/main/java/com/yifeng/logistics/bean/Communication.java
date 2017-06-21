package com.yifeng.logistics.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/3/13.
 */
public class Communication {
    private int ID;
    private String Name;
    private String Text;
    private String Pic;
    private String Record;
    private int RecordTime;
    private int Dlike;
    private String DLikeName;
    private String ComTime;
    private int openType=0;

    private String HeadPic;
    private List<CommunicationDetails> CommunicationDetailsList;
    private int order = 0;
    private int order1 = 0;

    public Communication(int ID, String Name, String Text, String Pic, String Record, int RecordTime, int Dlike, String ComTime, List<CommunicationDetails> CommunicationDetailsList) {
        this.ID = ID;
        this.Name = Name;
        this.Text = Text;
        this.Pic = Pic;
        this.Record = Record;
        this.RecordTime = RecordTime;
        this.Dlike = Dlike;
        this.ComTime = ComTime;
        this.CommunicationDetailsList = CommunicationDetailsList;
    }
    public int getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public String getText() {
        return Text;
    }

    public String getPic() {
        return Pic;
    }

    public String getRecord() {
        return Record;
    }

    public int getRecordTime() {
        return RecordTime;
    }

    public int getDlike() {
        return Dlike;
    }
    public String getDLikeName() {
        return DLikeName;
    }
    public String getComTime() {
        return ComTime;
    }

    public List<CommunicationDetails> getCommunicationDetailsList() {
        return CommunicationDetailsList;
    }
    public int getOrder() {
        return order;
    }

    public int getOrder1() {
        return order1;
    }
    public String getHeadPic() {
        return HeadPic;
    }
    public int getOpenType() {
        return openType;
    }

    public void setOpenType(int openType) {
        this.openType = openType;
    }

    public void setHeadPic(String headPic) {
        HeadPic = headPic;
    }
    public void setID(int ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setText(String text) {
        Text = text;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    public void setRecord(String record) {
        Record = record;
    }

    public void setRecordTime(int recordTime) {
        RecordTime = recordTime;
    }

    public void setDlike(int dlike) {
        Dlike = dlike;
    }
    public void setDLikeName(String DLikeName) {
        this.DLikeName = DLikeName;
    }

    public void setComTime(String comTime) {
        ComTime = comTime;
    }

    public void setCommunicationDetailsList(List<CommunicationDetails> communicationDetailsList) {
        CommunicationDetailsList = communicationDetailsList;
    }
    public void setOrder(int order) {
        this.order = order;
    }

    public void setOrder1(int order1) {
        this.order1 = order1;
    }

    public static class CommunicationDetails {
        public String DName;
        public String Content;
        public int UserID;

        public CommunicationDetails(String DName, String Content) {
            this.DName = DName;
            this.Content = Content;
        }

        public String getDName() {
            return DName;
        }

        public String getContent() {
            return Content;
        }

        public int getUserID() {
            return UserID;
        }

        public void setDName(String DName) {
            this.DName = DName;
        }

        public void setContent(String content) {
            Content = content;
        }

        public void setUserID(int userID) {
            UserID = userID;
        }


    }
}
