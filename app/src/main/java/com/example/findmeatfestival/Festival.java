package com.example.findmeatfestival;

public class Festival {
    private String festivalName;
    private String festivalDate;
    private String id;
    private String documentUserID;
    private String userID;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        userID = userID;
    }

    public String getDocumentUserID() {
        return documentUserID;
    }

    public void setDocumentUserID(String documentUserID) {
        this.documentUserID = documentUserID;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFestivalName() {
        return festivalName;
    }

    public void setFestivalName(String festivalName) {
        this.festivalName = festivalName;
    }

    public String getFestivalDate() {
        return festivalDate;
    }

    public void setFestivalDate(String festivalDate) {
        this.festivalDate = festivalDate;
    }

    public String toString(){
        String finalText=festivalName+"  "+festivalDate;
        return finalText;
    }

}
